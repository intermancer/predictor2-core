package com.intermancer.predictor.organism;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.constant.MultiplicationCG;
import com.intermancer.predictor.gene.window.MovingSumWG;

public class OrganismTestUtility {

	public static Organism getMovingAverage(int windowSize) {
		return getMovingAverage(-1, windowSize);
	}

	public static Organism getMovingAverage(int offset, int windowSize) {
		List<Gene> genes = new ArrayList<Gene>();
		genes.add(new MovingSumWG(offset, windowSize));
		genes.add(new MultiplicationCG(windowSize, false, true));
		return createOrganism(genes);
	}

	public static Organism createOrganism(List<Gene> genes) {
		BaseOrganism organism = new BaseOrganism();
		
		for(Gene gene : genes) {
			Chromosome chromosome = new Chromosome();
			chromosome.addGene(gene);
			organism.addChromosome(chromosome);
		}
		
		return organism;
	}

}
