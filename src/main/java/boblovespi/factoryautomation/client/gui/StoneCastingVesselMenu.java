package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class StoneCastingVesselMenu extends AbstractContainerMenu
{
	private final ContainerLevelAccess access;
	private final ContainerData data;

	public StoneCastingVesselMenu(int containerId, Inventory playerInv)
	{
		this(containerId, playerInv, new SimpleContainerData(1), ContainerLevelAccess.NULL);
	}

	public StoneCastingVesselMenu(int containerId, Inventory playerInv, ContainerData data, ContainerLevelAccess access)
	{
		super(MenuTypes.STONE_CASTING_VESSEL.get(), containerId);
		this.data = data;
		this.access = access;
		addDataSlots(data);

		int x = 8;
		int y = 98;

		for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 9; ++i)
				addSlot(new Slot(playerInv, i + j * 9 + 9, x + i * 18, y + j * 18));
		}
		for (int i = 0; i < 9; i++)
		{
			addSlot(new Slot(playerInv, i, x + i * 18, y + 58));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return AbstractContainerMenu.stillValid(access, player, FABlocks.STONE_CASTING_VESSEL.get());
	}

	@Override
	public void setData(int pId, int pData)
	{
		super.setData(pId, pData);
		broadcastChanges();
	}

	@Override
	public boolean clickMenuButton(Player player, int id)
	{
		if (id >= 6)
			return false;
		data.set(0, id);
		return true;
	}

	public int getForm()
	{
		return data.get(0);
	}
}
