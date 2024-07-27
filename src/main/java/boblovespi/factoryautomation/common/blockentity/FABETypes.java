package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FABETypes
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FactoryAutomation.MODID);

	public static final Supplier<BlockEntityType<ChoppingBlockBE>> CHOPPING_BLOCK_TYPE = type("chopping_block", ChoppingBlockBE::new, FABlocks.CHOPPING_BLOCK.get());

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> type(String name, BlockEntityType.BlockEntitySupplier<T> factory,
																									   Block... validBlocks)
	{
		return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(factory, validBlocks).build(null));
	}
}
