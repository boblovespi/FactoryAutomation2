package boblovespi.factoryautomation.data.tag;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FADamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class FADamageTypeTagProvider extends DamageTypeTagsProvider
{
	public FADamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, FactoryAutomation.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		tag(DamageTypeTags.BYPASSES_ARMOR).add(FADamageTypes.PUNCHING_WOOD, FADamageTypes.METAL_TOO_HOT);
		tag(DamageTypeTags.NO_KNOCKBACK).add(FADamageTypes.PUNCHING_WOOD, FADamageTypes.METAL_TOO_HOT);
		tag(DamageTypeTags.IS_FIRE).add(FADamageTypes.METAL_TOO_HOT);
	}
}
