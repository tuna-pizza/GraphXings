package GraphXings.Game.GameInstance;

import GraphXings.Algorithms.IntegerMaths;
import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;

import java.util.LinkedList;
import java.util.Random;

/**
 * A GameInstance factory creating random planar graphs.
 * Each graph is one of:
 * mode 0 - a planar triangulation
 * mode 1 - the dual of a planar triangulation (i.e., a triconnected cubic planar graph)
 * mode 2 - a random subgraph of mode 0, where each edge is selected with a probability prob
 */
public class PlanarGameInstanceFactory implements GameInstanceFactory
{
	/**
	 * Random number generator for random sampling.
	 */
	Random r;
	/**
	 * Probability for choosing an edge. Default: 0.8.
	 */
	double prob;

	/**
	 * Constructs a PlanarGameInstanceFactory.
	 * @param seed Seed for the random number generator.
	 */
	public PlanarGameInstanceFactory(long seed)
	{
		r = new Random(seed);
		prob = 0.8;
	}
	@Override
	public GameInstance getGameInstance()
	{
		int n_exp = r.nextInt(3) + 2;
		int n = r.nextInt(IntegerMaths.pow(10,n_exp-1)*9)+IntegerMaths.pow(10,n_exp-1);
		boolean dual;
		boolean randomEdge;
		int mode = r.nextInt(4);
		switch (mode)
		{
			case 0:
			{
				dual = false;
				randomEdge = false;
				break;
			}
			case 1:
			{
				dual = true;
				randomEdge = false;
				break;
			}
			default:
			{
				dual = false;
				randomEdge = true;
				break;
			}
		}
		Graph g = createPlanarGraph(n,dual,randomEdge);
		if (dual)
		{
			n = 2*n-4;
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
			width = r.nextInt(IntegerMaths.pow(10,w_exp-1)*9)+IntegerMaths.pow(10,w_exp-1);
			height = r.nextInt(IntegerMaths.pow(10,h_exp-1)*9)+IntegerMaths.pow(10,h_exp-1);
		}
		return new GameInstance(g,width,height);
	}

	/**
	 * Sets the probability for choosing each edge in mode 2.
	 * @param prob The probability for choosing an edge. Must be in range (0,1).
	 */
	public void setEdgeProbability (double prob)
	{
		if (prob > 0 || prob < 1)
		{
			this.prob = prob;
		}
	}

	/**
	 * Constructs a random planar graph.
	 * @param n The desired number of vertices. If dual is true, the output will have 2n-4 vertices.
	 * @param dual If true, provides the dual graph of a triangulation on n vertices instead.
	 * @param randomEdge If true, each edge of the primal graph is added with probability prob.
	 * @return A random planar graph according to the specification.
	 */
	private Graph createPlanarGraph(int n, boolean dual, boolean randomEdge)
	{
		Graph g = new Graph();
		Graph d = new Graph();
		LinkedList<Vertex> outerface = new LinkedList<>();
		LinkedList<Vertex> visibleFaces = new LinkedList<>();
		Vertex v1 = new Vertex("v1");
		Vertex v2 = new Vertex("v2");
		Vertex v3 = new Vertex("v3");
		Vertex t1 = new Vertex("t1");
		int faceCount = 2;
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		d.addVertex(t1);
		if (r.nextDouble(1) < prob || !randomEdge)
		{
			g.addEdge(new Edge(v1, v2));
		}
		if (r.nextDouble(1) < prob || !randomEdge)
		{
			g.addEdge(new Edge(v2, v3));
		}
		if (r.nextDouble(1) < prob || !randomEdge)
		{
			g.addEdge(new Edge(v3, v1));
		}
		outerface.add(v1);
		outerface.add(v3);
		outerface.add(v2);
		visibleFaces.add(t1);
		visibleFaces.add(t1);
		for (int k = 4; k < n; k++)
		{
			int p = r.nextInt(outerface.size());
			int q = p;
			while (q == p)
			{
				q = r.nextInt(outerface.size());
			}
			if (p > q)
			{
				int swap = p;
				p = q;
				q = swap;
			}
			Vertex vk = new Vertex("v"+k);
			g.addVertex(vk);
			LinkedList<Vertex> newOuterface = new LinkedList<>();
			for (int i = 0; i <= p; i++)
			{
				newOuterface.add(outerface.get(i));
			}
			for (int i = p; i <= q; i++)
			{
				if (r.nextDouble(1) < prob || !randomEdge)
				{
					g.addEdge(new Edge(vk, outerface.get(i)));
				}
			}
			newOuterface.add(vk);
			for (int i = q; i < outerface.size(); i++)
			{
				newOuterface.add(outerface.get(i));
			}
			outerface = newOuterface;
			LinkedList<Vertex> newVisibleFaces = new LinkedList<>();
			for (int i= 0; i < p; i++)
			{
				newVisibleFaces.add(visibleFaces.get(i));
			}
			if (q-p == 1)
			{
				Vertex t = new Vertex("t" + faceCount++);
				d.addVertex(t);
				d.addEdge(new Edge(t,visibleFaces.get(p)));
				newVisibleFaces.add(t);
				newVisibleFaces.add(t);
			}
			else
			{
				Vertex tLast = null;
				for (int i = p; i < q; i++)
				{
					Vertex t = new Vertex("t" + faceCount++);
					d.addVertex(t);
					if (tLast != null)
					{
						d.addEdge(new Edge(t,tLast));
					}
					else
					{
						newVisibleFaces.add(t);
					}
					tLast = t;
					d.addEdge(new Edge(t,visibleFaces.get(i)));
				}
				newVisibleFaces.add(tLast);
			}
			for (int i= q; i < visibleFaces.size(); i++)
			{
				newVisibleFaces.add(visibleFaces.get(i));
			}
			visibleFaces = newVisibleFaces;
		}
		Vertex vn = new Vertex("v"+n);
		g.addVertex(vn);
		for (int i = 0; i < outerface.size(); i++)
		{
			if (r.nextDouble(1) < prob || !randomEdge)
			{
				g.addEdge(new Edge(vn, outerface.get(i)));
			}
		}
		Vertex tFinal = new Vertex("t"+(faceCount+visibleFaces.size()));
		d.addVertex(tFinal);
		d.addEdge(new Edge(tFinal,t1));
		Vertex tLast = tFinal;
		for (int i = 0; i < visibleFaces.size(); i++)
		{
			Vertex t = new Vertex("t"+faceCount++);
			d.addVertex(t);
			d.addEdge(new Edge(tLast,t));
			d.addEdge(new Edge(t,visibleFaces.get(i)));
			tLast = t;
		}
		d.addEdge(new Edge(tFinal,tLast));
		if (!dual)
		{
			return g;
		}
		else
		{
			return d;
		}
	}
}
