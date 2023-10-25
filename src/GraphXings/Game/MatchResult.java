package GraphXings.Game;

import GraphXings.Algorithms.Player;

public class MatchResult
{
	private Player player1;
	private Player player2;
	private int gamesWon1;
	private int gamesWon2;
	public MatchResult(Player player1, Player player2, int gamesWon1, int gamesWon2)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.gamesWon1 = gamesWon1;
		this.gamesWon2 = gamesWon2;
	}
	public Player getPlayer1()
	{
		return player1;
	}
	public Player getPlayer2()
	{
		return player2;
	}
	public int getGamesWon1()
	{
		return gamesWon1;
	}
	public int getGamesWon2()
	{
		return gamesWon2;
	}
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
