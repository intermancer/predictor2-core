package com.intermancer.predictor.experiment;

import java.util.List;

import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.organism.breed.BreedStrategy;
import com.intermancer.predictor.organism.store.OrganismStore;

public interface Experiment {

	ExperimentCycleResult runExperimentCycle() throws Exception;
	void init() throws Exception;
	OrganismStore getOrganismStore();
	void setFeeder(Feeder feeder);
	void setBreedStrategy(BreedStrategy breedStrategy);
	void setOrganismStore(OrganismStore organismStore);
	void setExperimentStrategy(OrganismLifecycleStrategy experimentStrategy);
	void setListeners(List<ExperimentListener> listeners);

}
