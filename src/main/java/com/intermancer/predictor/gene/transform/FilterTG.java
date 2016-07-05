package com.intermancer.predictor.gene.transform;

public class FilterTG extends AbstractTransformationGene {

	@Override
	protected double operation(double val) {
		if (val < 0.0) {
			return 0.0;
		}
		return val;
	}

}
