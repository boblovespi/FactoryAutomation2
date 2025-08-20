package boblovespi.factoryautomation.common.multiblock;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("NotNullFieldNotInitialized")
public class Multiblocks
{
	public static Multiblock STONE_CRUCIBLE;
	public static Multiblock BRICK_CRUCIBLE;
	public static Multiblock TRIP_HAMMER;
	public static Multiblock LARGE_WATERWHEEL;
	public static Multiblock BRICK_KILN;

	public static void register()
	{
		var stoneCrucible = SimpleMultiblock.Builder.make("stone_crucible", 1, 2, 1)
													.layer("c")
													.layer("f")
													.define('f', Blocks.FURNACE)
													.define('c', FABlocks.STONE_CRUCIBLE)
													.setOffset(new Vec3i(0, 1, 0)).build();
		MultiblockRegistry.register(stoneCrucible);
		STONE_CRUCIBLE = stoneCrucible;

		var brickCrucible = SimpleMultiblock.Builder.make("brick_crucible", 1, 2, 1)
													.layer("c")
													.layer("f")
													.define('f', FABlocks.BRICK_FIREBOX)
													.define('c', FABlocks.BRICK_CRUCIBLE)
													.setOffset(new Vec3i(0, 1, 0)).build();
		MultiblockRegistry.register(brickCrucible);
		BRICK_CRUCIBLE = brickCrucible;
		var tripHammer = SimpleMultiblock.Builder.make("trip_hammer", 6, 2, 1)
												 .layer("ifflfs")
												 .layer("caalaa")
												 .define('c', FABlocks.TRIP_HAMMER)
												 .define('l', Blocks.OAK_LOG.defaultBlockState())
												 .define('f', Blocks.STRIPPED_SPRUCE_LOG)
												 .define('i', Blocks.IRON_BLOCK)
				.define('a', Blocks.AIR)
				.define('s', Blocks.CHISELED_STONE_BRICKS)
												 .setOffset(new Vec3i(0, 0, 0)).build();
		MultiblockRegistry.register(tripHammer);
		TRIP_HAMMER = tripHammer;

		var largeWaterwheel = SimpleMultiblock.Builder.make("large_waterwheel", 5, 5, 1)
									  .layer(" sps ")
									  .layer("s f s")
									  .layer("pfcfp")
									  .layer("s f s")
									  .layer(" sps ")
									  .define('s', Blocks.OAK_STAIRS)
									  .define('p', Blocks.OAK_PLANKS)
									  .define('f', Blocks.OAK_FENCE)
									  .define('c', FABlocks.LARGE_WATERWHEEL)
									  .setOffset(new Vec3i(2, 2, 0)).build();
		MultiblockRegistry.register(largeWaterwheel);
		LARGE_WATERWHEEL = largeWaterwheel;

		var brickKiln = SimpleMultiblock.Builder.make("brick_kiln", 3, 3, 3)
								.layer("bbb\nbbb\nbbb")
								.layer("bbb\ncbb\nbbb")
								.layer("bbb\nbbb\nbbb")
								.define('b', Blocks.BRICKS)
								.define('c', FABlocks.BRICK_KILN)
								.setOffset(new Vec3i(0, 1, 1)).build();
		MultiblockRegistry.register(brickKiln);
		BRICK_KILN = brickKiln;
	}
}
