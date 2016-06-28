package com.intermancer.predictor.experiment;

import java.util.List;

import com.intermancer.predictor.experiment.organism.OrganismStore;
import com.intermancer.predictor.experiment.organism.OrganismStoreRecord;

public interface ExperimentStrategy {
	
	List<OrganismStoreRecord> getAncestors(OrganismStore store);
	List<OrganismStoreRecord> generateNextGeneration(List<OrganismStoreRecord> ancestors);
	boolean mergeIntoPopulation(List<OrganismStoreRecord> ancestors, 
			List<OrganismStoreRecord> children, OrganismStore store);

}
