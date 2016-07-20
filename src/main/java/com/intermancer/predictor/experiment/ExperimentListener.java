package com.intermancer.predictor.experiment;

public interface ExperimentListener {

	void initializeExperimentListener(ExperimentContext context);
	void processExperimentCycleResult(ExperimentCycleResult cycleResult);

}
