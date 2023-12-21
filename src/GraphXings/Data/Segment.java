package GraphXings.Data;

/**
 * A class for managing a segment in the Euclidean plane.
 */
public class Segment
{
    /**
     * True if the segment is vertical, false otherwise.
     */
    private boolean isVertical;
    /**
     * The minimum x-coordinate.
     */
    private Rational startX;
    /**
     * The y-coordinate at startX.
     */
    private Rational startY;
    /**
     * The maximum x-coordinate.
     */
    private Rational endX;
    /**
     * The y-coordinate at endX.
     */
    private Rational endY;
    /**
     * The slope of the segment.
     */
    private Rational a;
    /**
     * The intersection with the y-axis.
     */
    private Rational b;

    /**
     * Constructs a segment between the two given coordinates.
     * @param start The first coordinate.
     * @param end The second coordinate.
     */
    public Segment(Coordinate start, Coordinate end)
    {
        if (start.getX() == end.getX())
        {
            isVertical = true;
            if (start.getY() < end.getY())
            {
                this.startX = new Rational(start.getX());
                this.startY = new Rational(start.getY());
                this.endX = new Rational(end.getX());
                this.endY = new Rational(end.getY());
            }
            else
            {
                this.startX = new Rational(end.getX());
                this.startY = new Rational(end.getY());
                this.endX = new Rational(start.getX());
                this.endY = new Rational(start.getY());
            }
        }
        else
        {
            isVertical = false;
            if (start.getX() < end.getX())
            {
                this.startX = new Rational(start.getX());
                this.startY = new Rational(start.getY());
                this.endX = new Rational(end.getX());
                this.endY = new Rational(end.getY());
            }
            else
            {
                this.startX = new Rational(end.getX());
                this.startY = new Rational(end.getY());
                this.endX = new Rational(start.getX());
                this.endY = new Rational(start.getY());
            }
            a = Rational.dividedBy(Rational.minus(endY,startY),Rational.minus(endX,startX));
            b = Rational.minus(startY,Rational.times(a,startX));
        }
    }

    /**
     * Gets whether the segment is vertical.
     * @return True if the segment is vertical, false otherwise.
     */
    public boolean isVertical()
    {
        return isVertical;
    }

    /**
     * Gets the minimum x-coordinate.
     * @return The minimum x-coordinate.
     */
    public Rational getStartX()
    {
        return startX;
    }

    /**
     * Gets the y-coordinate at the minimum x-coordinate.
     * @return The y-coordinate at startX.
     */
    public Rational getStartY()
    {
        return startY;
    }

    /**
     * Gets the maximum x-coordinate.
     * @return The maximum x-coordinate.
     */
    public Rational getEndX()
    {
        return endX;
    }

    /**
     * Gets the y-coordinate at the maximum x-coordinate.
     * @return The y-coordinate at endX.
     */
    public Rational getEndY()
    {
        return endY;
    }

    /**
     * Gets the slope of the segment.
     * @return The slope of the segment.
     */
    public Rational getA()
    {
        return a;
    }

    /**
     * Gets the intersection of the underlying line with the y-axis.
     * @return The y-coordinate of the intersection with the y-axis.
     */
    public Rational getB()
    {
        return b;
    }

    /**
     * Determines if two segments intersect.
     * @param s1 The first segment.
     * @param s2 The second segment.
     * @return True if s1 and s2 intersect, false otherwise.
     */
    public static boolean intersect(Segment s1, Segment s2)
    {
        if (!s1.isVertical() && !s2.isVertical())
        {
            if (!Rational.equals(s1.getA(),s2.getA()))
            {
                Rational x = Rational.dividedBy(Rational.minus(s2.getB(), s1.getB()), Rational.minus(s1.getA(), s2.getA()));
                return (Rational.lesserEqual(s1.getStartX(), x) && Rational.lesserEqual(s2.getStartX(), x) && Rational.lesserEqual(x, s1.getEndX()) && Rational.lesserEqual(x, s2.getEndX()));
            }
            else
            {
                if (Rational.equals(s1.getB(),s2.getB()))
                {
                    return ((Rational.lesserEqual(s1.getStartX(), s2.getStartX()) && Rational.lesserEqual(s2.getStartX(), s1.getEndX())) || (Rational.lesserEqual(s2.getStartX(), s1.getStartX()) && Rational.lesserEqual(s1.getStartX(), s2.getEndX())));
                }
                else
                {
                    return false;
                }
            }
        }
        if (!s1.isVertical())
        {
            Segment swap = s1;
            s1 = s2;
            s2 = swap;
        }
        if (!s2.isVertical())
        {
            Rational y = Rational.plus(Rational.times(s2.getA(),s1.getStartX()),s2.getB());
            return (Rational.lesserEqual(s2.getStartX(),s1.getStartX()) && Rational.lesserEqual(s1.getStartX(),s2.getEndX()) && Rational.lesserEqual(s1.getStartY(),y) && Rational.lesserEqual(y,s1.getEndY()));
        }
        else
        {
            if (!s1.getStartX().equals(s2.getStartX()))
            {
                return false;
            }
            else
            {
                if (Rational.lesserEqual(s2.getStartY(),s1.getStartY()))
                {
                    Segment swap = s1;
                    s1 = s2;
                    s2 = swap;
                }
                return (Rational.lesserEqual(s2.getStartY(),s1.getEndY()));
            }
        }
    }

    public static double squaredCosineOfAngle(Segment s1, Segment s2)
    {
        if (s1.isVertical() && s2.isVertical())
        {
            return 1;
        }
        if (s2.isVertical())
        {
            Segment swap = s1;
            s1 = s2;
            s2 = swap;
        }
        double ax;
        double ay;
        if (s1.isVertical())
        {
            ax = 0;
            ay = 1;
        }
        else
        {
            ax = s1.getA().getQ();
            ay = s1.getA().getP();
        }
        double bx = s2.getA().getQ();
        double by = s2.getA().getP();
        return Math.pow((ax * bx +  ay * by)/(Math.sqrt(Math.pow(ax,2)+Math.pow(ay,2))*Math.sqrt(Math.pow(bx,2)+Math.pow(by,2))),2);
    }
}
