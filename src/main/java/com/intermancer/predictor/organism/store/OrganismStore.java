package com.intermancer.predictor.organism.store;

import com.intermancer.predictor.organism.Organism;

public interface OrganismStore {

	boolean hasCapacity();
	long getCount();
	void deleteOrganism(String organismId);
	String putOrganism(Organism organism);
	Organism getOrganism(String organismId);
	
}
