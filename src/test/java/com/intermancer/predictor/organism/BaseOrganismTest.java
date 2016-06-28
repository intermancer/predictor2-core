package com.intermancer.predictor.organism;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.operation.AdditionOG;
import com.intermancer.predictor.gene.operation.MultiplicationOG;
import com.intermancer.predictor.mutation.DefaultMutationAssistant;
import com.intermancer.predictor.mutation.DefaultMutationContext;
import com.intermancer.predictor.mutation.MutationCommand;

public class BaseOrganismTest extends QuantumConsumerTest {
	
	private DefaultMutationAssistant mutationAssistant;
	private DefaultMutationContext context;
	private Organism organism;
	private List<MutationCommand> mutationCommands;
	
	@Before
	public void setup() {
		mutationAssistant = new DefaultMutationAssistant();
		context = new DefaultMutationContext();
		organism = getSimpleOrganism();
		mutationCommands = organism.assembleMutationCommandList();
	}
	
	@Test
	public void testNewSegmentMC() {
		assertEquals(12, mutationCommands.size());
		MutationCommand addSegmentMC = extractCommand(mutationCommands, BaseOrganism.NewSegmentMC.class);
		addSegmentMC.execute(mutationAssistant, context);
		assertEquals(3, organism.getChromosomes().size());
	}
	
	@Test
	public void testDeleteSegmentMC() {
		MutationCommand deleteSegmentMC = extractCommand(mutationCommands, BaseOrganism.DeleteSegmentMC.class);
		deleteSegmentMC.execute(mutationAssistant, context);
		assertEquals(1, organism.getChromosomes().size());
	}

	private Organism getSimpleOrganism() {
		Organism organism = new BaseOrganism();
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		organism.setChromosomes(chromosomes);

		Chromosome chromosome = new Chromosome();
		chromosome.addGene(new MultiplicationOG());
		chromosomes.add(chromosome);
		
		chromosome = new Chromosome();
		chromosome.addGene(new AdditionOG());
		chromosomes.add(chromosome);
		
		return organism;
	}

}
