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
												 .layer("ifflfi")
												 .layer("caalaa")
												 .define('c', FABlocks.TRIP_HAMMER)
												 .define('l', Blocks.OAK_LOG.defaultBlockState())
												 .define('f', Blocks.OAK_FENCE)
												 .define('i', Blocks.IRON_BLOCK)
												 .define('a', Blocks.AIR)
												 .setOffset(new Vec3i(0, 0, 0)).build();
		MultiblockRegistry.register(tripHammer);
		TRIP_HAMMER = tripHammer;
	}
}
