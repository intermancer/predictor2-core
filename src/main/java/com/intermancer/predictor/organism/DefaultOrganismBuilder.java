package com.intermancer.predictor.organism;

import java.util.List;

import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.Chromosome;

public class DefaultOrganismBuilder {

	public static Organism getOrganism(List<Gene> genes) {
		BaseOrganism organism = new BaseOrganism();
		if(genes != null) {
			for(Gene gene : genes) {
				Chromosome chromosome = new Chromosome();
				chromosome.addGene(gene);
				organism.addChromosome(chromosome);
			}
		}
		return organism;
	}

}
