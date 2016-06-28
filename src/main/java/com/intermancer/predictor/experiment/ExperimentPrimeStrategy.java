package com.intermancer.predictor.experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intermancer.predictor.breed.BreedStrategy;
import com.intermancer.predictor.experiment.organism.OrganismStore;
import com.intermancer.predictor.experiment.organism.OrganismStoreRecord;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.organism.Organism;

public class ExperimentPrimeStrategy implements ExperimentStrategy {
	
	private BreedStrategy breedStrategy;
	private Feeder feeder;
	
	public ExperimentPrimeStrategy() {
		
	}
	
	public ExperimentPrimeStrategy(BreedStrategy breedStrategy, Feeder feeder) {
		this.breedStrategy = breedStrategy;
		this.feeder = feeder;
	}

	@Override
	public List<OrganismStoreRecord> getAncestors(OrganismStore store) {
		List<OrganismStoreRecord> parents = new ArrayList<OrganismStoreRecord>();
		
		// We will pull alpha from the top quarter.
		OrganismStoreRecord alpha = store.getRandomOrganismStoreRecord(0.25);
		parents.add(alpha);

		// We will pull beta from the entire pool.
		OrganismStoreRecord beta = store.getRandomOrganismStoreRecord();
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
	public boolean mergeIntoPopulation(List<OrganismStoreRecord> ancestors, 
			List<OrganismStoreRecord> children, OrganismStore store) {
		List<OrganismStoreRecord> allOrganisms = new ArrayList<OrganismStoreRecord>();
		allOrganisms.addAll(children);
		allOrganisms.addAll(ancestors);
		Collections.sort(allOrganisms, OrganismStoreRecord.COMPARATOR);
		
		boolean parentReplaced = false;
		
		for (int i = 0; i < allOrganisms.size(); i++) {
			OrganismStoreRecord record = allOrganisms.get(i);
			if (i < 2) {
				if (record.getId() == null) {
					store.addRecord(record);
				}
			} else {
				if (record.getId() != null) {
					store.removeRecord(record);
					parentReplaced = true;
				}
			}
		}
		
		store.analyze();
		return parentReplaced;
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
