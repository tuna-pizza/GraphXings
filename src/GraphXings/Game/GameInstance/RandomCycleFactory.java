package GraphXings.Game.GameInstance;

import java.util.Random;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Data.Edge;
public class RandomCycleFactory implements GameInstanceFactory
{
	private Random r;
	public RandomCycleFactory()
	{
		r = new Random();
	}
	public RandomCycleFactory(long seed)
	{
		r = new Random(seed);
	}

	@Override
	public GameInstance getGameInstance()
	{
		int n_exp = r.nextInt(3) + 2;
		int n = r.nextInt(pow(10,n_exp-1)*9)+pow(10,n_exp-1);
		Graph g = createCycle(n);
		int width = 0;
		int height = 0;
		while (width*height < 10*n)
		{
			int w_exp = r.nextInt(4) + 1;
			int h_exp;
			boolean roughlySquare = r.nextBoolean();
			if (roughlySquare)
			{
				h_exp = w_exp;
			}
			else
			{
				h_exp = r.nextInt(4) + 1;
			}
			width = r.nextInt(pow(10,w_exp-1)*9)+pow(10,w_exp-1);
			height = r.nextInt(pow(10,h_exp-1)*9)+pow(10,h_exp-1);
		}
		return new GameInstance(g,width,height);
	}

	private int pow (int base, int exp)
	{
		int res = 1;
		for (int i = 0; i < exp; i++)
		{
			res *= base;
		}
		return res;
	}

	private Graph createCycle(int n)
	{
		int id = 0;
		Graph g = new Graph();
		Vertex first = new Vertex(String.valueOf(id++));
		g.addVertex(first);
		Vertex prev = first;
		Vertex next = null;
		while (id < n)
		{
			next = new Vertex(String.valueOf(id++));
			g.addVertex(next);
			Edge e = new Edge(prev,next);
			g.addEdge(e);
			prev = next;
		}
		if(next != null)
		{
			Edge e = new Edge(first,next);
			g.addEdge(e);
		}
		return g;
	}
}
