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
import com.intermancer.predictor.organism.BaseOrganism;
import com.intermancer.predictor.organism.Organism;
import com.intermancer.predictor.organism.breed.BreedStrategy;

public class DefaultOrganismStoreInitializer {

	private static Logger logger = LogManager.getLogger(DefaultOrganismStoreInitializer.class);
	private static final Charset CHARSET = Charset.forName("US-ASCII");

	/**
	 * These organisms came out of some practice runs. There is nothing particularly
	 * special about them.
	 */
	private static final String[] ORGANISM_DESCRIPTIONS = {};

	private static List<Organism> loadedOrganisms;

	private static ObjectMapper objectMapper;

	public static void fillStore(OrganismStore store, OrganismStoreIndex organismIndex, Feeder feeder,
			BreedStrategy breedStrategy, String diskStorePath) throws Exception {
		try {
			addDescribedOrganisms(store, organismIndex, feeder);
			logger.debug("Finished described organisms");
			loadDiskStoreOrganisms(store, organismIndex, feeder, diskStorePath);
			addLoadedOrganisms(store, organismIndex, feeder);
			logger.debug("Finished loaded organisms");
			logger.debug("Starting to breed to capacity");
			breedToCapacity(store, organismIndex, feeder, breedStrategy);
			logger.debug("Done with initial breeding");
		} catch (StoreFullException sfe) {
			// Nothing. Just means the store is full. Stop filling.
		}
	}

	public static void loadDiskStoreOrganisms(OrganismStore store, OrganismStoreIndex organismIndex, Feeder feeder,
			String diskStorePathString) throws StoreFullException {
		if (StringUtils.isNotBlank(diskStorePathString)) {
			if (objectMapper == null) {
				objectMapper = new ObjectMapper();
			}
			Path diskStoreDirectory = Paths.get(diskStorePathString);
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(diskStoreDirectory)) {
				for (Path file : stream) {
					try (BufferedReader reader = Files.newBufferedReader(file, CHARSET)) {
						Organism organism = objectMapper.readValue(reader, Organism.class);
						feedStoreAndIndexSingleOrganism(store, organismIndex, feeder, organism);
					}
				}
			} catch (IOException | DirectoryIteratorException x) {
				logger.error("Error in reading in organisms.");
				logger.error("Exception:", x);
			}
		}
	}

	private static void addLoadedOrganisms(OrganismStore store, OrganismStoreIndex organismIndex, Feeder feeder) throws StoreFullException {
		if (loadedOrganisms != null) {
			for (Organism organism : loadedOrganisms) {
				feedStoreAndIndexSingleOrganism(store, organismIndex, feeder, organism);
			}
		}
	}

	private static void addDescribedOrganisms(OrganismStore store, OrganismStoreIndex organismIndex, Feeder feeder)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		for (String organismJson : ORGANISM_DESCRIPTIONS) {
			Organism organism = mapper.readValue(organismJson, BaseOrganism.class);
			feedStoreAndIndexSingleOrganism(store, organismIndex, feeder, organism);
		}
	}

	private static void breedToCapacity(OrganismStore store, OrganismStoreIndex organismIndex, Feeder feeder, BreedStrategy breedStrategy)
			throws StoreFullException {
		List<Organism> parents = new ArrayList<Organism>();
		while (store.hasCapacity()) {
			Organism mommy = store.getOrganism(organismIndex.getRandomOrganismIndexRecord().getOrganismId());
			Organism daddy = store.getOrganism(organismIndex.getRandomOrganismIndexRecord().getOrganismId());

			parents.clear();
			parents.add(mommy);
			parents.add(daddy);
			List<Organism> children = breedStrategy.breed(parents);

			for (Organism child : children) {
				if (store.hasCapacity()) {
					feedStoreAndIndexSingleOrganism(store, organismIndex, feeder, child);
				}
			}
		}
	}

	private static void feedStoreAndIndexSingleOrganism(OrganismStore store, OrganismStoreIndex organismIndex,
			Feeder feeder, Organism organism) throws StoreFullException {
		feeder.setOrganism(organism);
		feeder.init();
		feeder.feedOrganism();
		store.putOrganism(organism);
		organismIndex.index(feeder.getEvaluator().getScore(), organism);
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
