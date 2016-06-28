package com.intermancer.predictor.gene.operation;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.gene.AbstractGene;

/**
 * OperationGenes operate two data Channels together in some way.
 * 
 * @author johnfryar
 * 
 */
public abstract class AbstractOperationGene extends AbstractGene {

	private int[] offsets;
	
	public AbstractOperationGene() {
		offsets = new int[2];
		offsets[0] = -2;
		offsets[1] = -1;
	}
	
	public AbstractOperationGene(int offset1, int offset2) {
		this();
		offsets[0] = offset1;
		offsets[1] = offset2;
	}

	public ConsumeResponse consumeInternal(Quantum quantum) {
		quantum
				.addChannel(new Channel(this, new Double(operation(getValues()[0], getValues()[1]))));
		return ConsumeResponse.CONSUME_COMPLETE;
	}

	abstract double operation(double val1, double val2);

	public void init() {
		// nothing.
	}
	
	@Override
	public int[] getOffsets() {
		return offsets;
	}

}
