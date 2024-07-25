package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class FAItemModelProvider extends ItemModelProvider
{
	public FAItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, FactoryAutomation.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		withExistingParent(FAItems.ROCK.getRegisteredName(), modLoc("block/cobblestone_rock"));
	}
}
