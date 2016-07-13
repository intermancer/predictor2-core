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
import com.intermancer.predictor.organism.store.OrganismStoreRecord;
import com.intermancer.predictor.organism.store.StoreFullException;

public class ExperimentPrimeStrategy implements ExperimentStrategy {

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
	public List<OrganismStoreRecord> getAncestors(OrganismStore store) {
		List<OrganismStoreRecord> parents = new ArrayList<OrganismStoreRecord>();

		// We will pull alpha from the top quarter.
		OrganismStoreRecord alpha = store.getRandomOrganismStoreRecordFromLowScorePool(0.25);
		parents.add(alpha);

		// We will pull beta from the entire pool.
		OrganismStoreRecord beta = alpha;
		do {
			beta = store.getRandomOrganismStoreRecord();
		} while (beta.equals(alpha));
		parents.add(beta);

		return parents;
	}

	@Override
	public List<OrganismStoreRecord> generateNextGeneration(List<OrganismStoreRecord> ancestors) {
		List<Organism> ancestorOrganisms = new ArrayList<Organism>();
		for (OrganismStoreRecord record : ancestors) {
			ancestorOrganisms.add(record.getOrganism());
		}
		List<Organism> children = breedStrategy.breed(ancestorOrganisms);
		List<OrganismStoreRecord> scoredChildren = new ArrayList<OrganismStoreRecord>();
		for (Organism child : children) {
			OrganismStoreRecord storeRecord = feedOrganism(child);
			scoredChildren.add(storeRecord);
		}
		return scoredChildren;
	}

	public OrganismStoreRecord feedOrganism(Organism organism) {
		feeder.setOrganism(organism);
		feeder.init();
		feeder.feedOrganism();
		OrganismStoreRecord storeRecord = new OrganismStoreRecord(feeder.getEvaluator().getScore(), organism);
		return storeRecord;
	}

	@Override
	public List<OrganismStoreRecord> mergeIntoPopulation(List<OrganismStoreRecord> ancestors, List<OrganismStoreRecord> children,
			OrganismStore store) throws StoreFullException {
		List<OrganismStoreRecord> allOrganisms = new ArrayList<OrganismStoreRecord>();
		allOrganisms.addAll(children);
		allOrganisms.addAll(ancestors);
		Collections.sort(allOrganisms, OrganismStoreRecord.COMPARATOR);

		List<OrganismStoreRecord> recordsToRemove = new ArrayList<OrganismStoreRecord>();
		List<OrganismStoreRecord> recordsToAdd = new ArrayList<OrganismStoreRecord>();
		List<OrganismStoreRecord> finals = new ArrayList<OrganismStoreRecord>();

		for (int i = 0; i < allOrganisms.size(); i++) {
			OrganismStoreRecord record = allOrganisms.get(i);
			if (i < 2) {
				if (record.getId() == null) {
					recordsToAdd.add(record);
				}
				finals.add(record);
			} else {
				if (record.getId() != null) {
					recordsToRemove.add(record);
				}
			}
		}

		for (OrganismStoreRecord record : recordsToRemove) {
			store.removeRecord(record);
		}

		for (OrganismStoreRecord record : recordsToAdd) {
			try {
				store.addRecord(record);
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

		store.analyze();
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
