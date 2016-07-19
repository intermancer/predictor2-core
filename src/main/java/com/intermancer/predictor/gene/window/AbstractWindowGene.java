package com.intermancer.predictor.gene.window;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.gene.AbstractGene;
import com.intermancer.predictor.mutation.MutationAssistant;
import com.intermancer.predictor.mutation.MutationCommand;
import com.intermancer.predictor.mutation.MutationContext;
//import com.intermancer.predictor.mutation.MutationUtility;

/**
 * Window Genes keep track of a moving set of data from the input Channel.
 * 
 * @author johnfryar
 * 
 */
public abstract class AbstractWindowGene extends AbstractGene {

	public class WindowGeneWindowSizeMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			windowSize = mutationAssistant.adjustInteger(windowSize, context,
					MINIMUM_WINDOW, MAXIMUM_WINDOW);
		}

	}
	
//	public class WindowGeneNewOffsetMC implements MutationCommand {
//
//		@Override
//		public void execute(MutationAssistant mutationAssistant,
//				MutationContext context) {
//			int newLength = getOffsets().length + 1;
//			int[] newOffsets = new int[newLength];
//			int i = 0;
//			for(int offset : getOffsets()) {
//				newOffsets[i] = offset;
//				i++;
//			}
//			newOffsets[newLength -1] = MutationUtility.getDiceroll(newLength) - 1;
//			setOffsets(newOffsets);
//		}
//		
//	}

	public static final int MINIMUM_WINDOW = 1;
	public static final int MAXIMUM_WINDOW = 100;
	protected List<Double[]> windowValues;
	private int windowSize;
	protected int windowIndex = 0;
	
	private int[] offsets;

	public AbstractWindowGene() {
		this(1);
	}

	public AbstractWindowGene(int windowSize) {
		this(-1, windowSize);
	}

	public AbstractWindowGene(int offset, int windowSize) {
		super();
		setWindowSize(windowSize);
		offsets = new int[1];
		offsets[0] = offset;
	}

	@Override
	public ConsumeResponse consumeInternal(Quantum quantum) {
		getWindowValues().add(getValues());
		
		ConsumeResponse response = null;
		if (getWindowValues().size() > getWindowSize()) {
			getWindowValues().remove(0);
		}
		if (getWindowValues().size() == getWindowSize()) {
			response = ConsumeResponse.CONSUME_COMPLETE;
			windowOperation(quantum);
		} else {
			int hungryRemaining = windowSize - getWindowValues().size();
			response = ConsumeResponse.getHungryResponse(hungryRemaining);
			quantum.addChannel(new Channel(this, 0.0));
		}
	
		return response;
	}
	
	@Override
	public void init() {
		super.init();
		windowValues = new ArrayList<Double[]>(windowSize + 1);
	}

	abstract void windowOperation(Quantum quantum);
	
	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		if (windowSize >= MINIMUM_WINDOW) {
			this.windowSize = windowSize;
		} else {
			this.windowSize = MINIMUM_WINDOW;
		}
	}

	@Override
	public int getHungryCycles() {
		return getWindowSize();
	}

	@Override
	public List<MutationCommand> assembleMutationCommandList() {
		List<MutationCommand> commands = super.assembleMutationCommandList();
		commands.add(new WindowGeneWindowSizeMC());
//		commands.add(new WindowGeneNewOffsetMC());
		return commands;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			AbstractWindowGene other = (AbstractWindowGene) obj;
			return windowSize == other.windowSize;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode += windowSize;
		return hashCode;
	}

	public int[] getOffsets() {
		return offsets;
	}

	public void setOffsets(int[] offsets) {
		this.offsets = offsets;
	}

	public List<Double[]> getWindowValues() {
		return windowValues;
	}

}
