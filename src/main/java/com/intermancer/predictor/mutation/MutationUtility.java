package com.intermancer.predictor.mutation;

/**
 * Collects some static utility methods for use in mutation.
 * 
 * @author johnfryar
 * 
 */
public class MutationUtility {

	/**
	 * @param maxValue
	 * @return A random number between 1 and maxValue, inclusive
	 */
	public static int getDiceroll(int maxValue) {
		double rand = Math.random();
		int diceroll = ((int) (rand * maxValue)) + 1;
		return diceroll;
	}

	public static int getBucket(double value, double[] valueArray) {
		int bucket = 0;
		for (double boundary : valueArray) {
			if (value > boundary) {
				bucket++;
			} else {
				break;
			}
		}
		return bucket;
	}

}
