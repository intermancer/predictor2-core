package com.intermancer.predictor.experiment;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExperimentResult {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private double startHighScore;
	private double startLowScore;
	private int cycles = 0;
	private int improvementCycles = 0;
	private double finishHighScore;
	private double finishLowScore;
	private long durationInMillis;
	
	public double getStartHighScore() {
		return startHighScore;
	}
	
	public void setStartHighScore(double startHighScore) {
		this.startHighScore = startHighScore;
	}
	
	public double getStartLowScore() {
		return startLowScore;
	}
	
	public void setStartLowScore(double startLowScore) {
		this.startLowScore = startLowScore;
	}
	
	public int getCycles() {
		return cycles;
	}

	public void setCycles(int cycles) {
		this.cycles = cycles;
	}

	public int getImprovementCycles() {
		return improvementCycles;
	}

	public void setImprovementCycles(int improvementCycles) {
		this.improvementCycles = improvementCycles;
	}

	public double getFinishHighScore() {
		return finishHighScore;
	}
	
	public void setFinishHighScore(double finishHighScore) {
		this.finishHighScore = finishHighScore;
	}
	
	public double getFinishLowScore() {
		return finishLowScore;
	}
	
	public void setFinishLowScore(double finishLowScore) {
		this.finishLowScore = finishLowScore;
	}

	public long getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis(long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

	public void incrementCycles() {
		cycles++;
	}
	
	public void incrementImprovementCycles() {
		improvementCycles++;
	}
	
	public void setImprovementPercentage(double improvementPercentage) {
		// no-op.  Just adding it so Jackson works well.
	}
	
	public double getImprovementPercentage() {
		return 1 - finishLowScore / startLowScore;
	}
	
	@Override
	public String toString() {
		String jsonOutput = null;
		try {
			jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (Exception ex) {
			// swallow
		}
		return jsonOutput;
	}

}
