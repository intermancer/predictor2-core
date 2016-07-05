package com.intermancer.predictor.experiment;

/**
 * This ExperimentListener expects to watch an entire experiment, with a beginning and end.
 * Not appropriate in situations where the system is simply started and allowed to run for an
 * arbitrary amount of time.
 * 
 * @author JohnFryar
 *
 */
public class AnalysisExperimentListener implements ExperimentListener {
	
	private ExperimentResult experimentResult = new ExperimentResult();
	private long startTimeInMillis;
	private long endTimeInMillis;

	public AnalysisExperimentListener() {

	}
	
	@Override
	public void initializeExperimentListener(Experiment experiment) {
		getStartingStats(experiment);
		startTimeInMillis = System.currentTimeMillis();
	}

	@Override
	public void processExperimentCycleResult(ExperimentCycleResult cycleResult, Experiment experiment) {
		if (cycleResult.isParentWasReplaced()) {
			experimentResult.incrementImprovementCycles();
		}
		
		// Every experiment cycle could be the last.
		getEndingStatistics(experiment);
		endTimeInMillis = System.currentTimeMillis();
		experimentResult.setDurationInMillis(endTimeInMillis - startTimeInMillis);
	}

	private void getStartingStats(Experiment experiment) {
		experimentResult.setStartHighScore(experiment.getOrganismStore().getHighestScore());
		experimentResult.setStartLowScore(experiment.getOrganismStore().getLowestScore());
	}

	private void getEndingStatistics(Experiment experiment) {
		experimentResult.setFinishHighScore(experiment.getOrganismStore().getHighestScore());
		experimentResult.setFinishLowScore(experiment.getOrganismStore().getLowestScore());
	}

	public ExperimentResult getExperimentResult() {
		return experimentResult;
	}

}
