package GraphXings.Data;

import java.util.*;

/**
 * A class for maintaining information about a graph.
 */
public class Graph
{
    /**
     * The vertex set.
     */
    private HashSet<Vertex> vertices;
    /**
     * The edge set.
     */
    private HashSet<Edge> edges;
    /**
     * The adjacency list representation.
     */
    private HashMap<Vertex,HashSet<Edge>> adjacentEdges;
    /**
     * The number of vertices.
     */
    private int n;
    /**
     * The number of edges.
     */
    private int m;

    /**
     * Initializes an empty graph.
     */
    public Graph()
    {
        vertices = new HashSet<>();
        edges = new HashSet<>();
        adjacentEdges = new HashMap<>();
        n = 0;
        m = 0;
    }

    /**
     * Adds a vertex to the graph.
     * @param v The vertex to be added.
     */
    public void addVertex(Vertex v)
    {
        if (!vertices.contains(v))
        {
            vertices.add(v);
            adjacentEdges.put(v,new HashSet<>());
            n++;
        }
    }

    /**
     * Add an edge to the graph.
     * @param e The edge to be added.
     * @return True, if the edge was successfully added, false otherwise.
     */
    public boolean addEdge(Edge e)
    {
        if (vertices.contains(e.getS())&&vertices.contains(e.getT()))
        {
            if (!adjacentEdges.get(e.getS()).contains(e))
            {
                adjacentEdges.get(e.getS()).add(e);
                adjacentEdges.get(e.getT()).add(e);
                edges.add(e);
                m++;
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the vertex set.
     * @return An Iterable over the vertex set.
     */
    public Iterable<Vertex> getVertices()
    {
        return vertices;
    }

    /**
     * Returns the edges incident to a specified vertex.
     * @param v The vertex whose adjacent edges are queried.
     * @return An Iterable over the edges incident to Vertex v.
     */
    public Iterable<Edge> getIncidentEdges(Vertex v)
    {
        if (!vertices.contains(v))
        {
            return null;
        }
        return adjacentEdges.get(v);
    }

    /**
     * Returns the edges of the graph.
     * @return An Iterable over the edge set.
     */
    public Iterable<Edge> getEdges()
    {
        return edges;
    }

    /**
     * Get the number of vertices.
     * @return The number of vertices.
     */
    public int getN()
    {
        return n;
    }

    /**
     * Gets the number of edges.
     * @return The number of edges.
     */
    public int getM()
    {
        return m;
    }

    /**
     * Creates a functionally equivalent copy of the graph that uses different references.
     * @return A Graph object containing a copy of the graph.
     */
    public Graph copy()
    {
        Graph copy = new Graph();
        for (Vertex v : vertices)
        {
            copy.addVertex(v);
        }
        for (Edge e : edges)
        {
            copy.addEdge(e);
        }
        return copy;
    }

    /**
     * Shuffles the order of vertices in the vertex set.
     */
    public void shuffleIndices()
    {
        List<Vertex> toBeShuffled = new ArrayList<>(vertices);
        Collections.shuffle(toBeShuffled);
        vertices = new HashSet<>(toBeShuffled);
    }

    /**
     * Provides the adjacency list representation of the graph.
     * @return The adjacency list representation.
     */
    @Override
    public String toString()
    {
        String out = "";
        for (Vertex v : vertices)
        {
            out += v.getId() + ": [";
            for (Edge e: adjacentEdges.get(v))
            {
                out += "(" + e.getS().getId() +"," + e.getT().getId() +"),";
            }
            out += "]\n";
        }
        return out;
    }
}
