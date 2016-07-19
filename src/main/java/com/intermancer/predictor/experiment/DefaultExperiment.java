package com.intermancer.predictor.experiment;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.breed.BreedStrategy;
import com.intermancer.predictor.organism.store.OrganismStore;
import com.intermancer.predictor.organism.store.OrganismStoreRecord;

public class DefaultExperiment implements Experiment {

	private static final int NUMBER_OF_ANCESTORS_TO_RETAIN = 20;

	private Feeder feeder;
	private BreedStrategy breedStrategy;
	private OrganismLifecycleStrategy organismLifecycleStrategy;

	private OrganismStore organismStore;

	private List<ExperimentListener> listeners;
	
	public DefaultExperiment() {
		listeners = new ArrayList<ExperimentListener>();
	}

	@Override
	public void init() throws Exception {
		feeder.init();
		for (ExperimentListener listener : listeners) {
			listener.initializeExperimentListener(this, organismStore);
		}

		organismStore.analyze();
	}

	@Override
	public ExperimentCycleResult runExperimentCycle() throws Exception {
		init();
		ExperimentCycleResult result = new ExperimentCycleResult();
		result.setAncestors(organismLifecycleStrategy.getAncestors(organismStore));
		result.setChildren(organismLifecycleStrategy.generateNextGeneration(result.getAncestors()));
		result.setFinals(organismLifecycleStrategy.mergeIntoPopulation(result.getAncestors(), result.getChildren(), organismStore));
		boolean parentWasReplaced = false;
		for (OrganismStoreRecord record : result.getFinals()) {
			if (result.getChildren().contains(record)) {
				parentWasReplaced = true;
				break;
			}
		}
		result.setParentWasReplaced(parentWasReplaced);
		for (ExperimentListener listener : listeners) {
			listener.processExperimentCycleResult(result);
		}
		return result;
	}

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

	public void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}

	public BreedStrategy getBreedStrategy() {
		return breedStrategy;
	}

	public void setBreedStrategy(BreedStrategy breedStrategy) {
		this.breedStrategy = breedStrategy;
	}

	public OrganismLifecycleStrategy getOrganismLifecycleStrategy() {
		return organismLifecycleStrategy;
	}

	public void setOrganismLifecycleStrategy(OrganismLifecycleStrategy organismLifecycleStrategy) {
		this.organismLifecycleStrategy = organismLifecycleStrategy;
	}

	public void setOrganismStore(OrganismStore organismStore) {
		this.organismStore = organismStore;
	}

	public void setListeners(List<ExperimentListener> listeners) {
		this.listeners = listeners;
	}

}
