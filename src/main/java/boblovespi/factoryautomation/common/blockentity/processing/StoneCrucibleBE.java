package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IMenuProviderProvider;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.menu.StoneFoundryMenu;
import boblovespi.factoryautomation.common.multiblock.IMultiblockBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Objects;

public class StoneCrucibleBE extends FABE implements IMultiblockBE, ITickable, IMenuProviderProvider
{
	private boolean breaking;
	private final CrucibleManager crucible;
	private final BurnerManager burner;
	private final HeatManager heat;
	private final ItemStackHandler inv;
	private final float efficiency = 0.5f;
	private float meltProgress;
	private float amount;
	private int color;

	public StoneCrucibleBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.STONE_CRUCIBLE_TYPE.get(), pos, state);
		crucible = new CrucibleManager.Single("crucible", Form.INGOT.amount() * 9 * 3);
		inv = new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
			}

			@Override
			public boolean isItemValid(int slot, ItemStack stack)
			{
				if (slot == 0)
					return stack.getItemHolder().getData(FuelInfo.FUEL_DATA) != null;
				return super.isItemValid(slot, stack);
			}
		};
		heat = new HeatManager("heat", 2300 * 1000, 300);
		burner = new BurnerManager("burner", () -> inv.getStackInSlot(0), this::takeFuel, (t, e) -> {
			if (t * efficiency + 273 * (1 - efficiency) >= heat.getTemperature())
				heat.heat(e * efficiency * 0.5f);
		});
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		crucible.save(tag);
		burner.save(tag);
		heat.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
		tag.putFloat("meltProgress", meltProgress);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		crucible.load(tag);
		burner.load(tag);
		heat.load(tag);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		meltProgress = tag.getFloat("meltProgress");
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.putFloat("amount", (float) crucible.getAmount() / (Form.INGOT.amount() * 9 * 3));
		tag.putInt("color", crucible.getCurrentMetal().color());
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		amount = tag.getFloat("amount");
		color = tag.getInt("color");
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

	public void pour(ICastingVessel castingVessel)
	{
		setChangedAndUpdateClient();
		castingVessel.cast(crucible::pour);
		heat.setHeatCapacity(crucible.getHeatCapacity() + 2300 * 1000);
	}

	@Override
	public void tick()
	{
		burner.progress();
		var meltStack = inv.getStackInSlot(1);
		if (!meltStack.isEmpty())
		{
			var metal = Metal.fromStack(meltStack);
			var form = Form.fromStack(meltStack);
			if ((metal == crucible.getCurrentMetal() || crucible.getCurrentMetal() == Metal.UNKNOWN) && crucible.getSpace() >= form.amount())
			{
				var meltTemp = metal.meltTemp();
				if (form == Form.SHARD)
					meltTemp = (int) (meltTemp * 0.75f);
				var shc = metal.massHeatCapacity() * metal.density() * form.fractionOfBlock();
				if (heat.getTemperature() > meltTemp)
				{
					// we subtract 300 because we assume our ore is at room temperature (300 K); we divide by 200 as we want to take 200 ticks to melt at max speed
					var energyIfMaxMeltTime = shc * (metal.meltTemp() - 300) / 200;
					var energyIfMaxTempTaken = (heat.getTemperature() - meltTemp) * heat.getHeatCapacity();
					var actualEnergyConsumed = Math.min(energyIfMaxMeltTime, energyIfMaxTempTaken);
					heat.cool(actualEnergyConsumed);
					var fractionOfMaxMeltSpeed = actualEnergyConsumed / energyIfMaxMeltTime;
					meltProgress += fractionOfMaxMeltSpeed / 200;
				}
				else
					meltProgress -= 0.01f;
				if (meltProgress >= 1)
				{
					meltProgress = 0;
					inv.extractItem(1, 1, false);
					heat.increaseHeatCapacity(shc);
					crucible.melt(metal, form.amount());
					setChangedAndUpdateClient();
				}
			}
			else
				meltProgress = 0;
		}
		else
			meltProgress = 0;
		if (meltProgress < 0)
			meltProgress = 0;
		setChanged();
	}

	public void addCoal(ItemStack coal)
	{
		inv.insertItem(0, coal, false);
	}

	@Override
	public MenuProvider getMenuProvider()
	{
		return new SimpleMenuProvider((i, v, p) -> new StoneFoundryMenu(i, v, inv, new Data(), ContainerLevelAccess.create(level, worldPosition)),
				Component.translatable("gui.stone_crucible.name"));
	}

	private FuelInfo takeFuel()
	{
		var stack = inv.extractItem(0, 1, false);
		if (stack.getItemHolder().getData(FuelInfo.FUEL_DATA) == null)
		{
			inv.setStackInSlot(0, ItemStack.EMPTY);
			FactoryAutomation.LOGGER.error("Stack has no fuel data!");
		}
		return Objects.requireNonNullElse(stack.getItemHolder().getData(FuelInfo.FUEL_DATA), new FuelInfo(0, 0, 0));
	}

	public float getAmount()
	{
		return amount;
	}

	public int getColor()
	{
		return color;
	}

	private class Data implements ContainerData
	{
		@Override
		public int get(int index)
		{
			return switch (index)
			{
				case 0 -> Float.floatToIntBits(burner.getBurnTime());
				case 1 -> Float.floatToIntBits(heat.getTemperature());
				case 2 -> Float.floatToIntBits(meltProgress);
				case 3 -> crucible.getAmount();
				case 4 -> crucible.getCurrentMetal().id();
				default -> 0;
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
