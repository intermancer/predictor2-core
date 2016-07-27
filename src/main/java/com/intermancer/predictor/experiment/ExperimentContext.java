package com.intermancer.predictor.experiment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.organism.breed.BreedStrategy;
import com.intermancer.predictor.organism.store.OrganismStore;

public class ExperimentContext {

	private static final int DEFAULT_NUMBER_OF_CYCLES = 10000;

	private int cycles = DEFAULT_NUMBER_OF_CYCLES;
	private Experiment experiment;
	private OrganismStore organismStore;
	private Feeder feeder;
	private OrganismLifecycleStrategy organismLifecycleStrategy;
	private BreedStrategy breedStrategy;
	private List<ExperimentListener> listeners;
	private int iteration;
	private Map<String, Object> resources;
	private String diskStorePath;
	
	public ExperimentContext() {
		resources = new HashMap<String, Object>();
	}

	public int getCycles() {
		return cycles;
	}

	public void setCycles(int numberOfCycles) {
		this.cycles = numberOfCycles;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
		experiment.setContext(this);
	}

	public OrganismStore getOrganismStore() {
		return organismStore;
	}

	public void setOrganismStore(OrganismStore organismStore) {
		this.organismStore = organismStore;
	}

	public Feeder getFeeder() {
		return feeder;
	}

	public void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}

	public OrganismLifecycleStrategy getOrganismLifecycleStrategy() {
		return organismLifecycleStrategy;
	}

	public void setOrganismLifecycleStrategy(OrganismLifecycleStrategy organismLifecycleStrategy) {
		this.organismLifecycleStrategy = organismLifecycleStrategy;
	}

	public BreedStrategy getBreedStrategy() {
		return breedStrategy;
	}

	public void setBreedStrategy(BreedStrategy breedStrategy) {
		this.breedStrategy = breedStrategy;
	}
	
	public List<ExperimentListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<ExperimentListener> listeners) {
		this.listeners = listeners;
	}
	
	public void addListener(ExperimentListener listener) {
		listeners.add(listener);
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
	
	public void registerResource(String key, Object resource) {
		resources.put(key, resource);
	}
	
	public Object getResource(String key) {
		return resources.get(key);
	}

	public String getDiskStorePath() {
		return diskStorePath;
	}

	public void setDiskStorePath(String diskStorePath) {
		this.diskStorePath = diskStorePath;
	}

}
