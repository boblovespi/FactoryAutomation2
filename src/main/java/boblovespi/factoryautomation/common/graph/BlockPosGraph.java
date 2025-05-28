package boblovespi.factoryautomation.common.graph;

import boblovespi.factoryautomation.common.util.IMaybeSerializable;
import boblovespi.factoryautomation.common.util.MathHelper;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Represents a connected graph based on world block positions, with connectivity being given by adjacency.
 * <p>
 * The graph guarantees three invariants:
 * <ol>
 * 	<li> The graph is connected.
 * 	<li> The graph contains its owner.
 * 	<li> The graph only has bidirectional edges (i.e. is not directed).
 * </ol>
 * given some relatively mild conditions. Firstly, {@link #addVertex} should never be called on any nonempty graph.
 * Next, {@link #addVertexAndJoin} should only be called for positions adjacent to a vertex already in the graph, with the correct
 * direction set in the {@code connections} set.
 *
 * @param <T>
 */
public class BlockPosGraph<T extends IMaybeSerializable<?>>
{
	private final int color;
	private final HashMap<BlockPos, EnumSet<Direction>> vertexSet;
	private final HashMap<BlockPos, T> vertexData;
	private final BlockPos owner;
	private IGraphListener<T> listener;

	public BlockPosGraph(int color, BlockPos owner)
	{
		this.color = color;
		this.owner = owner;
		this.listener = new EmptyListener<>();
		vertexSet = new HashMap<>();
		vertexData = new HashMap<>();
	}

	private BlockPosGraph(int color, HashMap<BlockPos, EnumSet<Direction>> vertexSet, BlockPos owner, HashMap<BlockPos, T> vertexData)
	{
		this.color = color;
		this.vertexSet = vertexSet;
		this.owner = owner;
		this.vertexData = vertexData;
		this.listener = new EmptyListener<>();
	}

	/**
	 * Adds a vertex to the graph with the specified connections.
	 *
	 * @param pos
	 * @param t           {@code null} if no data at this vertex
	 * @param connections
	 */
	private void addVertex(BlockPos pos, @Nullable T t, EnumSet<Direction> connections)
	{
		vertexSet.put(pos, connections);
		if (t != null)
		{
			vertexData.put(pos, t);
			listener.onDataAdded(this, pos, t);
		}
	}

	/**
	 * Adds a unidirectional edge for the given vertex.
	 *
	 * @param pos
	 * @param dir
	 */
	private void addEdge(BlockPos pos, Direction dir)
	{
		if (vertexSet.containsKey(pos))
		{
			var set = vertexSet.get(pos);
			var newSet = EnumSet.copyOf(set);
			newSet.add(dir);
			vertexSet.put(pos, newSet);
		}
	}

	/**
	 * Removes a directed edge from the graph. Does not ensure connectivity invariant holds!
	 *
	 * @param pos The vertex of start edge
	 * @param dir The direction of the edge
	 */
	private void removeEdgeUnsafe(BlockPos pos, Direction dir)
	{
		if (vertexSet.containsKey(pos))
		{
			var set = vertexSet.get(pos);
			var newSet = EnumSet.copyOf(set);
			newSet.remove(dir);
			vertexSet.replace(pos, newSet);
		}
	}

	/**
	 * Adds a vertex and ensures bidirectional connections with adjacent vertices.
	 *
	 * @param pos
	 * @param t
	 * @param connections
	 */
	public void addVertexAndJoin(BlockPos pos, @Nullable T t, EnumSet<Direction> connections)
	{
		addVertex(pos, t, connections);
		for (var direction : connections)
			addEdge(pos.relative(direction), direction.getOpposite());
	}

	/**
	 * Adds a bidirectional edge for a vertex in the given direction.
	 *
	 * @param pos
	 * @param dir
	 */
	public void addBiEdge(BlockPos pos, Direction dir)
	{
		if (vertexSet.containsKey(pos) && vertexSet.containsKey(pos.relative(dir)))
		{
			addEdge(pos, dir);
			addEdge(pos.relative(dir), dir.getOpposite());
		}
	}

	/**
	 * Removes a bidirectional edge for a vertex in the given direction.
	 *
	 * @param pos
	 * @param dir
	 *
	 * @return An {@link Optional} containing the new graph, if the edge removal results in a disconnected graph.
	 */
	public Optional<BlockPosGraph<T>> removeBiEdge(BlockPos pos, Direction dir)
	{
		if (vertexSet.containsKey(pos) && vertexSet.containsKey(pos.relative(dir)))
		{
			removeEdgeUnsafe(pos, dir);
			removeEdgeUnsafe(pos.relative(dir), dir.getOpposite());
			var newSet = new HashMap<BlockPos, EnumSet<Direction>>();
			var newDataSet = new HashMap<BlockPos, T>();
			if (aStarGraphSearch(pos, owner, newSet, newDataSet))
			{
				newSet = new HashMap<>();
				newDataSet = new HashMap<>();
				if (aStarGraphSearch(pos.relative(dir), owner, newSet, newDataSet))
					return Optional.empty();
				else
				{
					var newGraph = new BlockPosGraph<>(MathHelper.colorFromBlockPos(pos.relative(dir)), newSet, pos.relative(dir), newDataSet);
					for (var newPos : newSet.keySet())
					{
						vertexSet.remove(newPos);
						vertexData.remove(newPos);
					}
					listener.onGraphRemoved(this, newGraph.vertexData);
					// newGraph.listener.onNewSubgraph(newGraph);
					return Optional.of(newGraph);
				}
			}
			else
			{
				var newGraph = new BlockPosGraph<>(MathHelper.colorFromBlockPos(pos), newSet, pos, newDataSet);
				for (var newPos : newSet.keySet())
				{
					vertexSet.remove(newPos);
					vertexData.remove(newPos);
				}
				listener.onGraphRemoved(this, newGraph.vertexData);
				// newGraph.listener.onNewSubgraph(newGraph);
				return Optional.of(newGraph);
			}
		}
		return Optional.empty();
	}

	/**
	 * Removes a vertex from the graph.
	 *
	 * @param pos
	 *
	 * @return A map of any new disconnected graphs produced by the removal.
	 */
	public Map<Direction, BlockPosGraph<T>> removeVertex(BlockPos pos)
	{
		if (!vertexSet.containsKey(pos))
			return Map.of();
		var map = new EnumMap<Direction, BlockPosGraph<T>>(Direction.class);
		var dirs = vertexSet.get(pos);
		var graphToUse = this;

		// essentially unrolled recursion. when splitting an edge, we 'recurse' on the newly formed subgraph which contains us.
		// this way, we never change the owner pos
		// also, if our new subgraph does not contain a neighbor, don't look at it (it belongs to some other subgraph)
		for (var dir : dirs)
		{
			// this might actually be unnecessary
			if (!graphToUse.vertexSet.containsKey(pos.relative(dir)))
				continue;
			var maybe = graphToUse.removeBiEdge(pos, dir);
			if (maybe.isPresent() && maybe.get().vertexSet.containsKey(pos))
				graphToUse = maybe.get();
			else
				maybe.ifPresent(tBlockPosGraph -> map.put(dir, tBlockPosGraph));
		}
		graphToUse.vertexSet.remove(pos);
		var data = graphToUse.vertexData.remove(pos);
		if (data != null)
			listener.onDataRemoved(this, pos, data);
		return map;
	}

	public void joinGraph(BlockPosGraph<T> that)
	{
		vertexSet.putAll(that.vertexSet);
		vertexData.putAll(that.vertexData);
		listener.onGraphJoined(this, that.vertexData);
	}

	private boolean aStarGraphSearch(BlockPos startPos, BlockPos targetPos, HashMap<BlockPos, EnumSet<Direction>> outVertexSet, HashMap<BlockPos, T> outDataSet)
	{
		var helper = new AStarHelper(targetPos, outVertexSet, outDataSet);
		helper.add(startPos, 0);

		while (helper.hasNext())
		{
			var current = helper.next();
			if (current.equals(targetPos))
				return true;
			for (var dir : vertexSet.get(current))
			{
				var potentialScore = helper.scores.getInt(current) + 1;
				var neighbor = current.relative(dir);
				// should never reach more than once, since our heuristic is monotone -- thus lack of queue recalculation is ok
				if (helper.isBetterScore(neighbor, potentialScore))
					helper.add(neighbor, potentialScore);
			}
		}
		return false;
	}

	public Collection<BlockPos> getVertices()
	{
		return vertexSet.keySet();
	}

	public Collection<Direction> getEdgesForVertex(BlockPos pos)
	{
		return vertexSet.getOrDefault(pos, EnumSet.noneOf(Direction.class));
	}

	public void setListener(IGraphListener<T> listener)
	{
		this.listener = listener;
	}

	public void save(CompoundTag tag, HolderLookup.Provider provider)
	{
		var nbt = new CompoundTag();
		nbt.putInt("color", color);
		var list = new ListTag();
		for (var entry : vertexSet.entrySet())
		{
			var mini = new CompoundTag();
			mini.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
			var val = 0;
			for (var dir : entry.getValue())
				val |= 1 << dir.ordinal();
			mini.putInt("cons", val);
			if (vertexData.containsKey(entry.getKey()) && vertexData.get(entry.getKey()).shouldSerialize())
				mini.put("data", vertexData.get(entry.getKey()).serializeNBT(provider));
			list.add(mini);
		}
		nbt.put("vertices", list);
		nbt.put("owner", NbtUtils.writeBlockPos(owner));
		tag.put("posGraph", nbt);
	}

	public static <T extends IMaybeSerializable<? extends Tag>> BlockPosGraph<T> load(CompoundTag tag, HolderLookup.Provider provider,
																					  BiFunction<CompoundTag, HolderLookup.Provider, T> deserializer)
	{
		var nbt = tag.getCompound("posGraph");
		var color = nbt.getInt("color");
		var vertices = new HashMap<BlockPos, EnumSet<Direction>>();
		var dataMap = new HashMap<BlockPos, T>();
		var list = nbt.getList("vertices", 10);
		for (int i = 0; i < list.size(); i++)
		{
			var entry = list.getCompound(i);
			var pos = NbtUtils.readBlockPos(entry, "pos").orElseThrow();
			var cons = entry.getInt("cons");
			T data = null;
			if (entry.contains("data"))
				data = deserializer.apply(entry.getCompound("data"), provider);
			var conSet = EnumSet.noneOf(Direction.class);
			for (int j = 0; j < Direction.values().length; j++)
			{
				if ((cons & 1 << j) > 0)
					conSet.add(Direction.values()[j]);
			}
			vertices.put(pos, conSet);
			if (data != null)
				dataMap.put(pos, data);
		}
		var owner = NbtUtils.readBlockPos(nbt, "owner").orElseThrow();
		return new BlockPosGraph<>(color, vertices, owner, dataMap);
	}

	public int getColor()
	{
		return color;
	}

	public BlockPos getOwner()
	{
		return owner;
	}

	public boolean hasData(BlockPos pos)
	{
		return vertexData.containsKey(pos);
	}

	public T getData(BlockPos pos)
	{
		return vertexData.get(pos);
	}

	public void setData(BlockPos vertex, T data)
	{
		vertexData.put(vertex, data);
		listener.onDataChanged(this, vertex, data);
	}

	private static class EmptyListener<T extends IMaybeSerializable<?>> implements IGraphListener<T>
	{
	}

	private class AStarHelper
	{
		private final Object2IntOpenHashMap<BlockPos> scores;
		private final Object2IntOpenHashMap<BlockPos> guessScores;
		private final PriorityQueue<BlockPos> queue;
		private final HashMap<BlockPos, EnumSet<Direction>> outVertexSet;
		private final HashMap<BlockPos, T> outDataSet;
		private final BlockPos targetPos;

		private AStarHelper(BlockPos targetPos, HashMap<BlockPos, EnumSet<Direction>> outVertexSet, HashMap<BlockPos, T> outDataSet)
		{
			this.outVertexSet = outVertexSet;
			this.targetPos = targetPos;
			this.outDataSet = outDataSet;
			scores = new Object2IntOpenHashMap<>();
			guessScores = new Object2IntOpenHashMap<>();
			queue = new PriorityQueue<>(Comparator.comparingInt(guessScores::getInt));
		}

		private void add(BlockPos pos, int score)
		{
			var shouldAddToQueue = !scores.containsKey(pos);
			scores.put(pos, score);
			guessScores.put(pos, score + pos.distManhattan(targetPos));
			if (shouldAddToQueue)
			{
				queue.add(pos);
				outVertexSet.put(pos, vertexSet.get(pos));
				if (vertexData.containsKey(pos))
					outDataSet.put(pos, vertexData.get(pos));
			}
		}

		private boolean hasNext()
		{
			return !queue.isEmpty();
		}

		public BlockPos next()
		{
			return queue.poll();
		}

		public boolean isBetterScore(BlockPos pos, int potentialScore)
		{
			return !scores.containsKey(pos) || potentialScore <= scores.getInt(pos);
		}
	}
}
