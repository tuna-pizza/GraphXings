package GraphXings.Game;

import GraphXings.Algorithms.NewPlayer;

/**
 * An exception that can be thrown when a time out occured.
 */
public class NewTimeOutException extends Exception
{
	/**
	 * The player who ran out of time.
	 */
	private NewPlayer timeOutPlayer;

	/**
	 * Constructor that creates an TimeOutException.
	 * @param timeOutPlayer The player who ran out of time.
	 */
	protected NewTimeOutException(NewPlayer timeOutPlayer)
	{
		this.timeOutPlayer = timeOutPlayer;
	}

	/**
	 * Gets the player who ran out of time.
	 * @return The player who ran out of time.
	 */
	public NewPlayer getTimeOutPlayer()
	{
		return timeOutPlayer;
	}
}
