package GraphXings.Game;
import GraphXings.Data.Graph;
public class GameInstance
{
	private int width;
	private int height;
	private Graph g;
	public GameInstance(Graph g, int width, int height)
	{
		this.width=width;
		this.height=height;
		this.g = g;
	}

	public Graph getG()
	{
		return g;
	}
	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
}
