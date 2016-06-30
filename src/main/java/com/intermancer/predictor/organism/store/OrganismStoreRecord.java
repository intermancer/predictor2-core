package com.intermancer.predictor.organism.store;

import java.util.Comparator;

import com.intermancer.predictor.organism.Organism;

public class OrganismStoreRecord {
	
	private static class ScoreComparator implements Comparator<OrganismStoreRecord> {

		@Override
		public int compare(OrganismStoreRecord o1, OrganismStoreRecord o2) {
			return (int) (o1.getScore() - o2.getScore());
		}
		
	}
	
	public static final ScoreComparator COMPARATOR = new ScoreComparator();

	private Long id;
	private double score;
	private Organism organism;
	private int index;
	
	public OrganismStoreRecord() {
		
	}

	public OrganismStoreRecord(double score, Organism organism) {
		this.setScore(score);
		this.setOrganism(organism);
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Organism getOrganism() {
		return organism;
	}

	public void setOrganism(Organism organism) {
		this.organism = organism;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

}
