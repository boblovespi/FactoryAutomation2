package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.HandCrank;
import boblovespi.factoryautomation.common.block.processing.ChoppingBlock;
import boblovespi.factoryautomation.common.block.processing.LogPileLike;
import boblovespi.factoryautomation.common.block.processing.StoneCastingVessel;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WaterloggedTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

@SuppressWarnings("SameParameterValue")
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
		blockWithItem(FABlocks.GREEN_SAND);
		blockWithItem(FABlocks.CASSITERITE_ORE);
		blockWithItem(FABlocks.RAW_CASSITERITE_BLOCK);
		FABlocks.LIMONITE_ORES.values().forEach(this::blockWithItem);
		blockWithItem(FABlocks.RAW_LIMONITE_BLOCK);
		simpleBlock(FABlocks.CHARCOAL_PILE.get());
		simpleBlock(FABlocks.IRON_BLOOM.get());
		blockWithItem(FABlocks.DRIED_BRICKS);
		simpleBlock(FABlocks.TIN_BLOCK.get());
		simpleBlock(FABlocks.BRONZE_BLOCK.get());
		simpleBlock(FABlocks.STEEL_BLOCK.get());
		simpleBlock(FABlocks.COPPER_PLATE_BLOCK.get());
		simpleBlock(FABlocks.TIN_PLATE_BLOCK.get());
		simpleBlock(FABlocks.IRON_PLATE_BLOCK.get());
		simpleBlock(FABlocks.BRONZE_PLATE_BLOCK.get());
		simpleBlock(FABlocks.STEEL_PLATE_BLOCK.get());
		spaceFrame(FABlocks.COPPER_SPACE_FRAME);
		spaceFrame(FABlocks.TIN_SPACE_FRAME);
		spaceFrame(FABlocks.IRON_SPACE_FRAME);
		spaceFrame(FABlocks.BRONZE_SPACE_FRAME);
		spaceFrame(FABlocks.STEEL_SPACE_FRAME);
		blockWithItem(FABlocks.BRICK_TILES);
		FABlocks.CHOPPING_BLOCKS.forEach(this::choppingBlock);
		getVariantBuilder(FABlocks.LOG_PILE.get()).forAllStates(
				s -> ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc("log_pile" + (s.getValue(LogPileLike.ACTIVATED) ? "_activated" : "")))).build());
		blockWithItem(FABlocks.LIMONITE_CHARCOAL_MIX);
		horizontalBlock(FABlocks.STONE_CRUCIBLE.get(), multiblockComplete("stone_crucible", "stone_foundry_multiblock"), 270);
		castingVessel(FABlocks.STONE_CASTING_VESSEL);
		existingBlockWithItem(FABlocks.STONE_WORKBENCH);
		existingBlockModel(FABlocks.BRICK_MAKER_FRAME);
		horizontalBlock(FABlocks.BRICK_CRUCIBLE.get(), multiblockComplete("brick_crucible", "brick_foundry_multiblock"), 270);
		horizontalBlock(FABlocks.BRICK_FIREBOX.get(), modLoc("block/brick_firebox_side"), modLoc("block/brick_firebox_front"), modLoc("block/brick_firebox_top"));
		blockWithItem(FABlocks.CREATIVE_MECHANICAL_SOURCE);
		axisOnlyBlock(FABlocks.WOOD_POWER_SHAFT, modLoc("block/power_shaft"), mcLoc("block/oak_planks"), mcLoc("block/oak_planks"));
		directionalBlock(FABlocks.WOOD_GEARBOX, modLoc("block/gearbox"), modLoc("block/wood_gearbox_side"), modLoc("block/wood_gearbox_front"), modLoc("block/wood_gearbox_back"));
		axisOnlyBlock(FABlocks.IRON_POWER_SHAFT, modLoc("block/power_shaft"), mcLoc("block/iron_block"), mcLoc("block/iron_block"));
		directionalBlock(FABlocks.IRON_GEARBOX, modLoc("block/gearbox"), modLoc("block/iron_gearbox_side"), modLoc("block/iron_gearbox_front"), modLoc("block/iron_gearbox_back"));
		getVariantBuilder(FABlocks.HAND_CRANK.get()).forAllStates(
				s -> ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc("hand_crank" + (s.getValue(HandCrank.HANGING) ? "_hanging" : "")))).build());
	}

	private void existingBlockWithItem(DeferredBlock<?> block)
	{
		simpleBlockWithItem(block.get(), models().getExistingFile(block.getId().withPrefix("block/")));
	}

	private void blockWithItem(DeferredBlock<? extends Block> block)
	{
		simpleBlockWithItem(block.get(), cubeAll(block.get()));
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

	private void axisOnlyBlock(DeferredBlock<? extends RotatedPillarBlock> block, ResourceLocation base, ResourceLocation side, ResourceLocation end)
	{
		var model = models().withExistingParent(block.getRegisteredName(), base).texture("side", side).texture("front", end);
		axisBlock(block.get(), model, model);
		simpleBlockItem(block.get(), model);
	}

	private void directionalBlock(DeferredBlock<? extends Block> block, ResourceLocation base, ResourceLocation side, ResourceLocation end)
	{
		directionalBlock(block, base, side, end, end);
	}

	private void directionalBlock(DeferredBlock<? extends Block> block, ResourceLocation base, ResourceLocation side, ResourceLocation front, ResourceLocation back)
	{
		var model = models().withExistingParent(block.getRegisteredName(), base).texture("side", side).texture("front", front).texture("back", back);
		// directionalBlock(block.get(), model);
		getVariantBuilder(block.get())
				.forAllStates(state -> {
					var dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
										  .modelFile(model)
										  .rotationX(dir == Direction.DOWN ? 90 : dir.getAxis().isHorizontal() ? 0 : -90)
										  .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
										  .build();
				});
		simpleBlockItem(block.get(), model);
	}

	private void spaceFrame(DeferredBlock<WaterloggedTransparentBlock> spaceFrame)
	{
		var model = models().withExistingParent(spaceFrame.getRegisteredName(), modLoc("block/space_frame"))
							.texture("side", spaceFrame.getId().withPrefix("block/"))
							.texture("end", spaceFrame.getId().withPrefix("block/").withSuffix("_top"))
							.renderType("minecraft:cutout");
		simpleBlock(spaceFrame.get(), model);
	}
}
