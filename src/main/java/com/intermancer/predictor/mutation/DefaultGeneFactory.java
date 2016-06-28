package com.intermancer.predictor.mutation;

import java.util.ArrayList;
import java.util.List;

import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.GeneticUtility;
import com.intermancer.predictor.gene.constant.AdditionCG;
//import com.intermancer.predictor.gene.constant.ModulusCG;
//import com.intermancer.predictor.gene.constant.MultiplicationCG;
//import com.intermancer.predictor.gene.constant.PowerCG;
//import com.intermancer.predictor.gene.logic.ComparisonSwitchLG;
//import com.intermancer.predictor.gene.logic.SingleChannelSwitchLG;
//import com.intermancer.predictor.gene.operation.AdditionOG;
//import com.intermancer.predictor.gene.operation.DivisionOG;
//import com.intermancer.predictor.gene.operation.ModulusOG;
//import com.intermancer.predictor.gene.operation.MultiplicationOG;
//import com.intermancer.predictor.gene.operation.PowerOG;
//import com.intermancer.predictor.gene.operation.SubtractionOG;
//import com.intermancer.predictor.gene.transform.AbsoluteValueTG;
//import com.intermancer.predictor.gene.transform.CeilingTG;
//import com.intermancer.predictor.gene.transform.FloorTG;
//import com.intermancer.predictor.gene.transform.Logarithm10TG;
//import com.intermancer.predictor.gene.transform.LogarithmTG;
//import com.intermancer.predictor.gene.transform.RoundTG;
//import com.intermancer.predictor.gene.transform.SignContinuityTG;
//import com.intermancer.predictor.gene.transform.SineTG;
//import com.intermancer.predictor.gene.window.DelayWG;
//import com.intermancer.predictor.gene.window.LocalExtremeWG;
//import com.intermancer.predictor.gene.window.MovingSumWG;

/**
 * DefaultGeneFactory simply returns a random Gene.
 * 
 * @author johnfryar
 * 
 */
public class DefaultGeneFactory implements GeneFactory {

	private List<Gene> genes;

	public DefaultGeneFactory() {
		genes = new ArrayList<Gene>();
		createConstantGenes();
		createOperationGenes();
		createTransformGenes();
		createWindowGenes();
		createLogicGenes();
	}

	private void createLogicGenes() {
//		genes.add(new SingleChannelSwitchLG());
//		genes.add(new ComparisonSwitchLG());
	}

	private void createConstantGenes() {
		genes.add(new AdditionCG());
//		genes.add(new ModulusCG());
//		MultiplicationCG multiply = new MultiplicationCG();
//		multiply.setConstant(1.1);
//		genes.add(multiply);
//		PowerCG power = new PowerCG();
//		power.setConstant(1.1);
//		genes.add(power);
	}

	private void createOperationGenes() {
//		genes.add(new AdditionOG());
//		genes.add(new DivisionOG());
//		genes.add(new ModulusOG());
//		genes.add(new MultiplicationOG());
//		genes.add(new PowerOG());
//		genes.add(new SubtractionOG());
	}

	private void createTransformGenes() {
//		genes.add(new AbsoluteValueTG());
//		genes.add(new CeilingTG());
//		genes.add(new FloorTG());
//		genes.add(new Logarithm10TG());
//		genes.add(new LogarithmTG());
//		genes.add(new RoundTG());
//		genes.add(new SignContinuityTG());
//		genes.add(new SineTG());
	}

	private void createWindowGenes() {
//		genes.add(new DelayWG());
//		genes.add(new LocalExtremeWG());
//		genes.add(new MovingSumWG());
	}

	public Gene getGene() {
		int index = (int) (Math.random() * genes.size());
		Gene gene = genes.get(index);
		return GeneticUtility.cloneGene(gene);
	}
	
	public List<Gene> getGenes() {
		return genes;
	}

}
