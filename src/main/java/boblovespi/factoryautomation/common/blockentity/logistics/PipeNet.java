package boblovespi.factoryautomation.common.blockentity.logistics;

import boblovespi.factoryautomation.common.graph.BlockPosGraph;
import boblovespi.factoryautomation.common.graph.DirectionMap;
import boblovespi.factoryautomation.common.graph.IGraphListener;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PipeNet implements IGraphListener<DirectionMap<PipeNet.Node>>
{
	public final int ioRate;
	public final int ticksPerCycle;
	private int startY;
	private int endY;
	private Fluid fluid;
	private final IntObjectMap<Set<Node>> iNodes;
	private final IntObjectMap<Set<Node>> oNodes;
	private final int ioNodeCapacity;
	private int counter;

	public PipeNet(int ioRate, int ticksPerCycle, int startY, int endY)
	{
		this.ioRate = ioRate;
		this.ticksPerCycle = ticksPerCycle;
		this.startY = startY;
		this.endY = endY;
		fluid = Fluids.EMPTY;
		ioNodeCapacity = ioRate * ticksPerCycle * 2;
		iNodes = new IntObjectHashMap<>();
		oNodes = new IntObjectHashMap<>();
	}

	public void cycle()
	{
		var yIn = startY;
		var yOut = startY;
		while (yIn <= endY && yOut <= endY)
		{
			yIn = Math.max(yIn, yOut);
			var inBuffer = sumInBuffers(yIn);
			var outBuffers = sumOutBuffers(yOut);
			if (outBuffers == 0)
			{
				yOut++;
				continue;
			}
			if (inBuffer == 0)
			{
				yIn++;
				continue;
			}
			var toDistribute = Math.min(inBuffer, outBuffers);
			drawFromInputs(yIn, toDistribute, inBuffer);
			distributeToOutputs(yOut, toDistribute, outBuffers);
			if (inBuffer == outBuffers)
			{
				yIn++;
				yOut++;
			}
			else if (toDistribute < inBuffer)
				yOut++;
			else
				yIn++;
		}
	}

	public void recalculateCache(BlockPosGraph<DirectionMap<Node>> graph)
	{
		iNodes.clear();
		oNodes.clear();
		for (var pos : graph.getVertices())
		{
			if (graph.hasData(pos))
			{
				var data = graph.getData(pos);
				for (var entries : data.entrySet())
				{
					if (entries.getValue().type == NodeType.INPUT)
						iNodes.computeIfAbsent(pos.relative(entries.getKey()).getY(), i -> new HashSet<>()).add(entries.getValue());
					else
						oNodes.computeIfAbsent(pos.relative(entries.getKey()).getY(), i -> new HashSet<>()).add(entries.getValue());
				}
				startY = Math.min(startY, pos.getY() - 1);
				endY = Math.max(endY, pos.getY() + 1);
			}
		}
	}

	private void drawFromInputs(int y, int amount, int totalBuffer)
	{
		var drawn = 0;
		for (var node : iNodes.get(y))
		{
			var toDraw = amount * node.amount / totalBuffer;
			drawn += toDraw;
			node.drain(toDraw);
		}
		var remainder = amount - drawn;
		for (var node : iNodes.get(y))
		{
			if (remainder == 0)
				break;
			if (node.amount > 0)
			{
				node.drain(1);
				remainder--;
			}
		}
	}

	private void distributeToOutputs(int y, int amount, int totalBuffer)
	{
		var sent = 0;
		for (var node : oNodes.get(y))
		{
			var toSend = amount * (ioNodeCapacity - node.amount) / totalBuffer;
			sent += toSend;
			node.fill(toSend);
		}
		var remainder = amount - sent;
		for (var node : oNodes.get(y))
		{
			if (remainder == 0)
				break;
			if ((ioNodeCapacity - node.amount) > 0)
			{
				node.fill(1);
				remainder--;
			}
		}
	}

	private int sumOutBuffers(int y)
	{
		if (oNodes.get(y) == null)
			return 0;
		return oNodes.get(y).stream().mapToInt(s -> (ioNodeCapacity - s.amount)).sum();
	}

	private int sumInBuffers(int y)
	{
		if (iNodes.get(y) == null)
			return 0;
		return iNodes.get(y).stream().mapToInt(s -> s.amount).sum();
	}

	@Override
	public void onNewSubgraph(BlockPosGraph<DirectionMap<Node>> graph)
	{
		for (var vertex : graph.getVertices())
		{
			if (graph.hasData(vertex))
			{
				var data = graph.getData(vertex);
				for (var node : data.values())
					node.net = this;
			}
		}
		recalculateCache(graph);
	}

	@Override
	public void onDataAdded(BlockPosGraph<DirectionMap<Node>> graph, BlockPos vertex, DirectionMap<Node> data)
	{
		for (var entries : data.entrySet())
		{
			if (entries.getValue().type == NodeType.INPUT)
				iNodes.computeIfAbsent(vertex.relative(entries.getKey()).getY(), i -> new HashSet<>()).add(entries.getValue());
			else
				oNodes.computeIfAbsent(vertex.relative(entries.getKey()).getY(), i -> new HashSet<>()).add(entries.getValue());
			entries.getValue().net = this;
		}
		startY = Math.min(startY, vertex.getY() - 1);
		endY = Math.max(endY, vertex.getY() + 1);
	}

	@Override
	public void onDataRemoved(BlockPosGraph<DirectionMap<Node>> graph, BlockPos vertex, DirectionMap<Node> data)
	{
		for (var entries : data.entrySet())
		{
			if (entries.getValue().type == NodeType.INPUT)
				iNodes.computeIfAbsent(vertex.relative(entries.getKey()).getY(), i -> new HashSet<>()).remove(entries.getValue());
			else
				oNodes.computeIfAbsent(vertex.relative(entries.getKey()).getY(), i -> new HashSet<>()).remove(entries.getValue());
		}
	}

	@Override
	public void onDataChanged(BlockPosGraph<DirectionMap<Node>> graph, BlockPos vertex, DirectionMap<Node> newData)
	{
		for (var entries : newData.entrySet())
		{
			if (entries.getValue().type == NodeType.INPUT)
				iNodes.computeIfAbsent(vertex.relative(entries.getKey()).getY(), i -> new HashSet<>()).add(entries.getValue());
			else
				oNodes.computeIfAbsent(vertex.relative(entries.getKey()).getY(), i -> new HashSet<>()).add(entries.getValue());
			entries.getValue().net = this;
		}
		startY = Math.min(startY, vertex.getY() - 1);
		endY = Math.max(endY, vertex.getY() + 1);
	}

	@Override
	public void onGraphJoined(BlockPosGraph<DirectionMap<Node>> graph, HashMap<BlockPos, DirectionMap<Node>> newData)
	{
		for (var data : newData.values())
			for (var node : data.values())
				node.net = this;
		recalculateCache(graph);
	}

	@Override
	public void onGraphRemoved(BlockPosGraph<DirectionMap<Node>> graph, HashMap<BlockPos, DirectionMap<Node>> removedData)
	{
		recalculateCache(graph);
	}

	public void tick()
	{
		counter--;
		if (counter <= 0)
		{
			counter = ticksPerCycle;
			cycle();
		}
	}

	public static class Node implements IFluidHandler, INBTSerializable<CompoundTag>
	{
		private PipeNet net;
		private NodeType type;
		private int amount;

		public Node(NodeType type)
		{
			this.type = type;
		}

		@Override
		public int getTanks()
		{
			return 1;
		}

		@Override
		public FluidStack getFluidInTank(int tank)
		{
			return new FluidStack(net.fluid, amount);
		}

		@Override
		public int getTankCapacity(int tank)
		{
			return net.ioNodeCapacity;
		}

		@Override
		public boolean isFluidValid(int tank, FluidStack stack)
		{
			return true;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action)
		{
			if (type == NodeType.OUTPUT || resource.isEmpty())
				return 0;
			if (resource.is(net.fluid) || net.fluid == Fluids.EMPTY)
			{
				net.fluid = resource.getFluid();
				var taken = Math.min(resource.getAmount(), net.ioNodeCapacity - amount);
				if (action.execute())
					amount += taken;
				return taken;
			}
			return 0;
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action)
		{
			if (resource.is(net.fluid) || net.fluid == Fluids.EMPTY)
				return drain(resource.getAmount(), action);
			return FluidStack.EMPTY;
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action)
		{
			if (type == NodeType.INPUT)
				return FluidStack.EMPTY;
			var drained = Math.min(maxDrain, amount);
			if (action.execute())
				amount -= drained;
			return new FluidStack(net.fluid, drained);
		}

		private void drain(int amount)
		{
			this.amount = Math.max(this.amount - amount, 0);
		}

		private void fill(int amount)
		{
			this.amount += amount;
		}

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider provider)
		{
			var tag = new CompoundTag();
			tag.putBoolean("isInput", type == NodeType.INPUT);
			tag.putInt("amount", amount);
			return tag;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
		{
			type = nbt.getBoolean("isInput") ? NodeType.INPUT : NodeType.OUTPUT;
			amount = nbt.getInt("amount");
		}

		public static Node load(CompoundTag tag, HolderLookup.Provider provider)
		{
			var node = new Node(NodeType.INPUT);
			node.deserializeNBT(provider, tag);
			return node;
		}

		public boolean isInput()
		{
			return type == NodeType.INPUT;
		}

		public int getBuffer()
		{
			return isInput() ? net.ioNodeCapacity - amount : amount;
		}
	}

	public enum NodeType
	{
		INPUT, OUTPUT
	}
}
