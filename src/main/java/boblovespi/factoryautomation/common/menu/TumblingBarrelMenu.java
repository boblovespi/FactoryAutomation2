package boblovespi.factoryautomation.common.menu;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class TumblingBarrelMenu extends FAMenu
{
	public TumblingBarrelMenu(int containerId, Inventory playerInv)
	{
		this(containerId, playerInv, new ItemStackHandler(2), new SimpleContainerData(5), ContainerLevelAccess.NULL);
	}

	public TumblingBarrelMenu(int containerId, Inventory playerInv, IItemHandler inv, ContainerData data, ContainerLevelAccess access)
	{
		super(MenuTypes.TUMBLING_BARREL.get(), containerId, inv, data, access, FABlocks.TUMBLING_BARREL);

		addSlot(new SlotItemHandler(inv, 0, 57, 34));
		addSlot(new SlotOutputItem(inv, 1, 109, 34));

		buildPlayerSlots(playerInv, 8, 98);
	}
}
