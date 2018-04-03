package com.intermancer.predictor.experiment;

import java.util.List;

import com.intermancer.predictor.organism.store.OrganismIndexRecord;

public class ExperimentCycleResult {
	
	private List<OrganismIndexRecord> ancestors;
	private List<OrganismIndexRecord> children;
	private List<OrganismIndexRecord> finals;
	private boolean parentWasReplaced = false;
	
	public List<OrganismIndexRecord> getAncestors() {
		return ancestors;
	}

	public void setAncestors(List<OrganismIndexRecord> ancestors) {
		this.ancestors = ancestors;
	}

	public List<OrganismIndexRecord> getChildren() {
		return children;
	}
	
	public void setChildren(List<OrganismIndexRecord> children) {
		this.children = children;
	}
	
	public List<OrganismIndexRecord> getFinals() {
		return finals;
	}

	public void setFinals(List<OrganismIndexRecord> finals) {
		this.finals = finals;
	}

	public boolean isParentWasReplaced() {
		return parentWasReplaced;
	}

	public void setParentWasReplaced(boolean parentWasReplaced) {
		this.parentWasReplaced = parentWasReplaced;
	}
	
}
