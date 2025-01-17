package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.common.multiblock.IMultiblockBE;
import boblovespi.factoryautomation.common.multiblock.MultiblockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;

import javax.annotation.Nullable;

public class MultiblockPartBE extends FABE
{
	private ResourceLocation multiblockName;
	private BlockPos multiblockPos;
	private BlockPos multiblockControllerOffset;
	private BlockState multiblockState;
	private Direction facing;
	private boolean isBreaking;

	public MultiblockPartBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.MULTIBLOCK_PART_TYPE.get(), pPos, pBlockState);
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.putString("multiblockName", multiblockName.toString());
		tag.put("multiblockPos", NbtUtils.writeBlockPos(multiblockPos));
		tag.put("multiblockControllerOffset", NbtUtils.writeBlockPos(multiblockControllerOffset));
		tag.put("multiblockState", NbtUtils.writeBlockState(multiblockState));
		tag.putInt("facing", facing.get3DDataValue());
		tag.putBoolean("isBreaking", isBreaking);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		multiblockName = ResourceLocation.parse(tag.getString("multiblockName"));
		multiblockPos = NbtUtils.readBlockPos(tag, "multiblockPos").orElse(BlockPos.ZERO);
		multiblockControllerOffset = NbtUtils.readBlockPos(tag, "multiblockControllerOffset").orElse(BlockPos.ZERO);
		multiblockState = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("multiblockState"));
		facing = Direction.from3DDataValue(tag.getInt("facing"));
		isBreaking = tag.getBoolean("isBreaking");
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		save(tag, registries);
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		load(tag, registries);
	}

	@Override
	public void onDestroy()
	{
		if (!isBreaking)
		{
			markAsBreaking();
			var multiblock = MultiblockRegistry.get(multiblockName);
			multiblock.destroy(level, worldPosition.subtract(multiblockControllerOffset), facing);
		}
	}

	public void markAsBreaking()
	{
		isBreaking = true;
		setChanged();
	}

	public boolean isStructure(ResourceLocation name)
	{
		return multiblockName != null && multiblockName.equals(name);
	}

	public void setMultiblockInformation(ResourceLocation name, BlockPos relativePos, BlockPos controllerOffset, BlockState state, Direction facing)
	{
		multiblockName = name;
		multiblockPos = relativePos;
		multiblockControllerOffset = controllerOffset;
		multiblockState = state;
		this.facing = facing;
		setChanged();
	}

	public BlockState getMultiblockState()
	{
		return multiblockState;
	}

	public boolean isBreaking()
	{
		return isBreaking;
	}

	@Nullable
	public <T> T getCapability(BlockCapability<T, Direction> capability, Direction dir)
	{
		var be = level.getBlockEntity(worldPosition.subtract(multiblockControllerOffset));
		if (be instanceof IMultiblockBE mbe)
			return mbe.getCapability(multiblockControllerOffset, capability, dir);
		return null;
	}
}
