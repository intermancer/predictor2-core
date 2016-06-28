package com.intermancer.predictor.feeder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.intermancer.predictor.data.Quantum;

public class BufferedFeeder extends AbstractFeeder {
	
	private Feeder feeder;
	private List<Quantum> data;
	private int index;
	
	public BufferedFeeder() {
		super();
	}
	
	public BufferedFeeder(Feeder feeder) {
		this.feeder = feeder;
		extractFeederData();
	}

	private void extractFeederData() {
		data = new ArrayList<Quantum>();
		feeder.init();
		Iterator<Quantum> feederData =  feeder.getIterator();
		while(feederData.hasNext()) {
			data.add(feederData.next());
		}
	}

	@Override
	protected boolean generateQuantum() {
		if(index == data.size()) {
			return false;
		}
		setQuantum(data.get(index).clone());
		index++;
		return true;
	}

	@Override
	public void init() {
		index = 0;
		super.init();
	}

	public Feeder getFeeder() {
		return feeder;
	}

	public void setFeeder(Feeder feeder) {
		this.feeder = feeder;
		extractFeederData();
	}

}
