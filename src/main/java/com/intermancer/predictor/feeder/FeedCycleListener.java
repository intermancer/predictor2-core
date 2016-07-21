package com.intermancer.predictor.feeder;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;

/**
 * FeedCycleListeners are added to a Feeder and are called at the end of every
 * feed cycle.
 * 
 * @author johnfryar
 * 
 */
public interface FeedCycleListener {

	/**
	 * Called from Feeder.init().
	 */
	void init(Feeder feeder);

	/**
	 * Called after a Quantum is fed to an Organism
	 * 
	 * @param consumeResponse 
	 * @param quantum Post-feeding data Quantum.
	 * @return If handle() returns false then all processing is stopped.
	 */
	boolean handle(ConsumeResponse consumeResponse, Quantum quantum);

}
