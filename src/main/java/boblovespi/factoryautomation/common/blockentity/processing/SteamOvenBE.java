package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.SteamOvenRecipe;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import boblovespi.factoryautomation.common.util.jade.IJadeViewable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SteamOvenBE extends FABE implements ITickable, IClientTickable, IJadeViewable
{
	private final RecipeManager<SteamOvenRecipe> recipeManager;
	private final ItemStackHandler inv;

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
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.load(tag, registries);
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
		ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		recipeManager.onLoad();
	}

	@Override
	public void tick()
	{
		if (recipeManager.hasRecipe())
		{
			recipeManager.progress();
			if (level instanceof ServerLevel sl && !inv.getStackInSlot(0).isEmpty() && sl.random.nextDouble() < 0.2)
				sl.sendParticles(ParticleTypes.CLOUD, worldPosition.getX() + 0.5, worldPosition.getY() + 1,
						worldPosition.getZ() + 0.5, 3, 0.5, 0.1, 0.5, 0.03);
			if (recipeManager.isComplete())
			{
				var result = recipeManager.getCompleted();
				var assembled = result.value().assemble(new SteamOvenRecipe.Input(inv.extractItem(0, 1, false)),
						level.registryAccess());
				inv.insertItem(1, assembled, false);
				recipeManager.complete();
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
}
