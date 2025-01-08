package boblovespi.factoryautomation.common.util.jade;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.processing.BrickMakerFrameBE;
import boblovespi.factoryautomation.common.util.Codecs;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.view.*;

import java.util.List;

public enum BrickMakerProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView>
{
	INSTANCE;

	private final ResourceLocation id = FactoryAutomation.name("brick_maker");

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups)
	{
		return ClientViewGroup.map(groups, stack -> stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
														 .read(Codecs.JADE_COOKING)
														 .map(t -> IThemeHelper.get().seconds(t, accessor.tickRate()).getString())
														 .map(s -> (new ItemView(stack)).amountText(s)).result()
														 .orElse(null), null);
	}

	@Override
	public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor)
	{
		return switch (accessor.getTarget())
		{
			case BrickMakerFrameBE bmf -> List.of(new ViewGroup<>(bmf.makeViewStacks()));
			case null, default -> null;
		};
	}

	@Override
	public ResourceLocation getUid()
	{
		return id;
	}
}
