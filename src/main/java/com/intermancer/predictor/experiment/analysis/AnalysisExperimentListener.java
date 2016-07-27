package com.intermancer.predictor.experiment.analysis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;

import com.intermancer.predictor.experiment.ExperimentContext;
import com.intermancer.predictor.experiment.ExperimentCycleResult;
import com.intermancer.predictor.experiment.ExperimentListener;
import com.intermancer.predictor.experiment.ExperimentResult;
import com.intermancer.predictor.organism.store.OrganismStore;

/**
 * This ExperimentListener expects to watch an entire experiment, with a
 * beginning and end. Not appropriate in situations where the system is simply
 * started and allowed to run for an arbitrary amount of time.
 * 
 * @author JohnFryar
 *
 */
public class AnalysisExperimentListener implements ExperimentListener {

	private static final Logger logger = LogManager.getLogger(AnalysisExperimentListener.class);
	public static final String BEST_SCORE_TIME_DATA_KEY = "con.intermancer.predictor.experiment.analysis.BestScoreTimeDataKey";

	private ExperimentResult experimentResult;
	private long startTimeInMillis;
	private long endTimeInMillis;
	private OrganismStore organismStore;
	private int iteration;
	private TimeSeries bestScoreTimeData;

	public AnalysisExperimentListener(OrganismStore organismStore) {
		this.organismStore = organismStore;
	}

	@Override
	public void initializeExperimentListener(ExperimentContext context) {
		logger.debug("Initializing AnalysisExperimentListener...");
		startTimeInMillis = System.currentTimeMillis();
		experimentResult = new ExperimentResult();
		experimentResult.setCycles(context.getCycles());
		this.organismStore = context.getOrganismStore();
		getStartingStats();
		iteration = 0;
		bestScoreTimeData = new TimeSeries("Best score");
		context.registerResource(BEST_SCORE_TIME_DATA_KEY, bestScoreTimeData);
	}

	@Override
	public void processExperimentCycleResult(ExperimentCycleResult cycleResult) {
		iteration++;
		logger.debug("Analyzing iteration {}", iteration);
		if (cycleResult.isParentWasReplaced()) {
			experimentResult.incrementImprovementCycles();
		}

		// Every experiment cycle could be the last.
		getExperimentCycleStatistics();
	}

	private void getStartingStats() {
		experimentResult.setStartHighScore(organismStore.getHighestScore());
		experimentResult.setStartLowScore(organismStore.getLowestScore());
	}

	private void getExperimentCycleStatistics() {
		experimentResult.setIteration(iteration);
		experimentResult.setFinishHighScore(organismStore.getHighestScore());
		experimentResult.setFinishLowScore(organismStore.getLowestScore());
		endTimeInMillis = System.currentTimeMillis();
		experimentResult.setDurationInMillis(endTimeInMillis - startTimeInMillis);
		bestScoreTimeData.addOrUpdate(new FixedMillisecond(endTimeInMillis), organismStore.getLowestScore());
	}

	public ExperimentResult getExperimentResult() {
		return experimentResult;
	}

}
