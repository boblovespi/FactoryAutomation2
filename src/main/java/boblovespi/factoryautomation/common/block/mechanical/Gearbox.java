package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.mechanical.GearboxBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Gearbox extends Block implements EntityBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final VoxelShape BASE_BOX = Block.box(0, 0, 0, 16, 2, 16);
	public static final VoxelShape Z_BOX = Block.box(4, 2, 0, 12, 14, 16);
	public static final VoxelShape X_BOX = Block.box(0, 2, 4, 16, 14, 12);
	public static final VoxelShape Y_BOX = Block.box(4, 0, 2, 12, 16, 14);
	private static final VoxelShape[] COLLISON_BOXES = new VoxelShape[] {
			Shapes.or(Block.box(0, 0, 14, 16, 16, 16), Y_BOX, Block.box(2, 14, 0, 14, 16, 16)),
			Shapes.or(Block.box(0, 0, 0, 16, 16, 2), Y_BOX, Block.box(2, 0, 0, 14, 2, 16)),
			Shapes.or(Gearbox.BASE_BOX, Z_BOX, Block.box(2, 0, 14, 14, 16, 16)),
			Shapes.or(Gearbox.BASE_BOX, Z_BOX, Block.box(2, 0, 0, 14, 16, 2)),
			Shapes.or(Gearbox.BASE_BOX, X_BOX, Block.box(14, 0, 2, 16, 16, 14)),
			Shapes.or(Gearbox.BASE_BOX, X_BOX, Block.box(0, 0, 2, 2, 16, 14)),
	};

	public Gearbox(Properties p)
	{
		super(p);
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState pState)
	{
		return true;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new GearboxBE(pPos, pState);
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return COLLISON_BOXES[pState.getValue(FACING).ordinal()];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(FACING);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return IClientTickable.makeTicker(FABETypes.GEARBOX_TYPE.get(), beType);
		else
			return ITickable.makeTicker(FABETypes.GEARBOX_TYPE.get(), beType);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext)
	{
		return defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.GEARBOX_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
	{
		level.getBlockEntity(pos, FABETypes.GEARBOX_TYPE.get()).ifPresent(GearboxBE::updateInputs);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level level, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult)
	{
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;
		level.getBlockEntity(pPos, FABETypes.GEARBOX_TYPE.get()).ifPresent(b -> b.takeOrPlace(pStack, pPlayer));
		return ItemInteractionResult.CONSUME;
	}
}
