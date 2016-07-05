package com.intermancer.predictor.gene.logic;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.gene.AbstractGene;

public class SwitchLG extends AbstractGene {

	private int[] offsets;

	public SwitchLG() {
		offsets = new int[3];
		offsets[0] = -1;
		offsets[1] = -2;
		offsets[2] = -3;
	}

	@Override
	public int[] getOffsets() {
		return offsets;
	}

	@Override
	public ConsumeResponse consumeInternal(Quantum quantum) {
		if (quantum.getChannel(offsets[0]).getValue().doubleValue() < 0.0d) {
			quantum.addChannel(new Channel(this, 
					new Double(quantum.getChannel(offsets[1]).getValue().doubleValue())));
		} else {
			quantum.addChannel(new Channel(this, 
					new Double(quantum.getChannel(offsets[2]).getValue().doubleValue())));
		}
		return ConsumeResponse.CONSUME_COMPLETE;
	}

}
