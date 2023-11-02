package GraphXings.Game;

import GraphXings.Algorithms.Player;

/**
 * A class for describing the result of a GraphXings game!
 */
public class GameResult {
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
     * Constructs a GameResult object for storing the results of a GraphXings game!
     * 
     * @param crossingsGame1  The number of crossings in the first round.
     * @param crossingsGame2  The number of crossings in the second round.
     * @param player1         The first player that maximized the number of
     *                        crossings in the first round.
     * @param player2         The second player that maximized the number of
     *                        crossings in the second round.
     * @param cheatingPlayer1 True, if player 1 cheated, false otherwise.
     * @param cheatingPlayer2 True, if player 2 cheated, false otherwise.
     */
    public GameResult(int crossingsGame1, int crossingsGame2, Player player1, Player player2, boolean cheatingPlayer1,
            boolean cheatingPlayer2) {
        this.crossingsGame1 = crossingsGame1;
        this.crossingsGame2 = crossingsGame2;
        this.player1 = player1;
        this.player2 = player2;
        this.cheatingPlayer1 = cheatingPlayer1;
        this.cheatingPlayer2 = cheatingPlayer2;
    }

    /**
     * Creates a string that announces the result!
     * 
     * @return A string announcing the result of the GraphXings game!
     */
    public String announceResult() {
        if (cheatingPlayer1) {
            return (player1.getName() + " attempted an invalid move. " + player2.getName() + " wins!");
        }
        if (cheatingPlayer2) {
            return (player2.getName() + " attempted an invalid move. " + player1.getName() + " wins!");
        }
        if (crossingsGame1 == crossingsGame2) {
            return ("It's a tie between " + player1.getName() + " and " + player2.getName() + " with " + crossingsGame1
                    + " crossings!");
        }
        String winner;
        int crossingsWinner;
        String looser;
        int crossingsLooser;
        if (crossingsGame1 > crossingsGame2) {
            winner = player1.getName();
            looser = player2.getName();
            // crossingsWinner = crossingsGame1;
            // crossingsLooser = crossingsGame2;
        } else {
            winner = player2.getName();
            looser = player1.getName();
            // crossingsWinner = crossingsGame2;
            // crossingsLooser = crossingsGame1;
        }
        return (winner + " beats " + looser + " with " + crossingsGame1 + ":" + crossingsGame2 + " crossings!");
    }

    public String winner(String textOne, String textTwo) {
        if (crossingsGame1 > crossingsGame2) {
            return textOne;
        }
        if (crossingsGame1 < crossingsGame2) {
            return textTwo;
        }
        return "Tie";
    }
}
