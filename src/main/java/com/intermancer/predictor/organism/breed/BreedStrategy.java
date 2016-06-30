package com.intermancer.predictor.organism.breed;

import java.util.List;

import com.intermancer.predictor.organism.Organism;

/**
 * BreedStrategy defines the contract for classes that generate children
 * Organisms derived from parent Organisms. It should be true that all elements
 * of the children are clones of the corresponding elements of the parents. In
 * other words, there must be no shared classes at any point in the object
 * graphs of parents or children.
 * 
 * @author johnfryar
 * 
 */
public interface BreedStrategy {

	/**
	 * Concrete implementations of this method derive a List of Organisms from a
	 * List of Organisms.
	 * 
	 * @param parents
	 *            The Organisms that are used to derive the children
	 * @return A List of Organisms derived from the parents.
	 */
	List<Organism> breed(List<Organism> parents);

}
