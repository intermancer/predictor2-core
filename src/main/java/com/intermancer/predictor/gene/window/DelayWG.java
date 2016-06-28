package com.intermancer.predictor.gene.window;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;

public class DelayWG extends AbstractWindowGene {

	public static final int DEFAULT_WINDOW_SIZE = 1;

	public DelayWG() {
		this(DEFAULT_WINDOW_SIZE);
	}

	public DelayWG(int windowSize) {
		super(windowSize);
	}

	@Override
	void windowOperation(Quantum quantum) {
		quantum.addChannel(new Channel(this, getWindowValues().get(0)[0]
				.doubleValue()));
	}

	@Override
	public void setOffsets(int[] offsets) {
		int[] newOffsets = new int[1];
		newOffsets[0] = offsets[0];
		super.setOffsets(newOffsets);
	}

}
