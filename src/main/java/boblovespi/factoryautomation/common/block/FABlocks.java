package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.processing.*;
import boblovespi.factoryautomation.common.block.resource.ResourceRock;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.OreQualities;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

	// Resources

	public static final List<DeferredBlock<Rock>> ROCKS = Arrays.stream(Rock.Variants.values()).map(v -> register(v.getRockName(), () -> new Rock(v))).toList();
	public static final DeferredBlock<Rock> COBBLESTONE_ROCK = ROCKS.getFirst();
	public static final DeferredBlock<ResourceRock> FLINT_ROCK = register("flint_rock", p -> new ResourceRock(p, Items.FLINT), BlockProperties.ROCK);
	public static final DeferredBlock<Block> CASSITERITE_ORE = register("cassiterite_ore", Block::new, BlockProperties.ORE);
	public static final DeferredBlock<Block> RAW_CASSITERITE_BLOCK = register("raw_cassiterite_block", Block::new, BlockProperties.RAW_ORE(MapColor.COLOR_BLACK));
	public static final Map<OreQualities, DeferredBlock<Block>> LIMONITE_ORES = OreQualities.ore().stream().collect(
			Collectors.toMap(k -> k, k -> register(k.getName() + "_limonite_ore", Block::new, BlockProperties.ORE)));
	public static final DeferredBlock<Block> RAW_LIMONITE_BLOCK = register("raw_limonite_block", Block::new, BlockProperties.RAW_ORE(MapColor.COLOR_ORANGE));

	// Refined materials

	public static final DeferredBlock<Block> GREEN_SAND = register("green_sand", Block::new, BlockProperties.GREEN_SAND);
	public static final DeferredBlock<Block> CHARCOAL_PILE = register("charcoal_pile", Block::new, BlockProperties.CHARCOAL_PILE);
	public static final DeferredBlock<Block> IRON_BLOOM = register("iron_bloom", p -> new DropExperienceBlock(UniformInt.of(1, 4), p), BlockProperties.IRON_BLOOM);

	public static final DeferredBlock<Block> TIN_BLOCK = register("tin_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.COLOR_LIGHT_BLUE));
	public static final DeferredBlock<Block> COPPER_PLATE_BLOCK = register("copper_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.COLOR_ORANGE));
	public static final DeferredBlock<Block> TIN_PLATE_BLOCK = register("tin_plate_block", Block::new, BlockProperties.LIGHT_METAL(MapColor.COLOR_LIGHT_BLUE));

	// Processing

	public static final Map<WoodTypes, DeferredBlock<ChoppingBlock>> CHOPPING_BLOCKS = Arrays.stream(WoodTypes.values()).collect(
			Collectors.toMap(v -> v, v -> register(v.getName() + "_chopping_block", ChoppingBlock::new, BlockProperties.LOG(v.getColor()))));
	public static final DeferredBlock<ChoppingBlock> CHOPPING_BLOCK = CHOPPING_BLOCKS.get(WoodTypes.OAK);
	public static final DeferredBlock<LogPile> LOG_PILE = register("log_pile", LogPile::new, BlockProperties.LOG(MapColor.WOOD).lightLevel(s -> s.getValue(LogPileLike.ACTIVATED) ? 3 : 0));
	public static final DeferredBlock<OrePile> LIMONITE_CHARCOAL_MIX = register("limonite_charcoal_mix", p -> new OrePile(p, 20 * 60 * 5, IRON_BLOOM.get().defaultBlockState()), BlockProperties.RAW_ORE(MapColor.COLOR_ORANGE));
	public static final DeferredBlock<StoneCrucible> STONE_CRUCIBLE = register("stone_crucible", StoneCrucible::new, BlockProperties.COBBLESTONE_MACHINE);
	public static final DeferredBlock<StoneCastingVessel> STONE_CASTING_VESSEL = register("stone_casting_vessel", StoneCastingVessel::new, BlockProperties.COBBLESTONE_MACHINE);
	public static final DeferredBlock<StoneWorkbench> STONE_WORKBENCH = register("stone_workbench", StoneWorkbench::new, BlockProperties.COBBLESTONE_MACHINE);

	// Misc

	public static final DeferredBlock<MultiblockPart> MULTIBLOCK_PART = register("multiblock_part", MultiblockPart::new, BlockBehaviour.Properties.of().noLootTable().noOcclusion());

	private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> supplier)
	{
		return BLOCKS.register(name, supplier);
	}

	private static <T extends Block> DeferredBlock<T> register(String name, Function<BlockBehaviour.Properties, T> supplier, BlockBehaviour.Properties properties)
	{
		return BLOCKS.register(name, () -> supplier.apply(properties));
	}
}
