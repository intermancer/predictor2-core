package com.intermancer.predictor.evaluator;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.feeder.Feeder;

public class PredictiveEvaluator implements Evaluator {

	private List<Double> predictiveValueWindow;
	private double compositeScore;

	private int targetOffset = 1;
	private int predictiveOffset = -1;
	private int predictiveWindowSize = 2;
	
	private double trainingValue;
	private double predictedValue;

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
		predictiveValueWindow = new ArrayList<Double>(predictiveWindowSize + 1);
		compositeScore = 0.0;
	}

	@Override
	public boolean handle(ConsumeResponse consumeResponse, Quantum quantum) {
		if (consumeResponse.equals(ConsumeResponse.CONSUME_COMPLETE)) {
			predictiveValueWindow.add(quantum.getChannel(predictiveOffset).getValue());
			if (predictiveWindowSize < predictiveValueWindow.size()) {
				predictedValue = predictiveValueWindow.get(0).doubleValue();
				trainingValue = quantum.getChannel(targetOffset).getValue().doubleValue();
				double absoluteDifference = Math.abs(trainingValue - predictedValue);
				compositeScore += absoluteDifference;
				predictiveValueWindow.remove(0);
			}
		}
		return true;
	}

	@Override
	public double getScore() {
		return compositeScore;
	}

	@Override
	public double getTrainingValue() {
		return trainingValue;
	}

	@Override
	public double getPredictedValue() {
		return predictedValue;
	}

}
