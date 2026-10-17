package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.menu.LeatherSackMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ComponentItemHandler;

public class LeatherSack extends Item
{
	public LeatherSack(Properties properties)
	{
		super(properties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		var stack = player.getItemInHand(usedHand);
		if (!level.isClientSide)
		{
			var selected = usedHand == InteractionHand.OFF_HAND ? Inventory.SLOT_OFFHAND : player.getInventory().selected;
			player.openMenu(new SimpleMenuProvider((n, i, p) -> new LeatherSackMenu(n, i, new ComponentItemHandler(stack, DataComponents.CONTAINER, 9), selected),
					Component.translatable("gui.leather_sack.name")), b -> b.writeVarInt(selected));
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	@Override
	public boolean canFitInsideContainerItems(ItemStack stack)
	{
		return false;
	}
}
