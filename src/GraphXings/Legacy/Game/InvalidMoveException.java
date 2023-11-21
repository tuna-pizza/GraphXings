package GraphXings.Legacy.Game;

import GraphXings.Legacy.Algorithms.Player;

/**
 * An exception that can be thrown when an illegal move is detected.
 */
public class InvalidMoveException extends Exception
{
    /**
     * The cheating player.
     */
    private Player cheater;

    /**
     * Constructor that creates an InvalidMoveException.
     * @param cheater The cheating player.
     */
    protected InvalidMoveException(Player cheater)
    {
        this.cheater = cheater;
    }

    /**
     * Gets the player that cheated.
     * @return The cheating player.
     */
    public Player getCheater()
    {
        return cheater;
    }
}
