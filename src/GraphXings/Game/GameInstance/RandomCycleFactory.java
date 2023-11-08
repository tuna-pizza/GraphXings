package GraphXings.Game.GameInstance;

import java.util.HashSet;
import java.util.Random;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;
import GraphXings.Data.Edge;

/**
 * A class for generating random instances where a cycle is to be drawn.
 */
public class RandomCycleFactory implements GameInstanceFactory
{
	/**
	 * The random number generator.
	 */
	private Random r;
	/**
	 * True, if the cycle should be augmented by some matching.
	 */
	private boolean includeMatchingEdges;

	/**
	 * Standard constructor which uses the normal seed generation of Random.
	 */
	public RandomCycleFactory()
	{
		r = new Random();
	}

	/**
	 * Constructor that allows to specify a seed for the Random object allowing for replicable results.
	 * @param seed The seed for the Random object.
	 */
	public RandomCycleFactory(long seed)
	{
		r = new Random(seed);
		includeMatchingEdges = false;
	}

	/**
	 * Constructor that allows to specify a seed for the Random object allowing for replicable results.
	 * @param seed The seed for the Random object.
	 * @param includeMatchingEdges True, if the cycle should be augmented by a random matching, false otherwise.
	 */
	public RandomCycleFactory(long seed,boolean includeMatchingEdges)
	{
		r = new Random(seed);
		this.includeMatchingEdges = includeMatchingEdges;
	}

	@Override
	public GameInstance getGameInstance()
	{
		int n_exp = r.nextInt(3) + 2;
		int n = r.nextInt(pow(10,n_exp-1)*9)+pow(10,n_exp-1);
		Graph g = createCycle(n);
		if (includeMatchingEdges)
		{
			int edgesToAdd = 0;
			int edgeRatio = r.nextInt(3);
			switch (edgeRatio)
			{
				case 0:
				{
					edgesToAdd = 0;
					break;
				}
				case 1:
				{
					edgesToAdd = n/20;
					break;
				}
				case 2:
				{
					edgesToAdd = n/4;
					break;
				}
				default:
				{
					System.err.println("I should not be here.");
				}
			}
			HashSet<Vertex> matchedVertices = new HashSet<>();
			for (int i =0; i < edgesToAdd; i++)
			{
				Vertex s = null;
				Vertex t = null;
				boolean edgeFound = false;
				while (!edgeFound)
				{
					int sSkip = r.nextInt(n);
					int tSkip = r.nextInt(n);
					if (sSkip == tSkip)
					{
						continue;
					}
					int skipped = 0;
					for (Vertex v : g.getVertices())
					{
						if (skipped == sSkip)
						{
							s = v;
						}
						if (skipped == tSkip)
						{
							t = v;
						}
						skipped++;
						if (skipped > sSkip && skipped > tSkip)
						{
							break;
						}
					}
					if (!matchedVertices.contains(s) && !matchedVertices.contains(t))
					{
						boolean neighbored = false;
						for (Edge e : g.getIncidentEdges(s))
						{
							if (e.getS().equals(t) || e.getT().equals(t))
							{
								neighbored = true;
								break;
							}
						}
						if (!neighbored)
						{
							g.addEdge(new Edge(s,t));
							matchedVertices.add(s);
							matchedVertices.add(t);
							edgeFound = true;
						}
					}
				}
			}
		}
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

	/**
	 * Computes the power of an int.
	 * @param base An integer base.
	 * @param exp An integer exponent.
	 * @return The integer power.
	 */
	private int pow (int base, int exp)
	{
		int res = 1;
		for (int i = 0; i < exp; i++)
		{
			res *= base;
		}
		return res;
	}

	/**
	 * Creates a cycle of length n.
	 * @param n The length of the cycle.
	 * @return A cycle of length n.
	 */
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
