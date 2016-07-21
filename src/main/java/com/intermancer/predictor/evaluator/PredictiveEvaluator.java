package com.intermancer.predictor.evaluator;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.feeder.Feeder;

public class PredictiveEvaluator implements Evaluator {
	
	private List<Double> predictiveValueWindow;
	private double compositeScore;

	private int targetOffset = 0;
	private int predictiveOffset = -1;
	private int predictiveWindowSize = 1;
	
	public PredictiveEvaluator() {
		
	}

	@Override
	public int getTargetOffset() {
		return targetOffset;
	}
	
	@Override
	public int getEvaluationOffset() {
		return getPredictiveOffset();
	}

	public void setTargetOffset(int targetOffset) {
		this.targetOffset = targetOffset;
	}

	public int getPredictiveOffset() {
		return predictiveOffset;
	}

	public void setPredictiveOffset(int predictiveOffset) {
		this.predictiveOffset = predictiveOffset;
	}

	public int getPredictiveWindowSize() {
		return predictiveWindowSize;
	}

	public void setPredictiveWindowSize(int predictiveWindowSize) {
		this.predictiveWindowSize = predictiveWindowSize;
	}
	
	@Override
	public void init(Feeder feeder) {
		predictiveValueWindow = new ArrayList<Double>();
		compositeScore = 0.0;
	}

	@Override
	public boolean handle(ConsumeResponse consumeResponse, Quantum quantum) {
		if (consumeResponse.equals(ConsumeResponse.CONSUME_COMPLETE)) {
			predictiveValueWindow.add(quantum.getChannel(predictiveOffset).getValue());
			if (predictiveWindowSize < predictiveValueWindow.size()) {
				compositeScore += Math.abs(predictiveValueWindow.get(0).doubleValue()
						- quantum.getChannel(targetOffset).getValue().doubleValue());
			}
		}
		return true;
	}

	@Override
	public double getScore() {
		return compositeScore;
	}

}
