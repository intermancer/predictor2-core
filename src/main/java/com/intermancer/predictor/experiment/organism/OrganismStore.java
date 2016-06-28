package com.intermancer.predictor.experiment.organism;

public interface OrganismStore {

	void addRecord(OrganismStoreRecord storeRecord);
	Long getNextId();
	boolean hasCapacity();
	OrganismStoreRecord getRandomOrganismStoreRecord();
	OrganismStoreRecord getRandomOrganismStoreRecord(double d);

	long getCount();
	void removeRecord(OrganismStoreRecord storeRecord);
	
	void analyze();
	double getHighestScore();
	double getLowestScore();
	int findByScore(double targetScore);
	OrganismStoreRecord findByIndex(int index);

}
