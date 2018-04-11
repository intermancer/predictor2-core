package com.intermancer.predictor.organism.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intermancer.predictor.organism.Organism;


// TODO Correct misspelling in class name
public class InMemoryQuickAndDirtyOrganismStoreIndex implements OrganismStoreIndex {
	
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
	
	private Map<String, OrganismIndexRecord> idMap;
	private List<OrganismIndexRecord> scoreIndex;
	
	public InMemoryQuickAndDirtyOrganismStoreIndex() {
		idMap = new HashMap<String, OrganismIndexRecord>(InMemoryQuickAndDirtyOrganismStore.DEFAULT_CAPACITY);
		scoreIndex = new ArrayList<OrganismIndexRecord>(InMemoryQuickAndDirtyOrganismStore.DEFAULT_CAPACITY);
	}
	
	@Override
	public double getHighestScore() {
		if (scoreIndex.isEmpty()) {
			return Double.NaN;
		}
		return scoreIndex.get(scoreIndex.size() - 1).getScore();
	}

	@Override
	public double getLowestScore() {
		if (scoreIndex.isEmpty()) {
			return Double.NaN;
		}
		return scoreIndex.get(0).getScore();
	}

	@Override
	public int findIndexByScore(double targetScore) {
		OrganismIndexRecord dummyIndexRecord = new OrganismIndexRecord(targetScore, "dummy");
		int searchIndex = Collections.binarySearch(scoreIndex, dummyIndexRecord, SCORE_COMPARATOR);
		if (searchIndex < 0) {
			searchIndex++;
			searchIndex = Math.abs(searchIndex);
		}
		return searchIndex;
	}

	@Override
	public OrganismIndexRecord findByScoreIndex(int index) {
		if (index < scoreIndex.size()) {
			return scoreIndex.get(index);
		}
		return null;
	}

	@Override
	public OrganismIndexRecord getRandomOrganismIndexRecord() {
		int targetIndex = Double.valueOf(Math.floor(Math.random() * scoreIndex.size())).intValue();
		return scoreIndex.get(targetIndex);
	}

	@Override
	public OrganismIndexRecord getRandomOrganismIndexRecordFromLowScorePool(double poolSize) {
		if (poolSize > 1.0) {
			poolSize = 1.0;
		}
		int targetIndex = Double.valueOf(Math.floor(Math.random() * scoreIndex.size() * poolSize)).intValue();
		return scoreIndex.get(targetIndex);
	}

	@Override
	public void deleteRecord(String organismId) {
		OrganismIndexRecord indexRecord = idMap.get(organismId);
		if (indexRecord != null) {
			idMap.remove(organismId);
			scoreIndex.remove(indexRecord.getScoreIndex());
			for (int i = indexRecord.getScoreIndex(); i < scoreIndex.size(); i++) {
				scoreIndex.get(i).setScoreIndex(i);
			}
		}
	}

	@Override
	public OrganismIndexRecord index(double score, Organism organism) {
		OrganismIndexRecord indexRecord = new OrganismIndexRecord(score, organism.getId());
		idMap.put(organism.getId(), indexRecord);
		insertIntoScoreIndex(indexRecord);
		return indexRecord;
	}

	private void insertIntoScoreIndex(OrganismIndexRecord indexRecord) {
		int targetIndex = getTargetIndex(scoreIndex, indexRecord, SCORE_COMPARATOR);
		scoreIndex.add(targetIndex, indexRecord);
		for (int i = targetIndex; i < scoreIndex.size(); i++) {
			OrganismIndexRecord shiftingRecord = scoreIndex.get(i);
			shiftingRecord.setScoreIndex(i);
		}
	}

	private int getTargetIndex(List<OrganismIndexRecord> index, OrganismIndexRecord indexRecord,
			Comparator<OrganismIndexRecord> comparator) {
		int searchIndex = Collections.binarySearch(index, indexRecord, comparator);
		return Math.abs(searchIndex + 1);
	}

}
