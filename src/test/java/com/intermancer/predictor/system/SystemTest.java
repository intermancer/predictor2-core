package com.intermancer.predictor.system;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.intermancer.predictor.evaluator.PredictiveEvaluator;
import com.intermancer.predictor.experiment.organism.DefaultOrganismStoreInitializer;
import com.intermancer.predictor.feeder.BufferedFeeder;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.feeder.SimpleRF;
import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.organism.BaseOrganism;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.OrganismRegistryImpl;
import com.intermancer.predictor.organism.OrganismRegistry;


public class SystemTest {

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

	protected Organism createOrganism(List<Gene> genes) {
		BaseOrganism organism = new BaseOrganism();
		
		for(Gene gene : genes) {
			Chromosome chromosome = new Chromosome();
			chromosome.addGene(gene);
			organism.addChromosome(chromosome);
		}
		
		return organism;
	}

}
