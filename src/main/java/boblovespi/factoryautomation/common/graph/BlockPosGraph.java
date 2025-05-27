package boblovespi.factoryautomation.common.graph;

import boblovespi.factoryautomation.common.util.MathHelper;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;

import java.util.*;

/**
 * Represents a connected graph based on world block positions, with connectivity being given by adjacency.
 *
 * @param <T>
 */
public class BlockPosGraph<T>
{
	private final int color;
	private final HashMap<BlockPos, EnumSet<Direction>> vertexSet;
	private final BlockPos owner;

	public BlockPosGraph(int color, BlockPos owner)
	{
		this.color = color;
		this.owner = owner;
		vertexSet = new HashMap<>();
	}

	private BlockPosGraph(int color, HashMap<BlockPos, EnumSet<Direction>> vertexSet, BlockPos owner)
	{
		this.color = color;
		this.vertexSet = vertexSet;
		this.owner = owner;
	}

	/**
	 * Adds a vertex to the graph with the specified connections.
	 *
	 * @param pos
	 * @param t
	 * @param connections
	 */
	public void addVertex(BlockPos pos, T t, EnumSet<Direction> connections)
	{
		vertexSet.put(pos, connections);
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
	public void addVertexAndJoin(BlockPos pos, T t, EnumSet<Direction> connections)
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
			if (aStarGraphSearch(pos, owner, newSet))
			{
				newSet = new HashMap<>();
				if (aStarGraphSearch(pos.relative(dir), owner, newSet))
					return Optional.empty();
				else
				{
					var newGraph = new BlockPosGraph<T>(MathHelper.colorFromBlockPos(pos.relative(dir)), newSet, pos.relative(dir));
					for (var newPos : newSet.keySet())
						vertexSet.remove(newPos);
					return Optional.of(newGraph);
				}
			}
			else
			{
				var newGraph = new BlockPosGraph<T>(MathHelper.colorFromBlockPos(pos), newSet, pos);
				for (var newPos : newSet.keySet())
					vertexSet.remove(newPos);
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
			if (!graphToUse.vertexSet.containsKey(pos.relative(dir)))
				continue;
			var maybe = graphToUse.removeBiEdge(pos, dir);
			if (maybe.isPresent() && maybe.get().vertexSet.containsKey(pos))
				graphToUse = maybe.get();
			else
				maybe.ifPresent(tBlockPosGraph -> map.put(dir, tBlockPosGraph));
		}
		graphToUse.vertexSet.remove(pos);
		return map;
	}

	public void joinGraph(BlockPosGraph<T> that)
	{
		vertexSet.putAll(that.vertexSet);
	}

	private boolean aStarGraphSearch(BlockPos startPos, BlockPos targetPos, HashMap<BlockPos, EnumSet<Direction>> outVertexSet)
	{
		var helper = new AStarHelper(targetPos, outVertexSet);
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

	public void save(CompoundTag tag)
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
			list.add(mini);
		}
		nbt.put("vertices", list);
		nbt.put("owner", NbtUtils.writeBlockPos(owner));
		tag.put("posGraph", nbt);
	}

	public static <T> BlockPosGraph<T> load(CompoundTag tag)
	{
		var nbt = tag.getCompound("posGraph");
		var color = nbt.getInt("color");
		var vertices = new HashMap<BlockPos, EnumSet<Direction>>();
		var list = nbt.getList("vertices", 10);
		for (int i = 0; i < list.size(); i++)
		{
			var entry = list.getCompound(i);
			var pos = NbtUtils.readBlockPos(entry, "pos").orElseThrow();
			var cons = entry.getInt("cons");
			var conSet = EnumSet.noneOf(Direction.class);
			for (int j = 0; j < Direction.values().length; j++)
			{
				if ((cons & 1 << j) > 0)
					conSet.add(Direction.values()[j]);
			}
			vertices.put(pos, conSet);
		}
		var owner = NbtUtils.readBlockPos(nbt, "owner").orElseThrow();
		return new BlockPosGraph<>(color, vertices, owner);
	}

	public int getColor()
	{
		return color;
	}

	public BlockPos getOwner()
	{
		return owner;
	}

	private class AStarHelper
	{
		private final Object2IntOpenHashMap<BlockPos> scores;
		private final Object2IntOpenHashMap<BlockPos> guessScores;
		private final PriorityQueue<BlockPos> queue;
		private final HashMap<BlockPos, EnumSet<Direction>> outVertexSet;
		private final BlockPos targetPos;

		private AStarHelper(BlockPos targetPos, HashMap<BlockPos, EnumSet<Direction>> outVertexSet)
		{
			this.outVertexSet = outVertexSet;
			this.targetPos = targetPos;
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
