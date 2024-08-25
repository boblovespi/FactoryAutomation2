package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.blockentity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PowerShaft extends RotatedPillarBlock implements EntityBlock
{
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
	private static final VoxelShape[] SHAPES = new VoxelShape[] {Block.box(0, 6.5, 6.5, 16, 9.5, 9.5),
			Block.box(6.5, 0, 6.5, 9.5, 16, 9.5), Block.box(6.5, 6.5, 0, 9.5, 9.5, 16)};
	public final float maxSpeed;
	public final float maxTorque;

	public PowerShaft(Properties p_49795_, float maxSpeed, float maxTorque)
	{
		super(p_49795_);
		this.maxSpeed = maxSpeed;
		this.maxTorque = maxTorque;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new PowerShaftBE(pPos, pState);
	}

	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack)
	{
		super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return SHAPES[pState.getValue(AXIS).ordinal()];
	}

	@Override
	protected RenderShape getRenderShape(BlockState pState)
	{
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return IClientTickable.makeTicker(FABETypes.POWER_SHAFT_TYPE.get(), beType);
		return null;
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.POWER_SHAFT_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
	{
		var dir = Direction.getNearest(Vec3.atLowerCornerOf(neighborPos.subtract(pos)));
		level.getBlockEntity(pos, FABETypes.POWER_SHAFT_TYPE.get()).ifPresent(PowerShaftBE::updateInputs);
	}
}
