package GraphXings.Data;

/**
 * A class for managing a vertex of a graph.
 */
public class Vertex
{
    /**
     * The ID of the vertex.
     */
    private String id;

    /**
     * Creates a vertex with a given ID.
     * @param id The provided ID.
     */
    public Vertex(String id)
    {
        this.id = id;
    }

    /**
     * Gets the ID of the vertex.
     * @return The ID of the vertex.
     */
    public String getId()
    {
        return id;
    }
}
