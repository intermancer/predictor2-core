package com.intermancer.predictor.experiment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

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
import com.intermancer.predictor.organism.store.InMemoryQuickAndDirtyOrganismStoreIndex;

public class ExperimentPrimeRunner implements Runnable {

	private static final Logger logger = LogManager.getLogger(ExperimentPrimeRunner.class);

	protected static final String DEVELOPMENT_DATA_PATH = "com/intermancer/predictor/test/data/sp500-ascii/SP500.txt";
	public static final int DEFAULT_PREDICTIVE_WINDOW_SIZE = 4;
	public static final int DEFAULT_NUMBER_OF_MUTATIONS_FOR_INIT = 5;
	public static final int DEFAULT_NUMBER_OF_CYCLES = 10000;
	public static final String CYCLES_METER_NAME = "cycles";

	private ExperimentContext context;

	private ExperimentCycleResult lastExperimentCycleResult;
	private boolean continueExperimenting;
	private boolean experimentRunning = false;

	public ExperimentPrimeRunner() {
		context = new ExperimentContext();
		context.setOrganismStoreIndex(new InMemoryQuickAndDirtyOrganismStoreIndex());
		setUpFeeder();
		setUpEvaluator();
		setUpBreeder();
		setUpExperimentStrategy();
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
		DefaultOrganismStoreInitializer.fillStore(context.getOrganismStore(), context.getOrganismStoreIndex(), context.getFeeder(),
				context.getBreedStrategy(), context.getDiskStorePath());
		context.getExperiment().init();
	}

	private void setUpFeeder() {
		Reader fileReader = getFileReader(DEVELOPMENT_DATA_PATH);
		Feeder feeder = new SimpleRF(fileReader);
		feeder = new BufferedFeeder(feeder);
		context.setFeeder(feeder);
	}

	private void setUpExperimentStrategy() {
		OrganismLifecycleStrategy experimentStrategy = new ExperimentPrimeStrategy(context.getBreedStrategy(),
				context.getFeeder());
		context.setOrganismLifecycleStrategy(experimentStrategy);
	}

	private void setUpBreeder() {
		BreedStrategy breedStrategy = new DefaultBreedStrategy();
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
			fileReader = new InputStreamReader(new ClassPathResource(resourceClasspath).getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileReader;
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
			logger.error("There was an exception in running the experiment on a background thread.", ex);
		}
	}

	public synchronized void startExperiment() throws Exception {
		if (experimentRunning == false) {
			synchronized (this) {
				if (experimentRunning == false) {
					_startExperiment();
				}
			}
		}
	}

	private void _startExperiment() throws Exception {
		experimentRunning = true;
		logger.debug("Starting experiment run");
		logger.debug("cycles:{}", context.getCycles());
		logger.debug("continueExperimenting:{}", continueExperimenting);
		init();
		context.setExperimentStartTime();
		for (int iteration = 0; (iteration < context.getCycles()) || continueExperimenting; iteration++) {
			context.setIteration(iteration + 1);
			ExperimentCycleResult experimentCycleResult = context.getExperiment().runExperimentCycle();
			lastExperimentCycleResult = experimentCycleResult;
		}
		experimentRunning = false;
	}

	public boolean isContinueExperimenting() {
		return continueExperimenting;
	}

	public void setContinueExperimenting(boolean continueExperimenting) {
		this.continueExperimenting = continueExperimenting;
	}

}
