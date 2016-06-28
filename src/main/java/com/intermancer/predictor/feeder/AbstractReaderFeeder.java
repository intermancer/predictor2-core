package com.intermancer.predictor.feeder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import com.intermancer.predictor.data.Quantum;

public abstract class AbstractReaderFeeder extends AbstractFeeder {
	
	private Reader reader;
	private BufferedReader dataReader;

	public AbstractReaderFeeder() {
		super();
	}
	
	public AbstractReaderFeeder(Reader reader) {
		this();
		setReader(reader);
	}

	@Override
	protected boolean generateQuantum() {
		try {
			String dataPoint = dataReader.readLine();
			if(dataPoint != null) {
				setQuantum(generateQuantum(dataPoint));
				return true;
			} else {
				setQuantum(null);
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	abstract protected Quantum generateQuantum(String dataPoint);

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
		dataReader = new BufferedReader(reader);
	}

}
