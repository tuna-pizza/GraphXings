package GraphXings.Data;

import java.util.HashMap;
import java.util.HashSet;

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
     * The adjacency list representation.
     */
    private HashMap<Vertex,HashSet<Edge>> edges;
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
        edges = new HashMap<>();
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
            edges.put(v,new HashSet<>());
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
            if (!edges.get(e.getS()).contains(e))
            {
                edges.get(e.getS()).add(e);
                edges.get(e.getT()).add(e);
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
        HashSet<Vertex> result = new HashSet<>(vertices);
        return result;
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
        HashSet<Edge> result = new HashSet<>(edges.get(v));
        return result;
    }

    /**
     * Returns the edges of the graph.
     * @return An Iterable over the edge set.
     */
    public Iterable<Edge> getEdges()
    {
        HashSet<Edge> result = new HashSet<>();
        for (Vertex v : vertices)
        {
            for (Edge e : edges.get(v))
            {
                if (!result.contains(e))
                {
                    result.add(e);
                }
            }
        }
        return result;
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
     * @return A Graph object containign a copy of the graph.
     */
    public Graph copy()
    {
        Graph copy = new Graph();
        for (Vertex v : vertices)
        {
            copy.addVertex(v);
        }
        for (Vertex v : vertices)
        {
            for (Edge e : edges.get(v))
            {
                copy.addEdge(e);
            }
        }
        return copy;
    }
}
