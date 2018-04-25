package com.intermancer.predictor.experiment;

import java.util.List;

import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.store.OrganismStoreIndex;
import com.intermancer.predictor.organism.store.OrganismIndexRecord;
import com.intermancer.predictor.organism.store.OrganismStore;
import com.intermancer.predictor.organism.store.StoreFullException;

public interface OrganismLifecycleStrategy {

	List<OrganismIndexRecord> getAncestors(OrganismStoreIndex storeIndex);

	List<OrganismIndexRecord> generateNextGeneration(List<OrganismIndexRecord> ancestors);

	OrganismIndexRecord feedOrganism(Organism organism);

	List<OrganismIndexRecord> mergeIntoPopulation(List<OrganismIndexRecord> ancestors, List<OrganismIndexRecord> children,
			OrganismStoreIndex storeIndex, OrganismStore store) throws StoreFullException;

}
