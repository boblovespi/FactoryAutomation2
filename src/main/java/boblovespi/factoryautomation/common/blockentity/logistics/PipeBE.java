package boblovespi.factoryautomation.common.blockentity.logistics;

import boblovespi.factoryautomation.common.block.logistics.Pipe;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.graph.BlockPosGraph;
import boblovespi.factoryautomation.common.util.MathHelper;
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
		tag.putBoolean("isOwner", isGraphOwner);
		if (isGraphOwner && graph != null)
			graph.save(tag);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		if (tag.getBoolean("isOwner"))
		{
			isGraphOwner = true;
			graph = BlockPosGraph.load(tag);
		}
		else
			isGraphOwner = false;
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
		if (graph == null)
			return;
		var removed = graph.removeVertex(worldPosition);
		for (var entry : removed.entrySet())
			level.getBlockEntity(entry.getValue().getOwner(), FABETypes.PIPE_TYPE.get()).ifPresent(t -> t.setGraphAsOwner(entry.getValue()));
	}

	private void setGraphAsOwner(BlockPosGraph<Void> graph)
	{
		this.graph = graph;
		for (var pos : graph.getVertices())
			level.getBlockEntity(pos, FABETypes.PIPE_TYPE.get()).ifPresent(g -> g.setGraph(graph));
		this.isGraphOwner = true;
		setChangedAndUpdateClient();
	}

	private void setGraph(BlockPosGraph<Void> graph)
	{
		this.graph = graph;
		isGraphOwner = false;
		setChangedAndUpdateClient();
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
						that.joinTo(graph);
				}
			}
		}
		if (graph == null)
		{
			graph = new BlockPosGraph<>(MathHelper.colorFromBlockPos(worldPosition), worldPosition);
			isGraphOwner = true;
		}
		graph.addVertexAndJoin(worldPosition, null, set);
		this.graph = graph;
		setChangedAndUpdateClient();
	}

	private void joinTo(BlockPosGraph<Void> graph)
	{
		if (graph == this.graph)
			return;
		var oldGraph = this.graph;
		Objects.requireNonNull(oldGraph);
		Objects.requireNonNull(graph);
		for (var pos : oldGraph.getVertices())
		{
			if (level.getBlockEntity(pos) instanceof PipeBE pipe)
			{
				pipe.isGraphOwner = false;
				pipe.graph = graph;
				pipe.setChangedAndUpdateClient();
			}
		}
		graph.joinGraph(oldGraph);
		this.graph = graph;
		this.isGraphOwner = false;
		setChangedAndUpdateClient();
	}
}
