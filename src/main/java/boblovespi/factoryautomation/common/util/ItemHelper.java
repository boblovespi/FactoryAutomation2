package boblovespi.factoryautomation.common.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemHelper
{
	public static void putItemsInInventoryOrDrop(Player player, ItemStack stack, Level level)
	{
		if (!player.addItem(stack))
		{
			var itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack);
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
	}

	public static void putItemsInInventoryOrDropAt(Player player, ItemStack stack, Level level, Vec3 pos)
	{
		if (!player.addItem(stack))
		{
			var itemEntity = new ItemEntity(level, pos.x(), pos.y(), pos.z(), stack);
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
	}
}
