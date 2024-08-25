package boblovespi.factoryautomation.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public interface IClientTickable
{
	void tick();

	@Nullable
	static <T extends BlockEntity & IClientTickable, U extends BlockEntity> BlockEntityTicker<U> makeTicker(BlockEntityType<T> type, BlockEntityType<U> maybe)
	{
		if (maybe == type)
			return IClientTickable::tick;
		else
			return null;
	}

	private static <T extends BlockEntity> void tick(Level l, BlockPos p, BlockState s, T t)
	{
		((IClientTickable) t).tick();
	}
}
