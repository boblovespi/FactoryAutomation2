package boblovespi.factoryautomation.api;

public interface IHeatUser
{
	float getTemperature();

	float getHeatCapacity();

	default float getEnergy()
	{
		return getTemperature() * getHeatCapacity();
	}

	float getConductivity();

	/**
	 * The contact parameter is an abstracted constant that represents a surface's ability to
	 * conduct heat through contact. A value of 0 indicates that the surface is extremely rough, so
	 * no heat will ever conduct through contact. A value of 1 indicates that the surface is
	 * equivalent to a perfectly bonded material.
	 *
	 * @return The contact parameter, between 0 and 1.
	 */
	float getContactParameter();

	void heat(float energy);

	default void conductWith(IHeatUser that)
	{
		// see https://en.wikipedia.org/wiki/Thermal_contact_conductance
		var h_gas = 10;
		var mcp = 0f;
		if (getContactParameter() > 0.0001f && that.getContactParameter() > 0.0001f)
			mcp = 2 / (1 / getContactParameter() + 1 / that.getContactParameter());
		var h_c = h_gas + mcp > 0.9999f ? 1_000_000 : mcp / (1 - mcp);
		var k_inv = 0.5f / getConductivity() + 1 / h_c + 0.5f / that.getConductivity();
		var DeltaT = that.getTemperature() - getTemperature();
		var q = DeltaT / k_inv * 0.05f * 0.5f; // 20 ticks per second, twice a tick
		heat(q);
		that.heat(-q);
	}
}
