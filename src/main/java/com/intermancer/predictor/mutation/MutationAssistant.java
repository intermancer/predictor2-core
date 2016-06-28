package com.intermancer.predictor.mutation;

import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.Chromosome;

/**
 * A MutationAssistant is, as the name implies, a helper for mutation
 * activities. Implementations of MutationAssistant can provide subtly different
 * rules for mutation behavior.
 * 
 * It is assumed that a MutationContext has all of the properties expected by
 * the MutationAssistant.
 * 
 * @author johnfryar
 * 
 */
public interface MutationAssistant {

	/**
	 * An integer mutation without any explicit minimum or maximum.
	 * 
	 * @param value
	 *            The value that will be mutated.
	 * @param context
	 *            A repository for resources for use in mutation.
	 * @return The mutated value.
	 */
	int adjustInteger(int value, MutationContext context);

	/**
	 * An integer mutation with an explicit minimum and maximum.
	 * 
	 * @param value
	 *            The value that will be mutated.
	 * @param context
	 *            A repository for resources for use in mutation.
	 * @param min
	 *            Minimum value after mutation.
	 * @param max
	 *            Maximum value after mutation.
	 * @return The mutated value.
	 */
	int adjustInteger(int value, MutationContext context, int min, int max);

	/**
	 * A double adjustment.
	 * 
	 * @param value
	 *            The value that will be mutated.
	 * @param context
	 *            A repository for resources for use in mutation.
	 * @return The adjusted value.
	 */
	double adjustDouble(double value, MutationContext context);

	/**
	 * A source of Genes for use in Chromosome mutation.
	 * 
	 * @param context
	 *            A repository for resources for use in mutation.
	 * @return A pre-configured Gene. Must be a clone.
	 */
	Gene getGene(MutationContext context);

	/**
	 * A source of Chromosomes for use in Organism mutation.
	 * 
	 * @param context
	 *            A repository for resources for use in mutation.
	 * @return A full Chromosome that can be added to an Organism. Must be a
	 *         cloned Chromosome with fully cloned Genes.
	 */
	Chromosome getChromosome(MutationContext context);

}