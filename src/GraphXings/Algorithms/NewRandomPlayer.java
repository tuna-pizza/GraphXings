package GraphXings.Algorithms;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Game.GameMove;
import GraphXings.Game.GameState;

import java.util.Random;

public class NewRandomPlayer implements NewPlayer
{
	/**
	 * The name of the random player.
	 */
	private String name;
	/**
	 * A random number generator.
	 */
	private Random r;
	/**
	 * The graph to be drawn.
	 */
	private Graph g;
	/**
	 * The current state of the game;
	 */
	private GameState gs;
	/**
	 * The width of the game board.
	 */
	private int width;
	/**
	 * The height of the game board.
	 */
	private int height;
	/**
	 * Creates a random player with the assigned name.
	 * @param name
	 */
	public NewRandomPlayer(String name)
	{
		this.name = name;
		this.r =  new Random(name.hashCode());
	}

	@Override
	public GameMove maximizeCrossings(GameMove lastMove)
	{
		// First: Apply the last move by the opponent if there is one.
		if (lastMove != null)
		{
			gs.applyMove(lastMove);
		}
		// Second: Compute the new move.
		GameMove randomMove = randomMove();
		// Third: Apply the new move to the local GameState.
		gs.applyMove(randomMove);
		// Finally: Return the new move.
		return randomMove;
	}

	@Override
	public GameMove minimizeCrossings(GameMove lastMove)
	{
		// First: Apply the last move by the opponent.
		if (lastMove != null)
		{
			gs.applyMove(lastMove);
		}
		// Second: Compute the new move.
		GameMove randomMove = randomMove();
		// Third: Apply the new move to the local GameState.
		gs.applyMove(randomMove);
		// Finally: Return the new move.
		return randomMove;
	}

	@Override
	public void initializeNextRound(Graph g, int width, int height, Role role)
	{
		// Store graph, width, height and create a new GameState.
		this.g = g;
		this.width = width;
		this.height = height;
		this.gs = new GameState(g,width,height);
	}

	/**
	 * Computes a random valid move.
	 * @return A random valid move.
	 */
	private GameMove randomMove()
	{
		int stillToBePlaced = g.getN()- gs.getPlacedVertices().size();
		int next = r.nextInt(stillToBePlaced);
		int skipped = 0;
		Vertex v=null;
		for (Vertex u : g.getVertices())
		{
			if (!gs.getPlacedVertices().contains(u))
			{
				if (skipped < next)
				{
					skipped++;
					continue;
				}
				v=u;
				break;
			}
		}
		Coordinate c;
		do
		{
			c = new Coordinate(r.nextInt(width),r.nextInt(height));
		}
		while (gs.getUsedCoordinates()[c.getX()][c.getY()]!=0);
		return new GameMove(v,c);
	}

	@Override
	public String getName()
	{
		return name;
	}
}
