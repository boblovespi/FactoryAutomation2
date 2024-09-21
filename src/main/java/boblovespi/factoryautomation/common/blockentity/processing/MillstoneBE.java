package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.recipe.MillstoneRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.sound.FASounds;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
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

public class MillstoneBE extends FABE implements ITickable, IClientTickable, GeoBlockEntity
{
	private static final RawAnimation ACTIVE_STATE = RawAnimation.begin().thenLoop("state.millstone.active");
	private final AnimatableInstanceCache cache;
	private final RecipeManager<MillstoneRecipe> recipeManager;
	private final MechanicalManager mechanicalManager;
	private final ItemStackHandler inv;
	private float rot;
	private int audioLoop = 0;
	private final int audioLength = 20 * 3 + 6;

	public MillstoneBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.MILLSTONE_TYPE.get(), pPos, pBlockState);
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
		recipeManager.load(tag);
		mechanicalManager.load(tag);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
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
			if (level instanceof ServerLevel sl && !inv.getStackInSlot(0).isEmpty())
				sl.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, inv.getStackInSlot(0)), worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
						worldPosition.getZ() + 0.5, 3, 0.5, 0.1, 0.5, 0.03);
			if (recipeManager.isComplete())
			{
				var result = recipeManager.getCompleted();
				var assembled = result.value().assemble(new MillstoneRecipe.Input(inv.extractItem(0, 1, false), mechanicalManager.getSpeed(), mechanicalManager.getTorque()),
						level.registryAccess());
				var item = new ItemEntity(level, worldPosition.getX() - 0.5 + level.random.nextInt(3), worldPosition.getY() - 0.1,
						worldPosition.getZ() - 0.5 + level.random.nextInt(3), assembled);
				item.push(level.random.nextDouble() - 0.5, 0, level.random.nextDouble() - 0.5);
				level.addFreshEntity(item);
				recipeManager.complete();
			}

		}
		setChanged();
	}

	@Override
	public void clientTick()
	{
		rot += (float) (Math.toDegrees(mechanicalManager.getSpeed()) / 20);
		rot %= 360;
		if (audioLoop == 0)
			level.playLocalSound(worldPosition, FASounds.USE_MILLSTONE.get(), SoundSource.BLOCKS, 0.3f, 1, false);
		audioLoop = (audioLoop + 1) % audioLength;
	}

	public float getRenderRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (rot + delta * (float) (Math.toDegrees(mechanicalManager.getSpeed()) / 20)) % 360;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<GeoAnimatable>(this, s -> s.setAndContinue(ACTIVE_STATE)));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}

	private boolean isValid(RecipeHolder<MillstoneRecipe> r)
	{
		return r.value().matches(new MillstoneRecipe.Input(inv.getStackInSlot(0), mechanicalManager.getSpeed(),
				mechanicalManager.getTorque()), level);
	}

	@Nullable
	private RecipeHolder<MillstoneRecipe> findMatchingRecipe()
	{
		return level.getRecipeManager().getRecipeFor(RecipeThings.MILLSTONE_TYPE.get(), new MillstoneRecipe.Input(inv.getStackInSlot(0), mechanicalManager.getSpeed(),
				mechanicalManager.getTorque()), level).orElse(null);
	}

	private Optional<RecipeHolder<?>> getRecipe(ResourceLocation k)
	{
		return level.getRecipeManager().byKey(k);
	}

	@Nullable
	public IMechanicalInput input(Direction dir)
	{
		if (dir == Direction.DOWN)
			return mechanicalManager;
		return null;
	}

	@Nullable
	public IItemHandler itemHandler(Direction dir)
	{
		if (dir == Direction.UP)
			return inv;
		return null;
	}
}
