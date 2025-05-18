package boblovespi.factoryautomation.common.util.ponder;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FAParticleTypes;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.block.processing.*;
import boblovespi.factoryautomation.common.blockentity.mechanical.PowerShaftBE;
import boblovespi.factoryautomation.common.blockentity.processing.*;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.Metal;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.Ponder;
import net.createmod.ponder.api.ParticleEmitter;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Optional;

public class FAPonderPlugin implements PonderPlugin
{
	@Override
	public String getModId()
	{
		return FactoryAutomation.MODID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> h)
	{
		var helper = h.withKeyFunction(t -> switch (t)
		{
			case DeferredHolder<?, ?> holder -> holder.getId();
			case Item i -> BuiltInRegistries.ITEM.getKey(i);
			default -> throw new RuntimeException("Could not register ponder: %s is not an item or deferred holder???".formatted(t));
		});
		// helper.addStoryBoard(FAItems.LOG_PILE.getId(), "test", this::testScene).highlightAllTags();
		helper.addStoryBoard(FAItems.LOG_PILE, "log_pile", this::logPileScene).highlightAllTags();
		helper.forComponents(FAItems.STONE_CRUCIBLE, FAItems.STONE_CASTING_VESSEL, Items.FURNACE).addStoryBoard("stone_foundry", this::stoneFoundry);
		helper.forComponents(FAItems.ROCK).addStoryBoard("chopping_block_creation", this::choppingBlockCreationScene);
		helper.forComponents(FABlocks.CHOPPING_BLOCK, FAItems.CHOPPING_BLADE).addStoryBoard("chopping_block_usage", this::choppingBlockUsageScene);
		helper.forComponents(FAItems.IRON_SHARD, FABlocks.LIMONITE_CHARCOAL_MIX).addStoryBoard("iron_bloom_creation", this::ironBloomCreationScene);
		helper.forComponents(Items.BRICK, Items.BRICKS, FABlocks.BRICK_MAKER_FRAME, FAItems.DRIED_BRICK, FABlocks.DRIED_BRICKS).addStoryBoard("brick_making", this::brickMakingScene);
		helper.forComponents(FAItems.BRICK_CRUCIBLE, FAItems.BRICK_FIREBOX, FAItems.BRICK_CASTING_VESSEL).addStoryBoard("brick_foundry", this::brickFoundry);
		helper.forComponents(FAItems.TRIP_HAMMER).addStoryBoard("trip_hammer", this::tripHammer);
	}


	private void testScene(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("test_1", "Hello, world!");
		scene.showBasePlate();
		scene.idle(10);
		scene.world().showSection(util.select().layersFrom(1), Direction.DOWN);
		scene.idle(20);

		var zero = util.select().position(1, 1, 1);
		scene.addKeyframe();
		var logPile = FABlocks.LOG_PILE.get().defaultBlockState().setValue(LogPile.ACTIVATED, true);
		scene.world().setBlocks(zero, logPile, false);
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showOutlineWithText(zero, 40)
			 .colored(PonderPalette.INPUT)
			 .text("This turns into a charcoal pile");
		scene.idle(60);

		scene.addKeyframe();
		scene.world().replaceBlocks(zero, FABlocks.CHARCOAL_PILE.get().defaultBlockState(), false);
	}

	private void choppingBlockCreationScene(SceneBuilder scene, SceneBuildingUtil util){
		scene.title("chopping_block_creation", "Making and Usage of the Chopping Block I");
		scene.showBasePlate();
		scene.idle(10);
		scene.world().showSection(util.select().layersFrom(1), Direction.DOWN);
		scene.idle(30);

		scene.addKeyframe();
		scene.overlay().showControls(util.select().position(1, 1, 1).getCenter(), Pointing.RIGHT, 40)
				.rightClick()
				.whileSneaking()
				.withItem(FAItems.ROCK.toStack());
		scene.idle(30);

		scene.addKeyframe();
		scene.world().replaceBlocks(util.select().position(1, 1, 1), FABlocks.CHOPPING_BLOCK.get().defaultBlockState(), true);
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showText(60)
				.text("Put Things on it")
				.colored(PonderPalette.WHITE)
				.pointAt(util.select().position(1, 1, 1).getCenter());
		scene.idle(60);
	}

	private void choppingBlockUsageScene(SceneBuilder scene, SceneBuildingUtil util){
		scene.title("chopping_block_usage", "Making and Usage of the Chopping Block II");
		scene.showBasePlate();
		scene.idle(10);
		scene.world().showSection(util.select().layersFrom(1), Direction.DOWN);
		scene.idle(30);

		scene.addKeyframe();
		scene.overlay().showControls(util.select().position(1, 1, 1).getCenter(), Pointing.RIGHT, 40)
				.rightClick()
				.withItem(Items.OAK_LOG.getDefaultInstance());
		scene.idle(30);
		scene.world().modifyBlockEntity(util.grid().at(1, 1, 1), ChoppingBlockBE.class, be -> ((ChoppingBlockBE)be).placeItem(Items.OAK_LOG.getDefaultInstance()));
		scene.idle(30);

		scene.addKeyframe();
		scene.overlay().showControls(new Vec3(1, 3, 1), Pointing.DOWN, 60)
				.leftClick()
				.withItem(FAItems.CHOPPING_BLADE.get().getDefaultInstance());
		scene.overlay().showText(60)
				.text("Hit it many times")
				.colored(PonderPalette.WHITE)
				.pointAt(util.select().position(1, 1, 1).getCenter());
		scene.idle(50);

		scene.addKeyframe();
		scene.world().setBlocks(util.select().position(1, 1, 1), Blocks.AIR.defaultBlockState(), false);
		scene.world().setBlocks(util.select().position(1, 1, 1), FABlocks.CHOPPING_BLOCK.get().defaultBlockState(), true);
		scene.world().modifyBlockEntity(util.grid().at(1, 1, 1), ChoppingBlockBE.class, be -> ((ChoppingBlockBE)be).placeItem(Items.OAK_PLANKS.getDefaultInstance()));
		scene.idle(20);
	}

	private void logPileScene(SceneBuilder scene, SceneBuildingUtil util) {
		var dirtSide = util.select().fromTo(1, 1, 0, 2, 1, 0)
						   .add(util.select().fromTo(0, 1, 1, 0, 1, 2))
						   .add(util.select().fromTo(1, 1, 3, 2, 1, 3))
						   .add(util.select().fromTo(3, 1, 1, 3, 1, 2));
		var logPiles = util.select().fromTo(1, 1, 1, 2, 1, 2);
		var aboveLogPiles = util.select().fromTo(1, 2, 1, 2, 2, 2);
		var logPileLoc = util.grid().at(1, 1, 1);
		var logPileLocs = List.of(logPileLoc, util.grid().at(2, 1, 1), util.grid().at(2, 1, 2), util.grid().at(1, 1, 2));
		var dirt = Blocks.DIRT.defaultBlockState();
		var litLogPile = FABlocks.LOG_PILE.get().defaultBlockState().setValue(LogPile.ACTIVATED, true);
		var fire = Blocks.FIRE.defaultBlockState();
		var charcoalPile = FABlocks.CHARCOAL_PILE.get().defaultBlockState();

		scene.title("log_pile", "Firing Log Piles");
		scene.showBasePlate();
		scene.idle(10);
		scene.world().showSection(logPiles, Direction.DOWN);
		scene.idle(20);

		scene.addKeyframe();
		scene.world().showSection(dirtSide, Direction.DOWN);
		scene.world().showSection(util.select().layersFrom(2), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showOutlineWithText(dirtSide, 40)
			 .text("Surround the log piles with any solid block");
		scene.idle(60);

		scene.addKeyframe();
		scene.overlay().showControls(logPileLoc.above().getBottomCenter(), Pointing.DOWN, 20)
			 .rightClick()
			 .withItem(FAItems.FIREBOW.toStack());
		scene.idle(30);
		scene.world().replaceBlocks(logPiles, litLogPile, false);
		scene.idle(2);
		var r = Ponder.RANDOM;
		var lavaEmitter = inWholeBlock((w, x, y, z) -> w.addParticle(ParticleTypes.LAVA, x, y, z, r.nextDouble() / 20, r.nextDouble() / 20, r.nextDouble() / 20));
		var smokeEmitter = inWholeBlock((w, x, y, z) -> w.addParticle(ParticleTypes.SMOKE, x, y + 1.5, z, r.nextDouble() / 20, 0.05, r.nextDouble() / 20));
		logPileLocs.forEach(p -> {
			scene.effects().emitParticles(Vec3.atLowerCornerOf(p), lavaEmitter, 1, 100);
			scene.effects().emitParticles(Vec3.atLowerCornerOf(p), smokeEmitter, 2, 100);
		});
		scene.world().setBlocks(aboveLogPiles, fire, false);
		scene.idle(18);

		scene.addKeyframe();
		scene.world().replaceBlocks(aboveLogPiles, dirt, false);
		scene.idle(120);

		scene.addKeyframe();
		scene.world().replaceBlocks(logPiles, charcoalPile, false);
		for (var i = 0; i < 10; i++)
		{
			scene.idle(2);
			scene.world().incrementBlockBreakingProgress(util.grid().at(1, 2, 1));
		}
		scene.world().setBlocks(aboveLogPiles, Blocks.AIR.defaultBlockState(), true);
		scene.idle(20);

		scene.addKeyframe();
		scene.world().hideSection(dirtSide, Direction.UP);
		scene.idle(20);
		scene.overlay().showControls(logPileLoc.above().getBottomCenter(), Pointing.DOWN, 20)
			 .leftClick()
			 .withItem(Items.DIAMOND_SHOVEL.getDefaultInstance());
		scene.idle(20);
		for (var i = 0; i < 10; i++)
		{
			scene.idle(2);
			scene.world().incrementBlockBreakingProgress(logPileLoc);
		}
		scene.world().createItemEntity(logPileLoc.getCenter(), Vec3.ZERO, Items.CHARCOAL.getDefaultInstance().copyWithCount(5));
		scene.idle(20);
	}

	private void stoneFoundry(SceneBuilder scene, SceneBuildingUtil util) {
		var center = util.select().fromTo(1, 1, 1, 1, 2, 1);
		var crucibleLoc = util.grid().at(1, 2, 1);
		var furnaceLoc = util.grid().at(1, 1, 1);
		var castLoc = util.grid().at(0, 1, 1);
		var cast = util.select().position(castLoc);
		var slot = new Object();

		scene.title("stone_foundry", "Assembling the Stone Foundry");
		scene.showBasePlate();
		scene.idle(10);
		scene.world().showSection(center, Direction.DOWN);
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showControls(crucibleLoc.getCenter(), Pointing.RIGHT, 20)
			 .rightClick();
		scene.idle(30);
		scene.world().cycleBlockProperty(crucibleLoc, StoneCrucible.MULTIBLOCK_COMPLETE);
		scene.world().setBlock(furnaceLoc, FABlocks.MULTIBLOCK_PART.get().defaultBlockState(), false);
		scene.overlay().showOutline(PonderPalette.INPUT, slot, center, 20);
		scene.effects().indicateSuccess(crucibleLoc);
		scene.effects().indicateSuccess(furnaceLoc);
		scene.idle(40);

		scene.addKeyframe();
		scene.world().showSection(cast, Direction.DOWN);
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showControls(cast.getCenter(), Pointing.RIGHT, 20)
			 .rightClick()
			 .withItem(FAItems.GREEN_SAND.toStack());
		scene.idle(30);
		scene.world().cycleBlockProperty(castLoc, StoneCastingVessel.MOLD);
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showControls(cast.getCenter(), Pointing.RIGHT, 20)
			 .rightClick()
			 .withItem(Items.STICK.getDefaultInstance());
		scene.idle(30);
		scene.world().cycleBlockProperty(castLoc, StoneCastingVessel.MOLD);
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showControls(crucibleLoc.getCenter(), Pointing.LEFT, 20)
			 .rightClick();
		scene.idle(30);
		scene.world().modifyBlockEntity(castLoc, StoneCastingVesselBE.class, b -> b.cast(n -> Optional.of(Metal.COPPER)));
		var r = Ponder.RANDOM;
		var sparks = serverSent(FAParticleTypes.METAL_SPARK.get(), 0.3, 0, 0.3, 0);
		scene.effects().emitParticles(castLoc.getBottomCenter().add(0, 0.4, 0), sparks, 50, 1);
		scene.idle(20);
	}

	private void ironBloomCreationScene(SceneBuilder scene, SceneBuildingUtil util){
		scene.title("iron_bloom_creation", "Creation of Iron Shards");
		scene.showBasePlate();
		scene.idle(10);

		//ik you can "reveal" more or all blocks at once but its a fancier animation if they are revealed one by one...
		scene.addKeyframe();
		scene.world().replaceBlocks(util.select().position(1, 0, 1), FABlocks.COPPER_PLATE_BLOCK.get().defaultBlockState(), true);
		scene.idle(10);
		scene.world().replaceBlocks(util.select().position(2, 0, 1), FABlocks.COPPER_PLATE_BLOCK.get().defaultBlockState(), true);
		scene.overlay().showOutlineWithText(util.select().fromTo(1, 0, 1, 2, 0, 2), 50)
				.text("Make sure to enclose it all around")
				.colored(PonderPalette.WHITE)
				.pointAt(util.select().position(1, 1, 1).getCenter());
		scene.idle(10);
		scene.world().replaceBlocks(util.select().position(2, 0, 2), FABlocks.COPPER_PLATE_BLOCK.get().defaultBlockState(), true);
		scene.idle(10);
		scene.world().replaceBlocks(util.select().position(1, 0, 2), FABlocks.COPPER_PLATE_BLOCK.get().defaultBlockState(), true);
		scene.idle(30);

		scene.addKeyframe();
		scene.world().showSection(util.select().fromTo(3, 1, 1, 3, 1, 2), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().fromTo(2, 1, 3, 1, 1, 3), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().fromTo(2, 1, 0, 1, 1, 0), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().fromTo(0, 1, 1, 0, 1, 2), Direction.DOWN);
		scene.idle(20);
		scene.world().showSection(util.select().fromTo(1, 1, 1, 2, 1, 2), Direction.DOWN);
		scene.idle(20);

		scene.addKeyframe();
		scene.world().setBlock(util.grid().at(1, 2, 1), Blocks.AIR.defaultBlockState(),false);
		scene.world().showSection(util.select().position(1, 2, 1), Direction.DOWN);
		scene.overlay().showControls(util.grid().at(1, 1, 1).above().getBottomCenter(), Pointing.DOWN, 35)
				.rightClick()
				.withItem(FAItems.FIREBOW.toStack());
		scene.idle(15);
		scene.world().showSection(util.select().position(1, 2, 2), Direction.DOWN);
		scene.world().showSection(util.select().fromTo(2, 2, 1, 2, 2, 2), Direction.DOWN);
		scene.idle(5);
		scene.world().setBlock(util.grid().at(1, 2, 1), Blocks.FIRE.defaultBlockState(),false);
		scene.idle(5);
		var lavaEmitter = inWholeBlock((w, x, y, z) -> w.addParticle(ParticleTypes.LAVA, x, y, z, Ponder.RANDOM.nextDouble() / 20, Ponder.RANDOM.nextDouble() / 20, Ponder.RANDOM.nextDouble() / 20));
		var smokeEmitter = inWholeBlock((w, x, y, z) -> w.addParticle(ParticleTypes.SMOKE, x, y + 1.5, z, Ponder.RANDOM.nextDouble() / 20, 0.05, Ponder.RANDOM.nextDouble() / 20));
		util.select().fromTo(1, 1, 1, 2, 1, 2).forEach(p -> {
			scene.effects().emitParticles(Vec3.atLowerCornerOf(p), lavaEmitter, 1, 100);
			scene.effects().emitParticles(Vec3.atLowerCornerOf(p), smokeEmitter, 2, 100);
		});
		scene.idle(10);
		scene.world().setBlock(util.grid().at(1, 2, 1), FABlocks.COPPER_PLATE_BLOCK.get().defaultBlockState(),false);
		scene.idle(50);
		scene.world().replaceBlocks(util.select().fromTo(1, 1, 1, 2, 1, 2), FABlocks.IRON_BLOOM.get().defaultBlockState(), false);
		scene.idle(60);

		scene.addKeyframe();
		scene.world().hideSection(util.select().fromTo(1, 2, 1, 2, 2, 2), Direction.UP);
		scene.idle(20);
		scene.world().hideSection(util.select().fromTo(3, 1, 1, 3, 1, 2), Direction.UP);
		scene.idle(2);
		scene.world().hideSection(util.select().fromTo(2, 1, 3, 1, 1, 3), Direction.UP);
		scene.idle(2);
		scene.world().hideSection(util.select().fromTo(2, 1, 0, 1, 1, 0), Direction.UP);
		scene.idle(2);
		scene.world().hideSection(util.select().fromTo(0, 1, 1, 0, 1, 2), Direction.UP);
		scene.idle(20);
		scene.overlay().showControls(util.grid().at(1, 1, 1).above().getBottomCenter(), Pointing.DOWN, 35)
				.leftClick()
				.withItem(FAItems.COPPER_HAMMER.get().getDefaultInstance());
		scene.idle(20);
		for (var i = 0; i < 10; i++) {
			scene.idle(2);
			scene.world().incrementBlockBreakingProgress(util.grid().at(1, 1, 1));
		}
		scene.world().createItemEntity(util.grid().at(1, 1, 1).getCenter(), Vec3.ZERO, FAItems.IRON_SHARD.get().getDefaultInstance().copyWithCount(1));
		scene.idle(20);
	}

	private void brickMakingScene(SceneBuilder scene, SceneBuildingUtil util){
		scene.title("brick_making", "Creating Bricks");
		scene.showBasePlate();
		scene.idle(10);
		scene.world().setBlock(util.grid().at(1, 2, 1), Blocks.AIR.defaultBlockState(),false);

		scene.addKeyframe();
		scene.world().showSection(util.select().fromTo(2, 1, 2, 2, 1, 1), Direction.DOWN);
		scene.idle(20);
		scene.world().showSection(util.select().position(3,1,1), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().position(3,1,2), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().position(3,1,3), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().position(2,1,3), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().position(1,1,3), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().position(1,1,2), Direction.DOWN);
		scene.idle(2);
		scene.world().showSection(util.select().position(1,1,1), Direction.DOWN);
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showText(40)
				.text("Cover on at least two sides")
				.colored(PonderPalette.WHITE)
				.pointAt(util.select().position(2, 1, 1).getCenter());
		scene.idle(40);

		scene.addKeyframe();
		scene.world().showSection(util.select().fromTo(4, 1, 1, 4, 1, 3), Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(util.select().fromTo(3, 1, 4, 1, 1, 4), Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(util.select().fromTo(0, 1, 3, 0, 1, 1), Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(util.select().fromTo(3, 1, 0, 1, 1, 0), Direction.DOWN);
		scene.idle(20);

		scene.addKeyframe();
		scene.world().showSection(util.select().fromTo(1, 2, 1, 3, 2, 3), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showControls(util.grid().at(1, 2, 1).above().getBottomCenter(), Pointing.DOWN, 35)
				.rightClick()
				.withItem(FAItems.FIREBOW.toStack());
		scene.idle(10);
		scene.world().setBlock(util.grid().at(1, 2, 1), Blocks.FIRE.defaultBlockState(),false);
		scene.idle(10);
		scene.world().setBlock(util.grid().at(1, 2, 1), Blocks.COBBLESTONE.defaultBlockState(),false);
		scene.idle(5);
		var lavaEmitter = inWholeBlock((w, x, y, z) -> w.addParticle(ParticleTypes.LAVA, x, y, z, Ponder.RANDOM.nextDouble() / 20, Ponder.RANDOM.nextDouble() / 20, Ponder.RANDOM.nextDouble() / 20));
		var smokeEmitter = inWholeBlock((w, x, y, z) -> w.addParticle(ParticleTypes.SMOKE, x, y + 1.5, z, Ponder.RANDOM.nextDouble() / 20, 0.05, Ponder.RANDOM.nextDouble() / 20));
		util.select().fromTo(1, 1, 1, 3, 1, 3).forEach(p -> {
			scene.effects().emitParticles(Vec3.atLowerCornerOf(p), lavaEmitter, 1, 100);
			scene.effects().emitParticles(Vec3.atLowerCornerOf(p), smokeEmitter, 2, 100);
		});
		scene.idle(10);
		scene.world().replaceBlocks(util.select().fromTo(1, 1, 1, 3, 1, 3), FABlocks.CHARCOAL_PILE.get().defaultBlockState(),false);
		scene.idle(10);
		scene.world().replaceBlocks(util.select().fromTo(2, 1, 1, 2, 1, 2), Blocks.BRICKS.defaultBlockState(),false);
		scene.idle(100);

		scene.addKeyframe();
		scene.world().hideSection(util.select().fromTo(1,2,1, 3,2,3), Direction.UP);
		scene.idle(20);
		scene.world().hideSection(util.select().fromTo(1,1,0, 3,1,0), Direction.UP);
		scene.idle(2);
		scene.world().hideSection(util.select().fromTo(4,1,1, 4,1,3), Direction.UP);
		scene.idle(2);
		scene.world().hideSection(util.select().fromTo(3,1,4, 1,1,4), Direction.UP);
		scene.idle(2);
		scene.world().hideSection(util.select().fromTo(0,1,3, 0,1,1), Direction.UP);
		scene.idle(20);


	}

	private void brickFoundry(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("brick_foundry", "The Brick Foundry");
		var cruciblePos = util.grid().at(2,2,2);
		var vesselPos = util.grid().at(1,1,2);
		var fireboxPos = util.grid().at(2,1,2);
		var bellowPos = util.grid().at(3,1,2);

		scene.showBasePlate();
		scene.idle(20);
		scene.world().showSection(util.select().fromTo(1, 1, 1, 4, 2, 4), Direction.DOWN);
		scene.idle(20);
		scene.world().hideSection(util.select().position(bellowPos), Direction.UP);

		scene.addKeyframe();
		scene.world().setBlock(fireboxPos, FABlocks.BRICK_FIREBOX.get().defaultBlockState(),true);
		scene.overlay().showText(60)
				.text("Getting an Upgrade")
				.colored(PonderPalette.WHITE)
				.pointAt(fireboxPos.getCenter())
				.independent();
		scene.idle(10);
		scene.world().setBlock(vesselPos, FABlocks.BRICK_CASTING_VESSEL.get().defaultBlockState(),true);
		scene.idle(10);
		scene.world().setBlock(cruciblePos, FABlocks.BRICK_CRUCIBLE.get().defaultBlockState().setValue(BrickCrucible.FACING, Direction.WEST),true);
		scene.idle(40);

		scene.addKeyframe();
		scene.overlay().showControls(cruciblePos.above().getBottomCenter(), Pointing.RIGHT, 20)
				.rightClick();
		scene.idle(30);
		scene.world().setBlock(bellowPos, FABlocks.PAPER_BELLOWS.get().defaultBlockState().setValue(PaperBellows.FACING, Direction.WEST),false);
		scene.world().cycleBlockProperty(cruciblePos, BrickCrucible.MULTIBLOCK_COMPLETE);
		scene.world().setBlock(fireboxPos, FABlocks.MULTIBLOCK_PART.get().defaultBlockState(), false);
		scene.effects().indicateSuccess(fireboxPos);
		scene.effects().indicateSuccess(cruciblePos);
		scene.idle(10);

		scene.addKeyframe();
		scene.overlay().showControls(vesselPos.getCenter(), Pointing.RIGHT, 20)
				.rightClick()
				.withItem(FAItems.FIRED_TALLOW_MOLDS.get(Form.INGOT).get().getDefaultInstance());
		scene.idle(20);
		scene.world().cycleBlockProperty(vesselPos, BrickCastingVessel.MOLD);
		scene.world().modifyBlockEntity(vesselPos, BrickCastingVesselBE.class, be -> be.placeItem(FAItems.FIRED_TALLOW_MOLDS.get(Form.INGOT).get().getDefaultInstance()));
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showControls(cruciblePos.getCenter(), Pointing.LEFT, 20)
				.rightClick();
		scene.idle(30);
		scene.world().modifyBlockEntity(vesselPos, BrickCastingVesselBE.class, b -> b.cast(n -> Optional.of(Metal.COPPER)));
		var sparks = serverSent(FAParticleTypes.METAL_SPARK.get(), 0.3, 0, 0.3, 0);
		scene.effects().emitParticles(vesselPos.above().getBottomCenter(), sparks, 50, 1);
		scene.idle(30);

		scene.addKeyframe();
		scene.world().showSection(util.select().position(bellowPos), Direction.DOWN);
		scene.idle(20);
		scene.overlay().showText(40)
				.text("Efficiency!")
				.colored(PonderPalette.WHITE)
				.pointAt(bellowPos.getCenter())
				.independent();
		scene.idle(40);
	}

	private void tripHammer(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("trip_hammer", "The Trip Hammer");
		var hammerTablePos = util.grid().at(4,1,1);
		var buildingBlocksOfHammer = util.select().fromTo(4,2,1, 4, 2, 6).add(util.select().position(4,1,4));

		scene.showBasePlate();
		scene.idle(20);
		buildingBlocksOfHammer.forEach(p -> {
            scene.world().showSection(util.select().position(p), Direction.DOWN);
			scene.idle(7);
        });
		scene.idle(20);

		scene.addKeyframe();
		scene.overlay().showOutline(PonderPalette.GREEN, new Object(), util.select().position(hammerTablePos), 60);
		scene.overlay().showText(50)
				.text("Place hammer here")
				.colored(PonderPalette.WHITE)
				.pointAt(hammerTablePos.getCenter());
		scene.idle(60);
		scene.world().showSection(util.select().position(hammerTablePos), Direction.WEST);
		scene.idle(5);
		scene.world().replaceBlocks(buildingBlocksOfHammer, FABlocks.MULTIBLOCK_PART.get().defaultBlockState(), false);
		scene.idle(10);

		scene.addKeyframe();
		scene.overlay().showOutline(PonderPalette.GREEN, new Object(), util.select().position(4,2,6), 60);
		scene.overlay().showText(50)
				.text("Input here")
				.colored(PonderPalette.WHITE)
				.pointAt(util.select().position(4,2,6).getCenter());
		scene.idle(50);
		scene.world().setBlock(util.grid().at(3,2,6), FABlocks.WOOD_POWER_SHAFT.get().defaultBlockState().setValue(PowerShaft.AXIS, Direction.Axis.X),true);
		scene.world().showSection(util.select().position(3,2,6), Direction.EAST);
		scene.idle(10);

	}

	private ParticleEmitter inWholeBlock(ParticleEmitter emitter) {
		var r = Ponder.RANDOM;
		return (w, x, y, z) -> emitter.create(w, x + r.nextDouble(), y + r.nextDouble(), z + r.nextDouble());
	}

	private <T extends ParticleOptions> ParticleEmitter serverSent(T type, double xOff, double yOff, double zOff, double spd) {
		var r = Ponder.RANDOM;
		return (w, x, y, z) -> w.addParticle(type,
				x + r.nextGaussian() * xOff,
				y + r.nextGaussian() * yOff,
				z + r.nextGaussian() * zOff,
				spd * r.nextGaussian(),
				spd * r.nextGaussian(),
				spd * r.nextGaussian());
	}
}
