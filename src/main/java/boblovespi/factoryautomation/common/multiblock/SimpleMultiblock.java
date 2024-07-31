package boblovespi.factoryautomation.common.multiblock;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.MultiblockPartBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A simple orientable multiblock that accepts blockstate predicates.
 */
public class SimpleMultiblock extends Multiblock
{
	private static final Predicate<BlockState> ANY_STATE = b -> true;
	private final Predicate<BlockState>[][][] pattern;
	private final Vec3i offset;

	public SimpleMultiblock(ResourceLocation name, Predicate<BlockState>[][][] pattern, Vec3i offset)
	{
		super(name);
		this.pattern = pattern;
		this.offset = offset;
	}

	@Override
	public boolean isValid(Level level, BlockPos controllerPos, Direction facing)
	{
		return iterateOverMultiblock(level, controllerPos, facing, IterateAction.CHECK_VALID);
	}

	@Override
	public void build(Level level, BlockPos controllerPos, Direction facing)
	{
		iterateOverMultiblock(level, controllerPos, facing, IterateAction.BUILD);
	}

	@Override
	public void destroy(Level level, BlockPos controllerPos, Direction facing)
	{
		iterateOverMultiblock(level, controllerPos, facing, IterateAction.DESTROY);
	}

	private boolean iterateOverMultiblock(Level world, BlockPos controllerPos, Direction facing, IterateAction action)
	{
		var lowerLeftFront = addWithRotation(controllerPos, -offset.getX(), -offset.getY(), -offset.getZ(), facing);

		for (int x = 0; x < pattern.length; x++)
		{
			for (int y = 0; y < pattern[x].length; y++)
			{
				for (int z = 0; z < pattern[x][y].length; z++)
				{
					var loc = addWithRotation(lowerLeftFront, x, y, z, facing);
					var be = world.getBlockEntity(loc);
					var stateMatcher = pattern[x][y][z];

					//					System.out.println("loc = " + loc);
					//					System.out.println("relative = ( " + x + " , " + y + " , " + z + " )");
					//					System.out.println("stateMatcher = " + stateMatcher.GetBlock().getLocalizedName());
					//					System.out.println("block in world = " + world.getBlockState(loc).getBlock().getLocalizedName());

					if (be instanceof MultiblockPartBE bePart)
					{
						switch (action)
						{
							case CHECK_VALID ->
							{
								if (!bePart.isStructure(getName()))
									return false;
							}
							case DESTROY ->
							{
								if (!bePart.isBreaking())
								{
									bePart.markAsBreaking();
									world.setBlockAndUpdate(loc, bePart.getMultiblockState());
								}
							}
						}

					}
					else
					{
						switch (action)
						{
							case CHECK_VALID ->
							{
								if (!pattern[x][y][z].test(world.getBlockState(loc)))
									return false;
							}
							case BUILD ->
							{
								if (loc.equals(controllerPos))
									continue;
								if (stateMatcher == ANY_STATE)
									continue;

								var state = world.getBlockState(loc);
								world.setBlockAndUpdate(loc, FABlocks.MULTIBLOCK_PART.get().defaultBlockState());
								var newPart = world.getBlockEntity(loc, FABETypes.MULTIBLOCK_PART_TYPE.get());
								if (newPart.isPresent())
									newPart.get().setMultiblockInformation(SimpleMultiblock.this.getName(), new BlockPos(x, y, z),
											addWithRotation(addWithRotation(BlockPos.ZERO, x, y, z, facing), -offset.getX(), -offset.getY(), -offset.getZ(), facing), state,
											facing);
							}
						}
					}
				}
			}
		}

		var be = world.getBlockEntity(controllerPos);
		if (be instanceof IMultiblockBE imbe)
			switch (action)
			{
				case BUILD -> imbe.onMultiblockBuilt();
				case DESTROY -> imbe.onMultiblockDestroyed();
			}

		return true;
	}

	private static BlockPos addWithRotation(BlockPos pos, int x, int y, int z, Direction dir)
	{
		return switch (dir)
		{
			case NORTH -> pos.offset(-z, y, x);
			case SOUTH -> pos.offset(z, y, -x);
			case WEST -> pos.offset(x, y, z);
			case EAST -> pos.offset(-x, y, -z);
			default -> pos.offset(x, y, z);
		};
	}

	public static class Builder
	{
		private final ResourceLocation name;
		private final int sizeX;
		private final int sizeY;
		private final int sizeZ;
		private final char[][][] pattern;
		private final Predicate<BlockState>[] patternDef;
		private Vec3i offset;
		private int y;

		private Builder(ResourceLocation name, int sizeX, int sizeY, int sizeZ)
		{
			this.name = name;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.sizeZ = sizeZ;
			pattern = new char[sizeX][sizeY][sizeZ];
			patternDef = new Predicate[128];
			patternDef[' '] = patternDef[0] = ANY_STATE;
			y = sizeY - 1;
			offset = Vec3i.ZERO;
		}

		public static Builder make(String name, int sizeX, int sizeY, int sizeZ)
		{
			return new Builder(FactoryAutomation.name(name), sizeX, sizeY, sizeZ);
		}

		public Builder setOffset(Vec3i offset)
		{
			this.offset = offset;
			return this;
		}

		public Builder layer(String layer)
		{
			if (y < 0)
				throw new RuntimeException("Tried defining a layer for multiblock " + name + " that would make it taller than declared!");
			var slices = layer.split("\\r?\\n");
			for (int z = 0; z < slices.length; z++)
			{
				if (z >= sizeZ)
					throw new RuntimeException("Tried defining a layer for multiblock " + name + " that would make it longer than declared!");
				for (int x = 0; x < slices[z].length(); x++)
				{
					if (x >= sizeX)
						throw new RuntimeException("Tried defining a layer for multiblock " + name + " that would make it wider than declared!");
					var c = slices[z].charAt(x);
					if (c >= 128)
						throw new RuntimeException("Characters in multiblock definition for " + name + " must be in standard ASCII.");
					pattern[x][y][z] = c;
				}
			}
			y--;
			return this;
		}

		public Builder define(char c, Predicate<BlockState> p)
		{
			if (c >= 128)
				throw new RuntimeException("Characters in multiblock definition for " + name + " must be in standard ASCII.");
			patternDef[c] = p;
			return this;
		}

		public Builder define(char c, BlockState s)
		{
			return define(c, s::equals);
		}

		public Builder define(char c, Block b)
		{
			return define(c, s -> s.is(b));
		}

		public Builder define(char c, Supplier<? extends Block> b)
		{
			return define(c, s -> s.is(b.get()));
		}

		public Builder define(char c, TagKey<Block> b)
		{
			return define(c, s -> s.is(b));
		}

		public Builder defineAsAny(char c)
		{
			return define(c, ANY_STATE);
		}

		public SimpleMultiblock build()
		{
			Predicate<BlockState>[][][] realPattern = new Predicate[sizeX][sizeY][sizeZ];
			for (int x = 0; x < pattern.length; x++)
			{
				for (int y = 0; y < pattern[x].length; y++)
				{
					for (int z = 0; z < pattern[x][y].length; z++)
					{
						var pred = patternDef[pattern[x][y][z]];
						if (pred == null)
							throw new RuntimeException("Character" + pattern[x][y][z] + " in multiblock definition for " + name + " is lacking a predicate.");
						realPattern[x][y][z] = pred;
					}
				}
			}
			return new SimpleMultiblock(name, realPattern, offset);
		}
	}

	private enum IterateAction
	{
		BUILD, DESTROY, CHECK_VALID
	}
}
