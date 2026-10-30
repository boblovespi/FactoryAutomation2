package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.processing.StampMill;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.StampMillRecipe;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.MathHelper;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import boblovespi.factoryautomation.common.util.RecipeManager;
import boblovespi.factoryautomation.common.util.jade.IJadeViewable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class StampMillBE extends FABE implements ITickable, IClientTickable, IJadeViewable
{
	private final RecipeManager<StampMillRecipe> recipeManager;
	private final MechanicalManager mechanicalManager;
	private final ItemStackHandler inv;
	private float rot;
	private boolean hasRecipe;

	public StampMillBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.STAMP_MILL_TYPE.get(), pos, state);
		recipeManager = new RecipeManager<>("recipe", this::isValid, this::findMatchingRecipe, this::getRecipe);
		mechanicalManager = new MechanicalManager("rot", Function.identity(), Function.identity(), () -> {
			recipeManager.updateRecipe();
			setChangedAndUpdateClient();
		});
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

	public static <T> IBlockCapabilityProvider<T, Direction> getCap(BlockCapability<T, Direction> cap)
	{
		return (Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be, @Nullable Direction dir) -> {
			var isUpper = state.getValue(StampMill.HALF) == DoubleBlockHalf.UPPER;
			return (isUpper
							? level.getBlockEntity(pos.below(), FABETypes.STAMP_MILL_TYPE.get())
							: be instanceof StampMillBE hm
									  ? Optional.of(hm)
									  : Optional.<StampMillBE>empty())
						   .map(e -> {
							   if (cap == MechanicalCapability.INPUT)
								   return (T) e.input(dir, isUpper);
							   if (cap == Capabilities.ItemHandler.BLOCK)
								   return (T) e.itemHandler(dir, isUpper);
							   throw new RuntimeException();
						   }).orElseThrow();
		};
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.save(tag);
		mechanicalManager.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.load(tag, registries);
		mechanicalManager.load(tag);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		mechanicalManager.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
		tag.putBoolean("hasRecipe", recipeManager.hasRecipe());
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		mechanicalManager.load(tag);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		hasRecipe = tag.getBoolean("hasRecipe");
	}

	@Override
	public void onDestroy()
	{
		ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
	}

	@Override
	public void clientTick()
	{
		var oldRot = rot;
		rot += (float) (Math.toDegrees(mechanicalManager.getSpeed()) / 20);
		if (MathHelper.crossesThreshold(oldRot, rot, 90))
		{
			var pitch = Mth.nextFloat(level.random, 0.8f, 1);
			level.playLocalSound(worldPosition, SoundEvents.DEEPSLATE_TILES_BREAK, SoundSource.BLOCKS, 1, pitch, false);
			if (hasRecipe)
			{
				var particleCount = level.random.nextInt(3, 5);
				for (var i = 0; i < particleCount; i++)
				{
					var x = level.random.triangle(0.5, 0.3);
					var z = level.random.triangle(0.5, 0.3);
					level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, inv.getStackInSlot(0)), worldPosition.getX() + x, worldPosition.getY() + 0.25,
							worldPosition.getZ() + z, 0, 0, 0);
				}
			}
		}
		rot %= 360;
	}

	@Override
	public void tick()
	{
		var oldRot = rot;
		rot += (float) (Math.toDegrees(mechanicalManager.getSpeed()) / 20);
		if (recipeManager.hasRecipe())
		{
			if (MathHelper.crossesThreshold(oldRot, rot, 120))
			{
				recipeManager.progress();
				if (recipeManager.isComplete())
				{
					var result = recipeManager.getCompleted();
					recipeManager.clearRecipe();

					var input = new StampMillRecipe.Input(inv.extractItem(0, 1, false));
					var assembled = result.value().assemble(input, level.registryAccess());
					inv.insertItem(1, assembled, false);
					setChangedAndUpdateClient();
				}
				else
					setChanged();
			}
		}
		rot %= 360;
	}

	private boolean fits(StampMillRecipe recipe)
	{
		return inv.insertItem(1, recipe.getResultItem(level.registryAccess()), true).isEmpty();
	}

	private boolean isValid(RecipeHolder<StampMillRecipe> r)
	{
		return mechanicalManager.getTorque() >= 200 && r.value().matches(new StampMillRecipe.Input(inv.getStackInSlot(0)), level) && fits(r.value());
	}

	@Nullable
	private RecipeHolder<StampMillRecipe> findMatchingRecipe()
	{
		if (mechanicalManager.getTorque() >= 200)
			return level.getRecipeManager()
						.getRecipeFor(RecipeThings.STAMP_MILL_TYPE.get(), new StampMillRecipe.Input(inv.getStackInSlot(0)), level)
						.filter(r -> fits(r.value()))
						.orElse(null);
		return null;
	}

	private Optional<RecipeHolder<?>> getRecipe(ResourceLocation k)
	{
		return level.getRecipeManager().byKey(k);
	}

	@Override
	public List<ItemStack> makeViewStacks()
	{
		return List.of(inv.getStackInSlot(0), inv.getStackInSlot(1));
	}

	@Nullable
	public IMechanicalInput input(@Nullable Direction dir, boolean isUpper)
	{
		var state = getBlockState();
		if (isUpper && state.getValue(StampMill.FACING).getCounterClockWise() == dir)
			return mechanicalManager;
		return null;
	}

	@Nullable
	public IItemHandler itemHandler(@Nullable Direction dir, boolean isUpper)
	{
		var facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		if (dir == null)
			return inv;
		if (isUpper)
			return new RangedWrapper(inv, 0, 1);
		if (dir == facing || dir == Direction.DOWN)
			return new RangedWrapper(inv, 1, 2);
		return new RangedWrapper(inv, 0, 1);
	}
}
