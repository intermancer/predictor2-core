package com.intermancer.predictor.organism.store;

import com.intermancer.predictor.organism.Organism;


// TODO Correct misspelling in class name
public class InMemoryQuickAndDirtyOgranismStoreIndex implements OrganismStoreIndex {

	@Override
	public void analyze() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getHighestScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLowestScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int findIndexByScore(double targetScore) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OrganismIndexRecord findByIndex(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismIndexRecord indexAndStore(double score, Organism organism) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismStore getOrganismStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismIndexRecord getRandomOrganismIndexRecord() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismIndexRecord getRandomOrganismIndexRecordFromLowScorePool(double d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRecord(String organismId) {
		// TODO Auto-generated method stub
		
	}

}
