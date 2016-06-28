package com.intermancer.predictor.gene;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.data.QuantumConsumer;

/**
 * This utility class contains some useful static methods.
 * 
 * @author johnfryar
 * 
 */
public class GeneticUtility {

	public static Gene cloneGene(Gene gene) {
		return (Gene) cloneObject(gene);
	}

	private static Object cloneObject(Object object) {
		Object newObject = null;
		try {
			newObject = object.getClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"Could not instantiate object. Possibly missing no-arg constructor. Class: "
							+ object.getClass().getCanonicalName(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"Could not instantiate object. Possibly private no-arg constructor. Class: "
							+ object.getClass().getCanonicalName(), e);
		}
		BeanUtils.copyProperties(object, newObject);
		return newObject;
	}

	public static Chromosome cloneChromosome(Chromosome oldChromosome) {
		Chromosome newChromosome = new Chromosome();
		List<Gene> genes = new ArrayList<Gene>(oldChromosome.getGenes().size());
		for (Gene gene : oldChromosome.getGenes()) {
			genes.add(cloneGene(gene));
		}
		newChromosome.setGenes(genes);
		return newChromosome;
	}

	public static List<Chromosome> cloneChromosomeList(
			List<Chromosome> segmentList) {
		List<Chromosome> newList = new ArrayList<Chromosome>(segmentList.size());
		for (Chromosome segment : segmentList) {
			newList.add(cloneChromosome(segment));
		}
		return newList;
	}

	public static ConsumeResponse consume(Quantum quantum,
			List<? extends QuantumConsumer> consumers) {
		ConsumeResponse response = ConsumeResponse.CONSUME_COMPLETE;
		for (QuantumConsumer consumer : consumers) {
			ConsumeResponse geneResponse = consumer.consume(quantum);
			if (!ConsumeResponse.CONSUME_COMPLETE.equals(geneResponse)) {
				response = geneResponse;
				break;
			}
		}
		return response;
	}

}
