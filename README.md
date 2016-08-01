# predictor2-core

Core library for the Predictor genetic algorithm framework. The Predictor uses a gene-based approach to model genetic algorithms and the system used to cultivate them.

predictor2-core is built with Maven:
    mvn clean install

predictor2-core is a support framework and does not provide the facilities to build a full genetic algorithm-based application.  [predictor2-svc](https://github.com/intermancer/predictor2-svc) provides a full REST-based service front-end.

## Overview
The Predictor is a genetic algorithm framework modeled used to look for relationships between streams of numeric data.

### Basic Execution
1. Instantiate the ExperimentPrimeRunner.
1. Call startExperiment() on the instance of ExperimentPrimeRunner.
1. After the experiment has completed, call getLastExperimentCycleResult().

### Main Classes

**ExperimentPrimeRunner** - This is a combination Experiment factory and Runnable implementation.  When I eventually add some kind of IoC, this will become just the Runnable piece.

**DefualtExperiment** - I will eventually need to rename this DefaultExperimentCycle or something like that.  This manages a single cycle of experimentation.

**ExperimentContext** - Maintains references to all of the components that are used to run an experiment.  This will also go away when I have a proper IoC framework.

**OrganismStore** - This should be renamed.  This doesn't really store Organisms.  It stores OrganismStoreRecords, which are organisms that have been evaluated and have a score.  I will eventually be generalizing this, because I will need to share organisms between experiments, and the scores will not be relevant in that case.

**Feeder** - Feeds the Organisms data.  Right now we are using a very simple file-based feeder that only knows about open, high, low, close, volume data for the S&P500 index.  We will eventually have a data service that will be able to serve fairly broad sets of related data.

**OrganismLifecycleStrategy** - Encapsulates picking ancestors, "breeding" and mutating them to create the next generation, feeding the organisms, and putting the evaluated organisms back in the OrganismStore.  I need to take the feeding part out and put that somewhere more generic, like the DefaultExperiment. 

**FeedCycleListener** - Called after every feed cycle.

**Evaluator** - Implemented as a FeedCycleListener.  This is what actually scores the Organism while it is being fed.  This, combined with the OrganismLifecycleStrategy, determines how the scoring effects the development of Organisms.