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
		OrganismIndexRecord indexRecordA = putIndexRecord("A", 1);
		assertNotNull(indexRecordA);
		assertTrue(indexRecordA.getScoreIndex() == 0);
		
		OrganismIndexRecord indexRecordB = putIndexRecord("B", 0);
		assertNotNull(indexRecordB);
		assertTrue(indexRecordB.getScoreIndex() == 0);
		
		assertTrue("A".equals(index.findByScoreIndex(1).getOrganismId()));
	}
	
	@Test
	public void testFindByScoreIndex() {
		putIndexRecord("A", 0.5);
		putIndexRecord("B", 1);
		putIndexRecord("C", 0.25);
		
		assertTrue(index.findByScoreIndex(0).getOrganismId().equals("C"));
		assertTrue(index.findByScoreIndex(1).getOrganismId().equals("A"));
		assertTrue(index.findByScoreIndex(2).getOrganismId().equals("B"));
	}
	
	@Test
	public void testDelete() {
		putIndexRecord("A", 0.5);
		putIndexRecord("B", 1);
		
		assertTrue("A".equals(index.findByScoreIndex(0).getOrganismId()));
		index.deleteRecord("A");
		assertTrue("B".equals(index.findByScoreIndex(0).getOrganismId()));
	}
	
	@Test
	public void testGetLowestScore() {
		putIndexRecord("A", 0.5);
		putIndexRecord("B", 1);
		putIndexRecord("C", 0.25);
		
		assertTrue(0.25 == index.getLowestScore());
	}

	@Test
	public void testGetHighestScore() {
		putIndexRecord("A", 0.5);
		putIndexRecord("B", 1.0);
		putIndexRecord("C", 0.25);
		
		assertTrue(1.0 == index.getHighestScore());
	}

	@Test
	public void testFindIndexByScore() {
		putIndexRecord("A", 0.5);
		putIndexRecord("B", 1.0);
		putIndexRecord("C", 0.25);
		
		assertTrue(0 == index.findIndexByScore(0.1));
		assertTrue(0 == index.findIndexByScore(0.25));
		assertTrue(1 == index.findIndexByScore(0.2500001));
		assertTrue(1 == index.findIndexByScore(0.4));
		assertTrue(2 == index.findIndexByScore(0.75));
		assertTrue(3 == index.findIndexByScore(1.1));
	}
	
	@Test
	public void testGetRandomOrganismIndexRecord() {
		boolean[] visited = new boolean[10];
		for (int i = 0; i < 10; i++) {
			putIndexRecord(Integer.toString(i), i);
			visited[i] = false;
		}
		
		boolean allVisited = false;
		while (!allVisited) {
			OrganismIndexRecord indexRecord = index.getRandomOrganismIndexRecord();
			int idToInt = Integer.parseInt(indexRecord.getOrganismId());
			visited[idToInt] = true;
			boolean visitCheck = true;
			innerLoop: for (int i = 0; i < 10; i++) {
				if (!visited[i]) {
					visitCheck = false;
					break innerLoop;
				}
			}
			allVisited = visitCheck;
		}
		
	}

	@Test
	public void testGetRandomOrganismIndexRecordFromLowScorePool() {
		boolean[] visited = new boolean[10];
		for (int i = 0; i < 10; i++) {
			putIndexRecord(Integer.toString(i), i);
			visited[i] = false;
		}
		
		for (int i = 0; i < 20; i++) {
			OrganismIndexRecord indexRecord = index.getRandomOrganismIndexRecordFromLowScorePool(0.3);
			int idToInt = Integer.parseInt(indexRecord.getOrganismId());
			visited[idToInt] = true;
		}
		for (int i = 0; i < 3; i++) {
			assertTrue(visited[i]);
		}
		for (int i = 3; i < 10; i++) {
			assertFalse(visited[i]);
		}
		
	}

	private OrganismIndexRecord putIndexRecord(String id, double score) {
		Organism organism = OrganismTestUtility.getMovingAverage(2);
		organism.setId(id);
		return index.index(score, organism);
	}
	
}
