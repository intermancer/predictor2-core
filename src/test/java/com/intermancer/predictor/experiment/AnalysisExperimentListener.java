package com.intermancer.predictor.experiment;

import com.intermancer.predictor.organism.store.OrganismStore;

/**
 * This ExperimentListener expects to watch an entire experiment, with a beginning and end.
 * Not appropriate in situations where the system is simply started and allowed to run for an
 * arbitrary amount of time.
 * 
 * @author JohnFryar
 *
 */
public class AnalysisExperimentListener implements ExperimentListener {
	
	private ExperimentResult experimentResult;
	private long startTimeInMillis;
	private long endTimeInMillis;
	private OrganismStore organismStore;

	public AnalysisExperimentListener(OrganismStore organismStore) {
		this.organismStore = organismStore;
	}
	
	@Override
	public void initializeExperimentListener(Experiment experiment, OrganismStore organismStore) {
		startTimeInMillis = System.currentTimeMillis();
		experimentResult = new ExperimentResult();
		this.organismStore = organismStore;
		getStartingStats();
	}

	@Override
	public void processExperimentCycleResult(ExperimentCycleResult cycleResult) {
		if (cycleResult.isParentWasReplaced()) {
			experimentResult.incrementImprovementCycles();
		}
		
		// Every experiment cycle could be the last.
		getEndingStatistics();
		endTimeInMillis = System.currentTimeMillis();
		experimentResult.setDurationInMillis(endTimeInMillis - startTimeInMillis);
	}

	private void getStartingStats() {
		experimentResult.setStartHighScore(organismStore.getHighestScore());
		experimentResult.setStartLowScore(organismStore.getLowestScore());
	}

	private void getEndingStatistics() {
		experimentResult.setFinishHighScore(organismStore.getHighestScore());
		experimentResult.setFinishLowScore(organismStore.getLowestScore());
	}

	public ExperimentResult getExperimentResult() {
		return experimentResult;
	}

}
