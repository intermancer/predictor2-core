package com.intermancer.predictor.organism.store;

import com.intermancer.predictor.organism.Organism;

public interface OrganismStoreIndex {
	
	OrganismIndexRecord getRandomOrganismIndexRecord();
	OrganismIndexRecord getRandomOrganismIndexRecordFromLowScorePool(double d);
	double getHighestScore();
	double getLowestScore();
	int findIndexByScore(double targetScore);
	OrganismIndexRecord findByIndex(int index);
	OrganismIndexRecord index(double score, Organism organism);
	void removeRecord(String organismId);

}
