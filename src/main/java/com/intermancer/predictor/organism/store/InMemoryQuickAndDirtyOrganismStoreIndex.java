package com.intermancer.predictor.organism.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.intermancer.predictor.organism.Organism;


// TODO Correct misspelling in class name
public class InMemoryQuickAndDirtyOrganismStoreIndex implements OrganismStoreIndex {
	
	private static class IdComparator implements Comparator<OrganismIndexRecord> {

		@Override
		public int compare(OrganismIndexRecord arg0, OrganismIndexRecord arg1) {
			return arg0.getOrganismId().compareTo(arg1.getOrganismId());
		}
		
	}
	private static final Comparator<OrganismIndexRecord> ID_COMPARATOR = new IdComparator();
	
	private static class ScoreComparator implements Comparator<OrganismIndexRecord> {

		@Override
		public int compare(OrganismIndexRecord arg0, OrganismIndexRecord arg1) {
			if (arg0.getScore() > arg1.getScore()) {
				return 1;
			} else if (arg0.getScore() == arg1.getScore()) {
				return 0;
			} else {
				return -1;
			}
		}
		
	}
	private static final Comparator<OrganismIndexRecord> SCORE_COMPARATOR = new ScoreComparator();
	
	private List<OrganismIndexRecord> idIndex;
	private List<OrganismIndexRecord> scoreIndex;
	
	public InMemoryQuickAndDirtyOrganismStoreIndex() {
		idIndex = new ArrayList<OrganismIndexRecord>(InMemoryQuickAndDirtyOrganismStore.DEFAULT_CAPACITY);
		scoreIndex = new ArrayList<OrganismIndexRecord>(InMemoryQuickAndDirtyOrganismStore.DEFAULT_CAPACITY);
	}
	
	@Override
	public double getHighestScore() {
		if (!scoreIndex.isEmpty()) {
			return scoreIndex.get(0).getScore();
		}
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

	@Override
	public OrganismIndexRecord index(double score, Organism organism) {
		OrganismIndexRecord indexRecord = new OrganismIndexRecord(score, organism.getId());
		insertIntoIdIndex(indexRecord);
		insertIntoScoreIndex(indexRecord);
		return indexRecord;
	}

	private void insertIntoScoreIndex(OrganismIndexRecord indexRecord) {
		int targetIndex = getTargetIndex(scoreIndex, indexRecord, SCORE_COMPARATOR);
		scoreIndex.add(targetIndex, indexRecord);
		for (int i = targetIndex; i < idIndex.size(); i++) {
			OrganismIndexRecord shiftingRecord = scoreIndex.get(i);
			shiftingRecord.setScoreIndex(i);
		}
	}

	private void insertIntoIdIndex(OrganismIndexRecord indexRecord) {
		int targetIndex = getTargetIndex(idIndex, indexRecord, ID_COMPARATOR);
		idIndex.add(targetIndex, indexRecord);
		for (int i = targetIndex; i < idIndex.size(); i++) {
			OrganismIndexRecord shiftingRecord = idIndex.get(i);
			shiftingRecord.setOrganismIdIndex(i);
		}
	}

	private int getTargetIndex(List<OrganismIndexRecord> index, OrganismIndexRecord indexRecord,
			Comparator<OrganismIndexRecord> comparator) {
		int searchIndex = Collections.binarySearch(index, indexRecord, comparator);
		return Math.abs(searchIndex + 1);
	}

}
