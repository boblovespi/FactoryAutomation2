package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.resource.ResourceRock;
import boblovespi.factoryautomation.common.block.resource.Rock;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FABlocks
{
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FactoryAutomation.MODID);

	public static final List<DeferredBlock<Rock>> ROCKS = Arrays.stream(Rock.Variants.values()).map(v -> register(v.getRockName(), () -> new Rock(v))).toList();
	public static final DeferredBlock<Rock> COBBLESTONE_ROCK = ROCKS.getFirst();
	public static final DeferredBlock<ResourceRock> FLINT_ROCK = register("flint_rock", p -> new ResourceRock(p, Items.FLINT), BlockProperties.ROCK);

	private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> supplier)
	{
		return BLOCKS.register(name, supplier);
	}

	private static <T extends Block> DeferredBlock<T> register(String name, Function<BlockBehaviour.Properties, T> supplier, BlockBehaviour.Properties properties)
	{
		return BLOCKS.register(name, () -> supplier.apply(properties));
	}
}
