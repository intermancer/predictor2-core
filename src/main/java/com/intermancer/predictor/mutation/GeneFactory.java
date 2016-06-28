package com.intermancer.predictor.mutation;

import com.intermancer.predictor.gene.Gene;

/**
 * GeneFactory is responsible for creating Genes for use during mutation.
 * 
 * @author johnfryar
 * 
 */
public interface GeneFactory {

	Gene getGene();

}
