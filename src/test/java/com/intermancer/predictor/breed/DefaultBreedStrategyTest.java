package com.intermancer.predictor.breed;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.intermancer.predictor.breed.BreedStrategy;
import com.intermancer.predictor.breed.DefaultBreedStrategy;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.operation.AdditionOG;
import com.intermancer.predictor.gene.operation.MultiplicationOG;
import com.intermancer.predictor.gene.operation.SubtractionOG;
import com.intermancer.predictor.gene.transform.FloorTG;
import com.intermancer.predictor.gene.window.DelayWG;
import com.intermancer.predictor.organism.BaseOrganism;
import com.intermancer.predictor.organism.Organism;

public class DefaultBreedStrategyTest {
	
	@Test
	public void testMicrobreed() {
		List<Organism> organisms = new ArrayList<Organism>();
		Gene gene1 = new AdditionOG();
		Organism organism1 = generateSimpleOrganism(gene1);
		organisms.add(organism1);
		Gene gene2 = new MultiplicationOG();
		Organism organism2 = generateSimpleOrganism(gene2);
		organisms.add(organism2);
		BreedStrategy strategy = new DefaultBreedStrategy();
		List<Organism> children = strategy.breed(organisms);
		
		// Check that everything is the right size.
		assertEquals(2, children.size());
		Organism child1 = children.get(0);
		Organism child2 = children.get(1);

		assertEquals(2, child1.getChromosomes().size());
		assertEquals(2, child2.getChromosomes().size());
		
		// Convenience variables so that the code following is more readable.
		Chromosome segment1a = child1.getChromosomes().get(0);
		Gene gene1a = segment1a.getGenes().get(0);
		Chromosome segment1b = child1.getChromosomes().get(1);
		Gene gene1b = segment1b.getGenes().get(0);
		Chromosome segment2a = child2.getChromosomes().get(0);
		Gene gene2a = segment2a.getGenes().get(0);
		Chromosome segment2b = child2.getChromosomes().get(1);
		Gene gene2b = segment2b.getGenes().get(0);
		
		assertEquals(1, segment1a.getGenes().size());
		assertEquals(1, segment1b.getGenes().size());
		assertEquals(1, segment2a.getGenes().size());
		assertEquals(1, segment2b.getGenes().size());
		
		// Make sure everything is a clone.
		assertTrue(gene1 != gene1a);
		assertTrue(gene1 != gene1b);
		assertTrue(gene1 != gene2a);
		assertTrue(gene1 != gene2b);
		
		assertTrue(gene2 != gene1a);
		assertTrue(gene2 != gene1b);
		assertTrue(gene2 != gene2a);
		assertTrue(gene2 != gene2b);
		
		assertTrue(segment1a != organism1.getChromosomes().get(0));
		assertTrue(segment1b != organism1.getChromosomes().get(0));
		assertTrue(segment2a != organism1.getChromosomes().get(0));
		assertTrue(segment2b != organism1.getChromosomes().get(0));

		assertTrue(segment1a != organism2.getChromosomes().get(0));
		assertTrue(segment1b != organism2.getChromosomes().get(0));
		assertTrue(segment2a != organism2.getChromosomes().get(0));
		assertTrue(segment2b != organism2.getChromosomes().get(0));
	}
	
	@Test
	public void testTwoSizableOrganisms() {
		List<Organism> organisms = new ArrayList<Organism>();
		Organism organism1 = generateComplexOrganism(new AdditionOG(), new MultiplicationOG(), new FloorTG());
		organisms.add(organism1);
		Organism organism2 = generateComplexOrganism(new DelayWG(), new SubtractionOG());
		organisms.add(organism2);
		BreedStrategy strategy = new DefaultBreedStrategy();
		List<Organism> children = strategy.breed(organisms);

		// Check that everything is the right size.
		assertEquals(2, children.size());
		Organism child1 = children.get(0);
		Organism child2 = children.get(1);

		assertEquals(2, child1.getChromosomes().size());
		assertEquals(3, child2.getChromosomes().size());
		
	}

	private Organism generateSimpleOrganism(Gene ... genes) {
		Chromosome chromosome = new Chromosome();
		for(Gene gene : genes) {
			chromosome.addGene(gene);
		}
		Organism organism = new BaseOrganism(chromosome);
		return organism;
	}

	private Organism generateComplexOrganism(Gene ... genes) {
		Organism organism = new BaseOrganism();
		List<Chromosome> chromosomes = new ArrayList<Chromosome>(genes.length);
		for(Gene gene : genes) {
			Chromosome chromosome = new Chromosome();
			chromosome.addGene(gene);
			chromosomes.add(chromosome);
		}
		organism.setChromosomes(chromosomes);
		return organism;
	}

}
