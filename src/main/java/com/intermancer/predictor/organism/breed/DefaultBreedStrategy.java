package com.intermancer.predictor.organism.breed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.GeneticUtility;
import com.intermancer.predictor.organism.BaseOrganism;
import com.intermancer.predictor.organism.Organism;

public class DefaultBreedStrategy implements BreedStrategy {

	public List<Organism> breed(List<Organism> organisms) {
		if (organisms == null || organisms.size() != 2) {
			throw new BreedMutationException(
					"DefaultBreedStrategy supports the breeding of two and only two organisms");
		}
		if (microbreedingAppropriate(organisms)) {
			return microbreed(organisms);
		}

		Collections.sort(organisms, new Comparator<Organism>() {
			public int compare(Organism o1, Organism o2) {
				return o1.getChromosomes().size()
						- o2.getChromosomes().size();
			}
		});

		List<List<Chromosome>> rearrangedSegments = intermixSegments(organisms);

		return createNewOrganisms(rearrangedSegments);
	}

	private List<List<Chromosome>> intermixSegments(List<Organism> organisms) {
		List<Chromosome> shortOrganismGenes = organisms.get(0)
				.getChromosomes();
		List<Chromosome> longOrganismGenes = organisms.get(1)
				.getChromosomes();
		List<Chromosome> newShortOrganismGenes = new ArrayList<Chromosome>(
				shortOrganismGenes.size());
		List<Chromosome> newLongOrganismGenes = new ArrayList<Chromosome>(
				longOrganismGenes.size());
		boolean exchange = true;
		for (int i = 0; i < longOrganismGenes.size(); i++) {
			if ((i < shortOrganismGenes.size()) && exchange) {
				newShortOrganismGenes.add(GeneticUtility
						.cloneChromosome(longOrganismGenes.get(i)));
				newLongOrganismGenes.add(GeneticUtility
						.cloneChromosome(shortOrganismGenes.get(i)));
			} else if (i < shortOrganismGenes.size()) {
				newShortOrganismGenes.add(GeneticUtility
						.cloneChromosome(shortOrganismGenes.get(i)));
				newLongOrganismGenes.add(GeneticUtility
						.cloneChromosome(longOrganismGenes.get(i)));
			} else {
				newLongOrganismGenes.add(GeneticUtility
						.cloneChromosome(longOrganismGenes.get(i)));
			}
			exchange = !exchange;
		}
		List<List<Chromosome>> listOfLists = new ArrayList<List<Chromosome>>(2);
		listOfLists.add(newShortOrganismGenes);
		listOfLists.add(newLongOrganismGenes);
		return listOfLists;
	}

	private boolean microbreedingAppropriate(List<Organism> organisms) {
		for (Organism organism : organisms) {
			if (organism.getChromosomes().size() > 1) {
				return false;
			}
		}
		return true;
	}

	private List<Organism> microbreed(List<Organism> organisms) {
		Chromosome segment1 = organisms.get(0).getChromosomes().get(0);
		Chromosome segment2 = organisms.get(1).getChromosomes().get(0);
		List<Organism> children = new ArrayList<Organism>(2);
		children.add(createSimpleOrganism(segment1, segment2));
		children.add(createSimpleOrganism(segment2, segment1));
		return children;
	}

	private Organism createSimpleOrganism(Chromosome segment1,
			Chromosome segment2) {
		Chromosome newSegment1 = GeneticUtility.cloneChromosome(segment1);
		Chromosome newSegment2 = GeneticUtility.cloneChromosome(segment2);
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		chromosomes.add(newSegment1);
		chromosomes.add(newSegment2);
		Organism organism = new BaseOrganism();
		organism.setChromosomes(chromosomes);
		return organism;
	}

	private List<Organism> createNewOrganisms(
			List<List<Chromosome>> splitSegments) {
		List<Organism> organisms = new ArrayList<Organism>(2);
		organisms.add(new BaseOrganism(splitSegments.get(0)));
		organisms.add(new BaseOrganism(splitSegments.get(1)));
		return organisms;
	}

}
