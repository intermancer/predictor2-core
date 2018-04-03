package com.intermancer.predictor.experiment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.store.InMemoryQuickAndDirtyOrganismStore;
import com.intermancer.predictor.system.SystemTest;

public class InMemoryQuickAndDirtyOrganismStoreTest extends SystemTest {

	InMemoryQuickAndDirtyOrganismStore inMemoryQuickAndDirtyStore;
	
	@Before
	public void init() {
		inMemoryQuickAndDirtyStore = new InMemoryQuickAndDirtyOrganismStore();
	}
	
	@Test
	public void testPutGetDeleteCycle() throws Exception {
		assertTrue(inMemoryQuickAndDirtyStore.getCount() == 0L);

		Organism simpleOrganism = getSimpleOrganism();
		String organismId = inMemoryQuickAndDirtyStore.putOrganism(simpleOrganism);
		assertNotNull(organismId);
		assertTrue(inMemoryQuickAndDirtyStore.getCount() == 1L);
		
		Organism retreivedOrganism = inMemoryQuickAndDirtyStore.getOrganism(organismId);
		assertTrue(retreivedOrganism.equals(simpleOrganism));
		
		inMemoryQuickAndDirtyStore.deleteOrganism(organismId);
		assertTrue(inMemoryQuickAndDirtyStore.getCount() == 0L);

	}
	
}
