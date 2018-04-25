package com.intermancer.predictor.organism.store;

import org.junit.Test;
import static org.junit.Assert.*;

import com.intermancer.predictor.evaluator.PredictiveEvaluator;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.feeder.PassThroughFeeder;
import com.intermancer.predictor.system.SystemTest;

public class DefaultOrganismStoreInitializerTest extends SystemTest {

	private static final int DEFAULT_PREDICTIVE_WINDOW_SIZE = 2;
	private static final String FILE_STORE_PATH = "src/test/resources/com/intermancer/predictor/test/data/store";

	@Test
	public void testLoadDiskStoreOrganisms() throws Exception {
		Feeder feeder = new PassThroughFeeder();
		PredictiveEvaluator evaluator = new PredictiveEvaluator();
		evaluator.setPredictiveWindowSize(DEFAULT_PREDICTIVE_WINDOW_SIZE);
		feeder.addFeedCycleListener(evaluator);

		InMemoryQuickAndDirtyOrganismStore organismStore = new InMemoryQuickAndDirtyOrganismStore();
		InMemoryQuickAndDirtyOrganismStoreIndex organismStoreIndex = new InMemoryQuickAndDirtyOrganismStoreIndex();

		DefaultOrganismStoreInitializer.loadDiskStoreOrganisms(organismStore, organismStoreIndex, feeder,
				FILE_STORE_PATH);
		assertEquals(2, organismStore.getCount());

	}

}
