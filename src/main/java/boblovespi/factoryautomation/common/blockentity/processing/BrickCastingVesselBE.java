package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.common.FADamageTypes;
import boblovespi.factoryautomation.common.FAParticleTypes;
import boblovespi.factoryautomation.common.block.processing.BrickCastingVessel;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.ICastingVessel;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.Metal;
import boblovespi.factoryautomation.common.util.jei.CasterType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;
import java.util.function.Function;

public class BrickCastingVesselBE extends FABE implements ICastingVessel, ITickable
{
	private final ItemStackHandler inv;
	private float temp;

	public BrickCastingVesselBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.BRICK_CASTING_VESSEL_TYPE.get(), pPos, pBlockState);
		inv = new ItemStackHandler(2);
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
		else
			ItemHelper.dropItem(level, worldPosition.getCenter(), inv.getStackInSlot(0));
	}

	@Override
	public void tick()
	{
		if (temp > 0)
		{
			if (level.isRainingAt(worldPosition.above()))
				temp *= 0.9938f;
			else
				temp *= 0.9972f;
			setChangedAndUpdateClient();
		}
	}

	@Override
	public void cast(Function<Integer, Optional<Metal>> metalSource)
	{
		var moldStack = inv.getStackInSlot(0);
		var any = FAItems.FIRED_TALLOW_MOLDS.entrySet().stream().filter(e -> moldStack.is(e.getValue())).findAny();
		if (any.isEmpty())
			return;
		var form = any.get().getKey();
		var mult = CasterType.BRICK.efficiencies().get(form);
		var result = metalSource.apply((int) (form.amount() * mult));
		result.ifPresent(metal -> {
			inv.setStackInSlot(1, Metal.itemForMetalAndForm(metal, form).getDefaultInstance());
			if (level instanceof ServerLevel sl)
				inv.getStackInSlot(0).hurtAndBreak(1, sl, null, p -> {});
			temp = metal.meltTemp() - 300;
			if (temp > 0 && level instanceof ServerLevel sl)
				sl.sendParticles(FAParticleTypes.METAL_SPARK.get(), worldPosition.getX() + 0.5, worldPosition.getY() + 0.4, worldPosition.getZ() + 0.5, 50, 0.3, 0, 0.3, 0);
		});
		setChangedAndUpdateClient();
	}

	public void takeItem(Player player)
	{
		if (!inv.getStackInSlot(1).isEmpty())
		{
			if (temp < 40 + 273 - 300)
			{
				var taken = inv.extractItem(1, 64, false);
				ItemHelper.putItemsInInventoryOrDrop(player, taken, level);
				ItemHelper.putItemsInInventoryOrDrop(player, inv.extractItem(0, 64, false), level);
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BrickCastingVessel.MOLD, false));
				setChangedAndUpdateClient();
			}
			else
			{
				player.hurt(FADamageTypes.metalTooHot(level.registryAccess()), (temp - 40f) / (temp + 100f) * 20f);
				player.displayClientMessage(Component.translatable("info.too_hot", String.format("%1$.1f K", temp + 300)), true);
			}
		}
		else
		{
			ItemHelper.putItemsInInventoryOrDrop(player, inv.extractItem(0, 64, false), level);
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BrickCastingVessel.MOLD, false));
		}
	}

	public void placeItem(ItemStack stack)
	{
		var remainder = inv.insertItem(0, stack.split(1), false);
		stack.setCount(remainder.getCount());
		setChangedAndUpdateClient();
	}
}
