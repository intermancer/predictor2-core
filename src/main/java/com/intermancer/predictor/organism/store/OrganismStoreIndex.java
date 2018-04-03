package com.intermancer.predictor.organism.store;

import com.intermancer.predictor.organism.Organism;

public interface OrganismStoreIndex {
	
	OrganismIndexRecord getRandomOrganismIndexRecord();
	OrganismIndexRecord getRandomOrganismIndexRecordFromLowScorePool(double d);
	void analyze();
	double getHighestScore();
	double getLowestScore();
	int findIndexByScore(double targetScore);
	OrganismIndexRecord findByIndex(int index);
	OrganismIndexRecord indexAndStore(double score, Organism organism) throws StoreFullException;
	OrganismStore getOrganismStore();
	void removeRecord(String organismId);

}
