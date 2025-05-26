package boblovespi.factoryautomation.common.block.logistics;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.logistics.PipeBE;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

public class Pipe extends Block implements EntityBlock
{
	private static final EnumProperty<Connection> WEST = EnumProperty.create("west", Connection.class);
	private static final EnumProperty<Connection> EAST = EnumProperty.create("east", Connection.class);
	private static final EnumProperty<Connection> SOUTH = EnumProperty.create("south", Connection.class);
	private static final EnumProperty<Connection> NORTH = EnumProperty.create("north", Connection.class);
	private static final EnumProperty<Connection> DOWN = EnumProperty.create("down", Connection.class);
	private static final EnumProperty<Connection> UP = EnumProperty.create("up", Connection.class);

	public static final EnumProperty<Connection>[] CONNECTIONS = new EnumProperty[] {DOWN, UP, NORTH, SOUTH, WEST, EAST};

	private static final VoxelShape CENTER_BB = box(4, 4, 4, 12, 12, 12);
	private static final VoxelShape UP_BB = box(4, 12, 4, 12, 16, 12);
	private static final VoxelShape DOWN_BB = box(4, 0, 4, 12, 4, 12);
	private static final VoxelShape NORTH_BB = box(4, 4, 0, 12, 12, 4);
	private static final VoxelShape SOUTH_BB = box(4, 4, 12, 12, 12, 16);
	private static final VoxelShape WEST_BB = box(0, 4, 4, 4, 12, 12);
	private static final VoxelShape EAST_BB = box(12, 4, 4, 16, 12, 12);

	public static final MapCodec<Pipe> CODEC = simpleCodec(Pipe::new);

	@Override
	protected RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.INVISIBLE;
	}

	public Pipe(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any()
											.setValue(UP, Connection.NONE)
											.setValue(DOWN, Connection.NONE)
											.setValue(NORTH, Connection.NONE)
											.setValue(SOUTH, Connection.NONE)
											.setValue(EAST, Connection.NONE)
											.setValue(WEST, Connection.NONE));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new PipeBE(pos, state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		return Shapes.or(CENTER_BB, state.getValue(WEST) != Connection.NONE ? WEST_BB : Shapes.empty(),
				state.getValue(EAST) != Connection.NONE ? EAST_BB : Shapes.empty(),
				state.getValue(SOUTH) != Connection.NONE ? SOUTH_BB : Shapes.empty(),
				state.getValue(NORTH) != Connection.NONE ? NORTH_BB : Shapes.empty(),
				state.getValue(UP) != Connection.NONE ? UP_BB : Shapes.empty(),
				state.getValue(DOWN) != Connection.NONE ? DOWN_BB : Shapes.empty());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(CONNECTIONS);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		var state = defaultBlockState();
		var world = context.getLevel();
		var pos = context.getClickedPos();
		for (int i = 0; i < CONNECTIONS.length; i++)
			state = state.setValue(CONNECTIONS[i], getConnectionFor(world, pos, Direction.values()[i]));
		return state;
	}

	private Connection getConnectionFor(LevelAccessor level, BlockPos pos, Direction direction)
	{
		pos = pos.relative(direction);
		if (level.getBlockState(pos).getBlock() == this)
			return Connection.JOIN;
		if (level instanceof Level l && l.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction.getOpposite()) != null)
			return Connection.CONNECTOR;
		return Connection.NONE;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
	{
		return state.setValue(CONNECTIONS[facing.ordinal()], getConnectionFor(level, currentPos, facing));
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
	{
		if (level.isClientSide)
			return;
		if (state.is(oldState.getBlock()) && level.getBlockEntity(pos) instanceof PipeBE pipe)
		{
			for (var dir : Direction.values())
			{
				if (state.getValue(CONNECTIONS[dir.ordinal()]) == Connection.CONNECTOR)
					pipe.addIONode(dir);
				else
					pipe.removeIONode(dir);
			}
			return;
		}
		level.getBlockEntity(pos, FABETypes.PIPE_TYPE.get()).ifPresent(PipeBE::onPlace);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (!state.is(newState.getBlock()))
			level.getBlockEntity(pos, FABETypes.PIPE_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	public enum Connection implements StringRepresentable
	{
		NONE("none"), CONNECTOR("connector"), JOIN("join");

		private final String name;

		Connection(String name)
		{
			this.name = name;
		}

		@Override
		public String getSerializedName()
		{
			return name;
		}
	}
}
