package com.intermancer.predictor.gene.constant;

public class ExponentialCG extends AbstractConstantGene {

	public ExponentialCG() {
		super();
	}

	public ExponentialCG(double constant) {
		super(constant);
	}

	@Override
	protected double operation(double val) {
		return Math.pow(val, constantVal);
	}

}
