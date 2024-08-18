package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Form;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class FAItemModelProvider extends ItemModelProvider
{
	public FAItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, FactoryAutomation.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		withExistingParent(FAItems.ROCK.getRegisteredName(), modLoc("block/cobblestone_rock"));

		basicItem(FAItems.PLANT_FIBER.get());

		FAItems.COPPER_THINGS.forEach(this::metal);
		FAItems.TIN_THINGS.forEach(this::metal);

		basicItem(FAItems.TOASTED_BREAD.get());

		withExistingParent(FAItems.LOG_PILE.getRegisteredName(), modLoc("block/log_pile"));
		withExistingParent(FAItems.STONE_CRUCIBLE.getRegisteredName(), modLoc("block/stone_crucible"));

		handheld(FAItems.CHOPPING_BLADE);
		handheld(FAItems.FLINT_SHOVEL);
		handheld(FAItems.FLINT_PICKAXE);
		handheld(FAItems.FLINT_AXE);
		handheld(FAItems.FLINT_HOE);
		handheld(FAItems.FLINT_SWORD);
		handheld(FAItems.COPPER_SHOVEL);
		handheld(FAItems.COPPER_PICKAXE);
		handheld(FAItems.COPPER_AXE);
		handheld(FAItems.COPPER_HOE);
		handheld(FAItems.COPPER_SWORD);
	}

	private void metal(Form form, DeferredItem<Item> item)
	{
		basicItem(item.get());
	}

	private void handheld(DeferredItem<? extends Item> item)
	{
		withExistingParent(item.getRegisteredName(), mcLoc("item/handheld")).texture("layer0", item.getId().withPrefix("item/"));
	}
}
