package boblovespi.factoryautomation.common.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemHelper
{
	public static void putItemsInInventoryOrDrop(Player player, ItemStack stack, Level level)
	{
		putItemsInInventoryOrDropAt(player, stack, level, player.position());
	}

	public static void putItemsInInventoryOrDropAt(Player player, ItemStack stack, Level level, Vec3 pos)
	{
		if (stack.isEmpty())
			return;
		var oldCount = stack.getCount();
		var addedAll = player.addItem(stack);
		if (addedAll || stack.getCount() != oldCount)
			level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F,
					((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		if (!addedAll)
		{
			var itemEntity = new ItemEntity(level, pos.x(), pos.y(), pos.z(), stack);
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
	}

	public static void dropItem(Level level, Vec3 pos, ItemStack item)
	{
		Containers.dropItemStack(level, pos.x(), pos.y(), pos.z(), item);
	}

	public static void dropAllItems(Level level, Vec3 pos, ItemStackHandler items)
	{
		var count = items.getSlots();
		for (int i = 0; i < count; i++)
			Containers.dropItemStack(level, pos.x(), pos.y(), pos.z(), items.getStackInSlot(i));
	}
}
