package boblovespi.factoryautomation.api;

/**
 * An {@link IBellowsConsumer} is a device which accepts blasts of air from bellows, such as a furnace or a firebox.
 */
public interface IBellowsConsumer
{
	/**
	 * @param efficiency The efficiency of the bellows application, from 50% to 200%.
	 * @param time       The time in ticks to blow.
	 */
	void blow(float efficiency, int time);
}
