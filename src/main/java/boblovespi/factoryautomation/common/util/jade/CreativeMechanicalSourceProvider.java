package boblovespi.factoryautomation.common.util.jade;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.mechanical.CreativeMechanicalSourceBE;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CreativeMechanicalSourceProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor>
{
	INSTANCE;

	private final ResourceLocation id = FactoryAutomation.name("creative_mechanical_source");

	@Override
	public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig)
	{
		var tag = blockAccessor.getServerData();
		if (tag.contains("speed") && tag.contains("torque"))
		{
			var speed = tag.getFloat("speed");
			var torque = tag.getFloat("torque");
			var power = speed * torque;
			iTooltip.add(Component.translatable("misc.power_torque_speed", power, torque, speed));
		}
		else
			iTooltip.add(Component.literal("???"));
	}

	@Override
	public ResourceLocation getUid()
	{
		return id;
	}

	@Override
	public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor)
	{
		var be = blockAccessor.getBlockEntity();
		if (be instanceof CreativeMechanicalSourceBE cms)
		{
			tag.putFloat("speed", cms.getSpeed());
			tag.putFloat("torque", cms.getTorque());
		}
	}
}
