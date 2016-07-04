package com.intermancer.predictor.experiment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.store.InMemoryQuickAndDirtyOrganismStore;
import com.intermancer.predictor.organism.store.OrganismStoreRecord;
import com.intermancer.predictor.organism.store.StoreFullException;
import com.intermancer.predictor.system.SystemTest;

public class InMemoryQuickAndDirtyOrganismStoreTest extends SystemTest {

	ObjectMapper mapper;
	String jsonOrganism;
	InMemoryQuickAndDirtyOrganismStore inMemoryQuickAndDirtyStore;
	
	@Before
	public void init() {
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		inMemoryQuickAndDirtyStore = new InMemoryQuickAndDirtyOrganismStore();
	}
	
	@Test
	public void testAddRecord() throws Exception {
		Organism simpleOrganism = getSimpleOrganism();
		OrganismStoreRecord initialRecord = new OrganismStoreRecord(1.0, simpleOrganism);
		inMemoryQuickAndDirtyStore.addRecord(initialRecord);
		assertNotNull(initialRecord.getId());
		List<OrganismStoreRecord> records = inMemoryQuickAndDirtyStore.getRecords();
		assertNotNull(records);
		assertTrue(records.size() == 1);
	}
	
	@Test
	public void testGetNextId() throws Exception {
		testAddRecord();
		assertEquals(inMemoryQuickAndDirtyStore.getNextId(), new Long(1));
	}
	
	@Test
	public void testHasCapacity() throws Exception {
		inMemoryQuickAndDirtyStore.setMaxSize(1);
		assertTrue(inMemoryQuickAndDirtyStore.hasCapacity());
		testAddRecord();
		assertFalse(inMemoryQuickAndDirtyStore.hasCapacity());
	}
	
	@Test
	public void testGetRandomOrganismStoreRecord() throws Exception {
		fillStoreWithSimpleOrganisms(inMemoryQuickAndDirtyStore);
		OrganismStoreRecord record1 = inMemoryQuickAndDirtyStore.getRandomOrganismStoreRecord();
		OrganismStoreRecord record2 = inMemoryQuickAndDirtyStore.getRandomOrganismStoreRecord();
		int recordsMatch = 0;
		int recordsDoNotMatch = 0;
		for (int i = 0; i < 1000; i++) {
			if (record1.getId().equals(record2.getId())) {
				recordsMatch++;
			} else {
				recordsDoNotMatch++;
			}
		}
		assertTrue(recordsMatch + recordsDoNotMatch == 1000);
		assertTrue(recordsDoNotMatch > 10 * recordsMatch);
	}
	
	@Test
	public void testGetMaxCapacity() {
		assertTrue(inMemoryQuickAndDirtyStore.getMaxSize()
				== InMemoryQuickAndDirtyOrganismStore.DEFAULT_MAX_CAPACITY);
	}
	
	@Test
	public void testGetCount() throws Exception {
		assertEquals(inMemoryQuickAndDirtyStore.getCount(), 0);
		testAddRecord();
		assertEquals(inMemoryQuickAndDirtyStore.getCount(), 1);
		fillStoreWithSimpleOrganisms(inMemoryQuickAndDirtyStore);
		assertEquals(inMemoryQuickAndDirtyStore.getCount(),
				InMemoryQuickAndDirtyOrganismStore.DEFAULT_MAX_CAPACITY);
	}
	
	@Test
	public void testOrdering() throws Exception{
		// This depends on the values that get set in fillStoreWithSimpleOrganisms().
		// The asserts here wouldn't make sense except that our fillStore method sets the
		// score sequentially.
		fillStoreWithSimpleOrganisms(inMemoryQuickAndDirtyStore);
		for (int i = 0; i < InMemoryQuickAndDirtyOrganismStore.DEFAULT_MAX_CAPACITY; i++) {
			OrganismStoreRecord record = inMemoryQuickAndDirtyStore.findByIndex(i);
			assertEquals(record.getIndex() + 1, record.getScore(), 0D);
		}
		OrganismStoreRecord newStoreRecord = getSimpleOrganismStoreRecord(1, 500);
		try {
			inMemoryQuickAndDirtyStore.addRecord(newStoreRecord);
			assertTrue(false);
		} catch (StoreFullException ex) {
			// This is expected.
		}
//		assertEquals(newStoreRecord.getIndex(), 499);
	}

}
