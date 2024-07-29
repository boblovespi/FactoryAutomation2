package boblovespi.factoryautomation.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class FABE extends BlockEntity
{
	public FABE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState)
	{
		super(pType, pPos, pBlockState);
	}

	protected abstract void save(CompoundTag tag, HolderLookup.Provider registries);

	protected abstract void load(CompoundTag tag, HolderLookup.Provider registries);

	protected abstract void saveMini(CompoundTag tag, HolderLookup.Provider registries);

	protected abstract void loadMini(CompoundTag tag, HolderLookup.Provider registries);

	public abstract void onDestroy();

	@Override
	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries)
	{
		super.saveAdditional(pTag, pRegistries);
		save(pTag, pRegistries);
	}

	@Override
	protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(pTag, pRegistries);
		load(pTag, pRegistries);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries)
	{
		var tag = new CompoundTag();
		saveMini(tag, pRegistries);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider)
	{
		super.loadAdditional(tag, lookupProvider);
		loadMini(tag, lookupProvider);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider)
	{
		var tag = pkt.getTag();
		if (!tag.isEmpty())
		{
			super.loadAdditional(tag, lookupProvider);
			loadMini(tag, lookupProvider);
		}
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	protected void setChangedAndUpdateClient(boolean notifyNeighbors)
	{
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), notifyNeighbors ? Block.UPDATE_ALL : Block.UPDATE_CLIENTS);
	}

	protected void setChangedAndUpdateClient()
	{
		setChangedAndUpdateClient(false);
	}
}
