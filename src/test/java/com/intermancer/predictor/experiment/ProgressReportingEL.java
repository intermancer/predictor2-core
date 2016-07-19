package com.intermancer.predictor.experiment;

import com.intermancer.predictor.organism.store.OrganismStore;

public class ProgressReportingEL implements ExperimentListener {
	
	private int reportingIncrement = 500;
	private int lineCount = 10;
	private int carriageReturnCount = reportingIncrement * lineCount;
	private int count = 0;
	
	public ProgressReportingEL() {
		
	}

	@Override
	public void initializeExperimentListener(Experiment experiment, OrganismStore organismStore) {
	}

	@Override
	public void processExperimentCycleResult(ExperimentCycleResult cycleResult) {
		count++;
		if ((count % reportingIncrement) == 0) {
			System.out.print(".");
		}
		if ((count % carriageReturnCount) == 0) {
			System.out.println("|");
		}
	}

}
