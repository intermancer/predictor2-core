package com.intermancer.predictor.mutation;

/**
 * A repository for resources for use in mutation.
 * 
 * @author johnfryar
 * 
 */
public interface MutationContext {

	/**
	 * Looks up a particular integer value by name.
	 * 
	 * @param propertyName
	 *            The name of the property to be looked up.
	 * @return Stored value.
	 */
	int getIntegerProperty(String propertyName);

	/**
	 * Looks up an array of Doubles by name.
	 * 
	 * @param propertyName
	 *            The name of the property to be looked up.
	 * @return Stored value array.
	 */
	double[] getDoubleArrayProperty(String propertyName);

	/**
	 * Stores a GeneFactory for use in mutation.
	 * 
	 * @return configured GeneFactory
	 */
	GeneFactory getGeneFactory();
	
	/**
	 * Stores a ChromosomeFactory for use in mutation.
	 * 
	 * @return configured ChromosomeFactory
	 */
	ChromosomeFactory getChromosomeFactory();

}
