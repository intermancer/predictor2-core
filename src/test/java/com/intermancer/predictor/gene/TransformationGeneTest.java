package com.intermancer.predictor.gene;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.transform.AbsoluteValueTG;
import com.intermancer.predictor.gene.transform.CeilingTG;
import com.intermancer.predictor.gene.transform.FilterTG;
import com.intermancer.predictor.gene.transform.FloorTG;
import com.intermancer.predictor.gene.transform.RoundTG;
import com.intermancer.predictor.gene.transform.SignContinuityTG;

public class TransformationGeneTest extends QuantumConsumerTest {

	@Before
	public void setUp() {
		quantum = new Quantum();
		quantum.addChannel(new Channel(new Double(8.5)));
	}
	
	@Test
	public void testFloor() {
		testGene = new FloorTG();
		testGene.init();
		feed();
		assertEquals(new Double(8.0), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(-8.5)));
		feed();
		assertEquals(new Double(-9.0), quantum.getChannel(1).getValue());
	}
	
	@Test
	public void testCeiling() {
		testGene = new CeilingTG();
		testGene.init();
		feed();
		assertEquals(new Double(9.0), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(-8.5)));
		feed();
		assertEquals(new Double(-8.0), quantum.getChannel(1).getValue());
	}
	
	@Test
	public void testAbsoluteValue() {
		testGene = new AbsoluteValueTG();
		testGene.init();
		feed();
		assertEquals(new Double(8.5), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(-8.5)));
		feed();
		assertEquals(new Double(8.5), quantum.getChannel(1).getValue());
	}
	
	@Test
	public void testRound() {
		testGene = new RoundTG();
		testGene.init();
		feed();
		assertEquals(new Double(9.0), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(-8.5)));
		feed();
		assertEquals(new Double(-8.0), quantum.getChannel(1).getValue());
	}
	
	@Test
	public void testSignContinuity() {
		testGene = new SignContinuityTG();
		testGene.init();
		feed();
		assertEquals(new Double(1), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(6.0)));
		feed();
		assertEquals(new Double(2), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(370.02)));
		feed();
		assertEquals(new Double(3), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(-1.0)));
		feed();
		assertEquals(new Double(-1), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(-20.0)));
		feed();
		assertEquals(new Double(-2), quantum.getChannel(1).getValue());
		quantum.clearAllChannels();
		quantum.addChannel(new Channel(new Double(6.0)));
		feed();
		assertEquals(new Double(1), quantum.getChannel(1).getValue());
	}
	
	@Test
	public void testFilter() {
		testGene = new FilterTG();
		testGene.init();
		feed();
		assertEquals(new Double(8.5), quantum.getChannel(1).getValue());
		
		quantum = new Quantum();
		quantum.addChannel(new Channel(new Double(-8.5)));
		feed();
		assertEquals(new Double(0.0), quantum.getChannel(1).getValue());
	}
	
}
