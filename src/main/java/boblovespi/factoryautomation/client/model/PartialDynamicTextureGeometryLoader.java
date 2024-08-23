package boblovespi.factoryautomation.client.model;

import boblovespi.factoryautomation.FactoryAutomation;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class PartialDynamicTextureGeometryLoader implements IGeometryLoader<PartialDynamicTextureGeometry>
{
	public static final ResourceLocation ID = FactoryAutomation.name("pdtg_loader");
	public static final IGeometryLoader<PartialDynamicTextureGeometry> INSTANCE = new PartialDynamicTextureGeometryLoader();

	@Override
	public PartialDynamicTextureGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException
	{
		jsonObject.remove("loader");
		var elements = jsonObject.getAsJsonArray("elements");
		var overrides = new int[elements.size()];
		for (var i = 0; i < overrides.length; i++)
		{
			var element = elements.get(i);
			if (element.getAsJsonObject().has("dynamic_index"))
			{
				overrides[i] = element.getAsJsonObject().get("dynamic_index").getAsInt();
				element.getAsJsonObject().remove("dynamic_index");
			}
			else
				overrides[i] = -1;
		}
		var base = deserializationContext.<BlockModel>deserialize(jsonObject, BlockModel.class);
		return new PartialDynamicTextureGeometry(base, overrides);
	}
}
