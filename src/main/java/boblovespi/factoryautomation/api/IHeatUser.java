package boblovespi.factoryautomation.api;

import boblovespi.factoryautomation.common.util.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public interface IHeatUser
{
	float Pr = 0.72f; // https://en.wikipedia.org/wiki/Prandtl_number#Experimental_values
	float horiPrConst = MathHelper.pow(1 + MathHelper.pow(0.492f / Pr, 9 / 16f), 8 / 27f);
	// if 1 mc day = 1 irl day, then 1t = 3.6s, or in other words we need to be 72x faster
	// also makes the numbers not take forever
	float FUDGE_FACTOR = 72;
	float STEFAN_BOLTZMANN = 5.670e-8f;

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

	float getEmissivity();

	void heat(float energy);

	boolean canConvect(BlockPos pos, Direction face);

	float getConvectionSpeed(Direction face);

	default void conductWith(IHeatUser that)
	{
		// see https://en.wikipedia.org/wiki/Thermal_contact_conductance
		var h_gas = 10;
		var mcp = 0f;
		if (getContactParameter() > 0.0001f && that.getContactParameter() > 0.0001f)
			mcp = 2 / (1 / getContactParameter() + 1 / that.getContactParameter());
		var h_c = h_gas + (mcp > 0.9999f ? 1_000_000_000 : 10_000 * mcp / (1 - mcp));
		var k_inv = 0.5f / getConductivity() + 1 / h_c + 0.5f / that.getConductivity();
		var DeltaT = that.getTemperature() - getTemperature();
		var q = DeltaT / k_inv * 0.05f * 0.5f * FUDGE_FACTOR; // 20 ticks per second, twice a tick
		heat(q);
		that.heat(-q);
	}

	default void convectWith(IHeatUser that, Direction fromThisToThat)
	{
		// TODO: add forced convection
		// simplified assumption: flux into gas = flux out of gas, and gas is single temperature (avg temp)
		var DeltaT = that.getTemperature() - getTemperature();
		fromThisToThat = DeltaT > 0 ? fromThisToThat.getOpposite() : fromThisToThat;
		var p_air = 101325;
		var m_air = 4.81e-26f;
		var k_B = 1.380649e-23f;
		var T_avg = (that.getTemperature() + getTemperature()) / 2;
		var rho = p_air * m_air / k_B / T_avg; // https://en.wikipedia.org/wiki/Density_of_air
		var beta = 1 / T_avg;
		var l = 0.75f; // assume 0.75^3 m^3 air in convection
		var g = 9.8f;
		var eta = 2.791e-7f * MathHelper.pow(T_avg, 0.7355f); // https://www.tec-science.com/mechanics/gases-and-liquids/viscosity-of-liquids-and-gases/
		var alpha = 19e-6f; // https://en.wikipedia.org/wiki/Thermal_diffusivity
		var Ra = rho * beta * Mth.abs(DeltaT) * l * l * l * g / eta / alpha;
		var Nu = 1f;
		// https://en.wikipedia.org/wiki/Nusselt_number#Free,_or_natural,_convection
		if (Ra >= 10000)
		{
			if (fromThisToThat.getAxis().isHorizontal())
				Nu = Mth.square(0.825f + (0.387f * MathHelper.pow(Ra, 1 / 6f)) / horiPrConst);
			else if (fromThisToThat == Direction.UP)
				Nu = Ra <= 1e7 ? 0.54f * MathHelper.pow(Ra, 1 / 4f) : 0.15f * MathHelper.pow(Ra, 1 / 3f);
			else
				Nu = 0.27f * MathHelper.pow(Ra, 1 / 4f);
		}
		var k_air = 0.02614f;
		var h_air = Nu * k_air / l;
		var q = DeltaT * h_air * 0.05f * 0.5f * FUDGE_FACTOR;
		heat(q);
		that.heat(-q);
	}

	default void radiateWith(IHeatUser that)
	{
		// https://en.wikipedia.org/wiki/Thermal_radiation#Heat_transfer_between_surfaces
		var F = 1f;
		var A = 0.75f * 0.75f;
		var sigma = STEFAN_BOLTZMANN;
		var T_1 = that.getTemperature();
		var T_2 = getTemperature();
		var epsilon_1 = that.getEmissivity();
		var epsilon_2 = getEmissivity();
		var q = (sigma * (MathHelper.pow(T_1, 4) - MathHelper.pow(T_2, 4))) / ((1 - epsilon_1) / (A * epsilon_1) + 1 / (A * F) + (1 - epsilon_2) / (A * epsilon_2));
		q *= 0.05f * 0.5f * FUDGE_FACTOR;
		heat(q);
		that.heat(-q);
	}
}
