package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.client.gui.StoneFoundryMenu;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
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

public class StoneCrucibleBE extends FABE implements IMultiblockBE, ITickable, IMenuProviderProvider
{
	private boolean breaking;
	private final CrucibleManager crucible;
	private final BurnerManager burner;
	private final HeatManager heat;
	private final ItemStackHandler inv;
	private final float efficiency = 0.5f;

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
		};
		heat = new HeatManager("heat", 2300 * 1000, 300);
		burner = new BurnerManager("burner", () -> inv.getStackInSlot(0), () -> inv.extractItem(0, 1, false), (t, e) -> {
			if (t * efficiency >= heat.getTemperature())
				heat.heat(e * efficiency);
		});
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		crucible.save(tag);
		burner.save(tag);
		heat.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		crucible.load(tag);
		burner.load(tag);
		heat.load(tag);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
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

	// TODO: this is temporary, for testing the crucible manager
	public void addMetal(Metal metal, int i)
	{
		crucible.melt(metal, i);
		setChangedAndUpdateClient();
	}

	// TODO: this is temporary, for testing the crucible manager
	public ItemStack pour()
	{
		setChangedAndUpdateClient();
		var pour = crucible.pour(Form.INGOT.amount());
		if (pour.isEmpty())
			return ItemStack.EMPTY;
		else
			return new ItemStack(Metal.itemForMetalAndForm(pour.get(), Form.INGOT));
	}

	@Override
	public void tick()
	{
		burner.progress();
	}

	public void addCoal(ItemStack coal)
	{
		inv.insertItem(0, coal, false);
	}

	@Override
	public MenuProvider getMenuProvider()
	{
		return new SimpleMenuProvider((i, v, p) -> new StoneFoundryMenu(i, v, inv, new Data(), ContainerLevelAccess.create(level, worldPosition)), Component.literal("REPLACE ME"));
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
