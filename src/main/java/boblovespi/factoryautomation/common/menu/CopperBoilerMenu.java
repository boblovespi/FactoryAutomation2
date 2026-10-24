package boblovespi.factoryautomation.common.menu;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CopperBoilerMenu extends FAMenu
{
	public CopperBoilerMenu(int containerId, Inventory playerInv, ContainerData data, ContainerLevelAccess access)
	{
		super(MenuTypes.COPPER_BOILER.get(), containerId, new ItemStackHandler(0), data, access, FABlocks.COPPER_BOILER);

		buildPlayerSlots(playerInv, 8, 98);
	}

	public CopperBoilerMenu(int containerId, Inventory playerInv)
	{
		this(containerId, playerInv, new SimpleContainerData(5), ContainerLevelAccess.NULL);
	}
}
