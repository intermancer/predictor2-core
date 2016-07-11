package com.intermancer.predictor.experiment;

public interface ExperimentListener {

	void initializeExperimentListener(Experiment experiment);
	void processExperimentCycleResult(ExperimentCycleResult cycleResult);

}
