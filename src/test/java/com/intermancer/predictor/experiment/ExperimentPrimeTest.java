package com.intermancer.predictor.experiment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.constant.AdditionCG;
import com.intermancer.predictor.organism.BaseOrganism;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.store.InMemoryQuickAndDirtyOrganismStore;
import com.intermancer.predictor.organism.store.OrganismStoreRecord;
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
		assertTrue(experimentRunner.getOrganismStore()
				.getCount() == InMemoryQuickAndDirtyOrganismStore.DEFAULT_MAX_CAPACITY);

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
		for (int i = 0; i < 5; i++) {
			System.out.println(StringEscapeUtils.escapeJava(
					mapper.writeValueAsString(experimentRunner.getOrganismStore().findByIndex(i).getOrganism())));
		}

	}

	@Test
	public void testExperimentSeries() throws Exception {
		ExperimentPrimeRunner experimentRunner = new ExperimentPrimeRunner();
		experimentRunner.getOrganismStore().setMaxSize(5);

		experimentRunner.addExperimentListener(new ProgressReportingEL());
		AnalysisExperimentListener analysisListener = new AnalysisExperimentListener(
				experimentRunner.getOrganismStore());
		experimentRunner.addExperimentListener(analysisListener);
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
		Organism testOrganism = createOrganism(gene);
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

	@Test
	public void testHydration2() throws Exception {
		String jsonOrganism = "{\"id\":3011,\"score\":7.08695283596639E14,\"organism\":[\"com.intermancer.predictor.organism.BaseOrganism\",{\"chromosomes\":[\"java.util.ArrayList\",[{\"genes\":[\"java.util.ArrayList\",[[\"com.intermancer.predictor.gene.window.MovingSumWG\",{\"values\":[1398.08],\"windowValues\":[\"java.util.ArrayList\",[[1388.81],[1390.23],[1373.72],[1373.07],[1378.16],[1388.59],[1398.08]]],\"windowSize\":7,\"offsets\":[-3],\"hungryCycles\":7}],[\"com.intermancer.predictor.gene.constant.AdditionCG\",{\"values\":[9690.660000000016],\"offsets\":[-1],\"constant\":1.1,\"inverse\":false,\"negative\":false,\"hungryCycles\":0}],[\"com.intermancer.predictor.gene.constant.AdditionCG\",{\"values\":[9691.760000000017],\"offsets\":[-1],\"constant\":1.0,\"inverse\":false,\"negative\":true,\"hungryCycles\":0}]]]},{\"genes\":[\"java.util.ArrayList\",[[\"com.intermancer.predictor.gene.window.MovingSumWG\",{\"values\":[9690.760000000017],\"windowValues\":[\"java.util.ArrayList\",[[9842.760000000017],[9836.730000000016],[9804.800000000016],[9771.000000000016],[9734.810000000016],[9701.770000000017],[9690.760000000017]]],\"windowSize\":7,\"offsets\":[-1],\"hungryCycles\":7}],[\"com.intermancer.predictor.gene.constant.AdditionCG\",{\"values\":[68382.63000000018],\"offsets\":[-1],\"constant\":1.1,\"inverse\":true,\"negative\":true,\"hungryCycles\":0}],[\"com.intermancer.predictor.gene.constant.AdditionCG\",{\"values\":[1.2121308E12],\"offsets\":[0],\"constant\":1.0,\"inverse\":true,\"negative\":false,\"hungryCycles\":0}]]]}]]}]}";
		OrganismStoreRecord hydratedOrganism = mapper.readValue(jsonOrganism, OrganismStoreRecord.class);
		assertNotNull(hydratedOrganism);
		assertNotNull(hydratedOrganism.getOrganism());
		assertNotNull(hydratedOrganism.getOrganism().getChromosomes());
		assertTrue(hydratedOrganism.getOrganism().getChromosomes().size() > 0);
	}

	private void outputScoreStats(ExperimentPrimeRunner experimentRunner) {
		experimentRunner.getOrganismStore().analyze();
		double lowScore = experimentRunner.getOrganismStore().getLowestScore();
		double highScore = experimentRunner.getOrganismStore().getHighestScore();

		System.out.println("High Score: " + highScore);
		System.out.println("Low Score: " + lowScore);
	}

}
