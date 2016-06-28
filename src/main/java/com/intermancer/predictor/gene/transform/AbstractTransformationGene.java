package com.intermancer.predictor.gene.transform;

import java.util.List;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.gene.AbstractGene;
import com.intermancer.predictor.mutation.MutationCommand;

/**
 * TransformationGenes operate on a single Channel.
 * 
 * @author johnfryar
 * 
 */
public abstract class AbstractTransformationGene extends AbstractGene {

	private int[] offsets;
	private ConsumeResponse response = ConsumeResponse.CONSUME_COMPLETE;

	public AbstractTransformationGene() {
		super();
		setOffset(-1);
	}

	public AbstractTransformationGene(int offset) {
		this();
		setOffset(offset);
	}

	public void init() {
		super.init();
	}

	@Override
	public ConsumeResponse consumeInternal(Quantum quantum) {
		quantum.addChannel(new Channel(this, new Double(operation(getValues()[0]))));
		return response;
	}

	@Override
	public List<MutationCommand> assembleMutationCommandList() {
		return super.assembleMutationCommandList();
	}

	abstract protected double operation(double val);

	protected void setResponse(ConsumeResponse response) {
		this.response = response;
	}

	@Override
	public int[] getOffsets() {
		return offsets;
	}
	
	public void setOffset(int offset) {
		if(offsets == null) {
			offsets = new int[1];
		}
		offsets[0] = offset;
	}

}
