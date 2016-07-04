package com.intermancer.predictor.gene;

import com.intermancer.predictor.data.QuantumConsumer;
import com.intermancer.predictor.mutation.Mutable;

/**
 * A Gene is a step in an algorithm.
 * 
 * @author johnfryar
 * 
 */
public interface Gene extends QuantumConsumer, Mutable {

	/**
	 * Genes can be "hungry," or require a few feeding cycles before they
	 * produce meaningful data. Generally speaking, for hunger purposes, Genes
	 * are treated linearly. In other words, as an Organism is fed data, if any
	 * Gene returns a hungry response, that feed cycle stops, the
	 * FeedCycleListeners are called, and the next feed cycle is started.
	 * 
	 * @return The number of feed cycles before this Gene would be able to
	 *         produce meaningful data.
	 */
	int getHungryCycles();
	String getClassname();

}
