package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.api.capability.HeatCapability;
import boblovespi.factoryautomation.common.block.processing.CopperBoiler;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IMenuProviderProvider;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.menu.CopperBoilerMenu;
import boblovespi.factoryautomation.common.multiblock.IMultiblockBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.util.HeatManager;
import boblovespi.factoryautomation.common.util.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public class CopperBoilerBE extends FABE implements ITickable, IMultiblockBE, IMenuProviderProvider
{
	public static final float VOLUME = 10f;
	public static final float LATENT_HEAT = 2256_000f;
	private static final float SPECIFIC_HEAT_OF_WATER = 4180f;
	private static final float WATER_DENSITY = 1000f; // kg/m3
	private static final float R_water = 461.52f; // J/kg K; https://www.engineeringtoolbox.com/individual-universal-gas-constant-d_588.html
	private static final float R = 8.314462618f; // ideal gas constant; wikipedia
	private static final int STEAM_IN_WATER = 100;
	private final HeatManager heatManager;
	private final FluidTank waterTank;
	private final FluidTank steamTank;
	private float toBoil = 0;
	private int toDrain = 0;
	private float waterTemp = 300;
	private float steamTemp = 373;
	private boolean breaking;
	private float calcification;
	private static final float BLEVE_THRESHOLD = 0.5f;

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

			@Override
			public int fill(FluidStack resource, FluidAction action)
			{
				var old = this.getFluidAmount();
				var filled = super.fill(resource, action);
				if (action == FluidAction.EXECUTE && filled > 0)
					waterTemp = (old * waterTemp + filled * 300) / (old + filled);
				return filled;
			}
		};
		steamTank = new FluidTank(10000)
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
		tag.putFloat("calcification", calcification);
		tag.putFloat("waterTemp", waterTemp);
		tag.putFloat("steamTemp", steamTemp);
		tag.putFloat("toBoil", toBoil);
		tag.putInt("toDrain", toDrain);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		heatManager.load(tag);
		waterTank.readFromNBT(registries, tag.getCompound("waterTank"));
		steamTank.readFromNBT(registries, tag.getCompound("steamTank"));
		calcification = tag.getFloat("calcification");
		waterTemp = tag.getFloat("waterTemp");
		steamTemp = tag.getFloat("steamTemp");
		toBoil = tag.getFloat("toBoil");
		toDrain = tag.getInt("toDrain");
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
		return new SimpleMenuProvider((i, v, p) -> new CopperBoilerMenu(i, v, new Data(), ContainerLevelAccess.create(level, worldPosition)),
				Component.translatable("gui.copper_boiler.name"));
	}

	@Override
	public void tick()
	{
		if (waterTank.getFluidAmount() > 0)
		{
			var pressure = computePressure();
			var pressureLimit = Mth.clampedMap(heatManager.getTemperature(), 450, 700, 1, 0);
			pressureLimit = pressureLimit * 300_000 + 250_000;
			if (pressure > pressureLimit)
			{
				steamTank.drain(10000, IFluidHandler.FluidAction.EXECUTE);
				waterTank.drain(10000, IFluidHandler.FluidAction.EXECUTE);
				var power = Math.clamp(pressure / pressureLimit, 1f, 2f);
				var pos = worldPosition.relative(getBlockState().getValue(CopperBoiler.FACING), -2).getCenter();
				level.explode(null, pos.x, pos.y, pos.z, power, false, Level.ExplosionInteraction.BLOCK);
				((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, pos.x, pos.y + 1.5f, pos.z,5, 1, 0.2, 1, 0.2);
				return;
			}
			var waterBoilTemp = computeWaterBoilTemp(pressure);
			var deltaTWater = heatManager.getTemperature() - waterTemp;
			var h_copper = 5000f;
			var h = 1 / (1 / h_copper + calcification / 100f);
			var q = h * deltaTWater;
			var A = 50;
			var energy = q * A * 0.05f;
			var shc = calculateHcOfWater();
			var tempChange = energy / shc;
			waterTemp += tempChange;
			heatManager.cool(energy);
			if (waterTemp > waterBoilTemp)
			{
				var deltaTBoil = waterTemp - waterBoilTemp;
				var waterCanConvert = shc * deltaTBoil / (LATENT_HEAT * WATER_DENSITY); // m3
				waterCanConvert = Math.clamp(waterCanConvert, 0, waterTank.getFluidAmount() / 1000f);
				if (waterCanConvert >= BLEVE_THRESHOLD)
				{
					// explode!
					steamTank.drain(10000, IFluidHandler.FluidAction.EXECUTE);
					waterTank.drain(10000, IFluidHandler.FluidAction.EXECUTE);
					var power = Math.clamp(waterCanConvert / BLEVE_THRESHOLD * 10 - 8, 2, 20);
					var pos = worldPosition.relative(getBlockState().getValue(CopperBoiler.FACING), -2).getCenter();
					level.explode(null, pos.x, pos.y, pos.z, power, false, Level.ExplosionInteraction.BLOCK);
					return;
				}
				waterCanConvert = Math.clamp(waterCanConvert, 0, steamTank.getSpace() / 1000f / STEAM_IN_WATER);
				toBoil += waterCanConvert;
				waterTemp -= waterCanConvert * LATENT_HEAT * WATER_DENSITY / shc;
				var mbCanProduce = Mth.floor(toBoil * 1000 * STEAM_IN_WATER);
				if (mbCanProduce > 0)
				{
					toBoil -= mbCanProduce / (1000f * STEAM_IN_WATER);
					steamTemp = (steamTank.getFluidAmount() * steamTemp + mbCanProduce * waterBoilTemp) / (steamTank.getFluidAmount() + mbCanProduce);
					steamTank.fill(new FluidStack(FAFluids.STEAM_SOURCE, mbCanProduce), IFluidHandler.FluidAction.EXECUTE);
					toDrain += mbCanProduce;
				}
			}
			var mbCanDrain = toDrain / STEAM_IN_WATER;
			if (mbCanDrain > 0)
			{
				toDrain -= mbCanDrain * STEAM_IN_WATER;
				waterTank.drain(mbCanDrain, IFluidHandler.FluidAction.EXECUTE);
				calcification += mbCanDrain / 1000f / 1000f;
				calcification = Math.clamp(calcification, 0, 1);
			}
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

	private float calculateHcOfWater()
	{
		return waterTank.getFluidAmount() / 1000f * WATER_DENSITY * SPECIFIC_HEAT_OF_WATER;
	}

	private float computeWaterBoilTemp(float pressure)
	{
		// Clausius-Clapeyron
		return 1 / (1 / (100 + 273f) + R_water / LATENT_HEAT * MathHelper.log(101_325f / pressure));
	}

	private float computePressure()
	{
		var steamMass = WATER_DENSITY * steamTank.getFluidAmount() / 1000f / STEAM_IN_WATER;
		return Math.max(steamMass * R_water * steamTemp / VOLUME, 101_325f);
	}

	@Override
	public void onMultiblockBuilt()
	{

	}

	@Override
	public void onMultiblockDestroyed()
	{
		var waterBoilTemp = computeWaterBoilTemp(101325);
		if (waterTemp > waterBoilTemp)
		{
			var deltaTBoil = waterTemp - waterBoilTemp;
			var shc = calculateHcOfWater();
			var waterCanConvert = shc * deltaTBoil / (LATENT_HEAT * WATER_DENSITY); // m3
			if (waterCanConvert >= BLEVE_THRESHOLD)
			{
				// explode!
				steamTank.drain(10000, IFluidHandler.FluidAction.EXECUTE);
				waterTank.drain(10000, IFluidHandler.FluidAction.EXECUTE);
				var power = Math.clamp(waterCanConvert / BLEVE_THRESHOLD * 10 - 8, 2, 20);
				var pos = worldPosition.relative(getBlockState().getValue(CopperBoiler.FACING), -2).getCenter();
				level.explode(null, pos.x, pos.y, pos.z, power, false, Level.ExplosionInteraction.BLOCK);
			}
		}

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

	private class Data implements ContainerData
	{
		@Override
		public int get(int index)
		{
			return switch (index)
			{
				case 0 -> Float.floatToIntBits(heatManager.getTemperature());
				case 1 -> Float.floatToIntBits(waterTemp);
				case 2 -> waterTank.getFluidAmount();
				case 3 -> steamTank.getFluidAmount();
				case 4 -> Float.floatToIntBits(computePressure());
				default -> throw new IndexOutOfBoundsException("copper boiler data only has 5 elements");
			};
		}

		@Override
		public void set(int index, int value)
		{

		}

		@Override
		public int getCount()
		{
			return 5;
		}
	}
}
