package boblovespi.factoryautomation.common.util.jade;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.processing.StoneCastingVessel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

import java.util.List;

public class CastingVesselComponentProvider implements IBlockComponentProvider
{
	private final List<ResourceLocation> molds = List.of(
			FactoryAutomation.name("textures/block/green_sand.png"),
			FactoryAutomation.name("textures/block/casting_sand_ingot_pattern.png"),
			FactoryAutomation.name("textures/block/casting_sand_nugget_pattern.png"),
			FactoryAutomation.name("textures/block/casting_sand_sheet_pattern.png"),
			FactoryAutomation.name("textures/block/casting_sand_rod_pattern.png"),
			FactoryAutomation.name("textures/block/casting_sand_gear_pattern.png"),
			FactoryAutomation.name("textures/block/casting_sand_coin_pattern.png"));
	private final ResourceLocation id = FactoryAutomation.name("stone_casting_vessel");

	@Override
	public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig)
	{
		var form = blockAccessor.getBlockState().getValue(StoneCastingVessel.MOLD);
		if (form.ordinal() > 1)
		{
			var moldTexture = new ImageElement(molds.get(form.ordinal() - 1), 16, 16);
			moldTexture.message(null);
			iTooltip.add(moldTexture);
			iTooltip.append(IElementHelper.get().spacer(5, 1));
			var text = IElementHelper.get().text(Component.translatable("form." + form.metalForm.getName() + ".name")).translate(new Vec2(0, 4));
			iTooltip.append(text);
		}
	}

	@Override
	public ResourceLocation getUid()
	{
		return id;
	}
}
