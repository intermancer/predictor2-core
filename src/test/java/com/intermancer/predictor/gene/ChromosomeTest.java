package com.intermancer.predictor.gene;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.constant.AdditionCG;
import com.intermancer.predictor.gene.constant.MultiplicationCG;
import com.intermancer.predictor.gene.operation.AdditionOG;
import com.intermancer.predictor.gene.operation.MultiplicationOG;
import com.intermancer.predictor.mutation.DefaultMutationAssistant;
import com.intermancer.predictor.mutation.DefaultMutationContext;
import com.intermancer.predictor.mutation.MutationAssistant;
import com.intermancer.predictor.mutation.MutationCommand;
import com.intermancer.predictor.mutation.MutationContext;

public class ChromosomeTest extends QuantumConsumerTest {
	
	private Chromosome chromosome;
	private MutationAssistant mutationAssistant;
	private MutationContext context;
	private List<MutationCommand> mutationCommands;
	
	@Before
	public void setup() {
		chromosome = createSimpleChromosome();
		mutationAssistant = new DefaultMutationAssistant();
		context = new DefaultMutationContext();
		mutationCommands = chromosome.assembleMutationCommandList();
	}

	@Test
	public void testAddGene() {
		Chromosome chromosome = new Chromosome();
		chromosome.addGene(new MultiplicationCG(2.0));
		chromosome.init();
		List<Gene> genes = chromosome.getGenes();
		assertNotNull(genes);
		assertTrue(genes.size() == 1);
		assertTrue(genes.get(0) instanceof MultiplicationCG);
		chromosome.addGene(new AdditionCG(2.0));
		genes = chromosome.getGenes();
		assertNotNull(genes);
		assertTrue(genes.size() == 2);
		assertTrue(genes.get(0) instanceof MultiplicationCG);
		assertTrue(genes.get(1) instanceof AdditionCG);
	}
	
	@Test
	public void testAddGenes() {
		Chromosome chromosome = new Chromosome();
		List<Gene> genes = new ArrayList<Gene>();
		genes.add(new MultiplicationCG(2.0));
		genes.add(new AdditionCG(2.0));
		chromosome.setGenes(genes);
		assertEquals(2, chromosome.getGenes().size());
	}
	
	@Test
	public void testNewGeneMC() {
		MutationCommand addGeneMC = extractCommand(mutationCommands, Chromosome.NewGeneMC.class);
		addGeneMC.execute(mutationAssistant, context);
		assertEquals(3, chromosome.getGenes().size());
		chromosome.init();
		assertTrue(chromosome.getGenes().get(0) instanceof MultiplicationOG);
	}

	@Test
	public void testReplacementGeneMC() {
		Gene oldGene1 = chromosome.getGenes().get(0);
		Gene oldGene2 = chromosome.getGenes().get(1);
		chromosome.init();
		MutationCommand replaceGeneMC = extractCommand(mutationCommands, Chromosome.ReplacementGeneMC.class);
		replaceGeneMC.execute(mutationAssistant, context);
		assertEquals(2, chromosome.getGenes().size());
		boolean gene1Same = oldGene1 == chromosome.getGenes().get(0);
		boolean gene2Same = oldGene2 == chromosome.getGenes().get(1);
		assertTrue(gene1Same ^ gene2Same);
	}
	
	@Test
	public void testDeleteGeneMC() {
		MutationCommand deleteGeneMC = extractCommand(mutationCommands, Chromosome.DeleteGeneMC.class);
		deleteGeneMC.execute(mutationAssistant, context);
		assertEquals(2, chromosome.getGenes().size());
	}
	
	private Chromosome createSimpleChromosome() {
		Chromosome chromosome = new Chromosome();
		chromosome.addGene(new MultiplicationOG());
		chromosome.addGene(new AdditionOG());
		return chromosome;
	}
	
}
