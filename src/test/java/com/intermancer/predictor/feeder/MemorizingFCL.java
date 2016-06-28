package com.intermancer.predictor.feeder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.mutation.MutationException;

/**
 * This object is used to develop and debug some tests.  It memorizes the first few 
 * feeding cycles for an organism.
 * 
 * @author john
 *
 */
public class MemorizingFCL implements FeedCycleListener {
	
	private List<ConsumeResponse> responses;
	private List<Quantum> quanta;
	private int windowSize;
	private int count;

	public MemorizingFCL() {
		super();
	}

	public MemorizingFCL(int windowSize) {
		this();
		this.windowSize = windowSize;
	}

	@Override
	public void init() {
		setCount(0);
		//THIS NEEDS TO CLONE
		setResponses(new ArrayList<ConsumeResponse>(windowSize));
		setQuanta(new ArrayList<Quantum>(windowSize));
	}

	@Override
	public boolean handle(ConsumeResponse consumeResponse,
			Quantum quantum) {
		if(count < windowSize) {
			count++;
			getResponses().add((ConsumeResponse) (cloneObject(consumeResponse)));
			getQuanta().add((Quantum) (cloneObject(quantum)));
		}
		return true;
	}

	private static Object cloneObject(Object object) {
		Object newObject = null;
		try {
			newObject = object.getClass().newInstance();
		} catch (InstantiationException e) {
			throw new MutationException(
					"Could not instantiate object. The class probably lacks a no-arg constructor. Class: "
							+ object.getClass().getCanonicalName(), e);
		} catch (IllegalAccessException e) {
			throw new MutationException(
					"Could not instantiate object.  The class might have a private no-arg constructor.  Class: "
					+ object.getClass().getCanonicalName(), e);
		}
		BeanUtils.copyProperties(object, newObject);
		return newObject;
	}
	
	public List<ConsumeResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<ConsumeResponse> responses) {
		this.responses = responses;
	}

	public List<Quantum> getQuanta() {
		return quanta;
	}

	public void setQuanta(List<Quantum> quanta) {
		this.quanta = quanta;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
