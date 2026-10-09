package boblovespi.factoryautomation.common.util.patchouli;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.multiblock.Multiblock;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.multiblock.SimpleMultiblock;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FAPatchouliPlugin
{
	public void registerMultiblocks()
	{
		var api = PatchouliAPI.get();
		process(Multiblocks.STONE_CRUCIBLE, makePatchouliMultiblock(api));
		process(Multiblocks.BRICK_CRUCIBLE, makePatchouliMultiblock(api));
		process(Multiblocks.LARGE_WATERWHEEL, makePatchouliMultiblock(api));
	}

	private static Consumer<SimpleMultiblock> makePatchouliMultiblock(PatchouliAPI.IPatchouliAPI api)
	{
		return multiblock ->
		{
			var size = multiblock.getSize();
			var maxY = size.getY();
			var pattern = new String[maxY][size.getX()];
			var c = '1';
			var blockstateMap = new HashMap<BlockState, Character>();
			var zeroState = (BlockState) null;
			for (int x = 0; x < size.getX(); x++)
			{
				for (int y = 0; y < maxY; y++)
				{
					pattern[maxY - y - 1][x] = "";
					for (int z = 0; z < size.getZ(); z++)
					{
						var state = multiblock.getDisplayAt(x, y, z);
						if (x == 0 && y == 0 && z == 0)
						{
							pattern[maxY - y - 1][x] += '0';
							zeroState = state;
							continue;
						}
						if (!blockstateMap.containsKey(state))
						{
							blockstateMap.put(state, c);
							c++;
						}
						pattern[maxY - y - 1][x] += blockstateMap.get(state);
					}
				}
			}
			var mb = api.makeMultiblock(pattern,
					Stream.concat(Stream.of('0', zeroState), blockstateMap.entrySet().stream().flatMap(e -> Stream.of(e.getValue(), e.getKey()))).toArray());
			mb.offset(0, 0, 0);
			api.registerMultiblock(multiblock.getName(), mb);
		};
	}

	private void process(Multiblock mb, Consumer<SimpleMultiblock> f)
	{
		if (mb instanceof SimpleMultiblock smb)
			f.accept(smb);
		else
			FactoryAutomation.LOGGER.error("multiblock {} is not a simple multiblock; patchouli creation failed", mb.getName());
	}
}
