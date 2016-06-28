package com.intermancer.predictor.gene.constant;

public class ModulusCG extends AbstractConstantGene {

	public ModulusCG() {
		super();
	}
	
	public ModulusCG(double constant) {
		super(constant);
	}

	@Override
	protected double operation(double val) {
		return val % constantVal;
	}

}
