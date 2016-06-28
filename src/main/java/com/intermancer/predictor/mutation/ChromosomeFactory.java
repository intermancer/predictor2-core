package com.intermancer.predictor.mutation;

import java.util.List;

import com.intermancer.predictor.gene.Chromosome;

public interface ChromosomeFactory {

	Chromosome getChromosome();
	List<Chromosome> getChromosomes();

}
