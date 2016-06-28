package com.intermancer.predictor.evaluator;

import com.intermancer.predictor.feeder.FeedCycleListener;

/**
 * An Evaluator is a special case of FeedCycleListener. Every Feeder must
 * have at least one Evaluator, and currently only the first one in matters. The
 * Evaluator defines the fitness of our Organisms.
 * 
 * @author johnfryar
 * 
 */
public interface Evaluator extends FeedCycleListener {

	/**
	 * Getter for the score.
	 * 
	 * @return A lower score is better.
	 */
	double getScore();

}
