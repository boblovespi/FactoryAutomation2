package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.api.capability.BellowsCapability;
import boblovespi.factoryautomation.common.block.processing.PaperBellows;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PaperBellowsBE extends FABE implements IClientTickable, GeoBlockEntity
{
	private int lerp;
	private static final RawAnimation BLOW_ONCE = RawAnimation.begin().thenPlay("action.bellows.blow_once").thenLoop("state.bellows.base");
	private static final RawAnimation STANDBY_STATE = RawAnimation.begin().thenLoop("state.bellows.base");
	private final AnimatableInstanceCache cache;

	public PaperBellowsBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.PAPER_BELLOWS_TYPE.get(), pPos, pBlockState);
		cache = GeckoLibUtil.createInstanceCache(this);
	}

	private PlayState handleAnim(AnimationState<PaperBellowsBE> s)
	{
		return lerp > 0 ? s.setAndContinue(BLOW_ONCE) : s.setAndContinue(STANDBY_STATE);
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
	public void clientTick()
	{
		if (lerp > 0)
			lerp--;
	}

	public void blow()
	{
		if (level.isClientSide)
		{
			if (lerp == 0)
				lerp = 20;
		}
		else
		{
			var facing = getBlockState().getValue(PaperBellows.FACING);
			var cap = level.getCapability(BellowsCapability.BLOCK, worldPosition.relative(facing), facing.getOpposite());
			if (cap != null)
				cap.blow(0.75f, 400);
		}
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, this::handleAnim));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
}
