package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.mechanical.*;
import boblovespi.factoryautomation.common.block.processing.*;
import boblovespi.factoryautomation.common.block.resource.ResourceRock;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.OreQualities;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.util.StoneBlockForms;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FABlocks
{
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FactoryAutomation.MODID);

	// Building blocks

	public static final DeferredBlock<Block> CHERT = register("chert", Block::new, BlockProperties.CHERT);
	public static final Map<StoneBlockForms, DeferredBlock<? extends Block>> ANDESITE_BRICKS = StoneBlockForms.all().stream().collect(Collectors.toMap(k -> k,
			form -> registerStoneBlockForms(form, "andesite_brick", BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE), FABlocks::andesiteBricks)));
	public static final Map<StoneBlockForms, DeferredBlock<? extends Block>> GRANITE_BRICKS = StoneBlockForms.all().stream().collect(Collectors.toMap(k -> k,
			form -> registerStoneBlockForms(form, "granite_brick", BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE), FABlocks::graniteBricks)));
	public static final Map<StoneBlockForms, DeferredBlock<? extends Block>> DIORITE_BRICKS = StoneBlockForms.all().stream().collect(Collectors.toMap(k -> k,
			form -> registerStoneBlockForms(form, "diorite_brick", BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE), FABlocks::dioriteBricks)));

	// Resources

	public static final List<DeferredBlock<Rock>> ROCKS = Arrays.stream(Rock.Variants.values()).map(v -> register(v.getRockName(), () -> new Rock(v))).toList();
	public static final DeferredBlock<Rock> COBBLESTONE_ROCK = ROCKS.getFirst();
	public static final DeferredBlock<ResourceRock> FLINT_ROCK = register("flint_rock", p -> new ResourceRock(p, Items.FLINT), BlockProperties.ROCK);
	public static final DeferredBlock<Block> CASSITERITE_ORE = register("cassiterite_ore", Block::new, BlockProperties.ORE);
	public static final DeferredBlock<Block> RAW_CASSITERITE_BLOCK = register("raw_cassiterite_block", Block::new, BlockProperties.RAW_ORE(MapColor.COLOR_BLACK));
	public static final Map<OreQualities, DeferredBlock<Block>> LIMONITE_ORES = OreQualities.ore().stream().collect(
			Collectors.toMap(k -> k, k -> register(k.getName() + "_limonite_ore", Block::new, BlockProperties.ORE)));
	public static final DeferredBlock<Block> RAW_LIMONITE_BLOCK = register("raw_limonite_block", Block::new, BlockProperties.RAW_ORE(MapColor.COLOR_ORANGE));
	public static final DeferredBlock<Block> ANCIENT_IRON_BLOCK = register("ancient_iron_block", Block::new, BlockProperties.METAL(MapColor.METAL));
	public static final DeferredBlock<Block> WEAK_IRON_BLOCK = register("weak_iron_block", Block::new, BlockProperties.METAL(MapColor.METAL));
	public static final DeferredBlock<Block> IRON_SAND = register("iron_sand", p -> new ColoredFallingBlock(new ColorRGBA(14934489), p), BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND));
	public static final DeferredBlock<Block> IRON_SAND_CHARCOAL_MIX = register("iron_sand_charcoal_mix", p -> new ColoredFallingBlock(new ColorRGBA(14934489), p), BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND));
	//public static final Block SAND = register("sand", new ColoredFallingBlock(new ColorRGBA(14406560), BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));

	// Refined materials

	public static final DeferredBlock<Block> GREEN_SAND = register("green_sand", Block::new, BlockProperties.GREEN_SAND);
	public static final DeferredBlock<Block> CHARCOAL_PILE = register("charcoal_pile", Block::new, BlockProperties.CHARCOAL_PILE);
	public static final DeferredBlock<Block> IRON_BLOOM = register("iron_bloom", p -> new DropExperienceBlock(UniformInt.of(1, 4), p), BlockProperties.IRON_BLOOM);
	public static final DeferredBlock<Block> DRIED_BRICKS = register("dried_bricks", Block::new, BlockProperties.DRIED_BRICKS);

	public static final DeferredBlock<Block> TIN_BLOCK = register("tin_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.COLOR_LIGHT_BLUE));
	public static final DeferredBlock<Block> LEAD_BLOCK = register("lead_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.COLOR_PURPLE));
	public static final DeferredBlock<Block> BRONZE_BLOCK = register("bronze_block", Block::new, BlockProperties.METAL(MapColor.GOLD));
	public static final DeferredBlock<Block> NICKEL_BLOCK = register("nickel_block", Block::new, BlockProperties.METAL(MapColor.SAND));
	public static final DeferredBlock<Block> SILVER_BLOCK = register("silver_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.WOOL));
	public static final DeferredBlock<Block> MAGMATIC_BRASS_BLOCK = register("magmatic_brass_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.FIRE));
	public static final DeferredBlock<Block> PIG_IRON_BLOCK = register("pig_iron_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.TERRACOTTA_LIGHT_GRAY));
	public static final DeferredBlock<Block> STEEL_BLOCK = register("steel_block", Block::new, BlockProperties.METAL(MapColor.TERRACOTTA_CYAN));
	public static final DeferredBlock<Block> ALUMINUM_BLOCK = register("aluminum_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.GLOW_LICHEN));
	public static final DeferredBlock<Block> ALUMINUM_BRONZE_BLOCK = register("aluminum_bronze_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.RAW_IRON));
	public static final DeferredBlock<Block> CHROMIUM_BLOCK = register("chromium_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.SNOW));
	public static final DeferredBlock<Block> COPPER_PLATE_BLOCK = register("copper_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.COLOR_ORANGE));
	public static final DeferredBlock<Block> TIN_PLATE_BLOCK = register("tin_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.COLOR_LIGHT_BLUE));
	public static final DeferredBlock<Block> IRON_PLATE_BLOCK = register("iron_plate_block", Block::new, BlockProperties.METAL(MapColor.METAL));
	public static final DeferredBlock<Block> LEAD_PLATE_BLOCK = register("lead_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.COLOR_PURPLE));
	public static final DeferredBlock<Block> BRONZE_PLATE_BLOCK = register("bronze_plate_block", Block::new, BlockProperties.METAL(MapColor.GOLD));
	public static final DeferredBlock<Block> NICKEL_PLATE_BLOCK = register("nickel_plate_block", Block::new, BlockProperties.METAL(MapColor.SAND));
	public static final DeferredBlock<Block> SILVER_PLATE_BLOCK = register("silver_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.WOOL));
	public static final DeferredBlock<Block> MAGMATIC_BRASS_PLATE_BLOCK = register("magmatic_brass_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.FIRE));
	public static final DeferredBlock<Block> PIG_IRON_PLATE_BLOCK = register("pig_iron_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.TERRACOTTA_LIGHT_GRAY));
	public static final DeferredBlock<Block> STEEL_PLATE_BLOCK = register("steel_plate_block", Block::new, BlockProperties.METAL(MapColor.TERRACOTTA_CYAN));
	public static final DeferredBlock<Block> ALUMINUM_PLATE_BLOCK = register("aluminum_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.GLOW_LICHEN));
	public static final DeferredBlock<Block> ALUMINUM_BRONZE_PLATE_BLOCK = register("aluminum_bronze_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.RAW_IRON));
	public static final DeferredBlock<Block> CHROMIUM_PLATE_BLOCK = register("chromium_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.SNOW));
	public static final DeferredBlock<WaterloggedTransparentBlock> COPPER_SPACE_FRAME = register("copper_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.COLOR_ORANGE)));
	public static final DeferredBlock<WaterloggedTransparentBlock> TIN_SPACE_FRAME = register("tin_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.COLOR_LIGHT_BLUE)));
	public static final DeferredBlock<WaterloggedTransparentBlock> IRON_SPACE_FRAME = register("iron_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.METAL(MapColor.METAL)));
	public static final DeferredBlock<WaterloggedTransparentBlock> LEAD_SPACE_FRAME = register("lead_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.COLOR_PURPLE)));
	public static final DeferredBlock<WaterloggedTransparentBlock> BRONZE_SPACE_FRAME = register("bronze_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.METAL(MapColor.GOLD)));
	public static final DeferredBlock<WaterloggedTransparentBlock> NICKEL_SPACE_FRAME = register("nickel_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.METAL(MapColor.SAND)));
	public static final DeferredBlock<WaterloggedTransparentBlock> SILVER_SPACE_FRAME = register("silver_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.WOOL)));
	public static final DeferredBlock<WaterloggedTransparentBlock> MAGMATIC_BRASS_SPACE_FRAME = register("magmatic_brass_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.FIRE)));
	public static final DeferredBlock<WaterloggedTransparentBlock> PIG_IRON_SPACE_FRAME = register("pig_iron_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.TERRACOTTA_LIGHT_GRAY)));
	public static final DeferredBlock<WaterloggedTransparentBlock> STEEL_SPACE_FRAME = register("steel_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.METAL(MapColor.TERRACOTTA_CYAN)));
	public static final DeferredBlock<WaterloggedTransparentBlock> ALUMINUM_SPACE_FRAME = register("aluminum_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.GLOW_LICHEN)));
	public static final DeferredBlock<WaterloggedTransparentBlock> ALUMINUM_BRONZE_SPACE_FRAME = register("aluminum_bronze_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.RAW_IRON)));
	public static final DeferredBlock<WaterloggedTransparentBlock> CHROMIUM_SPACE_FRAME = register("chromium_space_frame", WaterloggedTransparentBlock::new, BlockProperties.SPACE_FRAME(BlockProperties.LIGHT_METAL(MapColor.SNOW)));

	// Building blocks

	public static final DeferredBlock<Block> BRICK_TILES = register("brick_tiles", Block::new, BlockProperties.BRICKS);

	// Processing

	public static final Map<WoodTypes, DeferredBlock<ChoppingBlock>> CHOPPING_BLOCKS = Arrays.stream(WoodTypes.values()).collect(
			Collectors.toMap(v -> v, v -> register(v.getName() + "_chopping_block", ChoppingBlock::new, BlockProperties.LOG(v.getColor()))));
	public static final DeferredBlock<ChoppingBlock> CHOPPING_BLOCK = CHOPPING_BLOCKS.get(WoodTypes.OAK);
	public static final DeferredBlock<LogPile> LOG_PILE = register("log_pile", LogPile::new, BlockProperties.LOG(MapColor.WOOD).lightLevel(s -> s.getValue(LogPileLike.ACTIVATED) ? 3 : 0));
	public static final DeferredBlock<OrePile> LIMONITE_CHARCOAL_MIX = register("limonite_charcoal_mix", p -> new OrePile(p, 20 * 60 * 5, IRON_BLOOM.get().defaultBlockState()), BlockProperties.RAW_ORE(MapColor.COLOR_ORANGE));
	public static final DeferredBlock<StoneCrucible> STONE_CRUCIBLE = register("stone_crucible", StoneCrucible::new, BlockProperties.COBBLESTONE_MACHINE);
	public static final DeferredBlock<StoneCastingVessel> STONE_CASTING_VESSEL = register("stone_casting_vessel", StoneCastingVessel::new, BlockProperties.COBBLESTONE_MACHINE);
	public static final DeferredBlock<StoneWorkbench> STONE_WORKBENCH = register("stone_workbench", StoneWorkbench::new, BlockProperties.COBBLESTONE_MACHINE);
	public static final DeferredBlock<BrickMakerFrame> BRICK_MAKER_FRAME = register("brick_maker_frame", BrickMakerFrame::new, BlockProperties.WOOD_MACHINE);
	public static final DeferredBlock<BrickCrucible> BRICK_CRUCIBLE = register("brick_crucible", BrickCrucible::new, BlockProperties.BRICK_MACHINE);
	public static final DeferredBlock<BrickFirebox> BRICK_FIREBOX = register("brick_firebox", BrickFirebox::new, BlockProperties.BRICK_MACHINE);
	public static final DeferredBlock<BrickCastingVessel> BRICK_CASTING_VESSEL = register("brick_casting_vessel", BrickCastingVessel::new, BlockProperties.BRICK_MACHINE);
	public static final DeferredBlock<Millstone> MILLSTONE = register("millstone", Millstone::new, BlockProperties.COBBLESTONE_MACHINE);
	public static final DeferredBlock<PaperBellows> PAPER_BELLOWS = register("paper_bellows", PaperBellows::new, BlockProperties.WOOD_MACHINE);
	public static final DeferredBlock<TripHammer> TRIP_HAMMER = register("trip_hammer", TripHammer::new, BlockProperties.IRON_MACHINE);

	// Mechanical

	public static final DeferredBlock<PowerShaft> WOOD_POWER_SHAFT = register("wood_power_shaft", p -> new PowerShaft(p, 2, 500), BlockProperties.WOOD_MACHINE_NO_OCCLUSION);
	public static final DeferredBlock<Gearbox> WOOD_GEARBOX = register("wood_gearbox", p -> new Gearbox(p, 1), BlockProperties.WOOD_MACHINE);
	public static final DeferredBlock<Splitter> WOOD_SPLITTER = register("wood_splitter", p -> new Splitter(p, 2, 500), BlockProperties.WOOD_MACHINE);
	public static final DeferredBlock<Joiner> WOOD_JOINER = register("wood_joiner", p -> new Joiner(p, 2, 500), BlockProperties.WOOD_MACHINE);
	public static final DeferredBlock<BevelGear> WOOD_BEVEL_GEAR = register("wood_bevel_gear", p -> new BevelGear(p, 2, 500), BlockProperties.WOOD_MACHINE);
	public static final DeferredBlock<PowerShaft> IRON_POWER_SHAFT = register("iron_power_shaft", p -> new PowerShaft(p, 5, 1000), BlockProperties.IRON_MACHINE_NO_OCCLUSION);
	public static final DeferredBlock<Gearbox> IRON_GEARBOX = register("iron_gearbox", p -> new Gearbox(p, 0.5f), BlockProperties.IRON_MACHINE);
	public static final DeferredBlock<Splitter> IRON_SPLITTER = register("iron_splitter", p -> new Splitter(p, 5, 1000), BlockProperties.IRON_MACHINE);
	public static final DeferredBlock<Joiner> IRON_JOINER = register("iron_joiner", p -> new Joiner(p, 5, 1000), BlockProperties.IRON_MACHINE);
	public static final DeferredBlock<BevelGear> IRON_BEVEL_GEAR = register("iron_bevel_gear", p -> new BevelGear(p, 5, 1000), BlockProperties.IRON_MACHINE);
	public static final DeferredBlock<HandCrank> HAND_CRANK = register("hand_crank",HandCrank::new, BlockProperties.WOOD_MACHINE_NO_OCCLUSION);
	public static final DeferredBlock<SmallWaterwheel> SMALL_WATERWHEEL = register("small_waterwheel", SmallWaterwheel::new, BlockProperties.WOOD_MACHINE_NO_OCCLUSION);

	// Misc

	public static final DeferredBlock<MultiblockPart> MULTIBLOCK_PART = register("multiblock_part", MultiblockPart::new, BlockBehaviour.Properties.of().noLootTable().noOcclusion());
	public static final DeferredBlock<CreativeMechanicalSource> CREATIVE_MECHANICAL_SOURCE = register("creative_mechanical_source", CreativeMechanicalSource::new, BlockBehaviour.Properties.of().noLootTable().strength(-1));

	private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> supplier)
	{
		return BLOCKS.register(name, supplier);
	}

	private static <T extends Block> DeferredBlock<T> register(String name, Function<BlockBehaviour.Properties, T> supplier, BlockBehaviour.Properties properties)
	{
		return BLOCKS.register(name, () -> supplier.apply(properties));
	}

	private static DeferredBlock<? extends Block> registerStoneBlockForms(StoneBlockForms form, String name, BlockBehaviour.Properties properties, Supplier<DeferredBlock<? extends Block>> base)
	{
		return switch (form)
		{
			case BLOCK -> register(name + form.getBrickName(), Block::new, properties);
			case STAIRS -> register(name + form.getBrickName(), p -> new StairBlock(base.get().get().defaultBlockState(), p), properties);
			case SLAB -> register(name + form.getBrickName(), SlabBlock::new, properties);
			case WALL -> register(name + form.getBrickName(), WallBlock::new, properties);
		};
	}

	private static DeferredBlock<? extends Block> andesiteBricks()
	{
		return ANDESITE_BRICKS.get(StoneBlockForms.BLOCK);
	}

	private static DeferredBlock<? extends Block> graniteBricks()
	{
		return GRANITE_BRICKS.get(StoneBlockForms.BLOCK);
	}

	private static DeferredBlock<? extends Block> dioriteBricks()
	{
		return DIORITE_BRICKS.get(StoneBlockForms.BLOCK);
	}
}
