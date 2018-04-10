package com.intermancer.predictor.organism.store;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.OrganismTestUtility;

public class InMemoryQuickAndDirtyOrganismStoreIndexTest {
	
	OrganismStoreIndex index;
	
	@Before
	public void init() {
		index = new InMemoryQuickAndDirtyOrganismStoreIndex();
	}
	
	@Test
	public void testBasicIndex() {
		Organism organismA = OrganismTestUtility.getMovingAverage(2);
		organismA.setId("A");
		OrganismIndexRecord indexRecordA = index.index(1, organismA);
		assertNotNull(indexRecordA);
		assertTrue(indexRecordA.getOrganismIdIndex() == 0);
		assertTrue(indexRecordA.getScoreIndex() == 0);
		
		Organism organismB = OrganismTestUtility.getMovingAverage(2);
		organismB.setId("B");
		OrganismIndexRecord indexRecordB = index.index(0, organismB);
		assertNotNull(indexRecordB);
		assertTrue(indexRecordB.getOrganismIdIndex() == 1);
		assertTrue(indexRecordB.getScoreIndex() == 0);
		
	}

}
