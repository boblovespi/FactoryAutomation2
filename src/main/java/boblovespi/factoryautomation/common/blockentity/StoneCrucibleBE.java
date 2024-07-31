package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.multiblock.IMultiblockBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class StoneCrucibleBE extends FABE implements IMultiblockBE
{
	private boolean breaking;

	public StoneCrucibleBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.STONE_CRUCIBLE_TYPE.get(), pos, state);
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
		if (getBlockState().getValue(StoneCrucible.MULTIBLOCK_COMPLETE))
		{
			breaking = true;
			Multiblocks.STONE_CRUCIBLE.destroy(level, worldPosition, getBlockState().getValue(StoneCrucible.FACING));
		}
	}

	@Override
	public void onMultiblockBuilt()
	{

	}

	@Override
	public void onMultiblockDestroyed()
	{
		if (!breaking)
			level.setBlock(worldPosition, getBlockState().setValue(StoneCrucible.MULTIBLOCK_COMPLETE, false), 2);
	}
}
