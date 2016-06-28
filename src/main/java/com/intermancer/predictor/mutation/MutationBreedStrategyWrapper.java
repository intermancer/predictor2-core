package com.intermancer.predictor.mutation;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.breed.BreedStrategy;
import com.intermancer.predictor.organism.Organism;

/**
 * Mutation is handled by wrapping a BreedStrategy.
 * 
 * @author johnfryar
 * 
 */
public class MutationBreedStrategyWrapper implements BreedStrategy {

	private BreedStrategy component;
	private int numberOfMutations;
	private MutationAssistant mutationAssistant;
	private MutationContext mutationContext;

	public MutationBreedStrategyWrapper() {
		super();
	}

	public MutationBreedStrategyWrapper(BreedStrategy breedStrategy,
			int numberOfMutations, MutationAssistant mutationAssistant,
			MutationContext mutationContext) {
		this();
		this.component = breedStrategy;
		this.numberOfMutations = numberOfMutations;
		this.mutationAssistant = mutationAssistant;
		this.mutationContext = mutationContext;
	}

	@Override
	public List<Organism> breed(List<Organism> parents) {
		List<Organism> children = component.breed(parents);
		for (Organism child : children) {
			mutate(child);
		}
		return children;
	}

	private void mutate(Organism organism) {
		List<MutationCommand> commands = organism.assembleMutationCommandList();
		List<MutationCommand> selectedCommands = selectCommands(commands);
		for (MutationCommand command : selectedCommands) {
			command.execute(mutationAssistant, mutationContext);
		}
	}

	private List<MutationCommand> selectCommands(List<MutationCommand> commands) {
		if (commands.size() <= numberOfMutations) {
			return commands;
		}
		List<MutationCommand> selectedCommands = new ArrayList<MutationCommand>(
				numberOfMutations);
		while (selectedCommands.size() < numberOfMutations) {
			int index = MutationUtility.getDiceroll(commands.size()) - 1;
			if (!selectedCommands.contains(commands.get(index))) {
				selectedCommands.add(commands.get(index));
			}
		}
		return selectedCommands;
	}

	public BreedStrategy getComponent() {
		return component;
	}

	public void setComponent(BreedStrategy component) {
		this.component = component;
	}

	public int getNumberOfMutations() {
		return numberOfMutations;
	}

	public void setNumberOfMutations(int numberOfMutations) {
		this.numberOfMutations = numberOfMutations;
	}

	public MutationAssistant getMutationAssistant() {
		return mutationAssistant;
	}

	public void setMutationAssistant(MutationAssistant mutationAssistant) {
		this.mutationAssistant = mutationAssistant;
	}

	public MutationContext getMutationContext() {
		return mutationContext;
	}

	public void setMutationContext(MutationContext mutationContext) {
		this.mutationContext = mutationContext;
	}

}
