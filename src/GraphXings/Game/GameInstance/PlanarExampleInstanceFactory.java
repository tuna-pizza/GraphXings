package GraphXings.Game.GameInstance;

import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;

import java.util.Random;

/**
 * A factory creating two examples of planar graphs.
 */
public class PlanarExampleInstanceFactory implements GameInstanceFactory
{
	/**
	 * True if the next game instance contains the first example graph, false otherwise.
	 */
	private boolean firstExample;
	/**
	 * A Random object for randomizing the grid size.
	 */
	private Random r;

	/**
	 * Standard constructor.
	 */
	public PlanarExampleInstanceFactory()
	{
		firstExample = true;
		r = new Random();
	}
	@Override
	public GameInstance getGameInstance()
	{
		Graph g;
		if (firstExample)
		{
			g = getG1();
		}
		else
		{
			g = getG2();
		}
		firstExample = !firstExample;
		int width = r.nextInt(150)+10;
		int height = r.nextInt(150)+10;
		return new GameInstance(g,width,height);
	}

	/**
	 * Creates the first example graph.
	 * @return The first example graph.
	 */
	private Graph getG1()
	{
		Graph g = new Graph();
		Vertex v1 = new Vertex("1");
		Vertex v2 = new Vertex("2");
		Vertex v3 = new Vertex("3");
		Vertex v4 = new Vertex("4");
		Vertex v5 = new Vertex("5");
		Vertex v6 = new Vertex("6");
		Vertex v7 = new Vertex("7");
		Vertex v8 = new Vertex("8");
		Vertex v9 = new Vertex("9");
		Vertex v10 = new Vertex("10");
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);
		g.addVertex(v5);
		g.addVertex(v6);
		g.addVertex(v7);
		g.addVertex(v8);
		g.addVertex(v9);
		g.addVertex(v10);
		g.addEdge(new Edge(v1,v2));
		g.addEdge(new Edge(v2,v3));
		g.addEdge(new Edge(v3,v4));
		g.addEdge(new Edge(v4,v5));
		g.addEdge(new Edge(v5,v6));
		g.addEdge(new Edge(v6,v7));
		g.addEdge(new Edge(v7,v8));
		g.addEdge(new Edge(v8,v9));
		g.addEdge(new Edge(v9,v10));
		g.addEdge(new Edge(v10,v1));
		g.addEdge(new Edge(v1,v9));
		g.addEdge(new Edge(v2,v6));
		g.addEdge(new Edge(v3,v5));
		g.addEdge(new Edge(v4,v7));
		g.addEdge(new Edge(v8,v10));
		return g;
	}

	/**
	 * Creates the second example graph.
	 * @return The second example graph.
	 */
	private Graph getG2()
	{
		Graph g = new Graph();
		Vertex v1 = new Vertex("1");
		Vertex v2 = new Vertex("2");
		Vertex v3 = new Vertex("3");
		Vertex v4 = new Vertex("4");
		Vertex v5 = new Vertex("5");
		Vertex v6 = new Vertex("6");
		Vertex v7 = new Vertex("7");
		Vertex v8 = new Vertex("8");
		Vertex v9 = new Vertex("9");
		Vertex v10 = new Vertex("10");
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);
		g.addVertex(v5);
		g.addVertex(v6);
		g.addVertex(v7);
		g.addVertex(v8);
		g.addVertex(v9);
		g.addVertex(v10);
		g.addEdge(new Edge(v1,v2));
		g.addEdge(new Edge(v2,v3));
		g.addEdge(new Edge(v3,v4));
		g.addEdge(new Edge(v4,v5));
		g.addEdge(new Edge(v5,v6));
		g.addEdge(new Edge(v6,v7));
		g.addEdge(new Edge(v7,v8));
		g.addEdge(new Edge(v8,v9));
		g.addEdge(new Edge(v9,v10));
		g.addEdge(new Edge(v1,v3));
		g.addEdge(new Edge(v3,v5));
		g.addEdge(new Edge(v5,v7));
		g.addEdge(new Edge(v7,v9));
		g.addEdge(new Edge(v2,v4));
		g.addEdge(new Edge(v4,v6));
		g.addEdge(new Edge(v6,v8));
		g.addEdge(new Edge(v8,v10));
		g.addEdge(new Edge(v1,v4));
		g.addEdge(new Edge(v1,v5));
		g.addEdge(new Edge(v1,v6));
		g.addEdge(new Edge(v1,v7));
		g.addEdge(new Edge(v1,v8));
		g.addEdge(new Edge(v1,v9));
		g.addEdge(new Edge(v1,v10));
		return g;
	}
}
