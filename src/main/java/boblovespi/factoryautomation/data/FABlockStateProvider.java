package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.SpaceFrameBlock;
import boblovespi.factoryautomation.common.block.logistics.Pipe;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
import boblovespi.factoryautomation.common.block.mechanical.HandCrank;
import boblovespi.factoryautomation.common.block.mechanical.Splitter;
import boblovespi.factoryautomation.common.block.processing.*;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.util.StoneBlockForms;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;
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
		blockWithItem(FABlocks.CHERT);
		stoneBlockForms(FABlocks.ANDESITE_BRICKS);
		stoneBlockForms(FABlocks.GRANITE_BRICKS);
		stoneBlockForms(FABlocks.DIORITE_BRICKS);
		FABlocks.ROCKS.forEach(this::rock);
		existingBlockModel(FABlocks.FLINT_ROCK);
		blockWithItem(FABlocks.GREEN_SAND);
		blockWithItem(FABlocks.CASSITERITE_ORE);
		blockWithItem(FABlocks.RAW_CASSITERITE_BLOCK);
		FABlocks.LIMONITE_ORES.values().forEach(this::blockWithItem);
		blockWithItem(FABlocks.RAW_LIMONITE_BLOCK);
		blockWithItem(FABlocks.ANCIENT_IRON_BLOCK);
		blockWithItem(FABlocks.IRON_SAND);
		blockWithItem(FABlocks.IRON_SAND_CHARCOAL_MIX);
		blockWithItem(FABlocks.WEAK_IRON_BLOCK);
		simpleBlock(FABlocks.CHARCOAL_PILE.get());
		simpleBlock(FABlocks.IRON_BLOOM.get());
		blockWithItem(FABlocks.DRIED_BRICKS);
		simpleBlock(FABlocks.TIN_BLOCK.get());
		simpleBlock(FABlocks.LEAD_BLOCK.get());
		simpleBlock(FABlocks.BRONZE_BLOCK.get());
		simpleBlock(FABlocks.NICKEL_BLOCK.get());
		simpleBlock(FABlocks.SILVER_BLOCK.get());
		simpleBlock(FABlocks.MAGMATIC_BRASS_BLOCK.get());
		simpleBlock(FABlocks.PIG_IRON_BLOCK.get());
		simpleBlock(FABlocks.STEEL_BLOCK.get());
		simpleBlock(FABlocks.ALUMINUM_BLOCK.get());
		simpleBlock(FABlocks.ALUMINUM_BRONZE_BLOCK.get());
		simpleBlock(FABlocks.CHROMIUM_BLOCK.get());
		simpleBlock(FABlocks.COPPER_PLATE_BLOCK.get());
		simpleBlock(FABlocks.TIN_PLATE_BLOCK.get());
		simpleBlock(FABlocks.LEAD_PLATE_BLOCK.get());
		simpleBlock(FABlocks.IRON_PLATE_BLOCK.get());
		simpleBlock(FABlocks.BRONZE_PLATE_BLOCK.get());
		simpleBlock(FABlocks.NICKEL_PLATE_BLOCK.get());
		simpleBlock(FABlocks.SILVER_PLATE_BLOCK.get());
		simpleBlock(FABlocks.MAGMATIC_BRASS_PLATE_BLOCK.get());
		simpleBlock(FABlocks.PIG_IRON_PLATE_BLOCK.get());
		simpleBlock(FABlocks.STEEL_PLATE_BLOCK.get());
		simpleBlock(FABlocks.ALUMINUM_PLATE_BLOCK.get());
		simpleBlock(FABlocks.ALUMINUM_BRONZE_PLATE_BLOCK.get());
		simpleBlock(FABlocks.CHROMIUM_PLATE_BLOCK.get());
		spaceFrame(FABlocks.COPPER_SPACE_FRAME);
		spaceFrame(FABlocks.TIN_SPACE_FRAME);
		spaceFrame(FABlocks.LEAD_SPACE_FRAME);
		spaceFrame(FABlocks.IRON_SPACE_FRAME);
		spaceFrame(FABlocks.BRONZE_SPACE_FRAME);
		spaceFrame(FABlocks.NICKEL_SPACE_FRAME);
		spaceFrame(FABlocks.SILVER_SPACE_FRAME);
		spaceFrame(FABlocks.MAGMATIC_BRASS_SPACE_FRAME);
		spaceFrame(FABlocks.PIG_IRON_SPACE_FRAME);
		spaceFrame(FABlocks.STEEL_SPACE_FRAME);
		spaceFrame(FABlocks.ALUMINUM_SPACE_FRAME);
		spaceFrame(FABlocks.ALUMINUM_BRONZE_SPACE_FRAME);
		spaceFrame(FABlocks.CHROMIUM_SPACE_FRAME);
		blockWithItem(FABlocks.BRICK_TILES);
		FABlocks.CHOPPING_BLOCKS.forEach(this::choppingBlock);
		getVariantBuilder(FABlocks.LOG_PILE.get()).forAllStates(
				s -> ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc("log_pile" + (s.getValue(LogPileLike.ACTIVATED) ? "_activated" : "")))).build());
		blockWithItem(FABlocks.LIMONITE_CHARCOAL_MIX);
		horizontalBlock(FABlocks.STONE_CRUCIBLE.get(),
				litMultiblockComplete("stone_crucible", "stone_foundry_multiblock", "front", mcLoc("block/furnace_front"), mcLoc("block/furnace_front_on")), 270);
		castingVessel(FABlocks.STONE_CASTING_VESSEL);
		existingBlockWithItem(FABlocks.STONE_WORKBENCH);
		existingBlockModel(FABlocks.BRICK_MAKER_FRAME);
		horizontalBlock(FABlocks.BRICK_CRUCIBLE.get(),
				litMultiblockComplete("brick_crucible", "brick_foundry_multiblock", "front", modLoc("block/brick_firebox_front"), modLoc("block/brick_firebox_front_lit")));
		horizontalBlock(FABlocks.BRICK_FIREBOX.get(), modLoc("block/brick_firebox_side"), modLoc("block/brick_firebox_front"), modLoc("block/brick_firebox_top"));
		//existingBlockWithItem(FABlocks.BRICK_CASTING_VESSEL);
		existingHorizontalBlockWithItem(FABlocks.BRICK_CASTING_VESSEL);
		blockWithItem(FABlocks.CREATIVE_MECHANICAL_SOURCE);
		axisOnlyBlock(FABlocks.WOOD_POWER_SHAFT, modLoc("block/power_shaft"), modLoc("block/wood_power_shaft"), mcLoc("block/oak_planks"));
		directionalBlock(FABlocks.WOOD_GEARBOX, modLoc("block/gearbox"), modLoc("block/wood_gearbox_side"), modLoc("block/wood_gearbox_front"), modLoc("block/wood_gearbox_back"));
		splitter(FABlocks.WOOD_SPLITTER, modLoc("block/splitter"), modLoc("block/wood_gearbox_side"), modLoc("block/wood_splitter_front"), modLoc("block/wood_splitter_back"));
		splitter(FABlocks.WOOD_JOINER, modLoc("block/joiner"), modLoc("block/wood_gearbox_side"), modLoc("block/wood_splitter_front"), modLoc("block/wood_splitter_back"));
		bevelGear(FABlocks.WOOD_BEVEL_GEAR, modLoc("block/bevel_gear"), modLoc("block/wood_gearbox_side"), modLoc("block/wood_bevel_gear_front"),
				modLoc("block/wood_splitter_back"));
		axisOnlyBlock(FABlocks.IRON_POWER_SHAFT, modLoc("block/power_shaft"), mcLoc("block/iron_block"), mcLoc("block/iron_block"));
		directionalBlock(FABlocks.IRON_GEARBOX, modLoc("block/gearbox"), modLoc("block/iron_gearbox_side"), modLoc("block/iron_gearbox_front"), modLoc("block/iron_gearbox_back"));
		splitter(FABlocks.IRON_SPLITTER, modLoc("block/splitter"), modLoc("block/iron_gearbox_side"), modLoc("block/iron_splitter_front"), modLoc("block/iron_splitter_back"));
		splitter(FABlocks.IRON_JOINER, modLoc("block/joiner"), modLoc("block/iron_gearbox_side"), modLoc("block/iron_splitter_front"), modLoc("block/iron_splitter_back"));
		bevelGear(FABlocks.IRON_BEVEL_GEAR, modLoc("block/bevel_gear"), modLoc("block/iron_gearbox_side"), modLoc("block/iron_bevel_gear_front"),
				modLoc("block/iron_splitter_back"));
		getVariantBuilder(FABlocks.HAND_CRANK.get()).forAllStates(
				s -> ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc("hand_crank" + (s.getValue(HandCrank.HANGING) ? "_hanging" : "")))).build());
		existingHorizontalBlockWithItem(FABlocks.TRIP_HAMMER);
		simplePillarBlock(FABlocks.WOODEN_TANK);
		existingHorizontalBlock(FABlocks.MINT_BUSH);
		pipe(FABlocks.COPPER_PIPE);
		fryingPan(FABlocks.FRYING_PAN);
	}

	private void stoneBlockForms(Map<StoneBlockForms, DeferredBlock<? extends Block>> blocks)
	{
		var texture = blockTexture(blocks.get(StoneBlockForms.BLOCK).get());
		var cube = cubeAll(blocks.get(StoneBlockForms.BLOCK).get());
		for (var entry : blocks.entrySet())
		{
			var f = entry.getKey();
			var b = entry.getValue();
			switch (f)
			{
				case BLOCK -> simpleBlockWithItem(b.get(), cube);
				case STAIRS -> stairsBlock((StairBlock) b.get(), texture);
				case SLAB -> slabBlock((SlabBlock) b.get(), models().slab(b.getRegisteredName(), texture, texture, texture),
						models().slabTop(b.getRegisteredName() + "_top", texture, texture, texture), cube);
				case WALL -> wallBlock((WallBlock) b.get(), texture);
			}
		}
	}

	private void existingBlockWithItem(DeferredBlock<?> block)
	{
		simpleBlockWithItem(block.get(), models().getExistingFile(block.getId().withPrefix("block/")));
	}

	private void existingHorizontalBlockWithItem(DeferredBlock<?> block)
	{
		var model = models().getExistingFile(block.getId().withPrefix("block/"));
		horizontalBlock(block.get(), model);
		simpleBlockItem(block.get(), model);
	}

	private void existingHorizontalBlock(DeferredBlock<?> block)
	{
		var model = models().getExistingFile(block.getId().withPrefix("block/"));
		horizontalBlock(block.get(), model);
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
		var logLoc = modLoc("block/" + type.getName() + "_chopping_block");
		var topLoc = logLoc.withSuffix("_top");
		simpleBlockWithItem(realCb, models().slab(choppingBlock.getRegisteredName(), logLoc, topLoc, topLoc));
	}

	private Function<BlockState, ModelFile> multiblockComplete(String base, String multiblock)
	{
		return state -> models().getExistingFile(modLoc("block/" + (state.getValue(StoneCrucible.MULTIBLOCK_COMPLETE) ? multiblock : base)));
	}

	private Function<BlockState, ModelFile> litMultiblockComplete(String base, String multiblock, String key, ResourceLocation unlitTexture, ResourceLocation litTexture)
	{
		return state -> {
			var complete = state.getValue(StoneCrucible.MULTIBLOCK_COMPLETE);
			var lit = state.getValue(BlockStateProperties.LIT);
			if (complete)
				return models().getBuilder(multiblock + (lit ? "_lit" : "_unlit"))
							   .parent(models().getExistingFile(modLoc("block/" + multiblock)))
							   .texture(key, lit ? litTexture : unlitTexture);
			else
				return models().getExistingFile(modLoc("block/" + base));
		};
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

	private void splitter(DeferredBlock<? extends Block> block, ResourceLocation base, ResourceLocation side, ResourceLocation front, ResourceLocation back)
	{
		var model = models().withExistingParent(block.getRegisteredName(), base).texture("side", side).texture("front", front).texture("back", back);
		// directionalBlock(block.get(), model);
		getVariantBuilder(block.get())
				.forAllStates(state -> {
					var dir = state.getValue(Splitter.FACING);
					var vertical = state.getValue(Splitter.VERTICAL);
					return ConfiguredModel.builder()
										  .modelFile(model)
										  .rotationX(vertical ? (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 90 : -90) : 0)
										  .rotationY(/*vertical ? 0 :*/ (((int) dir.toYRot()) + 180) % 360)
										  .build();
				});
		simpleBlockItem(block.get(), model);
	}

	private void bevelGear(DeferredBlock<? extends Block> block, ResourceLocation base, ResourceLocation side, ResourceLocation front, ResourceLocation back)
	{
		var model = models().withExistingParent(block.getRegisteredName(), base).texture("side", side).texture("front", front).texture("back", back);
		// directionalBlock(block.get(), model);
		getVariantBuilder(block.get())
				.forAllStates(state -> {
					var orientation = state.getValue(BevelGear.ORIENTATION);
					var vertical = orientation.front().getAxis() == Direction.Axis.Y;
					var dir = vertical ? orientation.top() : orientation.front();
					var up = orientation.front() == Direction.UP;
					return ConfiguredModel.builder()
										  .modelFile(model)
										  .rotationX(vertical ? (up ? -90 : 90) : 0)
										  .rotationY((((int) dir.toYRot()) + (vertical ? 270 : 180)) % 360)
										  .build();
				});
		simpleBlockItem(block.get(), model);
	}

	private void spaceFrame(DeferredBlock<SpaceFrameBlock> spaceFrame)
	{
		getVariantBuilder(spaceFrame.get()).forAllStates(blockState -> {
			if (blockState.getValue(SpaceFrameBlock.IS_TOP))
			{
				return new ConfiguredModel[] {
						new ConfiguredModel(models().withExistingParent(spaceFrame.getRegisteredName() + "_roofed", modLoc("block/space_frame_roofed"))
													.texture("side", spaceFrame.getId().withPrefix("block/"))
													.texture("end", spaceFrame.getId().withPrefix("block/").withSuffix("_top"))
													.texture("roof", spaceFrame.getId().withPrefix("block/").withSuffix("_top_roofed"))
													.renderType("minecraft:cutout"))
				};
			}
			else
			{
				return new ConfiguredModel[] {
						new ConfiguredModel(models().withExistingParent(spaceFrame.getRegisteredName(), modLoc("block/space_frame"))
													.texture("side", spaceFrame.getId().withPrefix("block/"))
													.texture("end", spaceFrame.getId().withPrefix("block/").withSuffix("_top"))
													.texture("roof", spaceFrame.getId().withPrefix("block/").withSuffix("_top"))
													.renderType("minecraft:cutout"))
				};
			}
		});
		simpleBlockItem(spaceFrame.get(), models().withExistingParent(spaceFrame.getRegisteredName() + "_roofed", modLoc("block/space_frame_roofed"))
												  .texture("side", spaceFrame.getId().withPrefix("block/"))
												  .texture("end", spaceFrame.getId().withPrefix("block/").withSuffix("_top"))
												  .texture("roof", spaceFrame.getId().withPrefix("block/").withSuffix("_top_roofed"))
												  .renderType("minecraft:cutout"));

	}

	private void simplePillarBlock(DeferredBlock<?> block)
	{
		var model = models().cubeColumn(block.getRegisteredName(), block.getId().withPrefix("block/").withSuffix("_side"),
				block.getId().withPrefix("block/").withSuffix("_top"));
		simpleBlockWithItem(block.get(), model);
	}

	private void pipe(DeferredBlock<Pipe> pipe)
	{
		var mpb = getMultipartBuilder(pipe.get()).part().modelFile(models().getExistingFile(modLoc("block/pipe_base"))).addModel().end();
		for (var dir : Direction.values())
		{
			mpb.part()
			   .modelFile(models().getExistingFile(modLoc("block/pipe_connector")))
			   .uvLock(false)
			   .rotationX(dir == Direction.DOWN ? 90 : dir.getAxis().isHorizontal() ? 0 : -90)
			   .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
			   .addModel()
			   .condition(Pipe.CONNECTIONS[dir.ordinal()], Pipe.Connection.JOIN)
			   .end();
			mpb.part()
			   .modelFile(models().getExistingFile(modLoc("block/pipe_end_connector")))
			   .uvLock(false)
			   .rotationX(dir == Direction.DOWN ? 90 : dir.getAxis().isHorizontal() ? 0 : -90)
			   .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
			   .addModel()
			   .condition(Pipe.CONNECTIONS[dir.ordinal()], Pipe.Connection.CONNECTOR)
			   .end();
		}
		simpleBlockItem(pipe.get(), models().getExistingFile(modLoc("item/pipe")));
	}

	private void fryingPan(DeferredBlock<FryingPan> pan)
	{
		horizontalBlock(pan.get(), s -> {
			var hot = s.getValue(FryingPan.TEMPERATURE) == FryingPan.Temperature.HOT;
			return models().getBuilder(pan.getRegisteredName() + (hot ? "_hot" : ""))
						   .parent(models().getExistingFile(modLoc("block/frying_pan")))
						   .texture("pan", pan.getId().withPrefix("block/") + (hot ? "_heated" : ""));
		}, 0);
		simpleBlockItem(pan.get(), models().getBuilder(pan.getRegisteredName())
										   .parent(models().getExistingFile(modLoc("block/frying_pan")))
										   .texture("pan", pan.getId().withPrefix("block/")));
	}

}
