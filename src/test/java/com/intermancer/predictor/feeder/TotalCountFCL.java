package com.intermancer.predictor.feeder;

import com.intermancer.predictor.audit.Counter;
import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;

public class TotalCountFCL implements FeedCycleListener, Counter {
	
	public int count;

	@Override
	public void init() {
		count = 0;
	}

	@Override
	public boolean handle(ConsumeResponse consumeResponse,
			Quantum quantum) {
		count++;
		return true;
	}

	@Override
	public int getCount() {
		return count;
	}

}
