package boblovespi.factoryautomation.common.recipe;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkbenchRecipeBuilder implements RecipeBuilder
{
	private final ItemStack result;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private final List<String> rows = new ArrayList<>();
	private final Map<Character, Ingredient> key = new LinkedHashMap<>();
	private final Map<ResourceLocation, WorkbenchRecipe.Usage> parts = new LinkedHashMap<>();
	private final Map<ResourceLocation, WorkbenchRecipe.Usage> tools = new LinkedHashMap<>();

	private WorkbenchRecipeBuilder(ItemStack result)
	{
		this.result = result;
	}

	public static WorkbenchRecipeBuilder of(ItemLike item)
	{
		return of(item, 1);
	}

	public static WorkbenchRecipeBuilder of(ItemLike item, int count)
	{
		return new WorkbenchRecipeBuilder(new ItemStack(item, count));
	}

	@Override
	public WorkbenchRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion)
	{
		criteria.put(pName, pCriterion);
		return this;
	}

	@Override
	public WorkbenchRecipeBuilder group(@Nullable String pGroupName)
	{
		return this;
	}

	public WorkbenchRecipeBuilder define(Character pSymbol, TagKey<Item> pTag)
	{
		return this.define(pSymbol, Ingredient.of(pTag));
	}

	public WorkbenchRecipeBuilder define(Character pSymbol, ItemLike pItem)
	{
		return this.define(pSymbol, Ingredient.of(pItem));
	}

	public WorkbenchRecipeBuilder define(Character pSymbol, Ingredient pIngredient)
	{
		if (key.containsKey(pSymbol))
			throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
		else if (pSymbol == ' ')
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");

		key.put(pSymbol, pIngredient);
		return this;
	}

	public WorkbenchRecipeBuilder pattern(String pPattern)
	{
		if (!rows.isEmpty() && pPattern.length() != rows.getFirst().length())
			throw new IllegalArgumentException("Pattern must be the same width on every line!");

		rows.add(pPattern);
		return this;
	}

	public WorkbenchRecipeBuilder part(String part, int tier, int usage)
	{
		return part(FactoryAutomation.name(part), tier, usage);
	}

	public WorkbenchRecipeBuilder part(ResourceLocation part, int tier, int usage)
	{
		if (parts.containsKey(part))
			throw new IllegalArgumentException("Part '" + part + "' is already defined!");
		parts.put(part, new WorkbenchRecipe.Usage(tier, usage));
		return this;
	}

	public WorkbenchRecipeBuilder tool(String tool, int tier, int usage)
	{
		return tool(FactoryAutomation.name(tool), tier, usage);
	}

	public WorkbenchRecipeBuilder tool(ResourceLocation tool, int tier, int usage)
	{
		if (tools.containsKey(tool))
			throw new IllegalArgumentException("Tool '" + tool + "' is already defined!");
		tools.put(tool, new WorkbenchRecipe.Usage(tier, usage));
		return this;
	}

	@Override
	public Item getResult()
	{
		return result.getItem();
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation location)
	{
		if (criteria.isEmpty())
			throw new IllegalStateException("No way of obtaining recipe " + location);
		var advancementBuilder = output.advancement()
									   .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location))
									   .rewards(AdvancementRewards.Builder.recipe(location))
									   .requirements(AdvancementRequirements.Strategy.OR);
		criteria.forEach(advancementBuilder::addCriterion);
		var recipe = new WorkbenchRecipe(WorkbenchRecipe.RecipePatternWrapper.of(key, rows), parts, tools, result);
		output.accept(location, recipe, advancementBuilder.build(location.withPrefix("recipes/")));
	}

	@Override
	public void save(RecipeOutput output)
	{
		save(output, BuiltInRegistries.ITEM.getKey(result.getItem()).withPrefix("workbench/"));
	}
}
