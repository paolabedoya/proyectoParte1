package com.proyectfinal.file.model;

import java.util.ArrayList;
import java.util.List;

public class DecisionTable {
	
	private String id;

	private String hitPolicy;

	private String aggregation;
	
	private List<Input> inputs;
	
	private List<Output> outputs;
	
	private List<Rule> rules;
	

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Input> getInputs() {
		if(inputs == null) {			
			inputs = new ArrayList<Input>();
		}
		return inputs;
	}

	public void setInputs(List<Input> inputs) {
		this.inputs = inputs;
	}

	public List<Output> getOutputs() {
		if(outputs == null) {
			outputs = new ArrayList<Output>();
		}
		return outputs;
	}

	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}

	public List<Rule> getRules() {
		if(rules == null) {
			rules = new ArrayList<Rule>();
		}
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public String getHitPolicy() {
		return hitPolicy;
	}

	public void setHitPolicy(String hitPolicy) {
		this.hitPolicy = hitPolicy;
	}

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}


	
	
}
