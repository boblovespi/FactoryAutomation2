package boblovespi.factoryautomation.common.util.jade;

import boblovespi.factoryautomation.common.block.processing.StoneCastingVessel;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class FAJadePlugin implements IWailaPlugin
{
	@Override
	public void register(IWailaCommonRegistration registration)
	{

	}

	@Override
	public void registerClient(IWailaClientRegistration registration)
	{
		registration.registerBlockComponent(new CastingVesselComponentProvider(), StoneCastingVessel.class);
	}
}
