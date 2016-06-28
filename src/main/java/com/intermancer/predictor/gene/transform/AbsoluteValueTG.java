package com.intermancer.predictor.gene.transform;

public class AbsoluteValueTG extends AbstractTransformationGene {

	@Override
	protected double operation(double val) {
		return Math.abs(val);
	}

}
