package com.intermancer.predictor.gene;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.window.AbstractWindowGene;
import com.intermancer.predictor.gene.window.DelayWG;
import com.intermancer.predictor.gene.window.LocalExtremeWG;
import com.intermancer.predictor.gene.window.MovingSumWG;
import com.intermancer.predictor.mutation.DefaultMutationAssistant;
import com.intermancer.predictor.mutation.DefaultMutationContext;
import com.intermancer.predictor.mutation.MutationAssistant;
import com.intermancer.predictor.mutation.MutationCommand;
import com.intermancer.predictor.mutation.MutationContext;

public class WindowGeneTest extends QuantumConsumerTest {

	@Test
	public void testDelay() {
		window = 5;
		testGene = new DelayWG(window);
		testGene.init();
		feedValues(0, 6, 1.0, 1.5, testGene);
		assertEquals(new Double(2.5), quantum.getChannel(1).getValue());
	}

	@Test
	public void testMovingSum() {
		window = 5;
		testGene = new MovingSumWG(window);
		testGene.init();
		int index = feedValues(0, window, 1.0, 1.5, testGene);
		assertEquals(new Double(20.0), quantum.getChannel(1).getValue());
		index = feedValues(index, 2, 10.0, 0.0, testGene);
		assertEquals(new Double(36.5), quantum.getChannel(1).getValue());
		index = feedValues(index, 2, -3.0, -1.0, testGene);
		assertEquals(new Double(20.0), quantum.getChannel(1).getValue());
	}

	@Test
	public void testLocalExtremeMaxSen2Win20MaxOff0() {
		window = 20;
		int sensitivity = 2;
		testGene = new LocalExtremeWG(window, sensitivity);
		testGene.init();
		int index = feedValues(0, 10, 50.0, -1.0, testGene);
		assertEquals(new Double(0.0), quantum.getChannel(1).getValue());
		index = feedValues(index, 10, values[index - 1], 2.0, testGene);
		assertEquals(new Double(0.0), quantum.getChannel(1).getValue());
		index = feedValues(index, 1, 50.0, 0.0, testGene);
		assertEquals(new Double(0.0), quantum.getChannel(1).getValue());
		index = feedValues(index, 1, 50.0, 0.0, testGene);
		assertEquals(new Double(59.0), quantum.getChannel(1).getValue());
	}

	@Test
	public void testLocalExtremeMinSen3Win20MaxOff1() {
		window = 20;
		int sensitivity = 3;
		testGene = new LocalExtremeWG(window, sensitivity);
		((LocalExtremeWG) testGene).setMaximum(false);
		((LocalExtremeWG) testGene).setExtremaOffset(1);
		testGene.init();
		int index = feedValues(0, 5, 0.0, 1.0, testGene);
		assertEquals(new Double(0.0), quantum.getChannel(1).getValue());
		index = feedValues(index, 5, 10.0, -1.0, testGene);
		assertEquals(new Double(0.0), quantum.getChannel(1).getValue());
		index = feedValues(index, 5, 5.0, 1.0, testGene);
		assertEquals(new Double(0.0), quantum.getChannel(1).getValue());
		index = feedValues(index, 5, 10.0, -2.0, testGene);
		assertEquals(new Double(0.0), quantum.getChannel(1).getValue());
		index = feedValues(index, 5, 20.0, -3.0, testGene);
		assertEquals(new Double(5.0), quantum.getChannel(1).getValue());
	}
	
	@Test
	public void testSmallWindow() {
		window = 3;
		int sensitivity = 1;
		testGene = new LocalExtremeWG(window, sensitivity);
		testGene.init();
		int index = feedValues(0, 5, 0.0, 1.0, testGene);
		index = feedValues(index, 5, 10.0, -1.0, testGene);
		index = feedValues(index, 5, 5.0, 1.0, testGene);
		index = feedValues(index, 5, 10.0, -2.0, testGene);
	}

	@Test
	public void testWindowSizeMC() {
		int originalWindow = 10;
		AbstractWindowGene testGene = new DelayWG(originalWindow);
		assertEquals(originalWindow, testGene.getWindowSize());
		MutationAssistant mutationAssistant = new DefaultMutationAssistant();
		MutationContext context = new DefaultMutationContext();
		List<MutationCommand> mutationCommands = testGene
				.assembleMutationCommandList();
		MutationCommand windowSizeCommand = extractCommand(mutationCommands,
				AbstractWindowGene.WindowGeneWindowSizeMC.class);
		windowSizeCommand.execute(mutationAssistant, context);
		assertFalse(originalWindow != testGene.getWindowSize());
	}

}
