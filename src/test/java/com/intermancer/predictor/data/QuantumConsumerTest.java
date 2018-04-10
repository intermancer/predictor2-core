package com.intermancer.predictor.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import com.intermancer.predictor.data.Channel;
import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.data.QuantumConsumer;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.constant.AdditionCG;
import com.intermancer.predictor.mutation.MutationCommand;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.OrganismTestUtility;

public class QuantumConsumerTest {

	protected double[] values;
	protected Quantum quantum;
	protected int window = 0;
	public Gene testGene;
	public ConsumeResponse response;

	@Before
	public void setUp() {
		values = new double[200];
		quantum = new Quantum();
	}

	protected int feedValues(int initialIndex, int numberOfQuanta,
			double initialValue, double delta, QuantumConsumer consumer) {
		double val = initialValue;
		for (int i = initialIndex; i < initialIndex + numberOfQuanta; i++) {
			System.out.println("i: " + i + " val: " + val);
			values[i] = val;
			quantum.clearAllChannels();
			quantum.addChannel(new Channel(val));
			ConsumeResponse response = consumer.consume(quantum);
			ConsumeResponse expectedResponse = null;
			int hungry = window - 1;
			if (i < hungry) {
				expectedResponse = ConsumeResponse
						.getHungryResponse(hungry - i);
			} else {
				expectedResponse = ConsumeResponse.CONSUME_COMPLETE;
			}
			assertTrue(expectedResponse.equals(response));
			val += delta;
		}
		return initialIndex + numberOfQuanta;
	}

	protected void feed() {
		response = testGene.consume(quantum);
		assertEquals(ConsumeResponse.CONSUME_COMPLETE, response);
		assertTrue(testGene == quantum.getLastChannel().getSource());
	}

	protected MutationCommand extractCommand(
			List<MutationCommand> mutationCommands,
			Class<? extends MutationCommand> targetClass) {
		for (MutationCommand command : mutationCommands) {
			if (command.getClass().equals(targetClass)) {
				return command;
			}
		}
		return null;
	}

	protected Organism getSimpleOrganism(int constant) {
		List<Gene> genes = new ArrayList<Gene>();
		genes.add(new AdditionCG(constant));
		return OrganismTestUtility.createOrganism(genes);
	}

	protected Organism getSimpleOrganism() {
		return getSimpleOrganism(1);
	}
	
}
