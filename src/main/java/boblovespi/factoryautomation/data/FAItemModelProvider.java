package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
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

		handheld(FAItems.CHOPPING_BLADE);
		handheld(FAItems.FLINT_SHOVEL);
		handheld(FAItems.FLINT_PICKAXE);
		handheld(FAItems.FLINT_AXE);
		handheld(FAItems.FLINT_HOE);
		handheld(FAItems.FLINT_SWORD);
	}

	private void handheld(DeferredItem<? extends Item> item)
	{
		withExistingParent(item.getRegisteredName(), mcLoc("item/handheld")).texture("layer0", item.getId().withPrefix("item/"));
	}
}
