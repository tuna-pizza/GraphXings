package GraphXings.Game.Match;

import GraphXings.Algorithms.NewPlayer;
import GraphXings.Game.NewGame;
import GraphXings.Game.GameInstance.GameInstance;
import GraphXings.Game.GameInstance.GameInstanceFactory;
import GraphXings.Game.NewGameResult;

/**
 * A class managing a Best of X type match of GraphXings.
 */
public class NewMatch
{
	/**
	 * The first player.
	 */
	private NewPlayer player1;
	/**
	 * The second player.
	 */
	private NewPlayer player2;
	/**
	 * The game instance factory.
	 */
	private GameInstanceFactory factory;
	/**
	 * The number of games to be played.
	 */
	private int bestOutOf;
	/**
	 * The time limit for each game in the match.
	 */
	private long gameTimeLimit;
	/**
	 * If true, print all game results (true by default).
	 */
	private boolean isVerbose;
	/**
	 * Constructs a match object.
	 * @param player1 The first player.
	 * @param player2 The second player.
	 * @param factory The game instance factory to be used for created the games!
	 * @param bestOutOf The number of games to be played.
	 */
	public NewMatch(NewPlayer player1, NewPlayer player2, GameInstanceFactory factory, int bestOutOf)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.bestOutOf = bestOutOf;
		this.factory = factory;
		this.gameTimeLimit = Long.MAX_VALUE;
		isVerbose = true;
	}

	/**
	 * Constructs a match object.
	 * @param player1 The first player.
	 * @param player2 The second player.
	 * @param factory The game instance factory to be used for created the games!
	 * @param bestOutOf The number of games to be played.
	 * @param gameTimeLimit The time limit for each game in the match.
	 */
	public NewMatch(NewPlayer player1, NewPlayer player2, GameInstanceFactory factory, int bestOutOf, long gameTimeLimit)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.bestOutOf = bestOutOf;
		this.factory = factory;
		this.gameTimeLimit = gameTimeLimit;
		isVerbose = true;
	}

	/**
	 * Plays the match.
	 * @return A MatchResult object containing the results of the match.
	 */
	public NewMatchResult play()
	{
		boolean end = false;
		int gamesWon1 = 0;
		int gamesWon2 = 0;
		int gamesPlayed = 0;
		while(!end)
		{
			GameInstance gi = factory.getGameInstance();
			NewGame game = new NewGame(gi.getG(),gi.getWidth(),gi.getHeight(),player1,player2,gameTimeLimit);
			NewGameResult gr = game.play();
			gamesPlayed++;
			if (isVerbose)
			{
				System.out.println("Game " + gamesPlayed + ": " + gr.announceResult());
			}
			if (gamesPlayed > 2*bestOutOf)
			{
				end = true;
			}
			if (gr.getWinner() == null)
			{
				continue;
			}
			else
			{
				int gamesWonWinner;
				if (gr.getWinner().equals(player1))
				{
					gamesWon1++;
					gamesWonWinner = gamesWon1;
				}
				else
				{
					gamesWon2++;
					gamesWonWinner = gamesWon2;
				}
				if (gamesWonWinner > bestOutOf/2)
				{
					end = true;
				}
			}
		}
		return new NewMatchResult(player1,player2,gamesWon1,gamesWon2);
	}

	/**
	 * Sets whether or not all intermediate game results should be displayed.
	 * @param verbose True, if intermediate results should be displayed, false otherwise.
	 */
	public void setVerbose(boolean verbose)
	{
		this.isVerbose = verbose;
	}
}