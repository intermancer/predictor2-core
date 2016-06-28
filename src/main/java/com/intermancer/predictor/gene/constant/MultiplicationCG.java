package com.intermancer.predictor.gene.constant;

public class MultiplicationCG extends AbstractConstantGene {

	public MultiplicationCG() {
		super();
	}
	
	public MultiplicationCG(double constant) {
		super(constant);
	}
	
	public MultiplicationCG(boolean negative, boolean inverse) {
		super(negative, inverse);
	}

	public MultiplicationCG(double constant, boolean negative, boolean inverse) {
		super(constant, negative, inverse);
	}

	public MultiplicationCG(int offset, double constant, boolean negative, boolean inverse) {
		super(offset, constant, negative, inverse);
	}

	@Override
	protected double operation(double val) {
		return val * constantVal;
	}

}
