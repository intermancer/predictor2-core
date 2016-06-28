package com.intermancer.predictor.gene;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.constant.AbstractConstantGene;
import com.intermancer.predictor.gene.constant.AdditionCG;
import com.intermancer.predictor.gene.constant.ModulusCG;
import com.intermancer.predictor.gene.constant.MultiplicationCG;
import com.intermancer.predictor.mutation.DefaultMutationAssistant;
import com.intermancer.predictor.mutation.DefaultMutationContext;
import com.intermancer.predictor.mutation.MutationAssistant;
import com.intermancer.predictor.mutation.MutationCommand;
import com.intermancer.predictor.mutation.MutationContext;

public class ConstantGeneTest extends QuantumConsumerTest {

	@Before
	public void setUp() {
		quantum = new Quantum();
		quantum.addChannel(new Channel(new Double(8.0)));
	}

	@Test
	public void testAddition() {
		testGene = new AdditionCG(2.8);
		testGene.init();
		feed();
		assertEquals(new Double(10.8), quantum.getChannel(1).getValue());
	}

	@Test
	public void testMultiplication() {
		testGene = new MultiplicationCG(3.2);
		((AbstractConstantGene) testGene).setNegative(true);
		testGene.init();
		feed();
		assertEquals(new Double(-25.6), quantum.getChannel(1).getValue());
	}

	@Test
	public void testDivision() {
		testGene = new MultiplicationCG(2.5);
		((AbstractConstantGene) testGene).setInverse(true);
		testGene.init();
		feed();
		assertEquals(new Double(3.2), quantum.getChannel(1).getValue());
	}

	@Test
	public void testSubtraction() {
		testGene = new AdditionCG(4.7);
		((AbstractConstantGene) testGene).setNegative(true);
		testGene.init();
		feed();
		assertEquals(new Double(3.3), quantum.getChannel(1).getValue());
	}

	@Test
	public void testModulus() {
		testGene = new ModulusCG(2.5);
		testGene.init();
		((AbstractConstantGene) testGene).setNegative(true);
		feed();
		assertEquals(new Double(0.5), quantum.getChannel(1).getValue());
	}

	@Test
	public void testGetMutationList() {
		testGene = new AdditionCG(2.5);
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		// MutationCommands: offset, constant, inverse, negative
		assertEquals(4, mutationCommands.size());
		assertNotNull(extractCommand(mutationCommands, AbstractGene.GeneOffsetMC.class));
		assertNotNull(extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneConstantMC.class));
		assertNotNull(extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneInverseMC.class));
		assertNotNull(extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneNegativeMC.class));
	}
	
	@Test
	public void testOffsetMC() {
		AdditionCG testGene = new AdditionCG(2.5);
		assertEquals(-1, testGene.getOffsets()[0]); 
		MutationAssistant mutationAssistant = new DefaultMutationAssistant();
		MutationContext context = new DefaultMutationContext();
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		MutationCommand offsetCommand = extractCommand(mutationCommands, AbstractGene.GeneOffsetMC.class);
		offsetCommand.execute(mutationAssistant, context);
		assertFalse(-1 == testGene.getOffsets()[0]);
	}
	
	@Test
	public void testConstantMCInMiddleOfArray() {
		double originalValue = 2.5;
		AdditionCG testGene = new AdditionCG(originalValue);
		assertEquals(originalValue, testGene.getConstant(), 1e-15); 
		DefaultMutationAssistant mutationAssistant = new DefaultMutationAssistant();
		DefaultMutationContext context = new DefaultMutationContext();
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		MutationCommand constantCommand = extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneConstantMC.class);
		constantCommand.execute(mutationAssistant, context);
		double constantAfterFirstMutation = testGene.getConstant();
		assertTrue(constantAfterFirstMutation != originalValue);
		double[] possibleValues = context.getDoubleArrayProperty(DefaultMutationContext.DEFAULT_CONSTANT_ARRAY_KEY);
		int bucket = Arrays.binarySearch(possibleValues, constantAfterFirstMutation);
		assertTrue(constantAfterFirstMutation == possibleValues[bucket]);
	}

	@Test
	public void testConstantMCBeforeBeginningOfArray() {
		DefaultMutationAssistant mutationAssistant = new DefaultMutationAssistant();
		DefaultMutationContext context = new DefaultMutationContext();
		double[] possibleValues = context.getDoubleArrayProperty(DefaultMutationContext.DEFAULT_CONSTANT_ARRAY_KEY);
		double originalValue = possibleValues[0] / 2.0;
		AdditionCG testGene = new AdditionCG(originalValue);
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		MutationCommand constantCommand = extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneConstantMC.class);
		constantCommand.execute(mutationAssistant, context);
		double constantAfterFirstMutation = testGene.getConstant();
		assertEquals(possibleValues[0], constantAfterFirstMutation, 1e-15);
	}

	@Test
	public void testConstantMCAtBeginningOfArray() {
		DefaultMutationAssistant mutationAssistant = new DefaultMutationAssistant();
		DefaultMutationContext context = new DefaultMutationContext();
		double[] possibleValues = context.getDoubleArrayProperty(DefaultMutationContext.DEFAULT_CONSTANT_ARRAY_KEY);
		double originalValue = possibleValues[0];
		AdditionCG testGene = new AdditionCG(originalValue);
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		MutationCommand constantCommand = extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneConstantMC.class);
		constantCommand.execute(mutationAssistant, context);
		double constantAfterFirstMutation = testGene.getConstant();
		assertEquals(possibleValues[1], constantAfterFirstMutation, 1e-15);
	}

	@Test
	public void testConstantMCAfterEndOfArray() {
		DefaultMutationAssistant mutationAssistant = new DefaultMutationAssistant();
		DefaultMutationContext context = new DefaultMutationContext();
		double[] possibleValues = context.getDoubleArrayProperty(DefaultMutationContext.DEFAULT_CONSTANT_ARRAY_KEY);
		double originalValue = possibleValues[possibleValues.length - 1] + 1.0;
		AdditionCG testGene = new AdditionCG(originalValue);
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		MutationCommand constantCommand = extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneConstantMC.class);
		constantCommand.execute(mutationAssistant, context);
		double constantAfterFirstMutation = testGene.getConstant();
		assertEquals(possibleValues[possibleValues.length - 1], constantAfterFirstMutation, 1e-15);
	}

	@Test
	public void testConstantMCAtEndOfArray() {
		DefaultMutationAssistant mutationAssistant = new DefaultMutationAssistant();
		DefaultMutationContext context = new DefaultMutationContext();
		double[] possibleValues = context.getDoubleArrayProperty(DefaultMutationContext.DEFAULT_CONSTANT_ARRAY_KEY);
		double originalValue = possibleValues[possibleValues.length - 1];
		AdditionCG testGene = new AdditionCG(originalValue);
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		MutationCommand constantCommand = extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneConstantMC.class);
		constantCommand.execute(mutationAssistant, context);
		double constantAfterFirstMutation = testGene.getConstant();
		assertEquals(possibleValues[possibleValues.length - 2], constantAfterFirstMutation, 1e-15);
	}
	
	@Test
	public void testInverseMC() {
		DefaultMutationAssistant mutationAssistant = new DefaultMutationAssistant();
		DefaultMutationContext context = new DefaultMutationContext();
		AdditionCG testGene = new AdditionCG(2.5);
		boolean inverse = testGene.isInverse();
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		MutationCommand constantCommand = extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneInverseMC.class);
		constantCommand.execute(mutationAssistant, context);
		boolean inverseAfterMutation = testGene.isInverse();
		assertFalse(inverse == inverseAfterMutation);
		constantCommand.execute(mutationAssistant, context);
		inverseAfterMutation = testGene.isInverse();
		assertTrue(inverse == inverseAfterMutation);
	}

	@Test
	public void testNegativeMC() {
		DefaultMutationAssistant mutationAssistant = new DefaultMutationAssistant();
		DefaultMutationContext context = new DefaultMutationContext();
		AdditionCG testGene = new AdditionCG(2.5);
		boolean negative = testGene.isNegative();
		List<MutationCommand> mutationCommands = testGene.assembleMutationCommandList();
		MutationCommand constantCommand = extractCommand(mutationCommands, AbstractConstantGene.ConstantGeneNegativeMC.class);
		constantCommand.execute(mutationAssistant, context);
		boolean negativeAfterMutation = testGene.isNegative();
		assertFalse(negative == negativeAfterMutation);
		constantCommand.execute(mutationAssistant, context);
		negativeAfterMutation = testGene.isNegative();
		assertTrue(negative == negativeAfterMutation);
	}
	
	@Test
	public void testEqualsFailsOnDifferentClass() {
		Gene gene1 = new MultiplicationCG(2.0);
		Gene gene2 = new AdditionCG(2.0);
		assertFalse(gene1.equals(gene2));
	}
	
	@Test
	public void testEqualsFailsOnDifferentOffset() {
		AbstractGene gene1 = new MultiplicationCG(2.0);
		gene1.getOffsets()[0] = 1;
		AbstractGene gene2 = new MultiplicationCG(2.0);
		assertFalse(gene1.equals(gene2));
	}
	
	@Test
	public void testEqualsFailsOnDifferentConstant() {
		AbstractConstantGene gene1 = new MultiplicationCG(2.0);
		AbstractConstantGene gene2 = new MultiplicationCG(2.5);
		assertFalse(gene1.equals(gene2));
	}
	
	@Test
	public void testSettingHungryCyclesToGetJacksonToWork() {
		AdditionCG gene = new AdditionCG();
		gene.setHungryCycles(1);
	}
	
}
