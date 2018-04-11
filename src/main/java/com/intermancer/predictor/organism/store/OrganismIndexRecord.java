package com.intermancer.predictor.organism.store;

// TODO: Rename this OrganismStoreIndexRecord
public class OrganismIndexRecord {
	
	private String organismId;
	private double score;
	private int scoreIndex;
	
	public OrganismIndexRecord(double score, String organismId) {
		setScore(score);
		setOrganismId(organismId);
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public int getScoreIndex() {
		return scoreIndex;
	}

	public void setScoreIndex(int scoreIndex) {
		this.scoreIndex = scoreIndex;
	}

	public String getOrganismId() {
		return organismId;
	}

	public void setOrganismId(String organismId) {
		this.organismId = organismId;
	}

}
