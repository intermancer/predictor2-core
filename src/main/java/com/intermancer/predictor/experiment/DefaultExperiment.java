package com.intermancer.predictor.experiment;

import com.intermancer.predictor.organism.store.OrganismIndexRecord;

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
		OrganismLifecycleStrategy lifecycle = context.getOrganismLifecycleStrategy();
		result.setAncestors(lifecycle.getAncestors(context.getOrganismStoreIndex()));
		result.setChildren(lifecycle.generateNextGeneration(result.getAncestors(), context.getOrganismStore()));
		result.setFinals(lifecycle.mergeIntoPopulation(result.getAncestors(),
				result.getChildren(), context.getOrganismStore(), context.getOrganismStoreIndex()));
		boolean parentWasReplaced = false;
		for (OrganismIndexRecord record : result.getFinals()) {
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
