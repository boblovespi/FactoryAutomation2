package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypes
{
	public static final DeferredRegister<MenuType<?>> TYPES = DeferredRegister.create(Registries.MENU, FactoryAutomation.MODID);

	public static final DeferredHolder<MenuType<?>, MenuType<StoneFoundryMenu>> STONE_FOUNDRY = register("stone_foundry", StoneFoundryMenu::new);

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, MenuType.MenuSupplier<T> supplier)
	{
		return TYPES.register(name, () -> new MenuType<>(supplier, FeatureFlags.DEFAULT_FLAGS));
	}
}
