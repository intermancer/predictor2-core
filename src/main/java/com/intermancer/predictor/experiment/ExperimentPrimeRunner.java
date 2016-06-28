package com.intermancer.predictor.experiment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.intermancer.predictor.breed.BreedStrategy;
import com.intermancer.predictor.breed.DefaultBreedStrategy;
import com.intermancer.predictor.evaluator.PredictiveEvaluator;
import com.intermancer.predictor.experiment.organism.DefaultOrganismStoreInitializer;
import com.intermancer.predictor.experiment.organism.InMemoryQuickAndDirtyOrganismStore;
import com.intermancer.predictor.experiment.organism.OrganismStore;
import com.intermancer.predictor.feeder.BufferedFeeder;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.feeder.SimpleRF;
import com.intermancer.predictor.mutation.DefaultMutationAssistant;
import com.intermancer.predictor.mutation.DefaultMutationContext;
import com.intermancer.predictor.mutation.MutationBreedStrategyWrapper;

public class ExperimentPrimeRunner {
	
	protected static final String DEVELOPMENT_DATA_PATH = "com/intermancer/predictor/test/data/sp500-ascii/GSPC.TXT";
	protected static final int DEFAULT_PREDICTIVE_WINDOW_SIZE = 4;
	private static final int DEFAULT_NUMBER_OF_MUTATIONS_FOR_INIT = 5;
	private static final int DEFAULT_NUMBER_OF_CYCLES = 10000;
	
	private Feeder feeder;
	private Experiment experiment;
	private BreedStrategy breedStrategy;
	private ExperimentStrategy experimentStrategy;
	private OrganismStore organismStore;
	
	private List<ExperimentListener> listeners;
	
	private int numberOfIterations = DEFAULT_NUMBER_OF_CYCLES;
	
	public ExperimentPrimeRunner() {
		listeners = new ArrayList<ExperimentListener>();
	}

	public void init() throws Exception {
		
		experiment = new DefaultExperiment();
		
		Reader fileReader = getFileReader(DEVELOPMENT_DATA_PATH);
		feeder = new SimpleRF(fileReader);
		feeder = new BufferedFeeder(feeder);

		PredictiveEvaluator evaluator = new PredictiveEvaluator();
		evaluator.setPredictiveWindowSize(DEFAULT_PREDICTIVE_WINDOW_SIZE);
		feeder.addFeedCycleListener(evaluator);

		experiment.setFeeder(feeder);
		
		breedStrategy = new DefaultBreedStrategy();
		breedStrategy = new MutationBreedStrategyWrapper(breedStrategy, DEFAULT_NUMBER_OF_MUTATIONS_FOR_INIT,
				new DefaultMutationAssistant(), new DefaultMutationContext());
		experiment.setBreedStrategy(breedStrategy);
		
		organismStore = new InMemoryQuickAndDirtyOrganismStore();
		experiment.setOrganismStore(organismStore);
		
		experimentStrategy = new ExperimentPrimeStrategy(breedStrategy, feeder);
		experiment.setExperimentStrategy(experimentStrategy);

		DefaultOrganismStoreInitializer.fillStore(organismStore, feeder, breedStrategy);
		
		experiment.init();
		
		for (ExperimentListener listener : listeners) {
			listener.initializeExperimentListener(experiment);
		}
	}

	protected Reader getFileReader(String resourceClasspath) {
		Reader fileReader = null;
		try {
			fileReader =
					new InputStreamReader(new ClassPathResource(resourceClasspath).getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileReader;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public OrganismStore getOrganismStore() {
		return organismStore;
	}

	public ExperimentCycleResult runExperimentCycle() throws Exception {
		return experiment.runExperimentCycle();
	}
	
	public void runExperimentSeries(ExperimentContext context) throws Exception {
		init();
		for (int i = 0; i < getNumberOfIterations(); i++) {
			ExperimentCycleResult cycleResult = experiment.runExperimentCycle();
			listenersProcessExperimentCycleResult(cycleResult);
		}
		finalizeListeners();
	}

	private void finalizeListeners() {
		for (ExperimentListener listener : listeners) {
			listener.endExperiment(experiment);
		}
	}

	private void listenersProcessExperimentCycleResult(ExperimentCycleResult cycleResult) {
		for (ExperimentListener listener : listeners) {
			listener.processExperimentCycleResult(cycleResult, experiment);
		}
	}

	public Feeder getFeeder() {
		return feeder;
	}

	public void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}

	public BreedStrategy getBreedStrategy() {
		return breedStrategy;
	}

	public void setBreedStrategy(BreedStrategy breedStrategy) {
		this.breedStrategy = breedStrategy;
	}

	public ExperimentStrategy getExperimentStrategy() {
		return experimentStrategy;
	}

	public void setExperimentStrategy(ExperimentStrategy experimentStrategy) {
		this.experimentStrategy = experimentStrategy;
	}

	public int getNumberOfIterations() {
		return numberOfIterations;
	}

	public void setNumberOfIterations(int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public void setOrganismStore(OrganismStore organismStore) {
		this.organismStore = organismStore;
	}

	public void addExperimentListener(ExperimentListener experimentListener) {
		listeners.add(experimentListener);
	}
	
}
