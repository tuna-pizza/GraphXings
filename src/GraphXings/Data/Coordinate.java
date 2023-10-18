package GraphXings.Data;

/**
 * A class for storing a coordinate on the integer grid.
 */
public class Coordinate
{
    private int x;
    private int y;
    /**
     * Constructs an integer Coordinate object
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate.
     * @return The x-coordinate.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Returns the y-coordinate.
     * @return The y-coordinate.
     */
    public int getY()
    {
        return y;
    }

    /**
     * Decides whether this and the Coordinate other are the same.
     * @param other Another coordinate.
     * @return True, if this equals other, false otherwise.
     */
    public boolean equals (Coordinate other)
    {
        return ((this.x == other.x) && (this.y == other.y));
    }
}
