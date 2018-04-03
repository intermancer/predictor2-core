package com.intermancer.predictor.organism.store;

import java.util.HashMap;
import java.util.Map;

import com.intermancer.predictor.organism.Organism;

/**
 * This OrganismStore is not thread-safe.
 * 
 * @author JohnFryar
 *
 */
public class InMemoryQuickAndDirtyOrganismStore implements OrganismStore {
	
	public static final int DEFAULT_CAPACITY = 1000;
	private Map<String, Organism> organisms;
	private int capacity;
	private long currentId;
	
	public InMemoryQuickAndDirtyOrganismStore() {
		this(DEFAULT_CAPACITY);
	}

	public InMemoryQuickAndDirtyOrganismStore(int capacity) {
		this.capacity = capacity;
		this.organisms = new HashMap<String, Organism>(capacity);
	}

	@Override
	public boolean hasCapacity() {
		return organisms.size() < capacity;
	}

	@Override
	public long getCount() {
		return organisms.size();
	}

	@Override
	public void deleteOrganism(String organismId) {
		organisms.remove(organismId);
	}
	
	@Override
	public String putOrganism(Organism organism) {
		String organismId = organism.getId();
		if (organismId == null) {
			organismId = getNextOrganismId();
			organism.setId(organismId);
		}
		organisms.put(organismId, organism);
		return organismId;
	}

	private synchronized String getNextOrganismId() {
		String stringId = Long.toString(currentId);
		currentId++;
		return stringId;
	}

	@Override
	public Organism getOrganism(String organismId) {
		return organisms.get(organismId);
	}

}
