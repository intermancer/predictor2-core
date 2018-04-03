package com.intermancer.predictor.organism.store;

public interface OrganismStore {

//	void addRecord(OrganismStoreRecord storeRecord) throws StoreFullException;
	String getNextOrganismId();
	boolean hasCapacity();
	long getCount();
	void removeOrganism(String organismId);
	
}
