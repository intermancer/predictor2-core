package com.intermancer.predictor.experiment;

import java.util.List;

import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.store.OrganismStoreIndex;
import com.intermancer.predictor.organism.store.OrganismIndexRecord;
import com.intermancer.predictor.organism.store.OrganismStore;
import com.intermancer.predictor.organism.store.StoreFullException;

public interface OrganismLifecycleStrategy {

	List<OrganismIndexRecord> getAncestors(OrganismStoreIndex storeIndex);

	OrganismIndexRecord feedOrganism(Organism organism);

	List<OrganismIndexRecord> mergeIntoPopulation(List<OrganismIndexRecord> ancestors, List<OrganismIndexRecord> children,
			OrganismStore store, OrganismStoreIndex storeIndex) throws StoreFullException;

	List<OrganismIndexRecord> generateNextGeneration(List<OrganismIndexRecord> ancestors, OrganismStore store);

}
