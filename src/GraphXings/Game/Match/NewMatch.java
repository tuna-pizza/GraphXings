package GraphXings.Game.Match;

import GraphXings.Algorithms.NewPlayer;
import GraphXings.Game.NewGame;
import GraphXings.Game.GameInstance.GameInstance;
import GraphXings.Game.GameInstance.GameInstanceFactory;
import GraphXings.Game.NewGameResult;
import GraphXings.Legacy.Game.Match.Match;

import java.util.Random;

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
	 * Types of matches that may be played.
	 * CROSSING_NUMBER - The objective is to maximize the number of crossings.
	 * CROSSING_ANGLE - The objective is to maximize the sum of the squared cosines of crossing angles.
	 * MIXED - Randomly chooses an objective for each game played during the match.
	 */
	public enum MatchType {CROSSING_NUMBER,CROSSING_ANGLE,MIXED};
	/**
	 * The type of the match.
	 */
	private MatchType matchType;
	/**
	 * A random number generator used to select the next game's objective.
	 */
	private Random r;
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
		matchType = MatchType.CROSSING_NUMBER;
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
		this.matchType = MatchType.CROSSING_NUMBER;
	}

	/**
	 * Constructs a match object.
	 * @param player1 The first player.
	 * @param player2 The second player.
	 * @param factory The game instance factory to be used for created the games!
	 * @param bestOutOf The number of games to be played.
	 * @param gameTimeLimit The time limit for each game in the match.
	 * @param matchType The type of the match to be played.
	 */
	public NewMatch(NewPlayer player1, NewPlayer player2, GameInstanceFactory factory, int bestOutOf, long gameTimeLimit, MatchType matchType)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.bestOutOf = bestOutOf;
		this.factory = factory;
		this.gameTimeLimit = gameTimeLimit;
		isVerbose = true;
		this.matchType = matchType;
		if (matchType.equals(MatchType.MIXED))
		{
			r = new Random();
		}
	}

	/**
	 * Constructs a match object.
	 * @param player1 The first player.
	 * @param player2 The second player.
	 * @param factory The game instance factory to be used for created the games!
	 * @param bestOutOf The number of games to be played.
	 * @param gameTimeLimit The time limit for each game in the match.
	 * @param matchType The type of the match to be played.
	 * @param seed Seed used for the random number generator that selects game objectives if matchType is MIXED.
	 */
	public NewMatch(NewPlayer player1, NewPlayer player2, GameInstanceFactory factory, int bestOutOf, long gameTimeLimit, MatchType matchType, long seed)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.bestOutOf = bestOutOf;
		this.factory = factory;
		this.gameTimeLimit = gameTimeLimit;
		isVerbose = true;
		this.matchType = matchType;
		if (matchType.equals(MatchType.MIXED))
		{
			r = new Random(seed);
		}
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
			NewGame.Objective nextObjective = NewGame.Objective.CROSSING_NUMBER;
			if (matchType.equals(MatchType.CROSSING_ANGLE))
			{
				nextObjective = NewGame.Objective.CROSSING_ANGLE;
			}
			if (matchType.equals(MatchType.MIXED))
			{
				if (r.nextBoolean())
				{
					nextObjective = NewGame.Objective.CROSSING_NUMBER;
				}
				else
				{
					nextObjective = NewGame.Objective.CROSSING_ANGLE;
				}
			}
			NewGame game = new NewGame(gi.getG(),gi.getWidth(),gi.getHeight(),player1,player2,nextObjective,gameTimeLimit);
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