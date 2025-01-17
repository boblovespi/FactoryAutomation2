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
		basicItem(FAItems.RAW_CASSITERITE.get());
		basicItem(FAItems.RAW_LIMONITE.get());
		basicItem(FAItems.PIG_TALLOW.get());

		basicItem(FAItems.IRON_SHARD.get());
		basicItem(FAItems.SLAG.get());
		FAItems.PIG_TALLOW_FORMS.forEach((a, b) -> basicItem(b.get()));
		FAItems.COPPER_THINGS.forEach(this::metal);
		FAItems.TIN_THINGS.forEach(this::metal);
		FAItems.IRON_THINGS.forEach(this::metal);
		FAItems.BRONZE_THINGS.forEach(this::metal);
		FAItems.STEEL_THINGS.forEach(this::metal);
		basicItem(FAItems.WHEAT_FLOUR.get());
		basicItem(FAItems.CALCITE_DUST.get());
		basicItem(FAItems.QUICKLIME.get());
		basicItem(FAItems.MUD_BRICK.get());
		basicItem(FAItems.DRIED_BRICK.get());

		basicItem(FAItems.SCREW.get());
		basicItem(FAItems.BUSHING.get());
		basicItem(FAItems.IRON_RAIL.get());
		basicItem(FAItems.GOLD_RAIL.get());

		basicItem(FAItems.TOASTED_BREAD.get());

		withExistingParent(FAItems.LOG_PILE.getRegisteredName(), modLoc("block/log_pile"));
		withExistingParent(FAItems.STONE_CRUCIBLE.getRegisteredName(), modLoc("block/stone_crucible"));
		withExistingParent(FAItems.BRICK_CRUCIBLE.getRegisteredName(), modLoc("block/brick_crucible"));
		withExistingParent(FAItems.BRICK_FIREBOX.getRegisteredName(), modLoc("block/brick_firebox"));
		withExistingParent(FAItems.HAND_CRANK.getRegisteredName(), modLoc("block/hand_crank"));

		handheld(FAItems.CHOPPING_BLADE);
		handheld(FAItems.FLINT_SHOVEL);
		handheld(FAItems.FLINT_PICKAXE);
		handheld(FAItems.FLINT_AXE);
		handheld(FAItems.FLINT_HOE);
		handheld(FAItems.FLINT_SWORD);
		handheld(FAItems.FIREBOW);
		handheld(FAItems.COPPER_SHOVEL);
		handheld(FAItems.COPPER_PICKAXE);
		handheld(FAItems.COPPER_AXE);
		handheld(FAItems.COPPER_HOE);
		handheld(FAItems.COPPER_SWORD);
		handheld(FAItems.COPPER_HAMMER);
		handheld(FAItems.COPPER_SHEARS);
		handheld(FAItems.IRON_HAMMER);
		handheld(FAItems.IRON_WRENCH);

		FAItems.GEARS.values().forEach(t -> basicItem(t.get()));
	}

	private void metal(Form form, DeferredItem<? extends Item> item)
	{
		if (form == Form.BLOCK || form == Form.PLATE_BLOCK || form == Form.SPACE_FRAME)
			withExistingParent(item.getRegisteredName(), item.getId().withPrefix("block/"));
		else
			basicItem(item.get());
	}

	private void handheld(DeferredItem<? extends Item> item)
	{
		withExistingParent(item.getRegisteredName(), mcLoc("item/handheld")).texture("layer0", item.getId().withPrefix("item/"));
	}
}
