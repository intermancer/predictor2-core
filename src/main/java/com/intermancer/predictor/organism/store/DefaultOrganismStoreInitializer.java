package com.intermancer.predictor.organism.store;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.gene.Chromosome;
import com.intermancer.predictor.gene.Gene;
import com.intermancer.predictor.mutation.DefaultGeneFactory;
import com.intermancer.predictor.mutation.GeneFactory;
import com.intermancer.predictor.organism.BaseOrganism;
import com.intermancer.predictor.organism.DefaultOrganismBuilder;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.breed.BreedStrategy;

public class DefaultOrganismStoreInitializer extends DefaultOrganismBuilder {

	private static Logger logger = LogManager.getLogger(DefaultOrganismStoreInitializer.class);
	private static final Charset CHARSET = Charset.forName("US-ASCII");

	/**
	 * These organisms came out of some practice runs. There is nothing
	 * particularly special about them.
	 */
	private static final String[] ORGANISM_DESCRIPTIONS = {};

	private static final int NUMBER_OF_CTHONIC_ORGANISMS = 1000;
	private static final int NUMBER_OF_GENES_IN_EACH_CTHONIC_ORGANISM = 6;
	private static final int NUMBER_OF_CHROMOSOMES_IN_EACH_CTHONIC_ORGANISM = 3;

	private static List<Organism> loadedOrganisms;

	private static ObjectMapper objectMapper;

	public static void fillStore(OrganismStoreIndex organismIndex, Feeder feeder, BreedStrategy breedStrategy,
			String diskStorePath) throws Exception {
		try {
			addDescribedOrganisms(organismIndex, feeder);
			logger.debug("Finished described organisms");
			loadDiskStoreOrganisms(organismIndex, feeder, diskStorePath);
			addLoadedOrganisms(organismIndex, feeder);
			logger.debug("Finished loaded organisms");
			addCthonicOrganisms(organismIndex, feeder);
			logger.debug("Finished cthonic organisms");
			logger.debug("Starting to breed to capacity");
			breedToCapacity(organismIndex, feeder, breedStrategy);
			logger.debug("Done with initial breeding");
		} catch (StoreFullException sfe) {
			// Nothing. Just means the store is full. Stop filling.
		}
	}

	public static void loadDiskStoreOrganisms(OrganismStoreIndex organismIndex, Feeder feeder, String diskStorePathString)
			throws StoreFullException {
		if (StringUtils.isNotBlank(diskStorePathString)) {
			if (objectMapper == null) {
				objectMapper = new ObjectMapper();
			}
			Path diskStoreDirectory = Paths.get(diskStorePathString);
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(diskStoreDirectory)) {
				for (Path file : stream) {
					try (BufferedReader reader = Files.newBufferedReader(file, CHARSET)) {
						Organism organism = objectMapper.readValue(reader, Organism.class);
						feedSingleOrganism(organismIndex, feeder, organism);
					}
				}
			} catch (IOException | DirectoryIteratorException x) {
				logger.error("Error in reading in organisms.");
				logger.error("Exception:", x);
			}
		}
	}

	private static void addCthonicOrganisms(OrganismStoreIndex organismIndex, Feeder feeder) throws StoreFullException {
		GeneFactory geneFactory = new DefaultGeneFactory();
		for (int i = 0; i < NUMBER_OF_CTHONIC_ORGANISMS; i++) {
			List<Gene> geneList = new ArrayList<Gene>();
			for (int j = 0; j < NUMBER_OF_GENES_IN_EACH_CTHONIC_ORGANISM; j++) {
				geneList.add(geneFactory.getGene());
			}
			List<Chromosome> chromosomes = transformGenesIntoChromosomes(geneList);
			feedSingleOrganism(organismIndex, feeder, new BaseOrganism(chromosomes));
		}
	}

	private static List<Chromosome> transformGenesIntoChromosomes(List<Gene> geneList) {
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		for (int i = 0; i < NUMBER_OF_CHROMOSOMES_IN_EACH_CTHONIC_ORGANISM; i++) {
			chromosomes.add(new Chromosome());
		}
		int chromosomeIndex = 0;
		for (Gene gene : geneList) {
			chromosomes.get(chromosomeIndex).addGene(gene);
			chromosomeIndex++;
			chromosomeIndex = chromosomeIndex % chromosomes.size();
		}

		return chromosomes;
	}

	private static void addLoadedOrganisms(OrganismStoreIndex organismIndex, Feeder feeder) throws StoreFullException {
		if (loadedOrganisms != null) {
			for (Organism organism : loadedOrganisms) {
				feedSingleOrganism(organismIndex, feeder, organism);
			}
		}
	}

	private static void addDescribedOrganisms(OrganismStoreIndex organismIndex, Feeder feeder) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		for (String organismJson : ORGANISM_DESCRIPTIONS) {
			Organism organism = mapper.readValue(organismJson, BaseOrganism.class);
			feedSingleOrganism(organismIndex, feeder, organism);
		}
	}

	private static void breedToCapacity(OrganismStoreIndex organismIndex, Feeder feeder, BreedStrategy breedStrategy)
			throws StoreFullException {
		List<Organism> parents = new ArrayList<Organism>();
		OrganismStore store = organismIndex.getOrganismStore();
		while (store.hasCapacity()) {
			Organism mommy = organismIndex.getRandomOrganismIndexRecord().getOrganism();
			Organism daddy = organismIndex.getRandomOrganismIndexRecord().getOrganism();

			parents.clear();
			parents.add(mommy);
			parents.add(daddy);
			List<Organism> children = breedStrategy.breed(parents);
			
			for (Organism child : children) {
				if (store.hasCapacity()) {
					feedIndexAndStoreSingleOrganism(organismIndex, feeder, child);
				}
			}
		}
	}

	private static void feedIndexAndStoreSingleOrganism( Organism organism, Feeder feeder,
			OrganismStoreIndex organismIndex, OrganismStore store)
			throws StoreFullException {
		feeder.setOrganism(organism);
		feeder.init();
		feeder.feedOrganism();
		store.putOrganism(organism);
		OrganismIndexRecord indexRecord = organismIndex.indexAndStore(feeder.getEvaluator().getScore(), organism);
	}

	public static synchronized List<Organism> getLoadedOrganisms() {
		if (loadedOrganisms == null) {
			loadedOrganisms = new ArrayList<Organism>();
		}
		return loadedOrganisms;
	}

	public static void setLoadedOrganisms(List<Organism> organisms) {
		loadedOrganisms = organisms;
	}

}
