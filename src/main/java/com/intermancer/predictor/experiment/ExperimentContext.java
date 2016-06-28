package com.intermancer.predictor.experiment;

public class ExperimentContext {

	private static final int DEFAULT_NUMBER_OF_CYCLES = 10000;

	private int numberOfCycles = DEFAULT_NUMBER_OF_CYCLES;

	public int getNumberOfCycles() {
		return numberOfCycles;
	}

	public void setNumberOfCycles(int numberOfCycles) {
		this.numberOfCycles = numberOfCycles;
	}
	
}
