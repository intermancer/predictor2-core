package com.intermancer.predictor.mutation;

import java.util.HashMap;
import java.util.Map;

public class DefaultMutationContext implements MutationContext {
	
	public static final String DEFAULT_CONSTANT_ARRAY_KEY = "default.constant.array";
	private static final double[] DEFAULT_CONSTANT_ARRAY = {1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 2.0, 2.5, 3.0, 3.5, 7.0, 14.0};
	public static final String INT_UP_THRESHOLD_KEY = "int.up";
	public static final String CONSTANT_UP_THRESHOLD_KEY = "constant.up";
	public static final String CHANNEL_OFFSET_UP_THRESHOLD_KEY = "channel.offset.up";
	private static final Integer FIFTY_FIFTY = new Integer(50); 

	private Map<String,Integer> thresholds;
	private Map<String,double[]> constantArrays;
	
	private GeneFactory geneFactory;
	private ChromosomeFactory chromosomeFactory;

	public DefaultMutationContext() {
		thresholds = new HashMap<String, Integer>();
		thresholds.put(DefaultMutationContext.CHANNEL_OFFSET_UP_THRESHOLD_KEY, FIFTY_FIFTY);
		thresholds.put(DefaultMutationContext.CONSTANT_UP_THRESHOLD_KEY, FIFTY_FIFTY);
		thresholds.put(DefaultMutationContext.INT_UP_THRESHOLD_KEY, FIFTY_FIFTY);
		
		constantArrays = new HashMap<String, double[]>();
		constantArrays.put(DEFAULT_CONSTANT_ARRAY_KEY, DEFAULT_CONSTANT_ARRAY);
		
		geneFactory = new DefaultGeneFactory();
		chromosomeFactory = new DefaultChromosomeFactory();
	}

	public int getIntegerProperty(String propertyName) {
		Integer value = thresholds.get(propertyName);
		if(value != null) {
			return value.intValue();
		}
		return 0;
	}
	
	public double[] getDoubleArrayProperty(String propertyName) {
		return constantArrays.get(propertyName);
	}

	public GeneFactory getGeneFactory() {
		return geneFactory;
	}

	public void setGeneFactory(GeneFactory geneFactory) {
		this.geneFactory = geneFactory;
	}

	public ChromosomeFactory getChromosomeFactory() {
		return chromosomeFactory;
	}

	public void setChromosomeFactory(ChromosomeFactory chromosomeFactory) {
		this.chromosomeFactory = chromosomeFactory;
	}

}
