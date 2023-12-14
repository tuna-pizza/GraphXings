package GraphXings.Game;

import GraphXings.Algorithms.CrossingCalculator;
import GraphXings.Algorithms.NewPlayer;
import GraphXings.Data.Graph;
import GraphXings.Legacy.Game.InvalidMoveException;

import java.util.Random;

/**
 * A class for managing a game of GraphXings!
 */
public class NewGame
{
	/**
	 * The width of the game board.
	 */
	private int width;
	/**
	 * The height of the game board.
	 */
	private int height;
	/**
	 * The graph to be drawn.
	 */
	private Graph g;
	/**
	 * The first player.
	 */
	private NewPlayer player1;
	/**
	 * The second player.
	 */
	private NewPlayer player2;
	/**
	 * The time limit for players.
	 */
	private long timeLimit;

	/**
	 * Instantiates a game of GraphXings.
	 * @param g The graph to be drawn.
	 * @param width The width of the game board.
	 * @param height The height of the game board.
	 * @param player1 The first player. Plays as the maximizer in round one.
	 * @param player2 The second player. Plays as the minimizer in round one.
	 */
	public NewGame(Graph g, int width, int height, NewPlayer player1, NewPlayer player2)
	{
		this.g = g;
		this.width = width;
		this.height = height;
		this.player1 = player1;
		this.player2 = player2;
		this.timeLimit = Long.MAX_VALUE;
	}

	/**
	 * Instantiates a game of GraphXings.
	 * @param g The graph to be drawn.
	 * @param width The width of the game board.
	 * @param height The height of the game board.
	 * @param player1 The first player. Plays as the maximizer in round one.
	 * @param player2 The second player. Plays as the minimizer in round one.
	 * @param timeLimit The time limit for players.
	 */
	public NewGame(Graph g, int width, int height, NewPlayer player1, NewPlayer player2, long timeLimit)
	{
		this.g = g;
		this.width = width;
		this.height = height;
		this.player1 = player1;
		this.player2 = player2;
		this.timeLimit = timeLimit;
	}

	/**
	 * Runs the full game of GraphXings.
	 * @return Provides a GameResult Object containing the game's results.
	 */
	public NewGameResult play()
	{
		Random r = new Random(System.nanoTime());
		if (r.nextBoolean())
		{
			NewPlayer swap = player1;
			player1 = player2;
			player2 = swap;
		}
		try
		{
			int crossingsGame1 = playRound(player1, player2);
			int crossingsGame2 = playRound(player2, player1);
			return new NewGameResult(crossingsGame1,crossingsGame2,player1,player2,false,false,false,false);
		}
		catch (NewInvalidMoveException ex)
		{
			System.out.println("E001:" + ex.getCheater().getName() + " cheated!");
			if (ex.getCheater().equals(player1))
			{
				return new NewGameResult(0, 0, player1, player2,true,false,false,false);
			}
			else if (ex.getCheater().equals(player2))
			{
				return new NewGameResult(0,0,player1,player2,false,true,false,false);
			}
			else
			{
				return new NewGameResult(0,0,player1,player2,false,false,false,false);
			}
		}
		catch (NewTimeOutException ex)
		{
			System.out.println("E002:" +ex.getTimeOutPlayer().getName() + " ran out of time!");
			if (ex.getTimeOutPlayer().equals(player1))
			{
				return new NewGameResult(0, 0, player1, player2,false,false,true,false);
			}
			else if (ex.getTimeOutPlayer().equals(player2))
			{
				return new NewGameResult(0,0,player1,player2,false,false,false,true);
			}
			else
			{
				return new NewGameResult(0,0,player1,player2,false,false,false,false);
			}
		}
	}

	/**
	 * Plays a single round of the game.
	 * @param maximizer The player with the goal to maximize the number of crossings.
	 * @param minimizer The player with the goal to minimize the number of crossings
	 * @return The number of crossings yielded in the final drawing.
	 * @throws InvalidMoveException An exception caused by cheating.
	 */
	private int playRound(NewPlayer maximizer, NewPlayer minimizer) throws NewInvalidMoveException, NewTimeOutException
	{
		int turn = 0;
		GameState gs = new GameState(g,width,height);
		GameMove lastMove = null;
		long timeMaximizer = 0;
		long timeMinimizer = 0;
		long initStartTimeMax = System.nanoTime();
		maximizer.initializeNextRound(g.copy(),width,height, NewPlayer.Role.MAX);
		timeMaximizer += System.nanoTime()-initStartTimeMax;
		if (timeMaximizer > timeLimit)
		{
			throw new NewTimeOutException(maximizer);
		}
		long initStartTimeMin = System.nanoTime();
		minimizer.initializeNextRound(g.copy(),width,height, NewPlayer.Role.MIN);
		timeMinimizer += System.nanoTime()-initStartTimeMin;
		if (timeMinimizer > timeLimit)
		{
			throw new NewTimeOutException(minimizer);
		}
		while (turn < g.getN())
		{
			GameMove newMove;
			if (turn%2 == 0)
			{
				long moveStartTime = System.nanoTime();
				try
				{
					newMove = maximizer.maximizeCrossings(lastMove);
				}
				catch (Exception ex)
				{
					System.out.println("E003:" +maximizer.getName() + " threw a " + ex.getClass() + " exception!");
					throw new NewInvalidMoveException(maximizer);
				}
				timeMaximizer += System.nanoTime()-moveStartTime;
				if (timeMaximizer > timeLimit)
				{
					throw new NewTimeOutException(maximizer);
				}
				if (!gs.checkMoveValidity(newMove))
				{
					throw new NewInvalidMoveException(maximizer);
				}
			}
			else
			{
				long moveStartTime = System.nanoTime();
				try
				{
					newMove = minimizer.minimizeCrossings(lastMove);
				}
				catch (Exception ex)
				{
					System.out.println("E004:" +minimizer.getName() + " threw a " + ex.getClass() + " exception!" );
					throw new NewInvalidMoveException(minimizer);
				}
				timeMinimizer += System.nanoTime()-moveStartTime;
				if (timeMinimizer > timeLimit)
				{
					throw new NewTimeOutException(minimizer);
				}
				if (!gs.checkMoveValidity(newMove))
				{
					throw new NewInvalidMoveException(minimizer);
				}
			}
			gs.applyMove(newMove);
			lastMove = newMove;
			turn++;
		}
		CrossingCalculator cc = new CrossingCalculator(g,gs.getVertexCoordinates());
		return  cc.computeCrossingNumber();
	}
}
