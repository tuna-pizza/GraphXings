package GraphXings.Game;

import GraphXings.Data.Graph;
public class ConstantGameInstanceFactory implements GameInstanceFactory
{
	private Graph g;
	private int width;
	private int height;
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
