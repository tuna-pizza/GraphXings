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
    public int computePartialCrossingNumber()
    {
        int crossingNumber = 0;
        try{
            for (Edge e1 : g.getEdges())
            {
                if (vertexCoordinates.containsKey(e1.getS()) && vertexCoordinates.containsKey(e1.getT())) // Check if both vertices of e1 are placed
                {
                    for (Edge e2 : g.getEdges())
                    {
                        if (!e1.equals(e2) && vertexCoordinates.containsKey(e2.getS()) && vertexCoordinates.containsKey(e2.getT())) // Check if both vertices of e2 are placed
                        {
                            if (!e1.equals(e2) && !e1.isAdjacent(e2))
                            {
                                Segment s1 = new Segment(vertexCoordinates.get(e1.getS()),vertexCoordinates.get(e1.getT()));
                                Segment s2 = new Segment(vertexCoordinates.get(e2.getS()),vertexCoordinates.get(e2.getT()));
                                System.out.println("--------------------");
                                System.out.println(vertexCoordinates.get(e1.getS()).getX() + "," + vertexCoordinates.get(e1.getS()).getY() + "  " + vertexCoordinates.get(e1.getT()).getX() + "," + vertexCoordinates.get(e1.getT()).getY());
                                System.out.println(vertexCoordinates.get(e2.getS()).getX() + "," + vertexCoordinates.get(e2.getS()).getY() + "  " + vertexCoordinates.get(e2.getT()).getX() + "," + vertexCoordinates.get(e2.getT()).getY());
                                //System.out.println(s1.getA().getP()/s1.getA().getQ() + "," + s1.getB().getP()/s1.getB().getQ() + " " + s2.getA().getP()/s2.getA().getQ() + "," + s2.getB().getP()/s2.getB().getQ());
                                if (Segment.intersect(s1,s2))
                                {
                                    crossingNumber++;
                                }
                            }
                        }
                    }
                }
            }
    }
    catch(java.lang.ArithmeticException e)
    {
        System.out.println("Arithmetic Exception");
        for (Coordinate c : vertexCoordinates.values())
        {
            //System.out.println(c.getX() + " " + c.getY());
        }
    }
        return crossingNumber/2;
    }
}
