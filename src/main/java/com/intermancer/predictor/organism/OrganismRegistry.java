package com.intermancer.predictor.organism;

import java.util.List;

/**
 * As the name implies, a registry for Organisms.
 * 
 * @author johnfryar
 * 
 */
public interface OrganismRegistry {

	/**
	 * A getter for the registered Organisms.
	 * 
	 * @return
	 */
	List<Organism> getOrganisms();

	/**
	 * Add an Organism to the registry.
	 * 
	 * @param movingAverage
	 */
	void addOrganism(Organism organism);

	/**
	 * 
	 * @return The current number of Organisms registered.
	 */
	int getSize();

}
