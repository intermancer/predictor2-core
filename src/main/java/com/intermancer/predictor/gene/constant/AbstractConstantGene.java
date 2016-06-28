package com.intermancer.predictor.gene.constant;

import java.util.List;

import com.intermancer.predictor.gene.transform.AbstractTransformationGene;
import com.intermancer.predictor.mutation.MutationAssistant;
import com.intermancer.predictor.mutation.MutationCommand;
import com.intermancer.predictor.mutation.MutationContext;

/**
 * ConstantGenes use a constant number to transform a single data Channel in
 * some way.
 * 
 * @author johnfryar
 * 
 */
public abstract class AbstractConstantGene extends AbstractTransformationGene {

	public static final double DEFAULT_CONSTANT = 1.0;

	public class ConstantGeneConstantMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			constant = mutationAssistant.adjustDouble(constant, context);
		}

	}

	public class ConstantGeneInverseMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			inverse = !inverse;
		}

	}

	public class ConstantGeneNegativeMC implements MutationCommand {

		public void execute(MutationAssistant mutationAssistant,
				MutationContext context) {
			negative = !negative;
		}

	}

	public static final int INVERSE_HASH_VALUE = 100;

	public static final int NEGATIVE_HASH_VALUE = 300;

	private double constant;
	protected double constantVal = 0.0;
	private boolean inverse;
	private boolean negative;

	public AbstractConstantGene() {
		this(DEFAULT_CONSTANT);
	}

	public AbstractConstantGene(double constant) {
		this(constant, false, false);
	}

	public AbstractConstantGene(boolean negative, boolean inverse) {
		this(DEFAULT_CONSTANT, negative, inverse);
	}

	public AbstractConstantGene(double constant, boolean negative,
			boolean inverse) {
		this(-1, constant, negative, inverse);
	}

	public AbstractConstantGene(int offset, double constant, boolean negative,
			boolean inverse) {
		super(offset);
		setConstant(constant);
		setNegative(negative);
		setInverse(inverse);
	}

	public double getConstant() {
		return constant;
	}

	public void setConstant(double constant) {
		this.constant = constant;
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	public boolean isNegative() {
		return negative;
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
	}

	public void init() {
		constantVal = constant;
		if (inverse) {
			constantVal = 1.0 / constant;
		}
		if (negative) {
			constantVal = 0.0 - constantVal;
		}
	}

	@Override
	public List<MutationCommand> assembleMutationCommandList() {
		List<MutationCommand> mutations = super.assembleMutationCommandList();
		mutations.add(new ConstantGeneConstantMC());
		mutations.add(new ConstantGeneInverseMC());
		mutations.add(new ConstantGeneNegativeMC());
		return mutations;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			AbstractConstantGene other = (AbstractConstantGene) obj;
			return (constant == other.constant) && (inverse == other.inverse)
					&& (negative == other.negative);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode += (int) constant;
		if (inverse) {
			hashCode += INVERSE_HASH_VALUE;
		}
		if (negative) {
			hashCode += NEGATIVE_HASH_VALUE;
		}
		return hashCode;
	}

}
