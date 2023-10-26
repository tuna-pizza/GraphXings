package GraphXings.Game.GameInstance;
import GraphXings.Data.Graph;

/**
 * A class describing an instance of the game.
 */
public class GameInstance
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
	 * Constructs a game instance.
	 * @param g The graph to be drawn.
	 * @param width The width of the game board.
	 * @param height The height of the game board.
	 */
	public GameInstance(Graph g, int width, int height)
	{
		this.width=width;
		this.height=height;
		this.g = g;
	}

	/**
	 * Gets the graph to be drawn.
	 * @return The graph to be drawn.
	 */
	public Graph getG()
	{
		return g;
	}

	/**
	 * Gets the width of the game board.
	 * @return The width of the game board.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Gets the height of the game board.
	 * @return The height of the game board.
	 */
	public int getHeight()
	{
		return height;
	}
}
