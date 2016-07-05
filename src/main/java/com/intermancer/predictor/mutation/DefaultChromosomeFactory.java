package com.intermancer.predictor.mutation;

import com.intermancer.predictor.gene.Chromosome;

/**
 * This very basic ChromosomeFactory just returns a Chromosome with three random Genes.
 * 
 * @author NolaFryar
 *
 */
public class DefaultChromosomeFactory implements ChromosomeFactory {
	
	GeneFactory geneFactory = new DefaultGeneFactory();

	@Override
	public Chromosome getChromosome() {
		Chromosome chromosome = new Chromosome();
		chromosome.addGene(geneFactory.getGene());
		chromosome.addGene(geneFactory.getGene());
		chromosome.addGene(geneFactory.getGene());
		return chromosome;
	}

	public GeneFactory getGeneFactory() {
		return geneFactory;
	}

	public void setGeneFactory(GeneFactory geneFactory) {
		this.geneFactory = geneFactory;
	}
	
}
