package com.intermancer.predictor.feeder;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;

public class PassThroughFeeder extends AbstractFeeder {

	protected static final int ARBITRARY_PASS_THROUGH_LIMIT = 10;
	private int count = 0;

	@Override
	protected boolean generateQuantum() {
		if (count < ARBITRARY_PASS_THROUGH_LIMIT) {
			Quantum quantum = new Quantum();
			Channel channel = new Channel();
			channel.setValue(new Double(count));
			count++;
			quantum.addChannel(channel);
			setQuantum(quantum);
			return true;
		}
		return false;
	}

}
