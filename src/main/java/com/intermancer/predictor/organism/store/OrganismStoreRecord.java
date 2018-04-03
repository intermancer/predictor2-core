package com.intermancer.predictor.organism.store;

import java.util.Comparator;

import com.intermancer.predictor.organism.Organism;

public class OrganismStoreRecord {
	
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

	@Override
	public int hashCode() {
		int sum = id.intValue() + organism.hashCode();
		return sum;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrganismStoreRecord) {
			OrganismStoreRecord other = (OrganismStoreRecord) obj;
			boolean idsEqual = ((id == null) && (other.id == null))
					|| ((id != null) && (id.equals(other.id)));
			boolean organismsEqual = ((organism == null) && (other.organism == null))
					|| ((organism != null) && (organism.equals(other.organism)));
			return idsEqual && organismsEqual;
		}
		return false;
	}

}
