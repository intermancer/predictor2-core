package com.intermancer.predictor.experiment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intermancer.predictor.experiment.analysis.AnalysisExperimentListener;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.constant.AdditionCG;
import com.intermancer.predictor.organism.BaseOrganism;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.OrganismTestUtility;
import com.intermancer.predictor.organism.store.InMemoryQuickAndDirtyOrganismStore;
import com.intermancer.predictor.organism.store.OrganismStore;
import com.intermancer.predictor.organism.store.OrganismStoreIndex;
import com.intermancer.predictor.system.SystemTest;

public class ExperimentPrimeTest extends SystemTest {

	private static final int MIN_ITERATIONS = 10;

	ObjectMapper mapper;
	String jsonOrganism;

	@Before
	public void init() {
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
	}

	@Test
	public void testRunExperimentCycle() throws Exception {
		ExperimentPrimeRunner experimentRunner = new ExperimentPrimeRunner();
		experimentRunner.init();

		System.out.println("Before running experiment:");
		outputScoreStats(experimentRunner);
		assertTrue(experimentRunner.getContext().getOrganismStore()
				.getCount() == InMemoryQuickAndDirtyOrganismStore.DEFAULT_CAPACITY);

		int parentReplacedCount = 0;
		ExperimentCycleResult result = null;
		int iteration = 0;
		while (iteration < MIN_ITERATIONS) {
			result = experimentRunner.getContext().getExperiment().runExperimentCycle();
			if (result.isParentWasReplaced()) {
				parentReplacedCount++;
			}
			iteration++;
			if (iteration % 50000 == 0) {
				System.out.println(".");
			} else if (iteration % 1000 == 0) {
				System.out.print(".");
			}
		}

		System.out.println("\nExperiemnt run successfully.");
		System.out.println("  Iteration: " + iteration);
		System.out.println("  Number of replacements: " + parentReplacedCount);
		outputScoreStats(experimentRunner);

		System.out.println("Five best organism records:");
		OrganismStore store = experimentRunner.getContext().getOrganismStore();
		OrganismStoreIndex index = experimentRunner.getContext().getOrganismStoreIndex();
		for (int i = 0; i < 5; i++) {
			Organism organism = store.getOrganism(index.findByScoreIndex(i).getOrganismId());
			String orgString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(organism);
			System.out.println(orgString);
		}

	}

	@Test
	public void testExperimentSeries() throws Exception {
		ExperimentPrimeRunner experimentRunner = new ExperimentPrimeRunner();

		experimentRunner.getContext().addExperimentListener(new ProgressReportingEL());
		AnalysisExperimentListener analysisListener = new AnalysisExperimentListener(
				experimentRunner.getContext().getOrganismStoreIndex());
		experimentRunner.getContext().addExperimentListener(analysisListener);
		experimentRunner.getContext().setCycles(10);

		experimentRunner.startExperiment();
		assertEquals(10, analysisListener.getExperimentResult().getIteration());
		System.out.println("");
		System.out.println(analysisListener.getExperimentResult());
	}

	@Test
	public void testRepresentation() throws Exception {
		List<Gene> gene = new ArrayList<Gene>();
		gene.add(new AdditionCG());
		Organism testOrganism = OrganismTestUtility.createOrganism(gene);
		jsonOrganism = mapper.writeValueAsString(testOrganism);
		System.out.println(jsonOrganism);
	}

	@Test
	public void testHydration() throws Exception {
		testRepresentation();
		Organism hydratedOrganism = mapper.readValue(jsonOrganism, BaseOrganism.class);
		assertNotNull(hydratedOrganism);
		assertNotNull(hydratedOrganism.getChromosomes());
		assertTrue(hydratedOrganism.getChromosomes().size() == 1);
		assertTrue(hydratedOrganism.getChromosomes().get(0).getGenes().get(0) instanceof AdditionCG);
	}

	private void outputScoreStats(ExperimentPrimeRunner experimentRunner) {
		double lowScore = experimentRunner.getContext().getOrganismStoreIndex().getLowestScore();
		double highScore = experimentRunner.getContext().getOrganismStoreIndex().getHighestScore();

		System.out.println("High Score: " + highScore);
		System.out.println("Low Score: " + lowScore);
	}

}
