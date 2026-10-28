package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.common.blockentity.*;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.menu.SteamOvenMenu;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.SteamOvenRecipe;
import boblovespi.factoryautomation.common.sound.FASounds;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.MathHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import boblovespi.factoryautomation.common.util.jade.IJadeViewable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SteamOvenBE extends FABE implements ITickable, IClientTickable, IJadeViewable, IMenuProviderProvider
{
	private final RecipeManager<SteamOvenRecipe> recipeManager;
	private final ItemStackHandler inv;
	private final FluidTank steamTank;

	public SteamOvenBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.STEAM_OVEN_TYPE.get(), pPos, pBlockState);
		recipeManager = new RecipeManager<>("recipe", this::isValid, this::findMatchingRecipe, this::getRecipe);
		inv = new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
				recipeManager.updateRecipe();
			}
		};
		steamTank = new FluidTank(1000, f -> f.is(FAFluids.STEAM_TYPE.get()))
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
		recipeManager.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
		tag.put("steamTank", steamTank.writeToNBT(registries, new CompoundTag()));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.load(tag, registries);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
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
		ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
	}

	@Override
	public void tick()
	{
		if (recipeManager.hasRecipe())
		{
			if (steamTank.drain(5, IFluidHandler.FluidAction.EXECUTE).getAmount() >= 5)
			{
				recipeManager.progress();
				if (recipeManager.isComplete())
				{
					var result = recipeManager.getCompleted();
					var assembled = result.value().assemble(new SteamOvenRecipe.Input(inv.extractItem(0, 1, false)),
							level.registryAccess());
					inv.insertItem(1, assembled, false);
					recipeManager.complete();
					if (level instanceof ServerLevel sl)
						sl.sendParticles(ParticleTypes.CLOUD, worldPosition.getX() + 0.5, worldPosition.getY() + 1,
								worldPosition.getZ() + 0.5, 3, 0.5, 0.1, 0.5, 0.03);
					level.playSound(null, worldPosition, FASounds.STEAM_RELEASES.get(), SoundSource.BLOCKS, 1f, (float) (MathHelper.uniformZeroD(level.random, 0.2) + 1));
				}
			}
		}
		setChanged();
	}

	@Override
	public void clientTick()
	{

	}

	private boolean fits(SteamOvenRecipe recipe)
	{
		return inv.insertItem(1, recipe.getResultItem(level.registryAccess()), true).isEmpty();
	}

	private boolean isValid(RecipeHolder<SteamOvenRecipe> r)
	{
		return r.value().matches(new SteamOvenRecipe.Input(inv.getStackInSlot(0)), level) && fits(r.value());
	}

	@Nullable
	private RecipeHolder<SteamOvenRecipe> findMatchingRecipe()
	{
		return level.getRecipeManager()
					.getRecipeFor(RecipeThings.STEAM_OVEN_TYPE.get(), new SteamOvenRecipe.Input(inv.getStackInSlot(0)), level)
					.filter(r -> fits(r.value()))
					.orElse(null);
	}

	private Optional<RecipeHolder<?>> getRecipe(ResourceLocation k)
	{
		return level.getRecipeManager().byKey(k);
	}

	@Nullable
	public IItemHandler itemHandler(@Nullable Direction dir)
	{
		if (dir == null)
			return inv;
		if (dir == Direction.UP || dir.getAxis() == getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis())
			return new RangedWrapper(inv, 0, 1);
		if (dir == Direction.DOWN)
			return new RangedWrapper(inv, 1, 2);
		return null;
	}

	@Override
	public List<ItemStack> makeViewStacks()
	{
		return List.of(inv.getStackInSlot(0));
	}

	public @Nullable IFluidHandler fluidHandler(@Nullable Direction direction)
	{
		return steamTank;
	}

	@Override
	public MenuProvider getMenuProvider()
	{
		return new SimpleMenuProvider((i, v, p) -> new SteamOvenMenu(i, v, inv, new SteamOvenBE.Data(), ContainerLevelAccess.create(level, worldPosition)),
				Component.translatable("gui.steam_oven.name"));
	}

	private class Data implements ContainerData
	{
		@Override
		public int get(int index)
		{
			return switch (index)
			{
				case 0 ->Float.floatToIntBits(recipeManager.getProgressRatio());
				case 1 -> steamTank.getFluidAmount();
				default -> throw new IndexOutOfBoundsException();
			};
		}

		@Override
		public void set(int index, int value)
		{

		}

		@Override
		public int getCount()
		{
			return 2;
		}
	}
}
