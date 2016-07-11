package com.intermancer.predictor.experiment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.intermancer.predictor.evaluator.PredictiveEvaluator;
import com.intermancer.predictor.feeder.BufferedFeeder;
import com.intermancer.predictor.feeder.Feeder;
import com.intermancer.predictor.feeder.SimpleRF;
import com.intermancer.predictor.mutation.DefaultMutationAssistant;
import com.intermancer.predictor.mutation.DefaultMutationContext;
import com.intermancer.predictor.mutation.MutationBreedStrategyWrapper;
import com.intermancer.predictor.organism.breed.BreedStrategy;
import com.intermancer.predictor.organism.breed.DefaultBreedStrategy;
import com.intermancer.predictor.organism.store.DefaultOrganismStoreInitializer;
import com.intermancer.predictor.organism.store.InMemoryQuickAndDirtyOrganismStore;
import com.intermancer.predictor.organism.store.OrganismStore;

public class ExperimentPrimeRunner implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(ExperimentPrimeRunner.class);
	
	protected static final String DEVELOPMENT_DATA_PATH = "com/intermancer/predictor/test/data/sp500-ascii/GSPC.TXT";
	public static final int DEFAULT_PREDICTIVE_WINDOW_SIZE = 4;
	public static final int DEFAULT_NUMBER_OF_MUTATIONS_FOR_INIT = 5;
	public static final int DEFAULT_NUMBER_OF_CYCLES = 10000;
	
	private Feeder feeder;
	private Experiment experiment;
	private BreedStrategy breedStrategy;
	private ExperimentStrategy experimentStrategy;
	private OrganismStore organismStore;
	
	private List<ExperimentListener> listeners;
	
	private int cycles;
	private int iteration;
	private ExperimentCycleResult lastExperimentCycleResult;
	private boolean continueExperimenting;
	private ExperimentResult lastExperimentResult;
	
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
		
		cycles = DEFAULT_NUMBER_OF_CYCLES;
		continueExperimenting = false;
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
		ExperimentCycleResult cycleResult = experiment.runExperimentCycle();
		listenersProcessExperimentCycleResult(cycleResult);
		return cycleResult;
	}
	
	public void runExperimentSeries(ExperimentContext context) throws Exception {
		init();
		for (int i = 0; i < context.getNumberOfCycles(); i++) {
			runExperimentCycle();
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

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public void setOrganismStore(OrganismStore organismStore) {
		this.organismStore = organismStore;
	}

	public void addExperimentListener(ExperimentListener experimentListener) {
		listeners.add(experimentListener);
	}
	
	public void setCycles(int cycles) {
		this.cycles = cycles;
	}
	
	public int getCycles() {
		return cycles;
	}
	
	public int getIteration() {
		return iteration;
	}
	
	public ExperimentCycleResult getLastExperimentCycleResult() {
		return lastExperimentCycleResult;
	}

	@Override
	public void run() {
		try {
			startExperiment();
		} catch (Exception ex) {
			logger.error("There was an exception in running the experiment on a background thread.", 
					ex);
		}
	}

	public synchronized void startExperiment() throws Exception {
		logger.debug("Starting experiment run");
		ExperimentResult experimentResult = new ExperimentResult();
		experimentResult.setCycles(cycles);
		experimentResult.setStartHighScore(getOrganismStore().getHighestScore());
		experimentResult.setStartLowScore(getOrganismStore().getLowestScore());
		long millisStart = System.currentTimeMillis();
		
		int parentsReplaced = 0;
		logger.debug("cycles:{}", cycles);
		logger.debug("continueExperimenting:{}", continueExperimenting);
		for (iteration = 0; (iteration < cycles) || continueExperimenting; iteration++) {
			ExperimentCycleResult experimentCycleResult = experiment.runExperimentCycle();
			if (experimentCycleResult.isParentWasReplaced()) {
				parentsReplaced++;
			}
			lastExperimentCycleResult = experimentCycleResult;
			experimentResult.setIteration(iteration);
		}
		
		experimentResult.setImprovementCycles(parentsReplaced);
		experimentResult.setFinishHighScore(getOrganismStore().getHighestScore());
		experimentResult.setFinishLowScore(getOrganismStore().getLowestScore());
		long millisEnd = System.currentTimeMillis();
		experimentResult.setDurationInMillis(millisEnd - millisStart);
		
		lastExperimentResult = experimentResult;
	}

	public boolean isContinueExperimenting() {
		return continueExperimenting;
	}

	public void setContinueExperimenting(boolean continueExperimenting) {
		this.continueExperimenting = continueExperimenting;
	}

	public ExperimentResult getLastExperimentResult() {
		return lastExperimentResult;
	}
	
}
