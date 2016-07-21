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

public class ExperimentPrimeRunner implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(ExperimentPrimeRunner.class);
	
	protected static final String DEVELOPMENT_DATA_PATH = "com/intermancer/predictor/test/data/sp500-ascii/GSPC.TXT";
	public static final int DEFAULT_PREDICTIVE_WINDOW_SIZE = 4;
	public static final int DEFAULT_NUMBER_OF_MUTATIONS_FOR_INIT = 5;
	public static final int DEFAULT_NUMBER_OF_CYCLES = 10000;
	public static final String CYCLES_METER_NAME = "cycles";
	
	private BreedStrategy breedStrategy;
	private OrganismLifecycleStrategy experimentStrategy;
	private InMemoryQuickAndDirtyOrganismStore organismStore;
	private ExperimentContext context;
	
	private List<ExperimentListener> listeners;
	
	private ExperimentCycleResult lastExperimentCycleResult;
	private boolean continueExperimenting;

	private String diskStorePath;
	
	public ExperimentPrimeRunner() {
		listeners = new ArrayList<ExperimentListener>();
		context = new ExperimentContext();
		organismStore = new InMemoryQuickAndDirtyOrganismStore();
		context.setOrganismStore(organismStore);
		context.setListeners(listeners);
	}
	
	public ExperimentPrimeRunner(boolean initialize) throws Exception {
		this();
		if (initialize) {
			init();
		}
	}

	public void init() throws Exception {
		
		Experiment experiment = new DefaultExperiment();
		context.setExperiment(experiment);
		setUpFeeder();
		setUpEvaluator();
		setUpBreeder();
		DefaultOrganismStoreInitializer.fillStore(organismStore, context.getFeeder(), breedStrategy, diskStorePath);
		setUpExperimentStrategy();
		context.setListeners(listeners);
		context.getExperiment().init();
		
		continueExperimenting = false;
	}

	private void setUpFeeder() {
		Reader fileReader = getFileReader(DEVELOPMENT_DATA_PATH);
		Feeder feeder = new SimpleRF(fileReader);
		feeder = new BufferedFeeder(feeder);
		context.setFeeder(feeder);
	}

	private void setUpExperimentStrategy() {
		experimentStrategy = new ExperimentPrimeStrategy(breedStrategy, context.getFeeder());
		context.setOrganismLifecycleStrategy(experimentStrategy);
	}

	private void setUpBreeder() {
		breedStrategy = new DefaultBreedStrategy();
		DefaultMutationContext mutationContext = new DefaultMutationContext();
		breedStrategy = new MutationBreedStrategyWrapper(breedStrategy, DEFAULT_NUMBER_OF_MUTATIONS_FOR_INIT,
				new DefaultMutationAssistant(), mutationContext);
		context.setBreedStrategy(breedStrategy);
	}

	private void setUpEvaluator() {
		PredictiveEvaluator evaluator = new PredictiveEvaluator();
		evaluator.setPredictiveWindowSize(DEFAULT_PREDICTIVE_WINDOW_SIZE);
		evaluator.setTargetOffset(1);
		context.getFeeder().addFeedCycleListener(evaluator);
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
	
	public void addExperimentListener(ExperimentListener experimentListener) {
		listeners.add(experimentListener);
	}
	
	public ExperimentContext getContext() {
		return context;
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
		logger.debug("cycles:{}", context.getCycles());
		logger.debug("continueExperimenting:{}", continueExperimenting);
		init();
		for (int iteration = 0; (iteration < context.getCycles()) || continueExperimenting; iteration++) {
			context.setIteration(iteration);
			ExperimentCycleResult experimentCycleResult = context.getExperiment().runExperimentCycle();
			lastExperimentCycleResult = experimentCycleResult;
		}
	}

	public boolean isContinueExperimenting() {
		return continueExperimenting;
	}

	public void setContinueExperimenting(boolean continueExperimenting) {
		this.continueExperimenting = continueExperimenting;
	}

	public void setDiskStorePath(String diskStorePath) {
		this.diskStorePath = diskStorePath;
	}

}
