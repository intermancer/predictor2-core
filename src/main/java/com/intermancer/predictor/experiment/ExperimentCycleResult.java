package com.intermancer.predictor.experiment;

import java.util.List;

import com.intermancer.predictor.organism.store.OrganismStoreRecord;

public class ExperimentCycleResult {
	
	private List<OrganismStoreRecord> ancestors;
	private List<OrganismStoreRecord> children;
	private List<OrganismStoreRecord> finals;
	private boolean parentWasReplaced = false;
	
	public List<OrganismStoreRecord> getAncestors() {
		return ancestors;
	}

	public void setAncestors(List<OrganismStoreRecord> ancestors) {
		this.ancestors = ancestors;
	}

	public List<OrganismStoreRecord> getChildren() {
		return children;
	}
	
	public void setChildren(List<OrganismStoreRecord> children) {
		this.children = children;
	}
	
	public List<OrganismStoreRecord> getFinals() {
		return finals;
	}

	public void setFinals(List<OrganismStoreRecord> finals) {
		this.finals = finals;
	}

	public boolean isParentWasReplaced() {
		return parentWasReplaced;
	}

	public void setParentWasReplaced(boolean parentWasReplaced) {
		this.parentWasReplaced = parentWasReplaced;
	}
	
}
