package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.api.capability.HeatCapability;
import boblovespi.factoryautomation.common.block.processing.CopperBoiler;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IMenuProviderProvider;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.multiblock.IMultiblockBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.util.HeatManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public class CopperBoilerBE extends FABE implements ITickable, IMultiblockBE, IMenuProviderProvider
{
	private final HeatManager heatManager;
	private final FluidTank waterTank;
	private final FluidTank steamTank;
	private boolean breaking;

	public CopperBoilerBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.COPPER_BOILER_TYPE.get(), pos, state);
		// TODO: compute correct heat capacity
		heatManager = new HeatManager("heat", 1.5846e7f, 3000, 0.8f, 0.75f);
		waterTank = new FluidTank(10000)
		{
			@Override
			protected void onContentsChanged()
			{
				setChanged();
			}
		};
		steamTank = new FluidTank(100000)
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
		heatManager.save(tag);
		tag.put("waterTank", waterTank.writeToNBT(registries, new CompoundTag()));
		tag.put("steamTank", steamTank.writeToNBT(registries, new CompoundTag()));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		heatManager.load(tag);
		waterTank.readFromNBT(registries, tag.getCompound("waterTank"));
		steamTank.readFromNBT(registries, tag.getCompound("steamTank"));
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
		if (getBlockState().getValue(CopperBoiler.MULTIBLOCK_COMPLETE))
		{
			breaking = true;
			Multiblocks.COPPER_BOILER.destroy(level, worldPosition, getBlockState().getValue(CopperBoiler.FACING));
		}
	}

	@Override
	public MenuProvider getMenuProvider()
	{
		// TODO: add gui
		return null;
	}

	@Override
	public void tick()
	{
		if (heatManager.getTemperature() > 100 + 273)
		{
			var deltaT = heatManager.getTemperature() - (100 + 273);
			var energy = deltaT * heatManager.getHeatCapacity();
			var latentHeat = 2256_000f;
			var density = 1000f;
			var waterCanConvert = energy / (latentHeat * density); // m3
			var testDrain = waterTank.drain((int) (waterCanConvert * 1000), IFluidHandler.FluidAction.SIMULATE);
			var actualFill = steamTank.fill(new FluidStack(FAFluids.STEAM_SOURCE, testDrain.getAmount() * 10), IFluidHandler.FluidAction.EXECUTE);
			var actualDrain = waterTank.drain(actualFill / 10, IFluidHandler.FluidAction.EXECUTE);
			heatManager.cool(actualDrain.getAmount() / 1000f * (latentHeat * density));
		}
		var facing = getBlockState().getValue(CopperBoiler.FACING);

		var pos = worldPosition.below(2);
		for (int i = -1; i < 2; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				var pos2 = pos.relative(facing, j).relative(facing.getClockWise().getAxis(), i);
				var cap = level.getCapability(HeatCapability.BLOCK, pos2, Direction.UP);
				if (cap != null)
				{
					if (cap.canConvect(pos2, Direction.UP) && heatManager.canConvect(pos2.above(), Direction.DOWN))
					{
						heatManager.radiateWith(cap);
						heatManager.convectWith(cap, Direction.DOWN);
					}
					else
						heatManager.conductWith(cap);
				}
			}
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
			level.setBlock(worldPosition, getBlockState().setValue(CopperBoiler.MULTIBLOCK_COMPLETE, false), 2);
	}

	@Nullable
	@Override
	public <T> T getCapability(BlockPos offset, BlockCapability<T, Direction> capability, Direction dir)
	{
		if (capability == HeatCapability.BLOCK)
			return (T) heatManager;
		if (capability == Capabilities.FluidHandler.BLOCK)
		{
			if (offset.getY() > 0 && dir == Direction.UP)
				return (T) steamTank;
			else if (offset.getY() < 0)
				return (T) waterTank;
		}
		return null;
	}
}
