package boblovespi.factoryautomation.common.menu;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.NonInteractiveResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerCopySlot;
import net.neoforged.neoforge.items.ItemStackHandler;

public class LeatherSackMenu extends FAMenu
{
	private final int sackSlot;

	public LeatherSackMenu(int containerId, Inventory playerInv, IItemHandler inv, int sackSlot)
	{
		super(MenuTypes.LEATHER_SACK.get(), containerId, inv, new SimpleContainerData(0), ContainerLevelAccess.NULL, null);
		this.sackSlot = sackSlot;
		for (int j = 0; j < 3; j++)
			for (int i = 0; i < 3; i++)
				addSlot(new ItemHandlerCopySlot(inv, i + j * 3, 62 + 18 * i, 17 + 18 * j));

		var invY = 84 + 8 + 3;
		for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 9; ++i)
				addSlot(new Slot(playerInv, i + j * 9 + 9, 8 + i * 18, invY + j * 18));
		}
		for (int i = 0; i < 9; i++)
		{
			if (i == sackSlot)
				addSlot(new NonInteractiveResultSlot(playerInv, i, 8 + i * 18, invY + 58));
			else
				addSlot(new Slot(playerInv, i, 8 + i * 18, invY + 58));
		}
	}

	public LeatherSackMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf)
	{
		this(containerId, playerInv, new ItemStackHandler(9), buf.readVarInt());
	}

	@Override
	public boolean stillValid(Player player)
	{
		return player.getInventory().getItem(sackSlot).is(FAItems.LEATHER_SACK);
	}
}
