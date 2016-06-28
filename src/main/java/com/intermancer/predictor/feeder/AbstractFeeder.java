package com.intermancer.predictor.feeder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.evaluator.Evaluator;
import com.intermancer.predictor.organism.Organism;

public abstract class AbstractFeeder implements Feeder {

	private static final FeedCycleListener DEFAULT_FEED_CYCLE_LISTENER = new FeedCycleListener() {
		public boolean handle(ConsumeResponse consume, Quantum quantum) {
			return true;
		}

		public void init() {
		}
	};

	private Organism organism;
	private Quantum quantum;
	private List<FeedCycleListener> feedCycleListeners;
	private boolean quantumAvailable;

	public AbstractFeeder() {
		feedCycleListeners = new ArrayList<FeedCycleListener>();
	}

	public void feedOrganism() {
		boolean responseContinue = true;
		while (quantumAvailable && responseContinue) {
			ConsumeResponse response = organism.consume(quantum);
			for (FeedCycleListener listener : feedCycleListeners) {
				responseContinue = responseContinue
						&& listener.handle(response, quantum);
			}
			quantumAvailable = generateQuantum();
		}
	}

	abstract protected boolean generateQuantum();

	public Organism getOrganism() {
		return organism;
	}

	public void setOrganism(Organism organism) {
		this.organism = organism;
	}

	public void init() {
		if(organism != null) {
			organism.init();
		}

		if (feedCycleListeners == null || feedCycleListeners.isEmpty()) {
			addFeedCycleListener(DEFAULT_FEED_CYCLE_LISTENER);
		} else {
			for(FeedCycleListener processor : feedCycleListeners) {
				processor.init();
			}
		}
		
		quantumAvailable = generateQuantum();
	}
	
	public Evaluator getEvaluator() {
		Evaluator evaluator = null;
		if((feedCycleListeners != null) && !(feedCycleListeners.isEmpty())) {
			for(FeedCycleListener listener : feedCycleListeners) {
				if(listener instanceof Evaluator) {
					evaluator = (Evaluator) listener;
					break;
				}
			}
		}
		return evaluator;
	}

	public List<FeedCycleListener> getFeedCycleListeners() {
		return feedCycleListeners;
	}

	@Override
	public void addFeedCycleListener(FeedCycleListener feedCycleListener) {
		feedCycleListeners.add(feedCycleListener);
	}
	
	@Override
	public void setFeedCycleListeners(List<FeedCycleListener> feedCycleListeners) {
		this.feedCycleListeners = feedCycleListeners;
	}

	@Override
	public Iterator<Quantum> getIterator() {
		return new Iterator<Quantum>() {
			
			@Override
			public boolean hasNext() {
				return quantumAvailable;
			}
	
			@Override
			public Quantum next() {
				Quantum returnQuantum = quantum;
				quantumAvailable = generateQuantum();
				return returnQuantum;
			}
	
			@Override
			public void remove() {
				// Nothing.
			}
			
		};
	}

	protected void setQuantum(Quantum quantum) {
		this.quantum = quantum;
	}

}
