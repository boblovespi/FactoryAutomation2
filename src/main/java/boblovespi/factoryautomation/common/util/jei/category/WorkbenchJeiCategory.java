package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.Workbench;
import boblovespi.factoryautomation.common.recipe.WorkbenchRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.LinkedHashMap;

public class WorkbenchJeiCategory extends FAJeiCategory<WorkbenchRecipe>
{
	public WorkbenchJeiCategory(IGuiHelper helper)
	{
		super(RecipeThings.WORKBENCH_RECIPE_TYPE.get(), helper, FABlocks.STONE_WORKBENCH, FactoryAutomation.locString("jei", "workbench.name"));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<WorkbenchRecipe> recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.OUTPUT, 185, 39).addItemStack(getResultItem(recipe.value()));
		var inputs = recipe.value().getRecipe();
		var slots = new IRecipeSlotBuilder[7 * 5];

		for (int i = 0; i < 7; i++) // init all the slots using black magic
		{
			for (int j = 0; j < 5; j++)
				slots[j + i * 5] = builder.addSlot(RecipeIngredientRole.INPUT, 3 + (i < 1 ? 0 : 26 + (i < 2 ? 0 : 26 + (i - 2) * 18)), 3 + 18 * j);
		}

		for (int y = 0; y < inputs.length; y++)
		{
			for (int x = 0; x < inputs[y].length; x++) // set the main recipe slots
			{
				if (!inputs[y][x].isEmpty())
					slots[y + x * 5 + 10].addIngredients(inputs[y][x]);
			}
		}

		var tools = recipe.value().getTools();
		var toolMap = new LinkedHashMap<ResourceLocation, Integer>(tools.size());
		var i = 0;
		for (var tool : tools.keySet())
		{
			toolMap.put(tool, i);
			i++;
		}
		BuiltInRegistries.ITEM.getDataMap(Workbench.TOOL_DATA).forEach((item, tool) -> {
			if (tools.containsKey(tool.name()) && tool.tier() >= tools.get(tool.name()).tier())
			{
				var stack = BuiltInRegistries.ITEM.getOrThrow(item).getDefaultInstance();
				stack.setDamageValue(tools.get(tool.name()).usage());
				slots[toolMap.get(tool.name())].addItemStack(stack);
			}
		});

		var parts = recipe.value().getParts();
		var partMap = new LinkedHashMap<ResourceLocation, Integer>(parts.size());
		i = 0;
		for (var tool : parts.keySet())
		{
			partMap.put(tool, i);
			i++;
		}
		BuiltInRegistries.ITEM.getDataMap(Workbench.PART_DATA).forEach((item, part) -> {
			if (parts.containsKey(part.name()) && part.tier() >= parts.get(part.name()).tier())
			{
				var stack = BuiltInRegistries.ITEM.getOrThrow(item).getDefaultInstance();
				stack.setCount(parts.get(part.name()).usage());
				slots[partMap.get(part.name()) + 5].addItemStack(stack);
			}
		});
	}

	@Override
	protected IDrawable createBackground()
	{
		return helper.createDrawable(FactoryAutomation.name("textures/gui/jei/workbench.png"), 0, 0, 208, 94);
	}
}
