package GraphXings.Game.GameInstance;

import GraphXings.Data.Graph;

/**
 * A game factory that always outputs the same game instance.
 */
public class ConstantGameInstanceFactory implements GameInstanceFactory
{
	/**
	 * The graph to be drawn.
	 */
	private Graph g;
	/**
	 * The width of the game board.
	 */
	private int width;
	/**
	 * The height of the game board.
	 */
	private int height;

	/**
	 * Constructs a game instance factory that always reports the same game instance.
	 * @param g The graph to be drawn.
	 * @param width The width of the game board.
	 * @param height The height of the game board.
	 */
	public ConstantGameInstanceFactory(Graph g, int width, int height)
	{
		this.g = g;
		this.width = width;
		this.height = height;
	}

	@Override
	public GameInstance getGameInstance()
	{
		return new GameInstance(g,width,height);
	}
}
