package com.intermancer.predictor.experiment.organism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intermancer.predictor.mutation.MutationUtility;

public class InMemoryQuickAndDirtyOrganismStore implements OrganismStore {
	
	public static final int DEFAULT_MAX_CAPACITY = 1000;

	private long nextId = 0;
	private List<OrganismStoreRecord> records;
	private int maxCapacity = DEFAULT_MAX_CAPACITY;

	private boolean isSorted = false;
	
	public InMemoryQuickAndDirtyOrganismStore() {
		records = new ArrayList<OrganismStoreRecord>();
	}

	@Override
	public void addRecord(OrganismStoreRecord storeRecord) {
		storeRecord.setId(getNextId());
		if (records.size() < maxCapacity) {
			if (isSorted) {
				int possibleIndex = 
						Collections.binarySearch(records, 
								storeRecord, OrganismStoreRecord.COMPARATOR);
				if (possibleIndex < 0) {
					possibleIndex = -possibleIndex;
					possibleIndex--;
				}
				records.add(possibleIndex, storeRecord);
				for (int i = possibleIndex + 1; i < records.size(); i++) {
					OrganismStoreRecord record = records.get(i);
					record.setIndex(i);
				}
			}
			records.add(storeRecord);
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
		return records.size() < maxCapacity;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	@Override
	public OrganismStoreRecord getRandomOrganismStoreRecord() {
		return getRandomOrganismStoreRecord(1.0);
	}
	
	@Override
	public OrganismStoreRecord getRandomOrganismStoreRecord(double percentage) {
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
		return records.get(records.size() - 1).getScore();
	}

	@Override
	public double getLowestScore() {
		return records.get(0).getScore();
	}

	@Override
	public void analyze() {
		if (!isSorted) {
			Collections.sort(records, OrganismStoreRecord.COMPARATOR);
			isSorted  = true;
		}
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
		return records.get(index);
	}

}
