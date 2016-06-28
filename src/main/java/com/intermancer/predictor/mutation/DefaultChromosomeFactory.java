package com.intermancer.predictor.mutation;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.Gene;

public class DefaultChromosomeFactory implements ChromosomeFactory {
	
	GeneFactory geneFactory = new DefaultGeneFactory();

	@Override
	public Chromosome getChromosome() {
		Chromosome chromosome = new Chromosome();
		chromosome.addGene(geneFactory.getGene());
		return chromosome;
	}

	public GeneFactory getGeneFactory() {
		return geneFactory;
	}

	public void setGeneFactory(GeneFactory geneFactory) {
		this.geneFactory = geneFactory;
	}
	
	@Override
	public List<Chromosome> getChromosomes() {
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		if(geneFactory instanceof DefaultGeneFactory) {
			for(Gene gene : ((DefaultGeneFactory)geneFactory).getGenes()) {
				Chromosome chromosome = new Chromosome();
				chromosome.addGene(gene);
				chromosomes.add(chromosome);
			}
		}
		return chromosomes;
	}

}
