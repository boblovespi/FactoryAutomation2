package boblovespi.factoryautomation.common.util.jade;

import boblovespi.factoryautomation.common.block.mechanical.CreativeMechanicalSource;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.processing.StoneCastingVessel;
import boblovespi.factoryautomation.common.blockentity.processing.BrickMakerFrameBE;
import boblovespi.factoryautomation.common.blockentity.processing.ChoppingBlockBE;
import boblovespi.factoryautomation.common.blockentity.processing.MillstoneBE;
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
		registration.registerBlockDataProvider(CreativeMechanicalSourceProvider.INSTANCE, CreativeMechanicalSource.class);
		registration.registerItemStorage(BrickMakerProvider.INSTANCE, BrickMakerFrameBE.class);
		registration.registerItemStorage(FAItemStorageProvider.INSTANCE, ChoppingBlockBE.class);
		registration.registerItemStorage(FAItemStorageProvider.INSTANCE, MillstoneBE.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration)
	{
		registration.registerBlockComponent(CastingVesselComponentProvider.INSTANCE, StoneCastingVessel.class);
		registration.registerBlockComponent(CreativeMechanicalSourceProvider.INSTANCE, CreativeMechanicalSource.class);
		registration.registerBlockComponent(GearboxProvider.INSTANCE, Gearbox.class);
		registration.registerItemStorageClient(BrickMakerProvider.INSTANCE);
		registration.registerItemStorageClient(FAItemStorageProvider.INSTANCE);
	}
}
