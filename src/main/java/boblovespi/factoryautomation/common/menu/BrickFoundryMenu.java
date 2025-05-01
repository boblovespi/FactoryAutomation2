package boblovespi.factoryautomation.common.menu;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.FuelInfo;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BrickFoundryMenu extends AbstractContainerMenu
{
	private final int invSize;
	private final ContainerData data;
	private final ContainerLevelAccess access;

	public BrickFoundryMenu(int containerId, Inventory playerInv)
	{
		this(containerId, playerInv, new ItemStackHandler(2)
		{
			@Override
			public boolean isItemValid(int slot, ItemStack stack)
			{
				if (slot == 0)
					return stack.getItemHolder().getData(FuelInfo.FUEL_DATA) != null;
				return super.isItemValid(slot, stack);
			}
		}, new SimpleContainerData(7), ContainerLevelAccess.NULL);
	}

	public BrickFoundryMenu(int containerId, Inventory playerInv, IItemHandler inv, ContainerData data, ContainerLevelAccess access)
	{
		super(MenuTypes.BRICK_FOUNDRY.get(), containerId);
		this.data = data;
		this.access = access;
		invSize = inv.getSlots();

		addSlot(new SlotItemHandler(inv, 1, 67, 18));
		addSlot(new SlotItemHandler(inv, 0, 67, 60));
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
	public ItemStack quickMoveStack(Player player, int index)
	{
		var previous = ItemStack.EMPTY;
		var slot = slots.get(index);

		if (slot != null && slot.hasItem())
		{
			var current = slot.getItem();
			previous = current.copy();

			if (index < invSize)
			{
				// From the block breaker inventory to player's inventory
				if (!moveItemStackTo(current, invSize, invSize + 36, true))
					return ItemStack.EMPTY;
			}
			else
			{
				// From the player's inventory to block breaker's inventory
				if (!moveItemStackTo(current, 0, invSize, false))
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
	public boolean stillValid(Player player)
	{
		return AbstractContainerMenu.stillValid(access, player, FABlocks.BRICK_CRUCIBLE.get());
	}

	public int getData(int index)
	{
		return data.get(index);
	}
}
