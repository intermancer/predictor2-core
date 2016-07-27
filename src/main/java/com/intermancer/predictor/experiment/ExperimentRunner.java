package com.intermancer.predictor.experiment;

public interface ExperimentRunner {
	
	void startExperiment();
	void stopExperiment();
	ExperimentContext getContext();

}
