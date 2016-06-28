package com.intermancer.predictor.mutation;

import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.Chromosome;

public class DefaultMutationAssistant implements MutationAssistant {

	public int adjustInteger(int value, MutationContext context) {
		int diceroll = MutationUtility.getDiceroll(100);
		int threshold = context
				.getIntegerProperty(DefaultMutationContext.CHANNEL_OFFSET_UP_THRESHOLD_KEY);
		if (diceroll < threshold) {
			return value - 1;
		} else {
			return value + 1;
		}
	}

	public double adjustDouble(double value, MutationContext context) {
		double[] presets = context
				.getDoubleArrayProperty(DefaultMutationContext.DEFAULT_CONSTANT_ARRAY_KEY);
		int bucket = MutationUtility.getBucket(value, presets);
		if (bucket == 0) {
			if (value < presets[0]) {
				return presets[0];
			} else {
				return presets[1];
			}
		} else if (bucket >= presets.length - 1) {
			if (value > presets[presets.length - 1]) {
				return presets[presets.length - 1];
			} else {
				return presets[presets.length - 2];
			}
		} else {
			int diceroll = MutationUtility.getDiceroll(100);
			int threshold = context
					.getIntegerProperty(DefaultMutationContext.CONSTANT_UP_THRESHOLD_KEY);
			if (diceroll < threshold) {
				return presets[bucket - 1];
			} else {
				return presets[bucket + 1];
			}
		}
	}

	public int adjustInteger(int value, MutationContext context, int min,
			int max) {
		int diceroll = MutationUtility.getDiceroll(100);
		int threshold = context
				.getIntegerProperty(DefaultMutationContext.INT_UP_THRESHOLD_KEY);
		if (value < min) {
			return min;
		} else if (value == min) {
			return value++;
		} else if (value > max) {
			return max;
		} else if (value == max) {
			return value--;
		} else if (diceroll < threshold) {
			return value--;
		} else {
			return value++;
		}
	}

	public Gene getGene(MutationContext context) {
		return context.getGeneFactory().getGene();
	}

	public Chromosome getChromosome(MutationContext context) {
		return context.getChromosomeFactory().getChromosome();
	}

}
