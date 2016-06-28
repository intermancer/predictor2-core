package com.intermancer.predictor.experiment;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.breed.BreedStrategy;
import com.intermancer.predictor.experiment.organism.OrganismStore;
import com.intermancer.predictor.experiment.organism.OrganismStoreRecord;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.organism.Organism;

public class DefaultExperiment implements Experiment {

	private static final int NUMBER_OF_ANCESTORS_TO_RETAIN = 20;

	private Feeder feeder;
	private BreedStrategy breedStrategy;
	private ExperimentStrategy experimentStrategy;

	private OrganismStore organismStore;

	@Override
	public void init() throws Exception {
		feeder.init();

		organismStore.analyze();
	}

	@Override
	public ExperimentCycleResult runExperimentCycle() throws Exception {
		init();
		ExperimentCycleResult result = new ExperimentCycleResult();
		List<OrganismStoreRecord> ancestors = experimentStrategy.getAncestors(organismStore);
		result.setAncestors(ancestors);
		List<OrganismStoreRecord> children = experimentStrategy.generateNextGeneration(ancestors);
		result.setParentWasReplaced(
				experimentStrategy.mergeIntoPopulation(ancestors, children, organismStore));
		result.setChildren(children);
		return result;
	}

	@Override
	public OrganismStore getOrganismStore() {
		return organismStore;
	}

	protected List<Organism> extractWinningOrganisms() {
		List<Organism> winners = new ArrayList<Organism>();
		for (int i = 0; i < NUMBER_OF_ANCESTORS_TO_RETAIN; i++) {
			winners.add(organismStore.findByIndex(i).getOrganism());
		}
		return winners;
	}

	public Feeder getFeeder() {
		return feeder;
	}

	@Override
	public void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}

	public BreedStrategy getBreedStrategy() {
		return breedStrategy;
	}

	@Override
	public void setBreedStrategy(BreedStrategy breedStrategy) {
		this.breedStrategy = breedStrategy;
	}

	public ExperimentStrategy getExperimentStrategy() {
		return experimentStrategy;
	}

	@Override
	public void setExperimentStrategy(ExperimentStrategy experimentStrategy) {
		this.experimentStrategy = experimentStrategy;
	}

	@Override
	public void setOrganismStore(OrganismStore organismStore) {
		this.organismStore = organismStore;
	}

}
