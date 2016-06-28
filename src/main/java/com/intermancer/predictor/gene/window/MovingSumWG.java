package com.intermancer.predictor.gene.window;

import java.util.List;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;

/**
 * The MovingSumWG calculates the moving sum of the first channel returned by
 * getOffsets().
 * 
 * @author johnfryar
 *
 */
public class MovingSumWG extends AbstractWindowGene {
	
	private double currentSum;
	private Double nextValueToSubtract;
	
	public MovingSumWG() {
		this(MINIMUM_WINDOW);
	}
	
	public MovingSumWG(int windowSize) {
		super(windowSize);
	}

	public MovingSumWG(int offset, int windowSize) {
		super(offset, windowSize);
	}

	@Override
	void windowOperation(Quantum quantum) {
		List<Double[]> windowValues = getWindowValues();
		if(currentSum == 0.0) {
			for(Double[] windowValue : getWindowValues()) {
				currentSum += windowValue[0].doubleValue();
			}
			nextValueToSubtract = getWindowValues().get(0)[0];
		} else {
			currentSum -= nextValueToSubtract;
			nextValueToSubtract = windowValues.get(0)[0];
			currentSum += windowValues.get(windowValues.size() - 1)[0].doubleValue();
		}
		quantum.addChannel(new Channel(this, currentSum));
	}

	@Override
	public void setOffsets(int[] offsets) {
		int[] newOffsets = new int[1];
		newOffsets[0] = offsets[0];
		super.setOffsets(newOffsets);
	}

	@Override
	public void init() {
		super.init();
		nextValueToSubtract = 0.0;
		currentSum = 0.0;
	}

}
