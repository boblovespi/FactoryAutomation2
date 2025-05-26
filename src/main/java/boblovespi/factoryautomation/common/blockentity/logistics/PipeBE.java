package boblovespi.factoryautomation.common.blockentity.logistics;

import boblovespi.factoryautomation.common.block.logistics.Pipe;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.graph.BlockPosGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

public class PipeBE extends FABE
{
	// TODO: properly encapsulate
	@Nullable
	public BlockPosGraph<Void> graph;
	public boolean isGraphOwner;


	public PipeBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.PIPE_TYPE.get(), pos, state);
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
		if (isGraphOwner && graph != null)
		{
			tag.putBoolean("isOwner", true);
			graph.save(tag);
		}
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		if (tag.contains("isOwner"))
		{
			isGraphOwner = true;
			graph = BlockPosGraph.load(tag);
		}
	}

	@Override
	public void onDestroy()
	{

	}

	public void addIONode(Direction dir)
	{

	}

	public void removeIONode(Direction dir)
	{

	}

	public void onPlace()
	{
		BlockPosGraph<Void> graph = null;
		var set = EnumSet.noneOf(Direction.class);
		for (var dir : Direction.values())
		{
			if (getBlockState().getValue(Pipe.CONNECTIONS[dir.ordinal()]) == Pipe.Connection.JOIN)
			{
				if (level.getBlockEntity(worldPosition.relative(dir)) instanceof PipeBE that)
				{
					set.add(dir);
					if (graph == null)
						graph = that.graph;
					else if (that.graph != null)
						that.joinTo(this);
				}
			}
		}
		if (graph == null)
		{
			graph = new BlockPosGraph<>(getBlockPos().hashCode() | 0xFF000000);
			isGraphOwner = true;
		}
		graph.addVertex(worldPosition, null, set);
		this.graph = graph;
		setChangedAndUpdateClient();
	}

	private void joinTo(PipeBE that)
	{
		var oldGraph = this.graph;
		Objects.requireNonNull(oldGraph);
		Objects.requireNonNull(that.graph);
		for (var pos : oldGraph.getVertices())
		{
			if (level.getBlockEntity(pos) instanceof PipeBE pipe)
			{
				pipe.isGraphOwner = false;
				pipe.graph = that.graph;
			}
		}
		that.graph.joinGraph(oldGraph);
		setChangedAndUpdateClient();
	}
}
