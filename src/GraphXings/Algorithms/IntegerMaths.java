package GraphXings.Algorithms;
public class IntegerMaths
{
	/**
	 * Computes the power of an int.
	 * @param base An integer base.
	 * @param exp An integer exponent.
	 * @return The integer power.
	 */
	public static int pow (int base, int exp)
	{
		int res = 1;
		for (int i = 0; i < exp; i++)
		{
			res *= base;
		}
		return res;
	}
}
