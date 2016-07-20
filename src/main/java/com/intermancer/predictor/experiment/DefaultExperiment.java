package com.intermancer.predictor.experiment;

import com.intermancer.predictor.organism.store.OrganismStore;
import com.intermancer.predictor.organism.store.OrganismStoreRecord;

public class DefaultExperiment implements Experiment {

	private ExperimentContext context;

	public DefaultExperiment() {
	}

	@Override
	public void init() throws Exception {
		context.getFeeder().init();
		for (ExperimentListener listener : context.getListeners()) {
			listener.initializeExperimentListener(context);
		}
	}

	@Override
	public ExperimentCycleResult runExperimentCycle() throws Exception {
		ExperimentCycleResult result = new ExperimentCycleResult();
		OrganismStore organismStore = context.getOrganismStore();
		OrganismLifecycleStrategy lifecycle = context.getOrganismLifecycleStrategy();
		result.setAncestors(lifecycle.getAncestors(organismStore));
		result.setChildren(lifecycle.generateNextGeneration(result.getAncestors()));
		result.setFinals(lifecycle.mergeIntoPopulation(result.getAncestors(), result.getChildren(), organismStore));
		boolean parentWasReplaced = false;
		for (OrganismStoreRecord record : result.getFinals()) {
			if (result.getChildren().contains(record)) {
				parentWasReplaced = true;
				break;
			}
		}
		result.setParentWasReplaced(parentWasReplaced);
		for (ExperimentListener listener : context.getListeners()) {
			listener.processExperimentCycleResult(result);
		}
		return result;
	}

	@Override
	public void setContext(ExperimentContext context) {
		this.context = context;
	}

}
