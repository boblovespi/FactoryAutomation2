package boblovespi.factoryautomation.common.menu;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SteamOvenMenu extends FAMenu
{
	public SteamOvenMenu(int containerId, Inventory playerInv, IItemHandler inv, ContainerData data, ContainerLevelAccess access)
	{
		super(MenuTypes.STEAM_OVEN.get(), containerId, inv, data, access, FABlocks.STEAM_OVEN);
		addSlot(new SlotItemHandler(inv, 0, 57, 34));
		addSlot(new SlotOutputItem(inv, 1, 109, 34));

		buildPlayerSlots(playerInv, 8, 98);
	}

	public SteamOvenMenu(int containerId, Inventory playerInv)
	{
		this(containerId, playerInv, new ItemStackHandler(2), new SimpleContainerData(2), ContainerLevelAccess.NULL);
	}
}
