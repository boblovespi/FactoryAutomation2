package boblovespi.factoryautomation.common.menu;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.function.Supplier;

public abstract class FAMenu extends AbstractContainerMenu
{
	protected final int inventorySize;
	protected final ContainerData data;
	protected final ContainerLevelAccess access;
	private final Supplier<? extends Block> block;
	private boolean hasBuiltPlayerSlots = false;

	protected FAMenu(MenuType<?> menuType, int containerId, IItemHandler inv, ContainerData data, ContainerLevelAccess access, Supplier<? extends Block> block)
	{
		super(menuType, containerId);
		this.inventorySize = inv.getSlots();
		this.data = data;
		this.access = access;
		this.block = block;
		addDataSlots(data);
	}

	protected void buildPlayerSlots(Inventory playerInv, int x, int y)
	{
		if (hasBuiltPlayerSlots)
			FactoryAutomation.LOGGER.warn("Menu {} is trying to build player slots more than once!", this.getClass().getCanonicalName());
		hasBuiltPlayerSlots = true;
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

			if (index < inventorySize)
			{
				// From the block breaker inventory to player's inventory
				if (!moveItemStackTo(current, inventorySize, inventorySize + 36, true))
					return ItemStack.EMPTY;
			}
			else
			{
				// From the player's inventory to block breaker's inventory
				if (!moveItemStackTo(current, 0, inventorySize, false))
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
		return AbstractContainerMenu.stillValid(access, player, block.get());
	}

	public int getData(int index)
	{
		return data.get(index);
	}
}
