package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.processing.BrickCastingVesselBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BrickCastingVessel extends Block implements EntityBlock
{
	public static final BooleanProperty MOLD = BooleanProperty.create("mold");
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape BOUNDING_BOX = Shapes.join(Shapes.block(), Block.box(2, 2, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);
	private static final VoxelShape FILLED_BOUNDING_BOX = Shapes.join(Shapes.block(), Block.box(2, 15, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);

	public BrickCastingVessel(Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(MOLD, false).setValue(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new BrickCastingVesselBE(pos, state);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MOLD, FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return state.getValue(MOLD) ? FILLED_BOUNDING_BOX : BOUNDING_BOX;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> beType)
	{
		if (level.isClientSide || !state.getValue(MOLD))
			return null;
		return ITickable.makeTicker(FABETypes.BRICK_CASTING_VESSEL_TYPE.get(), beType);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHitResult)
	{
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;
		if (stack.is(FATags.Items.FIRED_TALLOW_MOLDS) && !state.getValue(MOLD))
		{
			level.setBlockAndUpdate(pos, state.setValue(MOLD, true));
			level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS);
			level.getBlockEntity(pos, FABETypes.BRICK_CASTING_VESSEL_TYPE.get()).ifPresent(b -> b.placeItem(stack));
		}
		else if (state.getValue(MOLD))
			level.getBlockEntity(pos, FABETypes.BRICK_CASTING_VESSEL_TYPE.get()).ifPresent(b -> b.takeItem(player));
		return ItemInteractionResult.CONSUME;
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.BRICK_CASTING_VESSEL_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
}
