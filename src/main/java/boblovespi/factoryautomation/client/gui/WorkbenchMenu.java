package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.WorkbenchRecipe;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WorkbenchMenu extends AbstractContainerMenu
{
	private final IItemHandler inv;
	private final Player player;
	private final boolean is3x3;
	private final int invSize;
	private final RecipeManager<WorkbenchRecipe> rm;
	private final ContainerLevelAccess access;

	public WorkbenchMenu(int containerId, Inventory playerInv, FriendlyByteBuf data)
	{
		this(containerId, playerInv, new ItemStackHandler(data.readVarInt()), RecipeManager.dummy(), ContainerLevelAccess.NULL);
	}

	public WorkbenchMenu(int containerId, Inventory playerInv, IItemHandler inv, RecipeManager<WorkbenchRecipe> rm, ContainerLevelAccess access)
	{
		super(MenuTypes.WORKBENCH_MENU.get(), containerId);
		this.inv = inv;
		player = playerInv.player;
		this.rm = rm;
		this.access = access;
		var is3x3 = inv.getSlots() < 5 * 5 + 5 * 2 + 1;
		invSize = inv.getSlots();

		addSlot(new SlotOutputItem(inv, 0, 198, 53));
		if (is3x3)
		{
			this.is3x3 = true;
			for (int x1 = 0; x1 < 5; x1++)
			{
				for (int y1 = 0; y1 < 3; y1++)
					addSlot(new SlotItemHandler(inv, y1 + x1 * 3 + 1, 16 + (x1 < 1 ? 0 : 26 + (x1 < 2 ? 0 : 44 + (x1 - 2) * 18)), 35 + 18 * y1));
			}
		}
		else
		{
			this.is3x3 = false;
			for (int x1 = 0; x1 < 7; x1++)
			{
				for (int y1 = 0; y1 < 5; y1++)
					addSlot(new SlotItemHandler(inv, y1 + x1 * 5 + 1, 16 + (x1 < 1 ? 0 : 26 + (x1 < 2 ? 0 : 26 + (x1 - 2) * 18)), 17 + 18 * y1));
			}
		}

		int x = 37;
		int y = 120;

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
/*		// The quick moved slot stack
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

			*//*
			The following quick move logic can be simplified to if in data inventory,
			try to move to player inventory/hotbar and vice versa for containers
			that cannot transform data (e.g. chests).
			*//*
			if (index == 0)
			{
				// Try to move the result slot into the player inventory/hotbar
				if (!this.moveItemStackTo(rawStack, invSize, 36 + invSize, true))
				{
					// If cannot move, no longer quick move
					return ItemStack.EMPTY;
				}

				// Perform logic on result slot quick move
				quickMovedSlot.onQuickCraft(rawStack, quickMovedStack);
			}
			// Else if the quick move was performed on the player inventory or hotbar slot
			if (index >= invSize && index < 36 + invSize)
			{
				// Try to move the inventory/hotbar slot into the data inventory input slots
				if (!moveItemStackTo(rawStack, 1, invSize, false))
				{
					// If cannot move and in player inventory slot, try to move to hotbar
					if (index < invSize + 27)
					{
						if (!moveItemStackTo(rawStack, 27 + invSize, 36 + invSize, false))
						{
							// If cannot move, no longer quick move
							return ItemStack.EMPTY;
						}
					}
					// Else try to move hotbar into player inventory slot
					else if (!this.moveItemStackTo(rawStack, invSize, invSize + 27, false))
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
			*//*
			The following if statement and Slot#onTake call can be removed if the
			menu does not represent a container that can transform stacks (e.g.
			chests).
			*//*
			if (rawStack.getCount() == quickMovedStack.getCount())
			{
				// If the raw stack was not able to be moved to another slot, no longer quick move
				return ItemStack.EMPTY;
			}
			// Execute logic on what to do post move with the remaining stack
			quickMovedSlot.onTake(player, rawStack);
		}

		return quickMovedStack; // Return the slot stack*/
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem())
		{
			ItemStack current = slot.getItem();
			previous = current.copy();

			if (index < this.inv.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.moveItemStackTo(current, inv.getSlots(), inv.getSlots() + 36, true))
					return ItemStack.EMPTY;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.moveItemStackTo(current, 1, inv.getSlots(), false))
					return ItemStack.EMPTY;
			}

			if (current.isEmpty()) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
				slot.set(ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
			else
				slot.setChanged();

			if (current.getCount() == previous.getCount())
				return ItemStack.EMPTY;
			slot.onTake(player, current);

		}
		return previous;
	}

	@Override
	public boolean stillValid(Player pPlayer)
	{
		return AbstractContainerMenu.stillValid(access, player, FABlocks.STONE_WORKBENCH.get());
	}

	public boolean is3x3()
	{
		return is3x3;
	}
}
