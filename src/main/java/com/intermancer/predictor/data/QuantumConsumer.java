package com.intermancer.predictor.data;

/**
 * QuantumConsumers are composable: Organisms have Chromosomes, which have
 * Genes.
 * 
 * @author johnfryar
 * 
 */
public interface QuantumConsumer {

	/**
	 * Currently, ConsumeResponse is all about how hungry a QuantumConsumer is,
	 * or how many more feed cycles are necessary before this QuantumConsumer is
	 * able to start providing meaningful data. Generally, a QuantumConsumer is
	 * as hungry as its hungriest component.
	 * 
	 * @param quantum
	 *            The data being fed to the QuantumConsumer for this particular
	 *            feed cycle.
	 * @return A status for this particular feed cycle.
	 */
	ConsumeResponse consume(Quantum quantum);

	/**
	 * This method must be called at the start of the feeding process. It must
	 * NOT be called in between feed cycles.
	 */
	void init();

}
