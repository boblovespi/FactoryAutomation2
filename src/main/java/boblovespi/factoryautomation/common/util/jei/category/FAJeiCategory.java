package boblovespi.factoryautomation.common.util.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public abstract class FAJeiCategory<R extends Recipe<?>> implements IRecipeCategory<RecipeHolder<R>>
{
	protected final IGuiHelper helper;
	private final String localizationName;
	private final RecipeType<RecipeHolder<R>> recipeType;
	private final IDrawable icon;
	private final IDrawable background;

	public FAJeiCategory(net.minecraft.world.item.crafting.RecipeType<R> vanillaType, IGuiHelper helper, ItemLike representative, String localizationName)
	{
		recipeType = RecipeType.createFromVanilla(vanillaType);
		this.helper = helper;
		this.localizationName = localizationName;
		icon = helper.createDrawableItemStack(representative.asItem().getDefaultInstance());
		background = createBackground();
	}

	protected abstract IDrawable createBackground();

	@Override
	public RecipeType<RecipeHolder<R>> getRecipeType()
	{
		return recipeType;
	}

	@Override
	public Component getTitle()
	{
		return Component.translatable(localizationName);
	}

	@Override
	public @Nullable IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	protected static ItemStack getResultItem(Recipe<?> recipe)
	{
		var minecraft = Minecraft.getInstance();
		var level = minecraft.level;
		if (level == null)
		{
			throw new NullPointerException("I just did what JEI does for vanilla, why didn't it work?");
		}
		var registryAccess = level.registryAccess();
		return recipe.getResultItem(registryAccess);
	}
}
