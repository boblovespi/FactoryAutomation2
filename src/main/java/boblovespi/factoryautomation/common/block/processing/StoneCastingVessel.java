package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.blockentity.StoneCastingVesselBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StoneCastingVessel extends Block implements EntityBlock
{
	public StoneCastingVessel(Properties p)
	{
		super(p);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new StoneCastingVesselBE(pPos, pState);
	}
}
