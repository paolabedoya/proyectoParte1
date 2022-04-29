package com.proyectfinal.file.model;

import java.util.ArrayList;
import java.util.List;

public class Decision {
	private String id;
	
	private String name;
	
	private List<InformationRequirement> requirements;
	
	private List<DecisionTable> decisionTables;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DecisionTable> getDecisionTables() {
		if(decisionTables == null){
			decisionTables = new ArrayList<DecisionTable>();
		}
		return decisionTables;
	}

	public void setDecisionTables(List<DecisionTable> decisionTables) {
		this.decisionTables = decisionTables;
	}

	public List<InformationRequirement> getRequirements() {
		if(requirements == null) {
			requirements = new ArrayList<InformationRequirement>();
		}
		return requirements;
	}

	public void setRequirements(List<InformationRequirement> requirements) {
		this.requirements = requirements;
	}
	
	
	
}
