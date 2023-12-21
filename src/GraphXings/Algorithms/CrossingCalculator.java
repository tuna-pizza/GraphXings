package GraphXings.Algorithms;

import GraphXings.Data.*;

import java.util.HashMap;

/**
 * A class for computing the number of crossings of a partially embedded graph.
 */
public class CrossingCalculator
{
    /**
     * The graph g.
     */
    private Graph g;
    /**
     * The positions of the already placed vertices.
     */
    private HashMap<Vertex, Coordinate> vertexCoordinates;

    /**
     * Constructs a CrossingCalculator object.
     * @param g The partially embedded graph.
     * @param vertexCoordinates The coordinates of the already placed vertices.
     */
    public CrossingCalculator(Graph g, HashMap<Vertex,Coordinate> vertexCoordinates)
    {
        this.g = g;
        this.vertexCoordinates = vertexCoordinates;
    }

    /**
     * Computes the number of crossings. The implementation is not efficient and iterates over all pairs of edges resulting in quadratic time.
     * @return The number of crossings.
     */
    public int computeCrossingNumber()
    {
        int crossingNumber = 0;
        for (Edge e1 : g.getEdges())
        {
            for (Edge e2 : g.getEdges())
            {
                if (!e1.equals(e2))
                {
                    if (!e1.isAdjacent(e2))
                    {
                        if (!vertexCoordinates.containsKey(e1.getS()) || !vertexCoordinates.containsKey(e1.getT()) || !vertexCoordinates.containsKey(e2.getS())|| !vertexCoordinates.containsKey(e2.getT()))
                        {
                            continue;
                        }
                        Segment s1 = new Segment(vertexCoordinates.get(e1.getS()),vertexCoordinates.get(e1.getT()));
                        Segment s2 = new Segment(vertexCoordinates.get(e2.getS()),vertexCoordinates.get(e2.getT()));
                        if (Segment.intersect(s1,s2))
                        {
                            crossingNumber++;
                        }
                    }
                }
            }
        }
        return crossingNumber/2;
    }

    /**
     * Computes the sum of the squared cosines of crossing angles. The implementation is not efficient and iterates over all pairs of edges resulting in quadratic time.
     * @return The  sum of the squared cosines of crossing angles.
     */
    public double computeSumOfSquaredCosinesOfCrossingAngles()
    {
        int comp = 0;
        double result = 0;
        for (Edge e1 : g.getEdges())
        {
            for (Edge e2 : g.getEdges())
            {
                if (!e1.equals(e2))
                {
                    if (!e1.isAdjacent(e2))
                    {
                        if (!vertexCoordinates.containsKey(e1.getS()) || !vertexCoordinates.containsKey(e1.getT()) || !vertexCoordinates.containsKey(e2.getS())|| !vertexCoordinates.containsKey(e2.getT()))
                        {
                            continue;
                        }
                        Segment s1 = new Segment(vertexCoordinates.get(e1.getS()),vertexCoordinates.get(e1.getT()));
                        Segment s2 = new Segment(vertexCoordinates.get(e2.getS()),vertexCoordinates.get(e2.getT()));
                        if (Segment.intersect(s1,s2))
                        {
                            result+=Segment.squaredCosineOfAngle(s1,s2);
                        }
                    }
                }
            }
        }
        return result/2;
    }
}
