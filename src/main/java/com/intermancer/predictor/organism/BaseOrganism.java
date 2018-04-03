package com.intermancer.predictor.organism;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.GeneticUtility;
import com.intermancer.predictor.mutation.MutationAssistant;
import com.intermancer.predictor.mutation.MutationCommand;
import com.intermancer.predictor.mutation.MutationContext;
import com.intermancer.predictor.mutation.MutationUtility;

public class BaseOrganism implements Organism {
	
	String id;
	
	public class NewSegmentMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			chromosomes.add(mutationAssistant.getChromosome(context));
		}
		
	}
	
	public class DeleteSegmentMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			if(chromosomes.size() > 1) {
				int indexToRemove = MutationUtility.getDiceroll(chromosomes.size()) - 1;
				chromosomes.remove(indexToRemove);
			}
		}
		
	}

	protected List<Chromosome> chromosomes;

	public BaseOrganism() {
		super();
		chromosomes = new ArrayList<Chromosome>();
	}

	public BaseOrganism(List<Chromosome> segments) {
		this();
		this.chromosomes = segments;
	}

	public BaseOrganism(Chromosome chromosome) {
		this();
		addChromosome(chromosome);
	}

	@Override
	public ConsumeResponse consume(Quantum quantum) {
		return GeneticUtility.consume(quantum, chromosomes);
	}

	@Override
	public List<Chromosome> getChromosomes() {
		return chromosomes;
	}
	
	@Override
	public void setChromosomes(List<Chromosome> chromosomes) {
		this.chromosomes = chromosomes;
	}
	
	@Override
	public void addChromosome(Chromosome chromosome) {
		if(chromosomes == null) {
			chromosomes = new ArrayList<Chromosome>();
		}
		chromosomes.add(chromosome);
	}

	@Override
	public void init() {
		for (Chromosome chromosome : chromosomes) {
			chromosome.init();
		}
	}

	@Override
	public List<MutationCommand> assembleMutationCommandList() {
		List<MutationCommand> commands = new ArrayList<MutationCommand>();
		for(Chromosome chromosome : chromosomes) {
			commands.addAll(chromosome.assembleMutationCommandList());
		}
		commands.add(new NewSegmentMC());
		commands.add(new DeleteSegmentMC());
		return commands;
	}

	/**
	 * It is important to think of an organism in terms of its Chromosomes
	 * and not its Genes.  The genes are a performance consideration only.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BaseOrganism) {

			BaseOrganism other = (BaseOrganism) obj;
			if(chromosomes.size() != other.chromosomes.size()) { return false; }
			
			boolean equal = true;
			for (int i = 0; i < chromosomes.size(); i++) {
				if (!chromosomes.get(i).equals(other.chromosomes.get(i))) {
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
		for (Chromosome chromosome : chromosomes) {
			hashCode += chromosome.hashCode();
		}
		return hashCode;
	}
	
	@Override
	public String getId() {
		return this.id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}

}
