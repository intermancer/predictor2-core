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
	private OrganismLifecycleStrategy experimentStrategy;

	private OrganismStore organismStore;

	private List<ExperimentListener> listeners;
	
	public DefaultExperiment() {
		listeners = new ArrayList<ExperimentListener>();
	}

	@Override
	public void init() throws Exception {
		feeder.init();
		for (ExperimentListener listener : listeners) {
			listener.initializeExperimentListener(this);
		}

		organismStore.analyze();
	}

	@Override
	public ExperimentCycleResult runExperimentCycle() throws Exception {
		init();
		ExperimentCycleResult result = new ExperimentCycleResult();
		result.setAncestors(experimentStrategy.getAncestors(organismStore));
		result.setChildren(experimentStrategy.generateNextGeneration(result.getAncestors()));
		result.setFinals(experimentStrategy.mergeIntoPopulation(result.getAncestors(), result.getChildren(), organismStore));
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

	public OrganismLifecycleStrategy getExperimentStrategy() {
		return experimentStrategy;
	}

	@Override
	public void setExperimentStrategy(OrganismLifecycleStrategy experimentStrategy) {
		this.experimentStrategy = experimentStrategy;
	}

	@Override
	public void setOrganismStore(OrganismStore organismStore) {
		this.organismStore = organismStore;
	}

	@Override
	public void setListeners(List<ExperimentListener> listeners) {
		this.listeners = listeners;
	}

}
