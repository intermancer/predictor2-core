package com.intermancer.predictor.experiment;

public interface Experiment {

	ExperimentCycleResult runExperimentCycle() throws Exception;
	void init() throws Exception;
	void setContext(ExperimentContext context);

}
