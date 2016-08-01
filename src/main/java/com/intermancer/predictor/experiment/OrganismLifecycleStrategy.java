package com.intermancer.predictor.experiment;

import java.util.List;

import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.store.OrganismStore;
import com.intermancer.predictor.organism.store.OrganismStoreRecord;
import com.intermancer.predictor.organism.store.StoreFullException;

public interface OrganismLifecycleStrategy {

	List<OrganismStoreRecord> getAncestors(OrganismStore store);

	List<OrganismStoreRecord> generateNextGeneration(List<OrganismStoreRecord> ancestors);

	OrganismStoreRecord feedOrganism(Organism organism);

	List<OrganismStoreRecord> mergeIntoPopulation(List<OrganismStoreRecord> ancestors, List<OrganismStoreRecord> children,
			OrganismStore store) throws StoreFullException;

}
