package com.intermancer.predictor.system;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.evaluator.PredictiveEvaluator;
import com.intermancer.predictor.feeder.BufferedFeeder;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.feeder.SimpleRF;
import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.constant.AdditionCG;
import com.intermancer.predictor.organism.BaseOrganism;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.store.OrganismStore;
import com.intermancer.predictor.organism.store.OrganismStoreRecord;
import com.intermancer.predictor.organism.store.StoreFullException;


public class SystemTest extends QuantumConsumerTest {

	protected static final int WATCH_CHANNEL_OFFSET = 4;
	protected static final int LOOK_AHEAD = 5;
	protected static final String DATA_PATH = "com/intermancer/predictor/test/data/sp500-ascii/GSPC.TXT";

	protected Reader getFileReader(String resourceClasspath) {
		Reader fileReader = null;
		try {
			fileReader = new InputStreamReader(new ClassPathResource(
					resourceClasspath).getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileReader;
	}

	protected Feeder getFeeder() {
		Reader fileReader = getFileReader(DATA_PATH);
		Feeder feeder = new SimpleRF(fileReader);
		feeder = new BufferedFeeder(feeder);
	
		PredictiveEvaluator evaluator = new PredictiveEvaluator();
		feeder.addFeedCycleListener(evaluator);
		
		return feeder;
	}

	protected OrganismStoreRecord getSimpleOrganismStoreRecord(int constant, double score) {
		return new OrganismStoreRecord(score, getSimpleOrganism(constant));
	}

	protected void fillStoreWithSimpleOrganisms(OrganismStore store) throws StoreFullException {
		int constant = 1;
		Organism organism = null;
		while (store.hasCapacity()) {
			organism = getSimpleOrganism(constant);
			OrganismStoreRecord storeRecord = new OrganismStoreRecord(constant, organism);
			store.addRecord(storeRecord);
			constant++;
		}
	}

}
