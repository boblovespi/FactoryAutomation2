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
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class BrickCastingVesselBE extends FABE implements ICastingVessel, ITickable
{
	private final ItemStackHandler inv;
	private final FluidTank tank;
	private float temp;
	private int effectCounter = 40;

	public BrickCastingVesselBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.BRICK_CASTING_VESSEL_TYPE.get(), pPos, pBlockState);
		inv = new ItemStackHandler(2);
		tank = new FluidTank(500, f -> f.is(Tags.Fluids.WATER))
		{
			@Override
			protected void onContentsChanged()
			{
				setChangedAndUpdateClient();
			}
		};
		temp = 0;
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("inv", inv.serializeNBT(registries));
		tank.writeToNBT(registries, tag);
		tag.putFloat("temp", temp);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		tank.readFromNBT(registries, tag);
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
			if (!tank.isEmpty() && temp > 20)
			{
				// use at max 10mB/tick (200mB/s)
				var drained = tank.drain(10, IFluidHandler.FluidAction.EXECUTE).getAmount();
				for (int i = 0; i < drained; i++)
					temp *= 0.99342265857309f; // almost exactly 500mB per cast (or 2.5s)
				effectCounter--;
				if (effectCounter <= 0)
				{
					effectCounter = 40;
					if (temp > 40)
						spawnEffects();
				}
			}
			else if (level.isRainingAt(worldPosition.above()))
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
				inv.getStackInSlot(0).hurtAndBreak(1, sl, null, p -> level.playSound(null, worldPosition, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1f, 1f));
			temp = metal.meltTemp() - 300;
			if (temp > 0 && level instanceof ServerLevel sl)
				sl.sendParticles(FAParticleTypes.METAL_SPARK.get(), worldPosition.getX() + 0.5, worldPosition.getY() + 0.9, worldPosition.getZ() + 0.5, 50, 0.2, 0, 0.2, 0);
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

	public ItemStack getRenderStack()
	{
		if (level.isClientSide)
			return inv.getStackInSlot(0);
		return ItemStack.EMPTY;
	}

	private void spawnEffects()
	{
		var x = worldPosition.getX();
		var y = worldPosition.getY() + 1;
		var z = worldPosition.getZ();
		var times = Math.random() * 3 + 2;
		for (int i = 0; i < times; i++)
		{
			((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, x + Math.random(), y, z + Math.random(), 0, 0, 1,
					0, 0.02f + Math.random() / 10);
		}
		level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3f, 1f);
	}

	public IFluidHandler fluidHandler(@Nullable Direction direction)
	{
		return tank;
	}
}
