package com.intermancer.predictor.feeder;

import java.util.Iterator;
import java.util.List;

import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.evaluator.Evaluator;
import com.intermancer.predictor.organism.Organism;

public interface Feeder {
	
	void feedOrganism();
	void setOrganism(Organism organism);
	Organism getOrganism();
	void init();
	void addFeedCycleListener(FeedCycleListener feedCycleListener);
	void setFeedCycleListeners(List<FeedCycleListener> listeners);
	List<FeedCycleListener> getFeedCycleListeners();
	Evaluator getEvaluator();
	Iterator<Quantum> getIterator();

}
