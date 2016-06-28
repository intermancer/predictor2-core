package com.intermancer.predictor.gene.constant;

public class AdditionCG extends AbstractConstantGene {

	public AdditionCG() {
		super();
	}
	
	public AdditionCG(double constant) {
		super(constant);
	}

	@Override
	protected double operation(double val) {
		return val + constantVal;
	}

}
