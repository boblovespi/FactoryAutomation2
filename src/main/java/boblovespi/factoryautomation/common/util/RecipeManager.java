package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.recipe.IProgressRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.Nullable;
import java.util.Optional;

public class RecipeManager<R extends Recipe<?> & IProgressRecipe>
{
	private static final ResourceLocation NO_RECIPE = ResourceLocation.fromNamespaceAndPath("not_valid", "none");
	private final String nbtId;
	private final Validifier<RecipeHolder<R>> validifier;
	private final RecipeMatcher<RecipeHolder<R>> recipeMatcher;
	private final RecipeGrabber<Optional<RecipeHolder<?>>> recipeGrabber;
	@Nullable
	private RecipeHolder<R> currentRecipe;
	private ResourceLocation currentRecipeName;
	private int progress;

	public RecipeManager(String nbtId, Validifier<RecipeHolder<R>> validifier, RecipeMatcher<RecipeHolder<R>> recipeMatcher, RecipeGrabber<Optional<RecipeHolder<?>>> recipeGrabber)
	{
		this.nbtId = nbtId;
		this.validifier = validifier;
		this.recipeMatcher = recipeMatcher;
		this.recipeGrabber = recipeGrabber;
		currentRecipeName = NO_RECIPE;
	}

	public static <R extends Recipe<?> & IProgressRecipe> RecipeManager<R> dummy()
	{
		return new RecipeManager<>("dummy", r -> false, () -> null, r -> Optional.empty());
	}

	public void updateRecipe()
	{
		if (currentRecipe == null || !validifier.isValid(currentRecipe))
		{
			currentRecipe = recipeMatcher.findMatchingRecipe();
			currentRecipeName = currentRecipe == null ? NO_RECIPE : currentRecipe.id();
			progress = currentRecipe == null ? 0 : currentRecipe.value().getProgress();
		}
	}

	public void progress()
	{
		progress(1);
	}

	public void progress(int i)
	{
		if (i > 0 && progress > 0)
			progress -= i;
	}

	public boolean isComplete()
	{
		return currentRecipe != null && progress <= 0;
	}

	public RecipeHolder<R> getCompleted()
	{
		if (currentRecipe == null)
			throw new RuntimeException("Tried to complete a non-existent recipe!");
		return currentRecipe;
	}

	public void complete()
	{
		if (currentRecipe == null)
			clearRecipe();
		else if (!validifier.isValid(currentRecipe))
		{
			currentRecipe = recipeMatcher.findMatchingRecipe();
			currentRecipeName = currentRecipe == null ? NO_RECIPE : currentRecipe.id();
			progress = currentRecipe == null ? 0 : currentRecipe.value().getProgress();
		}
		else
			progress = currentRecipe.value().getProgress();
	}

	@Nullable
	public R getRecipe()
	{
		return currentRecipe != null ? currentRecipe.value() : null;
	}

	public void clearRecipe()
	{
		currentRecipe = null;
		currentRecipeName = NO_RECIPE;
		progress = 0;
	}

	public boolean hasRecipe()
	{
		return currentRecipe != null;
	}

	public void onLoad()
	{
		if (currentRecipeName != NO_RECIPE)
		{
			var maybe = recipeGrabber.getRecipe(currentRecipeName);
			if (maybe.isPresent())
			{
				var recipeHolder = maybe.get();
				try
				{
					//noinspection unchecked
					currentRecipe = (RecipeHolder<R>) recipeHolder;
				} catch (ClassCastException e)
				{
					FactoryAutomation.LOGGER.warn("The recipe {} has somehow changed types! It is now a {}", currentRecipeName, recipeHolder.getClass().getName());
					currentRecipeName = NO_RECIPE;
				}
			}
			else
			{
				FactoryAutomation.LOGGER.warn("The recipe {} no longer exists!", currentRecipeName);
				currentRecipeName = NO_RECIPE;
			}
		}
	}

	public void save(CompoundTag tag)
	{
		var nbt = new CompoundTag();
		if (currentRecipeName != NO_RECIPE)
			nbt.putString("recipe", currentRecipeName.toString());
		else
			nbt.putString("recipe", "NONE!");
		nbt.putInt("progress", progress);
		tag.put(nbtId, nbt);
	}

	public void load(CompoundTag tag)
	{
		var nbt = tag.getCompound(nbtId);
		var recipeName = nbt.getString("recipe");
		if ("NONE!".equals(recipeName))
			currentRecipeName = NO_RECIPE;
		else
			currentRecipeName = ResourceLocation.parse(recipeName);
		progress = nbt.getInt("progress");
	}

	@FunctionalInterface
	public interface Validifier<R>
	{
		boolean isValid(R currentRecipe);
	}

	@FunctionalInterface
	public interface RecipeMatcher<R>
	{
		@Nullable
		R findMatchingRecipe();
	}

	@FunctionalInterface
	public interface RecipeGrabber<R>
	{
		R getRecipe(ResourceLocation recipe);
	}
}
