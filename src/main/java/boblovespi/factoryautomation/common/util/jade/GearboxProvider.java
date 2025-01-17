package boblovespi.factoryautomation.common.util.jade;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.mechanical.GearboxBE;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum GearboxProvider implements IBlockComponentProvider
{
	INSTANCE;

	private final ResourceLocation id = FactoryAutomation.name("gearbox");

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig)
	{
		var be = blockAccessor.getBlockEntity();
		if (be instanceof GearboxBE gearbox)
		{
			if (gearbox.getInputGear().isEmpty() && gearbox.getOutputGear().isEmpty())
				return;
			tooltip.add(IElementHelper.get().item(gearbox.getInputGear()));
			tooltip.append(IElementHelper.get().spacer(1, 0));
			tooltip.append(IElementHelper.get().item(gearbox.getOutputGear()));
			if (!gearbox.getInputGear().isEmpty() && !gearbox.getOutputGear().isEmpty())
				tooltip.add(Component.translatable("misc.gear_ratio", formatRatio(gearbox.getOutputRatio()), formatRatio(gearbox.getInputRatio())));
		}
	}

	private String formatRatio(float r)
	{
		if (r == (int) r)
			return String.format("%d", (int) r);
		else
			return String.format("%s", r);
	}

	@Override
	public ResourceLocation getUid()
	{
		return id;
	}
}
