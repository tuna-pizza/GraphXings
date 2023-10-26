package GraphXings.Game.Match;

import GraphXings.Algorithms.Player;

/**
 * A class for storing the results of a match.
 */
public class MatchResult
{
	/**
	 * The first player.
	 */
	private Player player1;
	/**
	 * The second player.
	 */
	private Player player2;
	/**
	 * The number of games won by player1.
	 */
	private int gamesWon1;
	/**
	 * The number of games won by player2.
	 */
	private int gamesWon2;

	/**
	 * Constructs an object describing the results of a match.
	 * @param player1 The first player.
	 * @param player2 The second player.
	 * @param gamesWon1 The number of games won by the first player.
	 * @param gamesWon2 The number of games won by the second player.
	 */
	public MatchResult(Player player1, Player player2, int gamesWon1, int gamesWon2)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.gamesWon1 = gamesWon1;
		this.gamesWon2 = gamesWon2;
	}

	/**
	 * Gets the first player.
	 * @return The first player.
	 */
	public Player getPlayer1()
	{
		return player1;
	}

	/**
	 * Gets the second player.
	 * @return The second player.
	 */
	public Player getPlayer2()
	{
		return player2;
	}

	/**
	 * Gets the number of games won by player1.
	 * @return The number of games won by player1.
	 */
	public int getGamesWon1()
	{
		return gamesWon1;
	}

	/**
	 * Gets the number of games won by player2.
	 * @return The number of games won by player2.
	 */
	public int getGamesWon2()
	{
		return gamesWon2;
	}

	/**
	 * Gets a string announcing the results of the match!
	 * @return A string announcing the results of the match.
	 */
	public String announceResult()
	{
		String winner;
		String looser;
		if (gamesWon1 > gamesWon2)
		{
			winner = player1.getName();
			looser = player2.getName();
		}
		else if (gamesWon2 > gamesWon1)
		{
			winner = player2.getName();
			looser = player1.getName();
		}
		else
		{
			return ("It's a tie between " + player1.getName() + " and " + player2.getName() + " with " + gamesWon1 + " games won!");
		}
		return (winner + " beats " + looser + " with " + gamesWon1 + ":" + gamesWon2 + " games won!");
	}
}
