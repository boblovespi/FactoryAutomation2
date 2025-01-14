package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class BrickFireboxBE extends FABE
{
	public BrickFireboxBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.BRICK_FIREBOX_TYPE.get(), pPos, pBlockState);
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{

	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{

	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{

	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{

	}

	@Override
	public void onDestroy()
	{

	}
}
