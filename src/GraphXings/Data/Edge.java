package GraphXings.Data;

/**
 * A class managing an undirected edge of a graph.
 */
public class Edge
{
    /**
     * The first vertex.
     */
    private Vertex s;
    /**
     * The second vertex.
     */
    private Vertex t;

    /**
     * Creates an Edge between Vertices s and t.
     * @param s A vertex of the graph.
     * @param t A vertex of the graph.
     */
    public Edge(Vertex s, Vertex t)
    {
        this.s = s;
        this.t = t;
    }

    /**
     * Checks if this and other are the same undirected edge.
     * @param other Another Edge object.
     * @return True if both Edge objects connect the same two vertices, false otherwise.
     */
    public boolean equals(Edge other)
    {
        if (other == null)
        {
            return false;
        }
        return ((s.equals(other.getS())&t.equals(other.getT())) || (t.equals(other.getS())&s.equals(other.getT())));
    }

    /**
     * Returns the first endvertex.
     * @return The first endvertex.
     */
    public Vertex getS()
    {
        return s;
    }

    /**
     * Returns the second endvertex.
     * @return The second endvertex.
     */
    public Vertex getT()
    {
        return t;
    }

    /**
     * Decides if this and other share an endvertex.
     * @param other Another Edge object.
     * @return True, if both Edge objects share an endvertex, false otherwise.
     */
    public boolean isAdjacent(Edge other)
    {
        return (this.s.equals(other.getS()) || this.s.equals(other.getT()) || this.t.equals(other.getS()) || this.t.equals(other.getT()));
    }
}
