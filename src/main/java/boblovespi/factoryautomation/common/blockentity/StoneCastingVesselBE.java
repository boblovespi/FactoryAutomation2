package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.processing.StoneCastingVessel;
import boblovespi.factoryautomation.common.menu.StoneCastingVesselMenu;
import boblovespi.factoryautomation.common.util.ICastingVessel;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.Metal;
import boblovespi.factoryautomation.common.util.jei.CasterType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;
import java.util.function.Function;

public class StoneCastingVesselBE extends FABE implements ICastingVessel, ITickable, IMenuProviderProvider
{
	private final ItemStackHandler inv;
	private float temp;

	public StoneCastingVesselBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.STONE_CASTING_VESSEL_TYPE.get(), pos, state);
		inv = new ItemStackHandler(1);
		temp = 0;
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("inv", inv.serializeNBT(registries));
		tag.putFloat("temp", temp);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		temp = tag.getFloat("temp");
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		save(tag, registries);
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		load(tag, registries);
	}

	@Override
	public void onDestroy()
	{
		if (temp < 40 + 273 - 300)
			ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
		var mold = getBlockState().getValue(StoneCastingVessel.MOLD);
		if (mold != StoneCastingVessel.CastingVesselStates.EMPTY)
			ItemHelper.dropItem(level, worldPosition.getCenter(), new ItemStack(FABlocks.GREEN_SAND));
	}

	@Override
	public void cast(Function<Integer, Optional<Metal>> metalSource)
	{
		var mold = getBlockState().getValue(StoneCastingVessel.MOLD);
		if (mold == StoneCastingVessel.CastingVesselStates.EMPTY || mold == StoneCastingVessel.CastingVesselStates.SAND || !inv.getStackInSlot(0).isEmpty())
			return;
		var mult = CasterType.STONE.efficiencies().get(mold.metalForm);
		var result = metalSource.apply((int) (mold.metalForm.amount() * mult));
		result.ifPresent(metal -> {
			inv.setStackInSlot(0, Metal.itemForMetalAndForm(metal, mold.metalForm).getDefaultInstance());
			temp = metal.meltTemp() - 300;
		});
		setChangedAndUpdateClient();
	}

	public void takeItem(Player player)
	{
		if (!inv.getStackInSlot(0).isEmpty())
		{
			if (temp < 40 + 273 - 300)
			{
				var taken = inv.extractItem(0, 64, false);
				ItemHelper.putItemsInInventoryOrDrop(player, taken, level);
				ItemHelper.putItemsInInventoryOrDrop(player, new ItemStack(FABlocks.GREEN_SAND), level);
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(StoneCastingVessel.MOLD, StoneCastingVessel.CastingVesselStates.EMPTY));
				setChangedAndUpdateClient();
			}
			else
			{
				player.hurt(level.damageSources().generic(), (temp - 40f) / (temp + 100f) * 20f);
				player.displayClientMessage(Component.translatable("info.too_hot", String.format("%1$.1f K", temp + 300)), true);
			}
		}
	}

	@Override
	public void tick()
	{
		if (temp > 0)
		{
			if (level.isRaining())
				temp *= 0.9938f;
			else
				temp *= 0.9972f;
			setChangedAndUpdateClient();
		}
	}

	public ItemStack getRenderStack()
	{
		if (level.isClientSide)
			return inv.getStackInSlot(0);
		return ItemStack.EMPTY;
	}

	public float getRenderTemp()
	{
		if (level.isClientSide)
			return temp;
		return 0;
	}

	@Override
	public MenuProvider getMenuProvider()
	{
		return new SimpleMenuProvider((i, v, p) -> new StoneCastingVesselMenu(i, v, new StoneCastingVesselBE.Data(), ContainerLevelAccess.create(level, worldPosition)),
				Component.translatable("gui.stone_casting_vessel.name"));
	}

	private class Data implements ContainerData
	{
		@Override
		public int get(int pIndex)
		{
			return getBlockState().getValue(StoneCastingVessel.MOLD).ordinal() - 1;
		}

		@Override
		public void set(int pIndex, int pValue)
		{
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(StoneCastingVessel.MOLD, StoneCastingVessel.CastingVesselStates.values()[pValue + 1]));
		}

		@Override
		public int getCount()
		{
			return 1;
		}
	}
}
