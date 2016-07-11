package com.intermancer.predictor.organism;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.operation.AdditionOG;
import com.intermancer.predictor.gene.operation.MultiplicationOG;

public class OrganismFileUtilityTest extends QuantumConsumerTest {

	private static final String TEST_ORGANISM_DIRECTORY = "src/test/resources/com/intermancer/predictor/test/data/organisms";

	protected Organism getSimpleOrganism() {
		List<Gene> genes = new ArrayList<Gene>();
		genes.add(new MultiplicationOG());
		genes.add(new AdditionOG());
		return createOrganism(genes);
	}
	
	@Test
	public void testWriteAndRead() throws Exception {
		Path testDirectoryPath = FileSystems.getDefault().getPath(TEST_ORGANISM_DIRECTORY);
		Files.createDirectories(testDirectoryPath);
		
		Organism organism = getSimpleOrganism();
		
	}

}
