package GraphXings.Game;

import GraphXings.Algorithms.NewPlayer;

/**
 * An exception that can be thrown when an illegal move is detected.
 */
public class NewInvalidMoveException extends Exception
{
	/**
	 * The cheating player.
	 */
	private NewPlayer cheater;

	/**
	 * Constructor that creates an InvalidMoveException.
	 * @param cheater The cheating player.
	 */
	protected NewInvalidMoveException(NewPlayer cheater)
	{
		this.cheater = cheater;
	}

	/**
	 * Gets the player that cheated.
	 * @return The cheating player.
	 */
	public NewPlayer getCheater()
	{
		return cheater;
	}
}
