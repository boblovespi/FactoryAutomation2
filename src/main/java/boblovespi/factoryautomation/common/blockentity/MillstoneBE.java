package boblovespi.factoryautomation.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MillstoneBE extends FABE implements ITickable, IClientTickable, GeoBlockEntity
{
	private static final RawAnimation ACTIVE_STATE = RawAnimation.begin().thenLoop("state.millstone.active");
	private final AnimatableInstanceCache cache;
	private float rot;

	public MillstoneBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.MILLSTONE_TYPE.get(), pPos, pBlockState);
		cache = GeckoLibUtil.createInstanceCache(this);
		rot = 0;
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{

	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{

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

	}

	@Override
	public void tick()
	{

	}

	@Override
	public void clientTick()
	{
		rot += (float) (Math.toDegrees(/*manager.getSpeed()*/1f) / 20);
		rot %= 360;
	}

	public float getRenderRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (rot + delta * (float) (Math.toDegrees(/*manager.getSpeed()*/1f) / 20)) % 360;
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
}
