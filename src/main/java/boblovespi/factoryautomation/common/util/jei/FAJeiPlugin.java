package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.Metal;
import boblovespi.factoryautomation.common.util.jei.category.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NotNullFieldNotInitialized")
@JeiPlugin
public class FAJeiPlugin implements IModPlugin
{
	public static final IIngredientType<MetalStack> METAL_INGREDIENT = new MetalIngredient();
	private ChoppingBlockJeiCategory choppingBlockJeiCategory;
	private WorkbenchJeiCategory workbenchJeiCategory;
	private CastingJeiCategory castingJeiCategory;
	private MeltingJeiCategory meltingJeiCategory;
	private BrickDryingJeiCategory brickDryingJeiCategory;
	private MillstoneJeiCategory millstoneJeiCategory;
	private LogPileFiringCategory logPileFiringCategory;

	@Override
	public ResourceLocation getPluginUid()
	{
		return FactoryAutomation.name("jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		var guiHelper = registration.getJeiHelpers().getGuiHelper();
		choppingBlockJeiCategory = new ChoppingBlockJeiCategory(guiHelper);
		registration.addRecipeCategories(choppingBlockJeiCategory);
		workbenchJeiCategory = new WorkbenchJeiCategory(guiHelper);
		registration.addRecipeCategories(workbenchJeiCategory);
		castingJeiCategory = new CastingJeiCategory(guiHelper);
		registration.addRecipeCategories(castingJeiCategory);
		meltingJeiCategory = new MeltingJeiCategory(guiHelper);
		registration.addRecipeCategories(meltingJeiCategory);
		brickDryingJeiCategory = new BrickDryingJeiCategory(guiHelper);
		registration.addRecipeCategories(brickDryingJeiCategory);
		millstoneJeiCategory = new MillstoneJeiCategory(guiHelper);
		registration.addRecipeCategories(millstoneJeiCategory);
		logPileFiringCategory = new LogPileFiringCategory(guiHelper);
		registration.addRecipeCategories(logPileFiringCategory);
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration)
	{
		registration.register(METAL_INGREDIENT, Metal.allMetals().stream().map(k -> new MetalStack(k, 18)).collect(Collectors.toList()), new MetalIngredientHelper(),
				MetalIngredientRenderer.create());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		RecipeManager recipeManager;
		try
		{
			var minecraft = Minecraft.getInstance();
			var level = minecraft.level;
			recipeManager = level.getRecipeManager();
		} catch (NullPointerException e)
		{
			FactoryAutomation.LOGGER.error("Somehow something is null, so we couldn't find the recipes :(", e);
			return;
		}

		registration.addRecipes(choppingBlockJeiCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.CHOPPING_BLOCK_TYPE.get()));
		registration.addRecipes(workbenchJeiCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.WORKBENCH_RECIPE_TYPE.get()));
		registration.addRecipes(brickDryingJeiCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.BRICK_DRYING_TYPE.get()));
		registration.addRecipes(millstoneJeiCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.MILLSTONE_TYPE.get()));
		registration.addRecipes(logPileFiringCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.LOG_PILE_FIRING_TYPE.get())
																					.stream()
																					.map(LogPileFiringCategory.Holder.Real::new)
																					.collect(Collectors.toUnmodifiableList()));

		var anySolid = new ItemStack(Blocks.STONE);
		anySolid.set(DataComponents.ITEM_NAME, Component.translatable(FactoryAutomation.locString("jei", "misc.any_solid")));
		var dummyLogPileFiringRecipes = List.<LogPileFiringCategory.Holder>of(
				new LogPileFiringCategory.Holder.Dummy(anySolid, FABlocks.LOG_PILE, List.of(new ItemStack(Items.CHARCOAL, 8)), FABlocks.CHARCOAL_PILE.get().defaultBlockState()),
				new LogPileFiringCategory.Holder.Dummy(new ItemStack(FABlocks.COPPER_PLATE_BLOCK), FABlocks.LIMONITE_CHARCOAL_MIX,
						List.of(new ItemStack((ItemLike) FAItems.IRON_SHARD, 3), new ItemStack((ItemLike) FAItems.SLAG)), FABlocks.IRON_BLOOM.get().defaultBlockState()));
		registration.addRecipes(logPileFiringCategory.getRecipeType(), dummyLogPileFiringRecipes);

		var castingRecipes = new ArrayList<CastingJeiRecipe>();
		CasterType.STONE.efficiencies().forEach((f, e) -> castingRecipes.add(new CastingJeiRecipe(f, CasterType.STONE, e, FAItems.GREEN_SAND.toStack())));
		registration.addRecipes(castingJeiCategory.getRecipeType(), castingRecipes);

		var meltingRecipes = new ArrayList<MeltingJeiRecipe>();
		meltingRecipes.add(new MeltingJeiRecipe(Ingredient.of(Tags.Items.RAW_MATERIALS_COPPER), Metal.COPPER.meltTemp(), Metal.COPPER, Form.RAW_ORE));
		meltingRecipes.add(new MeltingJeiRecipe(Ingredient.of(FATags.Items.RAW_TIN), Metal.TIN.meltTemp(), Metal.TIN, Form.RAW_ORE));
		registration.addRecipes(meltingJeiCategory.getRecipeType(), meltingRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		FAItems.CHOPPING_BLOCKS.values().forEach(b -> registration.addRecipeCatalyst(b.toStack(), choppingBlockJeiCategory.getRecipeType()));
		registration.addRecipeCatalyst(FAItems.STONE_WORKBENCH.toStack(), workbenchJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.STONE_CASTING_VESSEL.toStack(), castingJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.STONE_CRUCIBLE.toStack(), meltingJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.BRICK_MAKER_FRAME.toStack(), brickDryingJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.MILLSTONE.toStack(), millstoneJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.LOG_PILE, logPileFiringCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.LIMONITE_CHARCOAL_MIX, logPileFiringCategory.getRecipeType());
	}

}
