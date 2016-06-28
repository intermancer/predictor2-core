package com.intermancer.predictor.gene.transform;

/**
 * SignContinuityTG outputs a count of how many cycles its input Channel has had
 * the same sign.
 * 
 * @author johnfryar
 * 
 */
public class SignContinuityTG extends AbstractTransformationGene {

	public static final int IS_POSITIVE_HASH_VALUE = 145;
	int count = 0;
	boolean isPositive = true;

	@Override
	protected double operation(double val) {
		if (isPositive && val < 0.0) {
			count = -1;
			isPositive = false;
		} else if (!isPositive && val >= 0.0) {
			count = 1;
			isPositive = true;
		} else if (isPositive) {
			count++;
		} else {
			count--;
		}
		return count;
	}

	public boolean isPositive() {
		return isPositive;
	}

	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			SignContinuityTG other = (SignContinuityTG) obj;
			return isPositive == other.isPositive;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		if (isPositive) {
			hashCode += IS_POSITIVE_HASH_VALUE;
		}
		return hashCode;
	}

}
