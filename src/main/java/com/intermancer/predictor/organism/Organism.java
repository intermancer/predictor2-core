package com.intermancer.predictor.organism;

import java.util.List;

import com.intermancer.predictor.data.QuantumConsumer;
import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.mutation.Mutable;

/**
 * An Organism is an evolutionary algorithm. It extends both QuantumConsumer and
 * Mutable (as do Chromosome and Gene), as well as having some methods for
 * managing Chromosomes.
 * 
 * @author johnfryar
 * 
 */
public interface Organism extends QuantumConsumer, Mutable {

	/**
	 * Setter for a List of Chromosomes.
	 * 
	 * @param chromosomes
	 */
	void setChromosomes(List<Chromosome> chromosomes);

	/**
	 * Getter for Chromosomes.
	 * 
	 * @return
	 */
	List<Chromosome> getChromosomes();

	/**
	 * Add a single Chromosome to the List.
	 * 
	 * @param chromosome
	 */
	void addChromosome(Chromosome chromosome);

}
