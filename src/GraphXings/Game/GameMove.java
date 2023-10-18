package GraphXings.Game;

import GraphXings.Data.Coordinate;
import GraphXings.Data.Vertex;

/**
 * A class for describing a game move consisting of a vertex to be placed at a coordinate.
 */
public class GameMove
{
    /**
     * The vertex to be placed.
     */
    private Vertex v;
    /**
     * The coordinate to place the vertex to.
     */
    private Coordinate c;
    /**
     * Constructs a GameMove object.
     * @param v The vertex to be placed at c.
     * @param c The coordinate where v is to be placed.
     */
    public GameMove(Vertex v, Coordinate c)
    {
        this.v = v;
        this.c = c;
    }

    /**
     * Returns the vertex to be placed.
     * @return The vertex to be placed.
     */
    public Vertex getVertex()
    {
        return v;
    }

    /**
     * Returns the coordinate to be used.
     * @return The coordinate to be used.
     */
    public Coordinate getCoordinate()
    {
        return c;
    }
}
