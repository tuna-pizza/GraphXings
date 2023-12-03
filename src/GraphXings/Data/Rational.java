package GraphXings.Data;

/**
 * A class for storing a rational number. At all times, the representation is simplified.
 */
public class Rational
{
    /**
     * The numerator.
     */
    private long p;
    /**
     * The denominator.
     */
    private long q;

    /**
     * Creates a rational number from an int.
     * @param n An integer.
     */
    public Rational (long n)
    {
        p = n;
        q = 1;
    }

    /**
     * Creates a rational number object.
     * @param p The numerator.
     * @param q The denominator.
     */
    public Rational (long p, long q)
    {
        if (q == 0)
        {
            throw new ArithmeticException();
        }
        this.p = p;
        this.q = q;
        simplify();
    }

    /**
     * Gets the numerator.
     * @return The numerator.
     */
    public long getP()
    {
        return p;
    }

    /**
     * Gets the denominator.
     * @return The denominator.
     */
    public long getQ()
    {
        return q;
    }

    /**
     * Determines if this and other are the same rational number.
     * @param other Another Rational object.
     * @return True, if both Rationals are the same, false otherwise.
     */
    public boolean equals(Rational other)
    {
        return ((p == other.getP()) && (q == other.getQ()));
    }

    /**
     * Simplifies the representation.
     */
    private void simplify()
    {
        if (q < 0)
        {
            p = -p;
            q = -q;
        }
        long gcd = gcd(p,q);
        p = p/gcd;
        q = q/gcd;
        if (p==0)
        {
            q=1;
        }
    }

    /**
     * Computes the gcd of two integers. Uses the Euclidean algorithm.
     * @param a The first integer.
     * @param b The second integer.
     * @return The gcd of a and b.
     */
    private long gcd(long a, long b)
    {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a < b)
        {
            long swap = b;
            b = a;
            a = swap;
        }
        if (b == 0)
        {
            return 1;
        }
        long c = a % b;
        if (c == 0)
        {
            return b;
        }
        else
        {
            return gcd(b,c);
        }
    }
    /**
     * Computes the sum of two rational numbers.
     * @param r1 The first addend.
     * @param r2 The second addend.
     * @return The sum r1+r2.
     */
    static public Rational plus(Rational r1, Rational r2)
    {
        return new Rational(r1.getP()*r2.getQ()+r2.getP()*r1.getQ(),r1.getQ()*r2.getQ());
    }

    /**
     * Computes the difference between two rational numbers.
     * @param r1 The minuend.
     * @param r2 The subtrahend.
     * @return The difference r1-r2.
     */
    static public Rational minus(Rational r1, Rational r2)
    {
        return new Rational(r1.getP()*r2.getQ()-r2.getP()*r1.getQ(),r1.getQ()*r2.getQ());
    }

    /**
     * Computes the product of two rational numbers.
     * @param r1 The first factor.
     * @param r2 The second factor.
     * @return The product r1*r2.
     */
    static public Rational times(Rational r1, Rational r2)
    {
        return new Rational(r1.getP()*r2.getP(),r1.getQ()*r2.getQ());
    }

    /**
     * Computes the quotient of two rational numbers.
     * @param r1 The dividend.
     * @param r2 The divisor.
     * @return The quotient r1/r2.
     */
    static public Rational dividedBy(Rational r1, Rational r2)
    {
        if (r2.getP() == 0)
        {
            throw new ArithmeticException();
        }
        return new Rational(r1.getP()*r2.getQ(),r1.getQ()*r2.getP());
    }

    /**
     * Determines whether the first Rational is lesser or equal the second Rational.
     * @param r1 The first rational.
     * @param r2 The second rational.
     * @return True if r1 <= r2, false otherwise.
     */
    static public boolean lesserEqual(Rational r1,Rational r2)
    {
        return (r1.getP()*r2.getQ() <= r2.getP()*r1.getQ());
    }
    /**
     * Determines whether the both Rational numbers are the same.
     * @param r1 The first rational.
     * @param r2 The second rational.
     * @return True if r1 == r2, false otherwise.
     */
    static public boolean equals(Rational r1, Rational r2)
    {
        return ((r1.getP() == r2.getP()) && (r1.getQ() == r2.getQ()));
    }
}
