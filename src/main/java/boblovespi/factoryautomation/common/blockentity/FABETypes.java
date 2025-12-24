package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.blockentity.logistics.PipeBE;
import boblovespi.factoryautomation.common.blockentity.logistics.SmallTankBE;
import boblovespi.factoryautomation.common.blockentity.mechanical.*;
import boblovespi.factoryautomation.common.blockentity.processing.*;
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
	public static final Supplier<BlockEntityType<BrickCrucibleBE>> BRICK_CRUCIBLE_TYPE = type("brick_crucible", BrickCrucibleBE::new, FABlocks.BRICK_CRUCIBLE);
	public static final Supplier<BlockEntityType<BrickCastingVesselBE>> BRICK_CASTING_VESSEL_TYPE = type("brick_casting_vessel", BrickCastingVesselBE::new, FABlocks.BRICK_CASTING_VESSEL);
	public static final Supplier<BlockEntityType<CreativeMechanicalSourceBE>> CREATIVE_MECHANICAL_SOURCE_TYPE = type("creative_mechanical_source", CreativeMechanicalSourceBE::new, FABlocks.CREATIVE_MECHANICAL_SOURCE);
	public static final Supplier<BlockEntityType<PowerShaftBE>> POWER_SHAFT_TYPE = type("power_shaft", PowerShaftBE::new, FABlocks.WOOD_POWER_SHAFT, FABlocks.IRON_POWER_SHAFT);
	public static final Supplier<BlockEntityType<MillstoneBE>> MILLSTONE_TYPE = type("millstone", MillstoneBE::new, FABlocks.MILLSTONE);
	public static final Supplier<BlockEntityType<GearboxBE>> GEARBOX_TYPE = type("gearbox", GearboxBE::new, FABlocks.WOOD_GEARBOX, FABlocks.IRON_GEARBOX);
	public static final Supplier<BlockEntityType<SplitterBE>> SPLITTER_TYPE = type("splitter", SplitterBE::new, FABlocks.WOOD_SPLITTER, FABlocks.IRON_SPLITTER);
	public static final Supplier<BlockEntityType<JoinerBE>> JOINER_TYPE = type("joiner", JoinerBE::new, FABlocks.WOOD_JOINER, FABlocks.IRON_JOINER);
	public static final Supplier<BlockEntityType<BevelGearBE>> BEVEL_GEAR_TYPE = type("bevel_gear", BevelGearBE::new, FABlocks.WOOD_BEVEL_GEAR, FABlocks.IRON_BEVEL_GEAR);
	public static final Supplier<BlockEntityType<HandCrankBE>> HANDCRANK_TYPE = type("hand_crank", HandCrankBE::new, FABlocks.HAND_CRANK);
	public static final Supplier<BlockEntityType<SmallWaterwheelBE>> SMALL_WATERWHEEL_TYPE = type("small_waterwheel", SmallWaterwheelBE::new, FABlocks.SMALL_WATERWHEEL);
	public static final Supplier<BlockEntityType<LargeWaterwheelBE>> LARGE_WATERWHEEL_TYPE = type("large_waterwheel", LargeWaterwheelBE::new, FABlocks.LARGE_WATERWHEEL);
	public static final Supplier<BlockEntityType<HorseEngineBE>> HORSE_ENGINE_TYPE = type("horse_engine", HorseEngineBE::new, FABlocks.HORSE_ENGINE);
	public static final Supplier<BlockEntityType<BrickFireboxBE>> BRICK_FIREBOX_TYPE = type("brick_firebox", BrickFireboxBE::new, FABlocks.BRICK_FIREBOX);
	public static final Supplier<BlockEntityType<PaperBellowsBE>> PAPER_BELLOWS_TYPE = type("paper_bellows", PaperBellowsBE::new, FABlocks.PAPER_BELLOWS);
	public static final Supplier<BlockEntityType<LeatherBellowsBE>> LEATHER_BELLOWS_TYPE = type("leather_bellows", LeatherBellowsBE::new, FABlocks.LEATHER_BELLOWS);
	public static final Supplier<BlockEntityType<TripHammerBE>> TRIP_HAMMER_TYPE = type("trip_hammer", TripHammerBE::new, FABlocks.TRIP_HAMMER);
	public static final Supplier<BlockEntityType<TumblingBarrelBE>> TUMBLING_BARREL_TYPE = type("tumbling_barrel", TumblingBarrelBE::new, FABlocks.TUMBLING_BARREL);
	public static final Supplier<BlockEntityType<SmallTankBE>> SMALL_TANK_TYPE = type("small_tank", SmallTankBE::new, FABlocks.WOODEN_TANK);
	public static final Supplier<BlockEntityType<PipeBE>> PIPE_TYPE = type("pipe", PipeBE::new, FABlocks.COPPER_PIPE);
	public static final Supplier<BlockEntityType<FryingPanBE>> FRYING_PAN_TYPE = type("frying_pan", FryingPanBE::new, FABlocks.FRYING_PAN);
	public static final Supplier<BlockEntityType<BambooBasketBE>> BAMBOO_BASKET_TYPE = type("bamboo_basket", BambooBasketBE::new, FABlocks.BAMBOO_BASKET);
	public static final Supplier<BlockEntityType<BrickKilnBE>> BRICK_KILN_TYPE = type("brick_kiln", BrickKilnBE::new, FABlocks.BRICK_KILN);
	public static final Supplier<BlockEntityType<SteamOvenBE>> STEAM_OVEN_TYPE = type("steam_oven", SteamOvenBE::new, FABlocks.STEAM_OVEN);

	@SafeVarargs
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> type(String name, BlockEntityType.BlockEntitySupplier<T> factory,
																									   Supplier<? extends Block>... validBlocks)
	{
		//noinspection DataFlowIssue (we don't need datafixerupper for our be's)
		return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(factory, Arrays.stream(validBlocks).map(Supplier::get).toArray(Block[]::new)).build(null));
	}

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> type(String name, BlockEntityType.BlockEntitySupplier<T> factory,
																									   Collection<? extends Supplier<? extends Block>> validBlocks)
	{
		//noinspection DataFlowIssue
		return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(factory, validBlocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
	}
}
