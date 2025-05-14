package boblovespi.factoryautomation.common.blockentity.logistics;

import boblovespi.factoryautomation.common.block.logistics.SmallTank;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

public class SmallTankBE extends FABE
{
	private final FluidTank tank;

	public SmallTankBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.SMALL_TANK_TYPE.get(), pos, state);
		var capacity = 0;
		if (state.getBlock() instanceof SmallTank smallTank)
			capacity = smallTank.capacity;
		else
			throw new RuntimeException("Tank block entities must be for tank block?!?!?");
		tank = new FluidTank(capacity)
		{
			@Override
			protected void onContentsChanged()
			{
				setChanged();
			}
		};
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("tank", tank.writeToNBT(registries, new CompoundTag()));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		tank.readFromNBT(registries, tag.getCompound("tank"));
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

	public IFluidHandler fluidHandler(@Nullable Direction direction)
	{
		return tank;
	}
}
