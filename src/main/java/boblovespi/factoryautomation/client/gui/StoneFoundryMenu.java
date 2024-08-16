package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

// TODO: move out of client package
public class StoneFoundryMenu extends AbstractContainerMenu
{
	private final int invSize;
	private final ContainerLevelAccess access;

	public StoneFoundryMenu(int containerId, Inventory playerInv)
	{
		this(containerId, playerInv, new ItemStackHandler(2), ContainerLevelAccess.NULL);
	}

	public StoneFoundryMenu(int containerId, Inventory playerInv, IItemHandler inv, ContainerLevelAccess access)
	{
		super(MenuTypes.STONE_FOUNDRY.get(), containerId);
		this.access = access;
		invSize = inv.getSlots();

		addSlot(new SlotItemHandler(inv, 1, 67, 18));
		addSlot(new SlotItemHandler(inv, 0, 67, 60));

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
	public ItemStack quickMoveStack(Player player, int index)
	{
		// The quick moved slot stack
		ItemStack quickMovedStack = ItemStack.EMPTY;
		// The quick moved slot
		Slot quickMovedSlot = slots.get(index);

		// If the slot is in the valid range and the slot is not empty
		if (quickMovedSlot != null && quickMovedSlot.hasItem())
		{
			// Get the raw stack to move
			ItemStack rawStack = quickMovedSlot.getItem();
			// Set the slot stack to a copy of the raw stack
			quickMovedStack = rawStack.copy();

			/*
			The following quick move logic can be simplified to if in data inventory,
			try to move to player inventory/hotbar and vice versa for containers
			that cannot transform data (e.g. chests).
			*/
			// Else if the quick move was performed on the player inventory or hotbar slot
			if (index >= invSize && index < 36 + invSize)
			{
				// Try to move the inventory/hotbar slot into the data inventory input slots
				if (!moveItemStackTo(rawStack, 0, invSize, false))
				{
					// If cannot move and in player inventory slot, try to move to hotbar
					if (index < 32)
					{
						if (!moveItemStackTo(rawStack, 27 + invSize, 36 + invSize, false))
						{
							// If cannot move, no longer quick move
							return ItemStack.EMPTY;
						}
					}
					// Else try to move hotbar into player inventory slot
					else if (!this.moveItemStackTo(rawStack, 5, 32, false))
					{
						// If cannot move, no longer quick move
						return ItemStack.EMPTY;
					}
				}
			}
			// Else if the quick move was performed on the data inventory input slots, try to move to player inventory/hotbar
			else if (!this.moveItemStackTo(rawStack, invSize, 36 + invSize, false))
			{
				// If cannot move, no longer quick move
				return ItemStack.EMPTY;
			}

			if (rawStack.isEmpty())
			{
				// If the raw stack has completely moved out of the slot, set the slot to the empty stack
				quickMovedSlot.set(ItemStack.EMPTY);
			}
			else
			{
				// Otherwise, notify the slot that that the stack count has changed
				quickMovedSlot.setChanged();
			}
			/*
			The following if statement and Slot#onTake call can be removed if the
			menu does not represent a container that can transform stacks (e.g.
			chests).
			*/
			if (rawStack.getCount() == quickMovedStack.getCount())
			{
				// If the raw stack was not able to be moved to another slot, no longer quick move
				return ItemStack.EMPTY;
			}
			// Execute logic on what to do post move with the remaining stack
			quickMovedSlot.onTake(player, rawStack);
		}

		return quickMovedStack; // Return the slot stack
	}

	@Override
	public boolean stillValid(Player player)
	{
		return AbstractContainerMenu.stillValid(access, player, FABlocks.STONE_CRUCIBLE.get());
	}
}
