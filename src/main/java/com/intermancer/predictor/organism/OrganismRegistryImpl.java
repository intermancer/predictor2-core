package com.intermancer.predictor.organism;

import java.util.ArrayList;
import java.util.List;

public class OrganismRegistryImpl implements OrganismRegistry {
	
	private List<Organism> organisms;

	public OrganismRegistryImpl() {
		organisms = new ArrayList<Organism>();
	}
	
	@Override
	public void addOrganism(Organism organism) {
		organisms.add(organism);
	}

	public void setOrganisms(List<Organism> organisms) {
		this.organisms.addAll(organisms);
	}

	@Override
	public List<Organism> getOrganisms() {
		return organisms;
	}

	@Override
	public int getSize() {
		return organisms.size();
	}

}
