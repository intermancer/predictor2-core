package com.intermancer.predictor.gene.transform;

public class RoundTG extends AbstractTransformationGene {

	@Override
	protected double operation(double val) {
		return Math.round(val);
	}

}
