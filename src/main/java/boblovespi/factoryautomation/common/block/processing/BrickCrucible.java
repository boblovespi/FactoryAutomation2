package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.api.capability.CastingCapability;
import boblovespi.factoryautomation.common.blockentity.BrickCrucibleBE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IMenuProviderProvider;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BrickCrucible extends Block implements EntityBlock
{
	public BrickCrucible(Properties p)
	{
		super(p);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new BrickCrucibleBE(pPos, pState);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult pHitResult)
	{
		if (!level.isClientSide)
		{
			var be = level.getBlockEntity(pos, FABETypes.BRICK_CRUCIBLE_TYPE.get()).orElseThrow();
			var castingVessel = level.getCapability(CastingCapability.BLOCK, pos.north().below(), Direction.UP);
				if (pHitResult.getDirection() == Direction.NORTH && castingVessel != null)
					be.pour(castingVessel);
				else
					player.openMenu(state.getMenuProvider(level, pos));
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos)
	{
		return pLevel.getBlockEntity(pPos, FABETypes.BRICK_CRUCIBLE_TYPE.get()).map(IMenuProviderProvider::getMenuProvider).orElse(null);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return null;
		return ITickable.makeTicker(FABETypes.BRICK_CRUCIBLE_TYPE.get(), beType);
	}
}
