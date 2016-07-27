package com.intermancer.predictor.organism;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intermancer.predictor.data.QuantumConsumerTest;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.gene.operation.AdditionOG;
import com.intermancer.predictor.gene.operation.MultiplicationOG;

public class OrganismFileUtilityTest extends QuantumConsumerTest {

	private static final String TEST_ORGANISM_DIRECTORY = "src/test/resources/com/intermancer/predictor/test/data/organisms";
	private static final ObjectMapper objectMapper = new ObjectMapper();

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
		String jsonOutput = null;
		try {
			jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(organism);
		} catch (Exception ex) {
			assertTrue("Failed json serialization", false);
		}
		
		String filename = Long.toString(System.currentTimeMillis()) + ".json";
		Path outputFilePath = Paths.get(TEST_ORGANISM_DIRECTORY, filename);
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath, charset)) {
			writer.write(jsonOutput);
		}
		
		Organism readInOrganism = null;
		try (BufferedReader reader = Files.newBufferedReader(outputFilePath, charset)) {
			readInOrganism = objectMapper.readValue(reader, BaseOrganism.class);			
		}
		
		assertEquals(organism, readInOrganism);
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(testDirectoryPath)) {
			for (Path file : stream) {
				Files.delete(file);
			}
		}

		
	}

}
