package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.processing.FryingPanBE;
import boblovespi.factoryautomation.common.sound.FASounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FryingPan extends Block implements EntityBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<Temperature> TEMPERATURE = EnumProperty.create("temperature", Temperature.class);
	public static final BooleanProperty SUPPORT = BooleanProperty.create("supported");
	private static final VoxelShape BOUNDING_BOX = Block.box(1, 0, 1, 15, 4, 15);

	public FryingPan(Properties p)
	{
		super(p);
		registerDefaultState(defaultBlockState().setValue(TEMPERATURE, Temperature.COLD).setValue(SUPPORT, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new FryingPanBE(pPos, pState);
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
	{
		level.scheduleTick(pos, this, 20 * 30);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		var below = level.getBlockState(pos.below());
		if (below.is(FATags.Blocks.PAN_HEAT_SOURCE) && below.getOptionalValue(BlockStateProperties.LIT).orElse(true))
			level.setBlockAndUpdate(pos, state.setValue(TEMPERATURE, Temperature.HOT));
		else
			level.setBlockAndUpdate(pos, state.setValue(TEMPERATURE, Temperature.COLD));
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return BOUNDING_BOX;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;
		level.getBlockEntity(pos, FABETypes.FRYING_PAN_TYPE.get()).ifPresent(b -> b.takeOrPlace(stack, player));
		return ItemInteractionResult.CONSUME;
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.FRYING_PAN_TYPE.get()).ifPresent(FryingPanBE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP) || level.getBlockState(pos.below()).is(BlockTags.CAMPFIRES);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos)
	{
		level.scheduleTick(pos, this, 20 * 30);
		return direction != Direction.DOWN || canSurvive(state, level, pos) ? state.setValue(SUPPORT,
				level.getBlockState(pos.below()).is(BlockTags.CAMPFIRES)) : Blocks.AIR.defaultBlockState();
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> beType)
	{
		if (!level.isClientSide && state.getValue(TEMPERATURE) == Temperature.HOT)
			return ITickable.makeTicker(FABETypes.FRYING_PAN_TYPE.get(), beType);
		return null;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, TEMPERATURE, SUPPORT);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if (state.getValue(TEMPERATURE) == Temperature.HOT && level.getBlockEntity(pos, FABETypes.FRYING_PAN_TYPE.get()).map(FryingPanBE::isCooking).orElse(false))
		{
			var d0 = pos.getX() + 0.5;
			var d1 = pos.getY() - 1;
			var d2 = pos.getZ() + 0.5;
			if (random.nextDouble() < 0.4)
				level.playLocalSound(d0, d1, d2, FASounds.PAN_SIZZLES.get(), SoundSource.BLOCKS, 0.3F, 1.5F, false);

			// var direction = state.getValue(FACING).getClockWise();
			// var direction$axis = direction.getAxis();
			// var d4 = random.nextDouble() * 0.6 - 0.3;
			// var d5 = direction$axis == Direction.Axis.X ? direction.getStepX() * 0.52 : d4;
			// var d6 = random.nextDouble() * 6.0 / 16.0;
			// var d7 = direction$axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : d4;
			// level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0);
			// level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0);
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	public enum Temperature implements StringRepresentable
	{
		COLD, HOT;

		@Override
		public String getSerializedName()
		{
			return switch (this)
			{
				case COLD -> "cold";
				case HOT -> "hot";
			};
		}
	}
}
