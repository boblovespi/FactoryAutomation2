package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.common.block.processing.TumblingBarrel;
import boblovespi.factoryautomation.common.blockentity.*;
import boblovespi.factoryautomation.common.menu.TumblingBarrelMenu;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.TumblingBarrelRecipe;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class TumblingBarrelBE extends FABE implements ITickable, IClientTickable, GeoBlockEntity, IMenuProviderProvider
{
	private static final RawAnimation ACTIVE_STATE = RawAnimation.begin().thenLoop("animation.tumbling_barrel.active");
	private static final float TORQUE_REQ = 100;
	private final AnimatableInstanceCache cache;
	private final RecipeManager<TumblingBarrelRecipe> recipeManager;
	private final MechanicalManager mechanicalManager;
	private final ItemStackHandler inv;
	private final FluidTank inTank;
	private final FluidTank outTank;
	private float rot;

	public TumblingBarrelBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.TUMBLING_BARREL_TYPE.get(), pos, state);
		cache = GeckoLibUtil.createInstanceCache(this);
		rot = 0;
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
		inTank = new FluidTank(2000)
		{
			@Override
			protected void onContentsChanged()
			{
				setChanged();
				recipeManager.updateRecipe();
			}
		};
		outTank = new FluidTank(2000)
		{
			@Override
			protected void onContentsChanged()
			{
				setChanged();
				recipeManager.updateRecipe();
			}
		};
	}

	private Optional<RecipeHolder<?>> getRecipe(ResourceLocation location)
	{
		return level.getRecipeManager().byKey(location);
	}

	@Nullable
	private RecipeHolder<TumblingBarrelRecipe> findMatchingRecipe()
	{
		var recipe = level.getRecipeManager().getRecipeFor(RecipeThings.TUMBLING_BARREL_TYPE.get(), getInput(), level).orElse(null);
		return recipe != null && !fits(recipe.value()) ? null : recipe;
	}

	private TumblingBarrelRecipe.Input getInput()
	{
		return new TumblingBarrelRecipe.Input(inv.getStackInSlot(0), inTank.getFluid(), mechanicalManager.getSpeed());
	}

	public float getRenderRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (rot + delta * (float) (Math.toDegrees(mechanicalManager.getSpeed()) / 20)) % 360;
	}

	private boolean fits(TumblingBarrelRecipe recipe)
	{
		return inv.insertItem(1, recipe.result(), true).isEmpty() &&
			   outTank.fill(recipe.fluidResult().copy(), IFluidHandler.FluidAction.SIMULATE) >= recipe.fluidResult().getAmount();
	}

	private boolean isValid(RecipeHolder<TumblingBarrelRecipe> recipe)
	{
		return recipe.value().matches(getInput(), level) && fits(recipe.value());
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		recipeManager.onLoad();
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.save(tag);
		mechanicalManager.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
		tag.put("inTank", inTank.writeToNBT(registries, new CompoundTag()));
		tag.put("outTank", outTank.writeToNBT(registries, new CompoundTag()));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.load(tag);
		mechanicalManager.load(tag);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		inTank.readFromNBT(registries, tag.getCompound("inTank"));
		outTank.readFromNBT(registries, tag.getCompound("outTank"));
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		mechanicalManager.save(tag);
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		mechanicalManager.load(tag);
	}

	@Override
	public void onDestroy()
	{
		ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
	}

	@Override
	public MenuProvider getMenuProvider()
	{
		return new SimpleMenuProvider((i, v, p) -> new TumblingBarrelMenu(i, v, inv, new Data(), ContainerLevelAccess.create(level, worldPosition)),
				Component.translatable("gui.tumbling_barrel.name"));
	}

	@Override
	public void tick()
	{
		if (recipeManager.hasRecipe())
		{
			if (mechanicalManager.getTorque() >= TORQUE_REQ)
				recipeManager.progress();
			if (recipeManager.isComplete())
			{
				var result = recipeManager.getCompleted();

				var input = getInput();
				var assembled = result.value().assemble(input, level.registryAccess());
				var outFluid = result.value().fluidResult().copy();

				inv.extractItem(0, 1, false);
				inTank.drain(result.value().fluidInput().amount(), IFluidHandler.FluidAction.EXECUTE);
				inv.insertItem(1, assembled, false);
				outTank.fill(outFluid, IFluidHandler.FluidAction.EXECUTE);

				recipeManager.complete();
				setChangedAndUpdateClient();
			}
			else
				setChanged();
		}
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<GeoAnimatable>(this, s -> s.setAndContinue(ACTIVE_STATE)));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}

	@Nullable
	public IMechanicalInput input(@Nullable Direction direction)
	{
		if (direction != null && direction.getAxis() == getBlockState().getValue(TumblingBarrel.AXIS))
			return mechanicalManager;
		return null;
	}

	public IItemHandler itemHandler(@Nullable Direction direction)
	{
		return direction == Direction.UP ? new RangedWrapper(inv, 0, 1) : new RangedWrapper(inv, 1, 2);
	}

	public IFluidHandler fluidHandler(@Nullable Direction direction)
	{
		if (direction != null && direction.getAxis() == getBlockState().getValue(TumblingBarrel.AXIS))
			return inTank;
		return outTank;
	}

	@Override
	public void clientTick()
	{
		rot += (float) (Math.toDegrees(mechanicalManager.getSpeed()) / 20);
		rot %= 360;
	}

	private class Data implements ContainerData
	{
		@Override
		public int get(int index)
		{
			return switch (index)
			{
				case 0 -> Float.floatToIntBits(recipeManager.getProgressRatio());
				case 1 -> inTank.getFluidAmount();
				case 2 -> outTank.getFluidAmount();
				case 3 -> BuiltInRegistries.FLUID.getId(inTank.getFluid().getFluid());
				case 4 -> BuiltInRegistries.FLUID.getId(outTank.getFluid().getFluid());
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
