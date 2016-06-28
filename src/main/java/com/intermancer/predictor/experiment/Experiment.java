package com.intermancer.predictor.experiment;

import com.intermancer.predictor.breed.BreedStrategy;
import com.intermancer.predictor.experiment.organism.OrganismStore;
import com.intermancer.predictor.feeder.Feeder;

public interface Experiment {

	ExperimentCycleResult runExperimentCycle() throws Exception;
	void init() throws Exception;
	OrganismStore getOrganismStore();
	void setFeeder(Feeder feeder);
	void setBreedStrategy(BreedStrategy breedStrategy);
	void setOrganismStore(OrganismStore organismStore);
	void setExperimentStrategy(ExperimentStrategy experimentStrategy);

}
