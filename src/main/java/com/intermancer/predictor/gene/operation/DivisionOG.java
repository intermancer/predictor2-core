package com.intermancer.predictor.gene.operation;

public class DivisionOG extends AbstractOperationGene {

	@Override
	double operation(double val1, double val2) {
		if(val2 == 0.0) {
			return 0.0;
		} else {
			return val1 / val2;
		}
	}

}
