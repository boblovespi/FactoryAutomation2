package boblovespi.factoryautomation.common.blockentity.logistics;

import boblovespi.factoryautomation.common.graph.BlockPosGraph;
import boblovespi.factoryautomation.common.graph.DirectionMap;
import boblovespi.factoryautomation.common.graph.IGraphListener;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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
	private FluidStack fluid;
	private int fluidAmount;
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
		fluid = FluidStack.EMPTY;
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
		fluidAmount = 0;
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
					fluidAmount += entries.getValue().amount;
				}
				startY = Math.min(startY, pos.getY() - 1);
				endY = Math.max(endY, pos.getY() + 1);
			}
		}
		if (fluidAmount <= 0)
			fluid = FluidStack.EMPTY;
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
			fluidAmount += entries.getValue().amount;
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
			fluidAmount -= entries.getValue().amount;
			if (fluidAmount <= 0)
				fluid = FluidStack.EMPTY;
		}
	}

	@Override
	public void onDataChanged(BlockPosGraph<DirectionMap<Node>> graph, BlockPos vertex, DirectionMap<Node> newData)
	{
		for (var entries : newData.entrySet())
		{
			var added = false;
			if (entries.getValue().type == NodeType.INPUT)
				added = iNodes.computeIfAbsent(vertex.relative(entries.getKey()).getY(), i -> new HashSet<>()).add(entries.getValue());
			else
				added = oNodes.computeIfAbsent(vertex.relative(entries.getKey()).getY(), i -> new HashSet<>()).add(entries.getValue());
			entries.getValue().net = this;
			if (added)
				fluidAmount += entries.getValue().amount;
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

	public void removeNode(int y, Node node)
	{
		var removed = false;
		if (node.type == NodeType.INPUT)
			removed = iNodes.computeIfAbsent(y, i -> new HashSet<>()).remove(node);
		else
			removed = oNodes.computeIfAbsent(y, i -> new HashSet<>()).remove(node);
		if (removed)
		{
			fluidAmount -= node.amount;
			if (fluidAmount <= 0)
				fluid = FluidStack.EMPTY;
		}
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

	public void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		var tag1 = new CompoundTag();
		tag1.put("fluid", fluid.saveOptional(registries));
		tag1.putInt("amount", fluidAmount);
		tag.put("fluidNet", tag1);
	}

	public void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		var tag1 = tag.getCompound("fluidNet");
		fluid = FluidStack.parseOptional(registries, tag1.getCompound("fluid"));
		fluidAmount = tag1.getInt("amount");
	}

	public void setFluid(PipeNet oldNet)
	{
		if (fluid.isEmpty())
			fluid = oldNet.fluid;
	}

	public boolean canMerge(PipeNet otherNet)
	{
		return fluid.isEmpty() || otherNet.fluid.isEmpty() || FluidStack.isSameFluidSameComponents(fluid, otherNet.fluid);
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
			return net.fluid.copyWithAmount(amount);
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
			if (FluidStack.isSameFluidSameComponents(net.fluid, resource) || net.fluid.isEmpty())
			{
				net.fluid = resource.copyWithAmount(1);
				var taken = Math.min(resource.getAmount(), net.ioNodeCapacity - amount);
				if (action.execute())
				{
					amount += taken;
					net.fluidAmount += taken;
				}
				return taken;
			}
			return 0;
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action)
		{
			if (FluidStack.isSameFluidSameComponents(net.fluid, resource) || net.fluid.isEmpty())
				return drain(resource.getAmount(), action);
			return FluidStack.EMPTY;
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action)
		{
			if (type == NodeType.INPUT)
				return FluidStack.EMPTY;
			var drained = Math.min(maxDrain, amount);
			var fluid = net.fluid;
			if (action.execute())
			{
				amount -= drained;
				net.fluidAmount -= drained;
				if (net.fluidAmount <= 0)
					net.fluid = FluidStack.EMPTY;
			}
			return fluid.copyWithAmount(drained);
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
