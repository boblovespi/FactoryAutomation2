package boblovespi.factoryautomation.common.graph;

import boblovespi.factoryautomation.common.util.IMaybeSerializable;
import net.minecraft.core.BlockPos;

import java.util.HashMap;

public interface IGraphListener<T extends IMaybeSerializable<?>>
{
	default void onNewSubgraph(BlockPosGraph<T> graph)
	{

	}

	default void onDataAdded(BlockPosGraph<T> graph, BlockPos vertex, T data)
	{

	}

	default void onDataRemoved(BlockPosGraph<T> graph, BlockPos vertex, T data)
	{

	}

	default void onGraphJoined(BlockPosGraph<T> graph, HashMap<BlockPos, T> newData)
	{

	}

	default void onGraphRemoved(BlockPosGraph<T> graph, HashMap<BlockPos, T> removedData)
	{

	}
}
