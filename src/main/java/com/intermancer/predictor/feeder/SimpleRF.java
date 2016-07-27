package com.intermancer.predictor.feeder;

import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;

public class SimpleRF extends AbstractReaderFeeder {
	
	public static final String DEFAULT_DATE_FORMAT = "M/d/yyyy";
	private String dateFormat = DEFAULT_DATE_FORMAT;
	private SimpleDateFormat dateParser;

	public SimpleRF() {
		super();
	}

	public SimpleRF(Reader reader) {
		super(reader);
	}

	@Override
	protected Quantum generateQuantum(String dataPoint) {
		StringTokenizer tokenizer = new StringTokenizer(dataPoint);
		String dateString = tokenizer.nextToken();
		Date timestamp = null;
		try {
			timestamp = dateParser.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Quantum quantum = new Quantum();
		quantum.setTimestamp(timestamp);
		quantum.addChannel(new Channel((double) timestamp.getTime()));
		while(tokenizer.hasMoreTokens()) {
			quantum.addChannel(new Channel(Double.parseDouble(tokenizer.nextToken())));
		}
		quantum.setCompressionBoundary();
		return quantum;
	}

	@Override
	public void init() {
		if(dateParser == null) {
			dateParser = new SimpleDateFormat(dateFormat);
		}
		super.init();
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}
