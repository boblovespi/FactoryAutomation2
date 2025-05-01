package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.common.util.Form;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record CasterType(Map<Form, Float> efficiencies)
{
	public static final CasterType STONE = normal(1.5f, List.of(Form.ROD, Form.SHEET, Form.GEAR));
	public static final CasterType BRICK = normal(4/3f, List.of(Form.BLOCK, Form.ROD, Form.SHEET, Form.GEAR));

	public static CasterType normal(float efficiency, Collection<Form> validForms)
	{
		var map = new LinkedHashMap<Form, Float>();
		map.put(Form.INGOT, 1f);
		map.put(Form.NUGGET, 1f);
		validForms.forEach(form -> map.put(form, form == Form.BLOCK ? 1f : efficiency));
		return new CasterType(map);
	}
}
