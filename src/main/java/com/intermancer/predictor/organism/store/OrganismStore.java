package com.intermancer.predictor.organism.store;

public interface OrganismStore {

	void addRecord(OrganismStoreRecord storeRecord) throws StoreFullException;
	Long getNextId();
	boolean hasCapacity();
	OrganismStoreRecord getRandomOrganismStoreRecord();
	OrganismStoreRecord getRandomOrganismStoreRecordFromLowScorePool(double d);

	long getCount();
	void removeRecord(OrganismStoreRecord storeRecord);
	
	void analyze();
	double getHighestScore();
	double getLowestScore();
	int findByScore(double targetScore);
	OrganismStoreRecord findByIndex(int index);

}
