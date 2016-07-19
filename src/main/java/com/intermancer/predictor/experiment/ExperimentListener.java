package com.intermancer.predictor.experiment;

import com.intermancer.predictor.organism.store.OrganismStore;

public interface ExperimentListener {

	void initializeExperimentListener(Experiment experiment, OrganismStore organismStore);
	void processExperimentCycleResult(ExperimentCycleResult cycleResult);

}
