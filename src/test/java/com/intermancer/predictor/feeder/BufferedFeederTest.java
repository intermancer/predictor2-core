package com.intermancer.predictor.feeder;

import java.io.Reader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.intermancer.predictor.data.ConsumeResponse;
import com.intermancer.predictor.data.Quantum;
import com.intermancer.predictor.evaluator.PredictiveEvaluator;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.store.DefaultOrganismStoreInitializer;
import com.intermancer.predictor.system.SystemTest;

/**
 * This test caches the whole data file so that it can feed and re-feed
 * organisms with repeatable results.  It demonstrates how to use a BufferedFeeder.
 * 
 * @author john
 *
 */
public class BufferedFeederTest extends SystemTest {
	
	private static final int MEMORIZER_WINDOW = 50;

	private Feeder readerFeeder;
	private Organism organism;
	private TotalCountFCL counter;
	private MemorizingFCL memorizer;

	@Before
	public void setup() {
		Reader fileReader = getFileReader(DATA_PATH);
		readerFeeder = new SimpleRF(fileReader);
		readerFeeder = new BufferedFeeder(readerFeeder);
		
		PredictiveEvaluator evaluator = new PredictiveEvaluator();
		readerFeeder.addFeedCycleListener(evaluator);
		
		counter = new TotalCountFCL();
		readerFeeder.addFeedCycleListener(counter);
		
		memorizer = new MemorizingFCL(MEMORIZER_WINDOW);
		readerFeeder.addFeedCycleListener(memorizer);

		DefaultOrganismStoreInitializer builder = new DefaultOrganismStoreInitializer();
		organism = builder.getMovingAverage(4);
		readerFeeder.setOrganism(organism);
	}
	
	@Test
	public void testRefeeding() {
		readerFeeder.init();
		readerFeeder.feedOrganism();
		double firstScore = readerFeeder.getEvaluator().getScore();
		int firstCount = counter.getCount();
		List<ConsumeResponse> firstResponses = memorizer.getResponses();
		List<Quantum> firstQuanta = memorizer.getQuanta();
		readerFeeder.init();
		readerFeeder.feedOrganism();
		double secondScore = readerFeeder.getEvaluator().getScore();
		int secondCount = counter.getCount();
		List<ConsumeResponse> secondResponses = memorizer.getResponses();
		List<Quantum> secondQuanta = memorizer.getQuanta();
		assertEquals(firstCount, secondCount);
		for(int i = 0; i < MEMORIZER_WINDOW; i++) {
			System.out.print("i: " + i);
			System.out.print(" firstResponse: " + firstResponses.get(i));
			System.out.print(" secondResponse: " + secondResponses.get(i));
			System.out.print(" firstQuantum: " + firstQuanta.get(i));
			System.out.println( " secondQuantum: " + secondQuanta.get(i));
			assertTrue(firstResponses.get(i).equals(secondResponses.get(i)));
			assertTrue(firstQuanta.get(i).equals(secondQuanta.get(i)));
		}
		assertEquals(firstScore, secondScore, 1e-15);
	}

}
