package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.block.processing.TripHammer;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.multiblock.IMultiblockBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.TripHammerRecipe;
import boblovespi.factoryautomation.common.sound.FASounds;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.MathHelper;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.ItemStackHandler;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class TripHammerBE extends FABE implements IMultiblockBE, ITickable, IClientTickable, GeoBlockEntity
{
	private static final RawAnimation ACTIVE_STATE = RawAnimation.begin().thenLoop("animation.hammer.active");
	private static final RawAnimation STANDBY_STATE = RawAnimation.begin().thenLoop("animation.hammer.standby");
	private final AnimatableInstanceCache cache;
	private final RecipeManager<TripHammerRecipe> recipeManager;
	private final MechanicalManager mechanicalManager;
	private final ItemStackHandler inv;
	private float rot;
	private boolean breaking;
	private static final float offset = 32;

	public TripHammerBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.TRIP_HAMMER_TYPE.get(), pPos, pBlockState);
		cache = GeckoLibUtil.createInstanceCache(this);
		rot = 0;
		recipeManager = new RecipeManager<>("recipe", this::isValid, this::findMatchingRecipe, this::getRecipe);
		mechanicalManager = new MechanicalManager("rot", Function.identity(), Function.identity(), () -> {
			recipeManager.updateRecipe();
			setChangedAndUpdateClient();
		});
		inv = new ItemStackHandler(1)
		{
			@Override
			protected void onContentsChanged(int slot)
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
	private RecipeHolder<TripHammerRecipe> findMatchingRecipe()
	{
		if (mechanicalManager.getTorque() >= 200)
			return level.getRecipeManager().getRecipeFor(RecipeThings.TRIP_HAMMER_TYPE.get(), new TripHammerRecipe.Input(inv.getStackInSlot(0)), level).orElse(null);
		return null;
	}

	private boolean isValid(RecipeHolder<TripHammerRecipe> recipe)
	{
		return mechanicalManager.getTorque() >= 200 && recipe.value().matches(new TripHammerRecipe.Input(inv.getStackInSlot(0)), level);
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
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		mechanicalManager.load(tag);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
	}

	@Override
	public void onDestroy()
	{
		if (getBlockState().getValue(StoneCrucible.MULTIBLOCK_COMPLETE))
		{
			breaking = true;
			Multiblocks.TRIP_HAMMER.destroy(level, worldPosition, getBlockState().getValue(TripHammer.FACING).getOpposite());
		}
	}

	@Override
	public void tick()
	{
		if (recipeManager.hasRecipe())
		{
			recipeManager.progress();
			if (recipeManager.isComplete())
			{
				var result = recipeManager.getCompleted();
				recipeManager.clearRecipe();

				var input = new TripHammerRecipe.Input(inv.getStackInSlot(0));
				var assembled = result.value().assemble(input, level.registryAccess());
				inv.setStackInSlot(0, assembled);
				setChangedAndUpdateClient();
			}
			else
				setChanged();
		}
	}

	@Override
	public void onMultiblockBuilt()
	{
		//server side only
	}

	@Override
	public void onMultiblockDestroyed()
	{
		if (!breaking)
			level.setBlock(worldPosition, getBlockState().setValue(TripHammer.MULTIBLOCK_COMPLETE, false), 2);
	}

	public void placeItem(ItemStack stack)
	{
		if (!inv.getStackInSlot(0).isEmpty() || stack.isEmpty())
			return;
		inv.insertItem(0, stack.split(1), false);
		recipeManager.updateRecipe();
		setChangedAndUpdateClient();
	}

	public ItemStack takeItem()
	{
		var stack = inv.extractItem(0, Item.ABSOLUTE_MAX_STACK_SIZE, false);
		recipeManager.updateRecipe();
		setChangedAndUpdateClient();
		return stack;
	}

	public void takeOrPlace(ItemStack stack, Player player)
	{
		if (!inv.getStackInSlot(0).isEmpty())
		{
			var taken = takeItem();
			ItemHelper.putItemsInInventoryOrDropAt(player, taken, level, Vec3.atLowerCornerWithOffset(worldPosition, 0.5f, 7 / 16f, 0.5f));
		}
		else
		{
			placeItem(stack);
		}
	}

	public ItemStack getRenderStack()
	{
		if (level.isClientSide)
			return inv.getStackInSlot(0);
		return ItemStack.EMPTY;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, this::handleAnim));
	}

	private PlayState handleAnim(AnimationState<TripHammerBE> s) {
		return s.setAndContinue(ACTIVE_STATE);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public <T> T getCapability(BlockPos offset, BlockCapability<T, Direction> capability, Direction dir)
	{
		if (capability == MechanicalCapability.INPUT)
		{
			var facing = getBlockState().getValue(TripHammer.FACING);
			if (offset.equals(BlockPos.ZERO.relative(facing, 5).above()) && dir.getAxis() == facing.getClockWise().getAxis())
				return (T) mechanicalManager;
		}
		return null;
	}

	public float getRenderInputRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return -(rot + delta * (float) (Math.toDegrees(mechanicalManager.getSpeed()) / 20)) % 360;
	}

	public float getRenderToolRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		
		var inputDeg = (-getRenderInputRot(delta) + 350) % 360;

		var pastRot = calcPhasedRotation(inputDeg-1);
		var futureRot = calcPhasedRotation(inputDeg+1);
		var presentRot = MathHelper.smoothInterpolate(pastRot, futureRot, 0.5);

		return ((float) presentRot*17.5f);
	}

	public double calcPhasedRotation(double input) {
		var inputRad = Math.toRadians(input);
		var linPhaseH = Math.asin(Math.abs(Math.sin(inputRad*2)));
		var powPhaseH = Math.asin(Math.abs(Math.pow(Math.sin(inputRad*2),30)));

		var lPhase = MathHelper.map(linPhaseH, 0.0d, 1.6d, 0, 1.00d);
		var pPhase = MathHelper.map(powPhaseH, 0.0d, 1.6d, 0, 1.00d);

		var ret = lPhase;

		if (input > 45 && input < 90 || input > 135 && input < 180 || input > 225 && input < 270 || input > 315) {
			ret = MathHelper.easeInOutBack(pPhase);
		}

		return ret;
	}

	@Override
	public void clientTick() {
		var oldRot = rot;
		rot += (float) (Math.toDegrees(mechanicalManager.getSpeed()) / 20);
		if (MathHelper.crossesThreshold(oldRot + offset, rot + offset, 90))
			level.playLocalSound(worldPosition, FASounds.USE_TRIP_HAMMER.get(), SoundSource.BLOCKS, 1, Mth.nextFloat(level.random, 0.8f, 1), false);
		rot %= 360;
	}
}
