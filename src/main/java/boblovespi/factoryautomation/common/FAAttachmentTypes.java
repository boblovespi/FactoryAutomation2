package boblovespi.factoryautomation.common;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.potion.FocusedPlayerData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FAAttachmentTypes
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, FactoryAutomation.MODID);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<FocusedPlayerData>> FOCUSED_PLAYER_DATA = ATTACHMENT_TYPES.register("focused_player_data", () -> AttachmentType.builder(FocusedPlayerData::new).build());
}
