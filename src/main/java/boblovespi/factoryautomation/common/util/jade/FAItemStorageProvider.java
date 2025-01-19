package boblovespi.factoryautomation.common.util.jade;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.List;
import java.util.stream.Collectors;

public enum FAItemStorageProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView>
{
	INSTANCE;

	private final ResourceLocation id = FactoryAutomation.name("item_storage");

	@Override
	public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor)
	{
		return switch (accessor.getTarget())
		{
			case IJadeViewable cb -> List.of(new ViewGroup<>(cb.makeViewStacks().stream().filter(s -> !s.isEmpty()).collect(Collectors.toList())));
			case null, default -> null;
		};
	}

	@Override
	public ResourceLocation getUid()
	{
		return id;
	}

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups)
	{
		return ClientViewGroup.map(groups, ItemView::new, null);
	}
}
