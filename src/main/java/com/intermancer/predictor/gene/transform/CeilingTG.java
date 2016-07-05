package com.intermancer.predictor.gene.transform;

public class CeilingTG extends AbstractTransformationGene {

	@Override
	protected double operation(double val) {
		return Math.ceil(val);
	}

}
