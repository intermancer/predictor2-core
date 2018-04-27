package com.intermancer.predictor.organism.store;

import java.util.Comparator;

// TODO: Rename this OrganismStoreIndexRecord
public class OrganismIndexRecord {
	
	private static class ScoreComparator implements Comparator<OrganismIndexRecord> {

		@Override
		public int compare(OrganismIndexRecord arg0, OrganismIndexRecord arg1) {
			if (arg0.getScore() > arg1.getScore()) {
				return 1;
			} else if (arg0.getScore() == arg1.getScore()) {
				return 0;
			} else {
				return -1;
			}
		}
		
	}
	public static final Comparator<OrganismIndexRecord> SCORE_COMPARATOR = new ScoreComparator();
	
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
