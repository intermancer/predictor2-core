package com.intermancer.predictor.organism.store;

import java.util.Comparator;

import com.intermancer.predictor.organism.Organism;

// TODO: Rename this OrganismStoreIndexRecord
public class OrganismIndexRecord {
	
	private static class ScoreComparator implements Comparator<OrganismIndexRecord> {

		@Override
		public int compare(OrganismIndexRecord o1, OrganismIndexRecord o2) {
			return (int) (o1.getScore() - o2.getScore());
		}
		
	}
	
	public static final ScoreComparator COMPARATOR = new ScoreComparator();

	private Organism organism;
	private String organismId;
	private double score;
	private int index;
	
	public OrganismIndexRecord() {
		// Nothing.
	}
	
	public OrganismIndexRecord(double score, Organism organism) {
		setScore(score);
		setOrganism(organism);
	}
	
	public Organism getOrganism() {
		return organism;
	}
	
	public void setOrganism(Organism organism) {
		this.organism = organism;
		setOrganismId(organism.getId());
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getOrganismId() {
		return organismId;
	}

	public void setOrganismId(String organismId) {
		this.organismId = organismId;
	}

}
