package GraphXings.Legacy.Game;

import GraphXings.Legacy.Algorithms.Player;

/**
 * A class for describing the result of a GraphXings game!
 */
public class GameResult
{
    /**
     * The number of crossings in the first round.
     */
    private int crossingsGame1;
    /**
     * The number of crossings in the second round.
     */
    private int crossingsGame2;
    /**
     * Player 1.
     */
    private Player player1;
    /**
     * Player 2.
     */
    private Player player2;
    /**
     * True if player one was caught cheating.
     */
    private boolean cheatingPlayer1;
    /**
     * True if player two was caught cheating.
     */
    private boolean cheatingPlayer2;
    /**
     * True if player one ran out of time.
     */
    private boolean timeOutPlayer1;
    /**
     * True if player two ran out of time.
     */
    private boolean timeOutPlayer2;

    /**
     * Constructs a GameResult object for storing the results of a GraphXings game!
     * @param crossingsGame1 The number of crossings in the first round.
     * @param crossingsGame2 The number of crossings in the second round.
     * @param player1 The first player that maximized the number of crossings in the first round.
     * @param player2 The second player that maximized the number of crossings in the second round.
     * @param cheatingPlayer1 True, if player 1 cheated, false otherwise.
     * @param cheatingPlayer2 True, if player 2 cheated, false otherwise.
     * @param timeOutPlayer1 True, if player 1 ran out of time, false otherwise.
     * @param timeOutPlayer2 True, if player 1 ran out of time, false otherwise.
     */
    public GameResult(int crossingsGame1, int crossingsGame2, Player player1, Player player2, boolean cheatingPlayer1, boolean cheatingPlayer2, boolean timeOutPlayer1, boolean timeOutPlayer2)
    {
        this.crossingsGame1 = crossingsGame1;
        this.crossingsGame2 = crossingsGame2;
        this.player1 = player1;
        this.player2 = player2;
        this.cheatingPlayer1 = cheatingPlayer1;
        this.cheatingPlayer2 = cheatingPlayer2;
        this.timeOutPlayer1 = timeOutPlayer1;
        this.timeOutPlayer2 = timeOutPlayer2;
    }

    /**
     * Gets the winning player.
     * @return The winning player.
     */
    public Player getWinner()
    {
        if (timeOutPlayer1)
        {
            return player2;
        }
        if (timeOutPlayer2)
        {
            return player1;
        }
        if (cheatingPlayer1)
        {
            return player2;
        }
        if (cheatingPlayer2)
        {
            return player1;
        }
        if (crossingsGame1 == crossingsGame2)
        {
            return null;
        }
        Player winner;
        if (crossingsGame1 > crossingsGame2)
        {
            winner = player1;
        }
        else
        {
            winner = player2;
        }
        return winner;
    }
    /**
     * Creates a string that announces the result!
     * @return A string announcing the result of the GraphXings game!
     */
    public String announceResult()
    {
        if (timeOutPlayer1)
        {
            return(player1.getName() + " ran out of time. " + player2.getName() + " wins!");
        }
        if (timeOutPlayer2)
        {
            return(player2.getName() + " ran out of time. " + player1.getName() + " wins!");
        }
        if (cheatingPlayer1)
        {
            return(player1.getName() + " attempted an invalid move. " + player2.getName() + " wins!");
        }
        if (cheatingPlayer2)
        {
            return(player2.getName() + " attempted an invalid move. " + player1.getName() + " wins!");
        }
        Player winner = getWinner();
        if (winner == null)
        {
            return ("It's a tie between " + player1.getName() + " and " + player2.getName() + " with " + crossingsGame1 + " crossings!");
        }
        else
        {
            Player looser;
            if (winner.equals(player1))
            {
                looser = player2;
            }
            else
            {
                looser = player1;
            }
            return (winner.getName() + " beats " + looser.getName() + " with " + crossingsGame1 + ":" + crossingsGame2 + " crossings!");
        }
    }
}
