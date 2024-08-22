package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

public class FABETypes
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FactoryAutomation.MODID);

	public static final Supplier<BlockEntityType<ChoppingBlockBE>> CHOPPING_BLOCK_TYPE = type("chopping_block", ChoppingBlockBE::new, FABlocks.CHOPPING_BLOCKS.values());
	public static final Supplier<BlockEntityType<MultiblockPartBE>> MULTIBLOCK_PART_TYPE = type("multiblock_part", MultiblockPartBE::new, FABlocks.MULTIBLOCK_PART);
	public static final Supplier<BlockEntityType<StoneCrucibleBE>> STONE_CRUCIBLE_TYPE = type("stone_crucible", StoneCrucibleBE::new, FABlocks.STONE_CRUCIBLE);
	public static final Supplier<BlockEntityType<StoneCastingVesselBE>> STONE_CASTING_VESSEL_TYPE = type("stone_casting_vessel", StoneCastingVesselBE::new, FABlocks.STONE_CASTING_VESSEL);
	public static final Supplier<BlockEntityType<WorkbenchBE.Stone>> STONE_WORKBENCH_TYPE = type("stone_workbench", WorkbenchBE.Stone::new, FABlocks.STONE_WORKBENCH);
	public static final Supplier<BlockEntityType<BrickMakerFrameBE>> BRICK_MAKER_FRAME_TYPE = type("brick_maker_frame", BrickMakerFrameBE::new, FABlocks.BRICK_MAKER_FRAME);

	@SafeVarargs
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> type(String name, BlockEntityType.BlockEntitySupplier<T> factory,
																									   Supplier<? extends Block>... validBlocks)
	{
		return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(factory, Arrays.stream(validBlocks).map(Supplier::get).toArray(Block[]::new)).build(null));
	}

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> type(String name, BlockEntityType.BlockEntitySupplier<T> factory,
																									   Collection<? extends Supplier<? extends Block>> validBlocks)
	{
		return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(factory, validBlocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
	}
}
