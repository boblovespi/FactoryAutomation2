package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.ICastingVessel;
import boblovespi.factoryautomation.common.util.Metal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Function;

public class StoneCastingVesselBE extends FABE implements ICastingVessel
{
	public StoneCastingVesselBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.STONE_CASTING_VESSEL_TYPE.get(), pos, state);
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
	public void cast(Function<Integer, Optional<Metal>> metalSource)
	{
		var result = metalSource.apply(Form.INGOT.amount());
		if (result.isPresent())
		{
			var itemEntity = new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
					Metal.itemForMetalAndForm(result.get(), Form.INGOT).getDefaultInstance());
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
	}
}
