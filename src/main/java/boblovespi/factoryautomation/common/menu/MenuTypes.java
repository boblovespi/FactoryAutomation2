package boblovespi.factoryautomation.common.menu;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypes
{
	public static final DeferredRegister<MenuType<?>> TYPES = DeferredRegister.create(Registries.MENU, FactoryAutomation.MODID);

	public static final DeferredHolder<MenuType<?>, MenuType<StoneFoundryMenu>> STONE_FOUNDRY = register("stone_foundry", StoneFoundryMenu::new);
	public static final DeferredHolder<MenuType<?>, MenuType<StoneCastingVesselMenu>> STONE_CASTING_VESSEL = register("stone_casting_vessel", StoneCastingVesselMenu::new);
	public static final DeferredHolder<MenuType<?>, MenuType<WorkbenchMenu>> WORKBENCH_MENU = register("workbench", WorkbenchMenu::new);

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, MenuType.MenuSupplier<T> supplier)
	{
		return TYPES.register(name, () -> new MenuType<>(supplier, FeatureFlags.DEFAULT_FLAGS));
	}

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, IContainerFactory<T> supplier)
	{
		return TYPES.register(name, () -> IMenuTypeExtension.create(supplier));
	}
}
