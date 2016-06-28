package com.intermancer.predictor.gene;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.operation.AbstractOperationGene;
import com.intermancer.predictor.gene.operation.AdditionOG;
import com.intermancer.predictor.gene.operation.DivisionOG;
import com.intermancer.predictor.gene.operation.ModulusOG;
import com.intermancer.predictor.gene.operation.MultiplicationOG;
import com.intermancer.predictor.gene.operation.SubtractionOG;
import com.intermancer.predictor.mutation.DefaultMutationAssistant;
import com.intermancer.predictor.mutation.DefaultMutationContext;
import com.intermancer.predictor.mutation.MutationCommand;

public class OperationGeneTest extends QuantumConsumerTest {
	
	@Before
	public void setUp() {
		quantum = new Quantum();
		quantum.addChannel(new Channel(new Double(5.0)));
		quantum.addChannel(new Channel(new Double(8.0)));
	}

	@Test
	public void testMultiplication() {
		testGene = new MultiplicationOG();
		feed();
		assertEquals(new Double(40.0), quantum.getChannel(2).getValue());
	}

	@Test
	public void testDivision() {
		testGene = new DivisionOG();
		feed();
		assertEquals(new Double(0.625), quantum.getChannel(2).getValue());
	}
	
	@Test
	public void testAddition() {
		testGene = new AdditionOG();
		feed();
		assertEquals(new Double(13.0), quantum.getChannel(2).getValue());
	}
	
	@Test
	public void testSubtraction() {
		testGene = new SubtractionOG();
		feed();
		assertEquals(new Double(-3.0), quantum.getChannel(2).getValue());
	}
	
	@Test
	public void testModulus() {
		testGene = new ModulusOG();
		feed();
		assertEquals(new Double(5.0), quantum.getChannel(2).getValue());
	}
	
	@Test
	public void testOffset1MC() {
		DefaultMutationAssistant mutationAssistant = new DefaultMutationAssistant();
		DefaultMutationContext context = new DefaultMutationContext();
		AbstractOperationGene testGene = new MultiplicationOG();
		int offset1BeforeMutation = testGene.getOffsets()[0];
		assertEquals(-2, offset1BeforeMutation);
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		assertEquals(2, mutationCommands.size());
		AbstractGene.GeneOffsetMC offset1Command = (AbstractGene.GeneOffsetMC) extractCommand(mutationCommands, AbstractGene.GeneOffsetMC.class);
		offset1Command.execute(mutationAssistant, context);
		assertFalse(offset1BeforeMutation == testGene.getOffsets()[0]);
	}
	
}
