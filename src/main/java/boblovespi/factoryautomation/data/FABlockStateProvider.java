package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.ChoppingBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.processing.LogPileLike;
import boblovespi.factoryautomation.common.block.processing.StoneCastingVessel;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

public class FABlockStateProvider extends BlockStateProvider
{
	public FABlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, FactoryAutomation.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		FABlocks.ROCKS.forEach(this::rock);
		existingBlockModel(FABlocks.FLINT_ROCK);
		simpleBlockWithItem(FABlocks.GREEN_SAND.get(), cubeAll(FABlocks.GREEN_SAND.get()));
		simpleBlock(FABlocks.CHARCOAL_PILE.get());
		simpleBlock(FABlocks.TIN_BLOCK.get());
		FABlocks.CHOPPING_BLOCKS.forEach(this::choppingBlock);
		getVariantBuilder(FABlocks.LOG_PILE.get()).forAllStates(
				s -> ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc("log_pile" + (s.getValue(LogPileLike.ACTIVATED) ? "_activated" : "")))).build());
		horizontalBlock(FABlocks.STONE_CRUCIBLE.get(), multiblockComplete("stone_crucible", "stone_foundry_multiblock"));
		castingVessel(FABlocks.STONE_CASTING_VESSEL);
	}

	private void existingBlockModel(DeferredBlock<?> block)
	{
		simpleBlock(block.get(), models().getExistingFile(block.getId().withPrefix("block/")));
	}

	private void rock(DeferredBlock<Rock> rock)
	{
		var realRock = rock.get();
		simpleBlock(realRock, models().singleTexture(rock.getRegisteredName(), modLoc("block/rock"), mcLoc("block/" + realRock.variant.getSerializedName())));
	}

	private void choppingBlock(WoodTypes type, DeferredBlock<ChoppingBlock> choppingBlock)
	{
		var realCb = choppingBlock.get();
		var logLoc = mcLoc("block/" + type.getName() + "_log");
		var topLoc = logLoc.withSuffix("_top");
		simpleBlockWithItem(realCb, models().slab(choppingBlock.getRegisteredName(), logLoc, topLoc, topLoc));
	}

	private Function<BlockState, ModelFile> multiblockComplete(String base, String multiblock)
	{
		return state -> models().getExistingFile(modLoc("block/" + (state.getValue(StoneCrucible.MULTIBLOCK_COMPLETE) ? multiblock : base)));
	}

	private void castingVessel(DeferredBlock<StoneCastingVessel> cv)
	{
		var mpb = getMultipartBuilder(cv.get());
		mpb.part().modelFile(models().getExistingFile(modLoc("block/stone_casting_vessel"))).addModel().end();
		for (var value : StoneCastingVessel.CastingVesselStates.values())
		{
			if (value != StoneCastingVessel.CastingVesselStates.EMPTY)
			{
				var texture = value == StoneCastingVessel.CastingVesselStates.SAND ? modLoc("block/green_sand") : modLoc("block/casting_sand_" + value + "_pattern");
				mpb.part().modelFile(models().singleTexture("block/stone_casting_vessel_mold_" + value, modLoc("block/stone_casting_vessel_mold"), "top", texture)).addModel()
				   .condition(StoneCastingVessel.MOLD, value).end();
			}
		}
		simpleBlockItem(cv.get(), models().getExistingFile(modLoc("block/stone_casting_vessel")));
	}
}
