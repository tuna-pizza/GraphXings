package GraphXings.Legacy.Game;

import GraphXings.Legacy.Algorithms.Player;

/**
 * An exception that can be thrown when a time out occured.
 */
public class TimeOutException extends Exception
{
	/**
	 * The player who ran out of time.
	 */
	private Player timeOutPlayer;

	/**
	 * Constructor that creates an TimeOutException.
	 * @param timeOutPlayer The player who ran out of time.
	 */
	protected TimeOutException(Player timeOutPlayer)
	{
		this.timeOutPlayer = timeOutPlayer;
	}

	/**
	 * Gets the player who ran out of time.
	 * @return The player who ran out of time.
	 */
	public Player getTimeOutPlayer()
	{
		return timeOutPlayer;
	}
}
