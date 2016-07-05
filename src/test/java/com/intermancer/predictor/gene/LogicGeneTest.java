package com.intermancer.predictor.gene;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.logic.SwitchLG;

public class LogicGeneTest extends QuantumConsumerTest {

	@Test
	public void testSwitchLG() {
		testGene = new SwitchLG();
		testGene.init();

		quantum = new Quantum();
		quantum.addChannel(new Channel(new Double(8.0)));
		quantum.addChannel(new Channel(new Double(4.0)));
		quantum.addChannel(new Channel(new Double(6.0)));
		
		feed();
		assertEquals(new Double(8.0), quantum.getChannel(3).getValue());

		quantum = new Quantum();
		quantum.addChannel(new Channel(new Double(6.0)));
		quantum.addChannel(new Channel(new Double(-2.0)));
		quantum.addChannel(new Channel(new Double(-3.0)));
		
		feed();
		assertEquals(new Double(-2.0), quantum.getChannel(3).getValue());

	}

}
