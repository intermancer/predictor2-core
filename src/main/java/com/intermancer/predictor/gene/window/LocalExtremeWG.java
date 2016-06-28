package com.intermancer.predictor.gene.window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;

/**
 * Implicitly compares the first channel in the list of 
 * @author johnfryar
 *
 */
public class LocalExtremeWG extends AbstractWindowGene {

	public static final int MINIMUM_SENSITIVITY = 1;
	public static final int MINIMUM_WINDOW = 3;
	public static final int MAXIMUM_HASH_VALUE = 100;
	private int sensitivity = MINIMUM_SENSITIVITY;
	private boolean maximum = true;
	private int extremaOffset = 0;
	private boolean windowInitialized;
	List<Integer> extrema;
	
	public LocalExtremeWG() {
	}

	public LocalExtremeWG(int window, int sensitivity) {
		setWindowSize(window);
		setSensitivity(sensitivity);
	}

	@Override
	void windowOperation(Quantum quantum) {
		if(!windowInitialized) {
			windowInitialized = true;
			initializeWindow();
		} else {
			maintainExtremaIndex();
		}
		addReturnValues(quantum);
	}

	private void addReturnValues(Quantum quantum) {
		if(extrema.size() < extremaOffset + 1) {
			for (int i = 0; i < getOffsets().length; i++) {
				quantum.addChannel(new Channel(this, 0.0));
			}
		} else {
			Double[] values = getWindowValues().get(extrema.get(extremaOffset));
			for(int i = 0; i < values.length; i++) {
				quantum.addChannel(new Channel(this, values[i]));
			}
		}
	}

	private void maintainExtremaIndex() {
		decrementExistingExtremaIndex();
		testForExtremeAndIndex(getWindowValues().size() - getSensitivity() - 1);
	}

	private void decrementExistingExtremaIndex() {
		for(int i = 0; i < getExtrema().size(); i++) {
			Integer newValue = new Integer(getExtrema().get(i).intValue() - 1);
			getExtrema().remove(i);
			getExtrema().add(i, newValue);
		}
		for(Iterator<Integer> extremaItr = getExtrema().iterator(); extremaItr.hasNext(); ) {
			Integer extremum = extremaItr.next();
			if(extremum.intValue() < 0) {
				extremaItr.remove();
			}
		}
	}

	private void initializeWindow() {
		int startingFocus = getSensitivity();
		int guardFocus = getWindowSize() - getSensitivity();
		for (int focus = startingFocus; focus < guardFocus; focus++){
			testForExtremeAndIndex(focus);
		}
	}

	private void testForExtremeAndIndex(int focus) {
		if(isExtremum(focus)) {
			indexExtremum(focus);
		}
	}

	private boolean isExtremum(int focus) {
		boolean isExtreme = true;
		for(int i = 1; i <= getSensitivity(); i++) {
			if(firstArgumentIsMoreExtreme(focus - i, focus) 
					|| firstArgumentIsMoreExtreme(focus + i, focus)) {
				isExtreme = false;
			}
		}
		return isExtreme;
	}

	private boolean firstArgumentIsMoreExtreme(int aIndex, int bIndex) {
		double aValue = getWindowValues().get(aIndex)[0].doubleValue();
		double bValue = getWindowValues().get(bIndex)[0].doubleValue();
		return (((aValue > bValue) && isMaximum())
				|| ((aValue < bValue) && !isMaximum()));
	}

	private void indexExtremum(int focus) {
		int extremaIndex = 0;
		while (extremaIndex < extrema.size()) {
			if (firstArgumentIsMoreExtreme(focus, extrema.get(extremaIndex))) {
				break;
			}
			extremaIndex++;
		}
		extrema.add(extremaIndex, new Integer(focus));
	}

	@Override
	public void init() {
		super.init();
		windowInitialized = false;
		if (getWindowSize() < MINIMUM_WINDOW) {
			setWindowSize(MINIMUM_WINDOW);
		}
		setExtrema( new ArrayList<Integer>(getExtremaOffset() + 1) );
	}

	public int getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
	}

	public boolean isMaximum() {
		return maximum;
	}

	public void setMaximum(boolean maximum) {
		this.maximum = maximum;
	}

	public int getExtremaOffset() {
		return extremaOffset;
	}

	public void setExtremaOffset(int extremaOffset) {
		this.extremaOffset = extremaOffset;
	}

	public List<Integer> getExtrema() {
		return extrema;
	}

	public void setExtrema(List<Integer> extrema) {
		this.extrema = extrema;
	}
	
}
