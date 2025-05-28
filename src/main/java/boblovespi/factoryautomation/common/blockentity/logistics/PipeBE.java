package boblovespi.factoryautomation.common.blockentity.logistics;

import boblovespi.factoryautomation.common.block.logistics.Pipe;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.graph.BlockPosGraph;
import boblovespi.factoryautomation.common.graph.DirectionMap;
import boblovespi.factoryautomation.common.util.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

public class PipeBE extends FABE implements ITickable
{
	// TODO: properly encapsulate
	@Nullable
	public BlockPosGraph<DirectionMap<PipeNet.Node>> graph;
	@Nullable
	private PipeNet net;
	public boolean isGraphOwner;
	private final DirectionMap<PipeNet.Node> nodes;
	private boolean wasPlaced;

	public PipeBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.PIPE_TYPE.get(), pos, state);
		nodes = new DirectionMap<>();
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.putBoolean("isOwner", isGraphOwner);
		if (isGraphOwner && graph != null)
		{
			graph.save(tag, registries);
			net.save(tag, registries);
		}
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		if (tag.getBoolean("isOwner"))
		{
			isGraphOwner = true;
			graph = BlockPosGraph.load(tag, registries, DirectionMap.loader(PipeNet.Node::load));
			if (net == null)
			{
				net = new PipeNet(25, 20, worldPosition.getY() - 1, worldPosition.getY() + 1);
				graph.setListener(net);
			}
			net.load(tag, registries);
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
			level.getBlockEntity(entry.getValue().getOwner(), FABETypes.PIPE_TYPE.get()).ifPresent(t -> t.setGraphAsOwner(entry.getValue(), net));
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		if (wasPlaced)
			return;
		if (isGraphOwner && graph != null)
		{
			net.onNewSubgraph(graph);
			for (var pos : graph.getVertices())
				level.getBlockEntity(pos, FABETypes.PIPE_TYPE.get()).ifPresent(g -> g.setGraphAndNodes(graph, net));
			isGraphOwner = true;
		}
		setChangedAndUpdateClient();
	}

	private void setGraphAndNodes(BlockPosGraph<DirectionMap<PipeNet.Node>> graph, PipeNet net)
	{
		setGraph(graph, net);
		if (graph.hasData(worldPosition))
			nodes.putAll(graph.getData(worldPosition));
	}

	private void setGraphAsOwner(BlockPosGraph<DirectionMap<PipeNet.Node>> graph, PipeNet oldNet)
	{
		this.graph = graph;
		this.net = new PipeNet(25, 20, worldPosition.getY() - 1, worldPosition.getY() + 1);
		graph.setListener(net);
		net.setFluid(oldNet);
		net.onNewSubgraph(graph);
		for (var pos : graph.getVertices())
			level.getBlockEntity(pos, FABETypes.PIPE_TYPE.get()).ifPresent(g -> g.setGraph(graph, net));
		this.isGraphOwner = true;
		setChangedAndUpdateClient();
	}

	private void setGraph(BlockPosGraph<DirectionMap<PipeNet.Node>> graph, PipeNet net)
	{
		this.graph = graph;
		this.net = net;
		isGraphOwner = false;
		setChangedAndUpdateClient();
	}

	public void addIONode(Direction dir)
	{
		if (!nodes.containsKey(dir))
		{
			var node = new PipeNet.Node(dir == Direction.UP ? PipeNet.NodeType.INPUT : PipeNet.NodeType.OUTPUT);
			nodes.put(dir, node);
			if (graph != null)
				graph.setData(worldPosition, nodes);
		}
	}

	public void removeIONode(Direction dir)
	{
		if (nodes.containsKey(dir))
		{
			var node = nodes.remove(dir);
			if (graph != null)
			{
				net.removeNode(worldPosition.relative(dir).getY(), node);
				graph.setData(worldPosition, nodes);
			}
		}
	}

	public void onPlace()
	{
		wasPlaced = true;
		BlockPosGraph<DirectionMap<PipeNet.Node>> graph = null;
		PipeNet net = null;
		var set = EnumSet.noneOf(Direction.class);
		for (var dir : Direction.values())
		{
			if (getBlockState().getValue(Pipe.CONNECTIONS[dir.ordinal()]) == Pipe.Connection.JOIN)
			{
				if (level.getBlockEntity(worldPosition.relative(dir)) instanceof PipeBE that)
				{
					if (graph == null)
					{
						set.add(dir);
						graph = that.graph;
						net = that.net;
					}
					else if (that.graph != null)
					{
						if (that.net.canMerge(net))
						{
							set.add(dir);
							that.joinTo(graph, net);
						}
						else
						{
							level.setBlock(worldPosition, getBlockState().setValue(Pipe.CONNECTIONS[dir.ordinal()], Pipe.Connection.NONE), 2);
							level.setBlock(worldPosition.relative(dir),
									level.getBlockState(worldPosition.relative(dir)).setValue(Pipe.CONNECTIONS[dir.getOpposite().ordinal()], Pipe.Connection.NONE), 2);
						}
					}
				}
			}
		}
		if (graph == null)
		{
			graph = new BlockPosGraph<>(MathHelper.colorFromBlockPos(worldPosition), worldPosition);
			net = new PipeNet(25, 20, worldPosition.getY() - 1, worldPosition.getY() + 1);
			graph.setListener(net);
			isGraphOwner = true;
		}
		graph.addVertexAndJoin(worldPosition, nodes, set);
		this.graph = graph;
		this.net = net;

		for (var dir : Direction.values())
		{
			if (getBlockState().getValue(Pipe.CONNECTIONS[dir.ordinal()]) == Pipe.Connection.CONNECTOR)
				addIONode(dir);
		}
		setChangedAndUpdateClient();
	}

	private void joinTo(BlockPosGraph<DirectionMap<PipeNet.Node>> graph, PipeNet net)
	{
		if (graph == this.graph)
			return;
		var oldGraph = this.graph;
		Objects.requireNonNull(oldGraph);
		Objects.requireNonNull(graph);
		// we know nets are compatible. let us set the fluid correctly.
		net.setFluid(this.net);
		for (var pos : oldGraph.getVertices())
		{
			if (level.getBlockEntity(pos) instanceof PipeBE pipe)
			{
				pipe.isGraphOwner = false;
				pipe.graph = graph;
				pipe.net = net;
				pipe.setChangedAndUpdateClient();
			}
		}
		graph.joinGraph(oldGraph);
		this.graph = graph;
		this.net = net;
		this.isGraphOwner = false;
		setChangedAndUpdateClient();
	}

	@Override
	public void tick()
	{
		if (level.isClientSide)
			return;
		if (isGraphOwner)
			net.tick();
		for (var entry : nodes.entrySet())
		{
			var capability = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition.relative(entry.getKey()), entry.getKey().getOpposite());
			if (capability != null)
			{
				var node = entry.getValue();
				var toTransfer = Math.min(node.getBuffer(), 25);
				if (node.isInput())
					FluidUtil.tryFluidTransfer(node, capability, toTransfer, true);
				else
					FluidUtil.tryFluidTransfer(capability, node, toTransfer, true);
			}
		}
	}
}
