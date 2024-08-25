package boblovespi.factoryautomation.api;

public interface IMechanicalInput
{
	/**
	 *  An {@link IMechanicalOutput} will notify this mechanical input when its state changes.
	 *
	 * @param output The {@link IMechanicalOutput} whose state has changed.
	 */
	void update(IMechanicalOutput output);
}
