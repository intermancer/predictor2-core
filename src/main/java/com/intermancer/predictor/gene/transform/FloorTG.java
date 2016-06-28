package com.intermancer.predictor.gene.transform;

public class FloorTG extends AbstractTransformationGene {

	@Override
	protected double operation(double val) {
		return Math.floor(val);
	}

}
