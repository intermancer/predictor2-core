package com.intermancer.predictor.mutation;

import java.util.List;

/**
 * Mutable is the interface for "things that mutate." It so happens that, as of
 * this writing, everything that can mutate is also something that can consume
 * data.
 * 
 * @author johnfryar
 * 
 */
public interface Mutable {

	/**
	 * It is assumed that mutation is composable. In other words, calling
	 * assembleMutationCommandList() on an Organism should also return all of
	 * the MutationCommands for its Chromosomes, which, in their turn, should
	 * also return all of the MutationCommands for their Genes.
	 * 
	 * @return The MutationCommands that can be executed.
	 */
	List<MutationCommand> assembleMutationCommandList();

}
