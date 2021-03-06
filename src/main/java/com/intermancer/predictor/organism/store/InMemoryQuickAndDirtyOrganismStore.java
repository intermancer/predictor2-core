package com.intermancer.predictor.organism.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intermancer.predictor.mutation.MutationUtility;

/**
 * This OrganismStore is not thread-safe.
 * 
 * @author JohnFryar
 *
 */
public class InMemoryQuickAndDirtyOrganismStore implements OrganismStore {
	
	public static final long DEFAULT_MAX_CAPACITY = 1000;

	private long nextId = 0;
	private List<OrganismStoreRecord> records;
	private long maxSize = DEFAULT_MAX_CAPACITY;

	public InMemoryQuickAndDirtyOrganismStore() {
		records = new ArrayList<OrganismStoreRecord>();
	}

	@Override
	public void addRecord(OrganismStoreRecord storeRecord) throws StoreFullException {
		if (records.size() < maxSize) {
			storeRecord.setId(getNextId());
			int possibleIndex = 
					Collections.binarySearch(records, 
							storeRecord, OrganismStoreRecord.COMPARATOR);
			if (possibleIndex < 0) {
				possibleIndex = -possibleIndex;
				possibleIndex--;
			}
			records.add(possibleIndex, storeRecord);
			for (int i = possibleIndex; i < records.size(); i++) {
				OrganismStoreRecord record = records.get(i);
				record.setIndex(i);
			}
		} else {
			throw new StoreFullException("OrganismStore maxCapacity:" + maxSize);
		}
	}

	/**
	 * Not thread safe right now
	 * @return
	 */
	@Override
	public Long getNextId() {
		Long nextIdObj = new Long(nextId);
		nextId++;
		return nextIdObj;
	}

	@Override
	public boolean hasCapacity() {
		return records.size() < maxSize;
	}

	@Override
	public long getMaxSize() {
		return maxSize;
	}

	@Override
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public void setMaxSize(Long maxSize) {
		setMaxSize(maxSize.longValue());
	}

	@Override
	public OrganismStoreRecord getRandomOrganismStoreRecord() {
		return getRandomOrganismStoreRecordFromLowScorePool(1.0);
	}
	
	@Override
	public OrganismStoreRecord getRandomOrganismStoreRecordFromLowScorePool(double percentage) {
		int highIndex = records.size() - 1;
		if (percentage < 1.0) {
			highIndex = (int) (percentage * highIndex);
		}
		int index = MutationUtility.getDiceroll(highIndex);
		return records.get(index);
	}

	@Override
	public long getCount() {
		return records.size();
	}

	@Override
	public void removeRecord(OrganismStoreRecord storeRecord) {
		int index = 0;
		boolean found = false;
		while (index < records.size()) {
			if (records.get(index).getId().equals(storeRecord.getId())) {
				found = true;
				break;
			} else {
				index++;
			}
		}
		if (found) {
			records.remove(index);
		}
	}


	@Override
	public double getHighestScore() {
		if (records.size() > 0) {
			return records.get(records.size() - 1).getScore();
		} else {
			return 0;
		}
	}

	@Override
	public double getLowestScore() {
		if (records.size() > 0) {
			return records.get(0).getScore();
		} else {
			return 0;
		}
	}

	@Override
	public void analyze() {
		for (int i = 0; i < records.size(); i++) {
			records.get(i).setIndex(i);
		}
	}

	@Override
	public int findByScore(double targetScore) {
		OrganismStoreRecord key = new OrganismStoreRecord();
		key.setScore(targetScore);
		int index = Collections.binarySearch(records, key, OrganismStoreRecord.COMPARATOR);
		return index;
	}

	@Override
	public OrganismStoreRecord findByIndex(int index) {
		if (index < records.size()) {
			return records.get(index);			
		}
		return null;
	}
	
	public List<OrganismStoreRecord> getRecords() {
		return records;
	}

}
