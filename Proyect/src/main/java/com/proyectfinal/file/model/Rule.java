package com.proyectfinal.file.model;

import java.util.ArrayList;
import java.util.List;

public class Rule {

	private String id;
	
	private List<InputEntry> inputEntrys;
	
	private List <OutputEntry> outputEntrys;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<InputEntry> getInputEntrys() {
		if(inputEntrys == null) {
			inputEntrys = new ArrayList<InputEntry>();
		}
		return inputEntrys;
	}

	public void setInputEntrys(List<InputEntry> inputEntrys) {
		this.inputEntrys = inputEntrys;
	}

	public List<OutputEntry> getOutputEntrys() {
		if(outputEntrys == null) {
			outputEntrys = new ArrayList<OutputEntry>();
		}
		return outputEntrys;
	}

	public void setOutputEntrys(List<OutputEntry> outputEntrys) {
		this.outputEntrys = outputEntrys;
	}
	
	
	
}
