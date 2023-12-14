package GraphXings.Game.GameInstance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import GraphXings.Data.Edge;
import GraphXings.Data.Graph;
import GraphXings.Data.Vertex;

/**
 * A GameInstanceFactory that can parse a random graph from the output of the software plantri using its standard output format.
 * See also https://users.cecs.anu.edu.au/~bdm/plantri/
 */
public class PlantriGameInstanceFactory implements GameInstanceFactory
{
	/**
	 * The path to the plantri output.
	 */
	String plantriFile;
	/**
	 * The number of instances in the plantri output.
	 */
	int numberOfInstances;
	/**
	 * A random object for selecting a random graph.
	 */
	Random r;

	/**
	 * Creates a new PlantriGameInstanceFactory.
	 * @param plantriFile The path to the output file of plantri.
	 * @param numberOfInstances The number of instances encoded in plantriFile.
	 * @param seed The seed for the random number generator.
	 */
	public PlantriGameInstanceFactory(String plantriFile, int numberOfInstances, long seed)
	{
		r = new Random(seed);
		this.plantriFile = plantriFile;
		this.numberOfInstances = numberOfInstances;
	}
	@Override
	public GameInstance getGameInstance()
	{
		try
		{
			BufferedReader plantriReader = new BufferedReader(new FileReader(plantriFile));
			int toSkip = r.nextInt(numberOfInstances);
			for (int i = 0; i < 15; i++)
			{
				plantriReader.read();
			}
			for (int i = 0; i < toSkip; i++)
			{
				int n = plantriReader.read();
				int zerosFound = 0;
				while (zerosFound < n)
				{
					if (plantriReader.read() == 0)
					{
						zerosFound++;
					}
				}
			}
			int n = plantriReader.read();
			Vertex[] vertices = new Vertex[n];
			Graph g = new Graph();
			for (int i = 0; i < n; i++)
			{
				vertices[i] = new Vertex("v"+i);
				g.addVertex(vertices[i]);
			}
			for (int i = 0; i < n; i++)
			{
				int next = plantriReader.read();
				while (next != 0)
				{
					if (next - 1 > i)
					{
						g.addEdge(new Edge(vertices[i], vertices[next - 1]));
					}
					next = plantriReader.read();
				}
			}
			int width = 0;
			int height = 0;
			while (width * height < 2 * n)
			{
				width = r.nextInt(2*n);
				height = r.nextInt(2*n);
			}
			plantriReader.close();
			return new GameInstance(g,width,height);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
}
