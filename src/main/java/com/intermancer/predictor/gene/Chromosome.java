package com.intermancer.predictor.gene;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.data.QuantumConsumer;
import com.intermancer.predictor.mutation.Mutable;
import com.intermancer.predictor.mutation.MutationAssistant;
import com.intermancer.predictor.mutation.MutationCommand;
import com.intermancer.predictor.mutation.MutationContext;
import com.intermancer.predictor.mutation.MutationUtility;

/**
 * An Organism is composed of Chromosomes. Chromosomes facilitate breeding and
 * mutation. They also play a role in data compression, and, theoretically, help
 * control "cancer" or runaway wasted genetic material.
 * 
 * Chromosomes facilitate breeding by providing a natural mechanism for swapping
 * genetic material between parent organisms.
 * 
 * Organisms mutate by adding or subtracting a Chromosome. A Chromosome can
 * mutate by adding, subtracting, or replacing a Gene.
 * 
 * The last Gene in a Chromosome is a CompressionGene. If there is no
 * CompressionGene in a Chromosome then one is inserted at the end of the chain
 * when init() is called. There are a few reasons to do this, but it remains to
 * be seen if experimentation shows them to be valid.
 * 
 * @author johnfryar
 * 
 */
public class Chromosome implements QuantumConsumer, Mutable {

	public class NewGeneMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			addGene(mutationAssistant.getGene(context));
		}

	}

	public class ReplacementGeneMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			int upperIndex = genes.size() - 1;
			if (upperIndex > -1) {
				int switchedGeneIndex = MutationUtility.getDiceroll(upperIndex) - 1;
				genes.remove(switchedGeneIndex);
				genes
						.add(switchedGeneIndex, mutationAssistant
								.getGene(context));
			}
		}

	}

	public class DeleteGeneMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			if (genes.size() > 2) {
				int indexToRemove = MutationUtility
						.getDiceroll(genes.size() - 1) - 1;
				genes.remove(indexToRemove);
			}
		}

	}

	private List<Gene> genes;

	public Chromosome() {
		genes = new ArrayList<Gene>();
	}

	public List<Gene> getGenes() {
		return genes;
	}

	public void setGenes(List<Gene> genes) {
		if (genes != null) {
			this.genes = genes;
		}
	}

	public void addGene(Gene gene) {
		assert (genes != null);
		genes.add(gene);
	}

	public List<MutationCommand> assembleMutationCommandList() {
		List<MutationCommand> commands = new ArrayList<MutationCommand>();
		for (Gene gene : genes) {
			commands.addAll(gene.assembleMutationCommandList());
		}
		commands.add(new NewGeneMC());
		commands.add(new ReplacementGeneMC());
		commands.add(new DeleteGeneMC());
		return commands;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Chromosome) {

			Chromosome other = (Chromosome) obj;
			if (genes.size() != other.genes.size()) {
				return false;
			}

			boolean equal = true;
			for (int i = 0; i < genes.size(); i++) {
				if (!genes.get(i).equals(other.genes.get(i))) {
					equal = false;
					break;
				}
			}
			return equal;

		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (Gene gene : genes) {
			hashCode += gene.hashCode();
		}
		return hashCode;
	}

	@Override
	public ConsumeResponse consume(Quantum quantum) {
		ConsumeResponse returnResponse = GeneticUtility.consume(quantum, genes);
		quantum.compress();
		return returnResponse;
	}

	@Override
	public void init() {
		for (Gene gene : genes) {
			gene.init();
		}
	}

}
