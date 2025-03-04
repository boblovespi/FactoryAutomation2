package boblovespi.factoryautomation.common.util.ponder;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.processing.LogPile;
import boblovespi.factoryautomation.common.item.FAItems;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class FAPonderPlugin implements PonderPlugin
{
	@Override
	public String getModId()
	{
		return FactoryAutomation.MODID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper)
	{
		helper.addStoryBoard(FAItems.LOG_PILE.getId(), "test", this::testScene).highlightAllTags();
	}

	private void testScene(SceneBuilder scene, SceneBuildingUtil util)
	{
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
}
