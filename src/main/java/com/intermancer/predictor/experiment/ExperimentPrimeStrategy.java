package com.intermancer.predictor.experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.breed.BreedStrategy;
import com.intermancer.predictor.organism.store.OrganismStore;
import com.intermancer.predictor.organism.store.OrganismStoreIndex;
import com.intermancer.predictor.organism.store.OrganismIndexRecord;
import com.intermancer.predictor.organism.store.StoreFullException;

public class ExperimentPrimeStrategy implements OrganismLifecycleStrategy {

	private static final Logger logger = LogManager.getLogger(ExperimentPrimeStrategy.class);
	private final ObjectMapper mapper;

	private BreedStrategy breedStrategy;
	private Feeder feeder;

	public ExperimentPrimeStrategy() {
		mapper = new ObjectMapper();
	}

	public ExperimentPrimeStrategy(BreedStrategy breedStrategy, Feeder feeder) {
		this();
		this.breedStrategy = breedStrategy;
		this.feeder = feeder;
	}

	@Override
	public List<OrganismIndexRecord> getAncestors(OrganismStoreIndex storeIndex) {
		List<OrganismIndexRecord> parents = new ArrayList<OrganismIndexRecord>();

		// We will pull alpha from the top quarter.
		OrganismIndexRecord alpha = storeIndex.getRandomOrganismIndexRecordFromLowScorePool(0.25);
		parents.add(alpha);

		// We will pull beta from the entire pool.
		OrganismIndexRecord beta = alpha;
		do {
			beta = storeIndex.getRandomOrganismIndexRecord();
		} while (beta.equals(alpha));
		parents.add(beta);

		return parents;
	}

	@Override
	public List<OrganismIndexRecord> generateNextGeneration(List<OrganismIndexRecord> ancestors) {
		List<Organism> ancestorOrganisms = new ArrayList<Organism>();
		for (OrganismIndexRecord record : ancestors) {
			ancestorOrganisms.add(record.getOrganism());
		}
		List<Organism> children = breedStrategy.breed(ancestorOrganisms);
		List<OrganismIndexRecord> scoredChildren = new ArrayList<OrganismIndexRecord>();
		for (Organism child : children) {
			OrganismIndexRecord storeRecord = feedOrganism(child);
			scoredChildren.add(storeRecord);
		}
		return scoredChildren;
	}

	@Override
	public OrganismIndexRecord feedOrganism(Organism organism) {
		feeder.setOrganism(organism);
		feeder.init();
		feeder.feedOrganism();
		OrganismIndexRecord storeRecord = new OrganismIndexRecord(feeder.getEvaluator().getScore(), organism);
		return storeRecord;
	}

	@Override
	public List<OrganismIndexRecord> mergeIntoPopulation(List<OrganismIndexRecord> ancestors, List<OrganismIndexRecord> children,
			OrganismStoreIndex storeIndex) throws StoreFullException {
		List<OrganismIndexRecord> allOrganisms = new ArrayList<OrganismIndexRecord>();
		allOrganisms.addAll(children);
		allOrganisms.addAll(ancestors);
		Collections.sort(allOrganisms, OrganismIndexRecord.COMPARATOR);

		List<OrganismIndexRecord> recordsToRemove = new ArrayList<OrganismIndexRecord>();
		List<OrganismIndexRecord> recordsToAdd = new ArrayList<OrganismIndexRecord>();
		List<OrganismIndexRecord> finals = new ArrayList<OrganismIndexRecord>();

		for (int i = 0; i < allOrganisms.size(); i++) {
			OrganismIndexRecord record = allOrganisms.get(i);
			if (i < 2) {
				if (record.getOrganismId() == null) {
					recordsToAdd.add(record);
				}
				finals.add(record);
			} else {
				if (record.getOrganismId() != null) {
					recordsToRemove.add(record);
				}
			}
		}

		OrganismStore store = storeIndex.getOrganismStore();
		for (OrganismIndexRecord record : recordsToRemove) {
			store.deleteOrganism(record.getOrganismId());
			storeIndex.removeRecord(record.getOrganismId());
		}

		for (OrganismIndexRecord record : recordsToAdd) {
			try {
				storeIndex.indexAndStore(record.getScore(), record.getOrganism());
			} catch (StoreFullException ex) {
				try {
					// This happened when the two ancestors were the same
					// record.
					ArrayList<Object> items = new ArrayList<Object>();
					items.add("==========ancestors==========");
					items.add(ancestors);
					items.add("==========children==========");
					items.add(children);
					items.add("==========allOrganisms==========");
					items.add(allOrganisms);
					items.add("==========recordsToRemove==========");
					items.add(recordsToRemove);
					items.add("==========recordsToAdd==========");
					items.add(recordsToAdd);
					logger.error("Combined record:{}", mapper.writeValueAsString(items));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				throw ex;
			}
		}

		return finals;
	}

	public BreedStrategy getBreedStrategy() {
		return breedStrategy;
	}

	public void setBreedStrategy(BreedStrategy breedStrategy) {
		this.breedStrategy = breedStrategy;
	}

	public Feeder getFeeder() {
		return feeder;
	}

	public void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}

}
