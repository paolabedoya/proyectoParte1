package com.proyectfinal.file.EPL;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proyectfinal.file.model.*;

public class FIleXML {


	//-----------------------------------------------------------------------------------------

	public List<Decision> parseXML (String path) throws ParserConfigurationException, SAXException, IOException{

		// Creo una instancia de DocumentBuilderFactory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// Creo un documentBuilder
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Obtengo el documento, a partir del XML
		Document documento = builder.parse(new File(path));

		List<Decision> decisions = new ArrayList<Decision>();

		NodeList decisionNodeList = documento.getElementsByTagName("decision");
		for(int i= 0; i < decisionNodeList.getLength(); i++) {

			Node decisionNode = decisionNodeList.item(i);
			Decision decision = parseDecision(decisionNode);
			decisions.add(decision);
		}

		return decisions;
	}

	private Decision parseDecision(Node decisionNode ) {

		Decision decision = new Decision();

		decision.setName(decisionNode.getAttributes().getNamedItem("name").getTextContent());
		decision.setId(decisionNode.getAttributes().getNamedItem("id").getTextContent());

		NodeList listaDecisionTalbeNodes = decisionNode.getChildNodes();

		for(int i= 0; i < listaDecisionTalbeNodes.getLength(); i++) {

			if(listaDecisionTalbeNodes.item(i).getNodeName().equals("decisionTable")) {
				Node decisionTableNode = listaDecisionTalbeNodes.item(i);
				DecisionTable decisionTable = parseDecisionTable(decisionTableNode);
				decision.getDecisionTables().add(decisionTable);
			}

			if(listaDecisionTalbeNodes.item(i).getNodeName().equals("informationRequirement")) {
				InformationRequirement informationRequirement = new InformationRequirement();
				informationRequirement.setId(listaDecisionTalbeNodes.item(i).getAttributes().getNamedItem("id").getTextContent());

				for(int j= 0; j < listaDecisionTalbeNodes.item(i).getChildNodes().getLength(); j++) {
					if(listaDecisionTalbeNodes.item(i).getChildNodes().item(j).getNodeName().equals("requiredDecision")) {
						informationRequirement.setHref(listaDecisionTalbeNodes.item(i).getChildNodes().item(j).getAttributes().getNamedItem("href").getTextContent());
					}
				}
				decision.getRequirements().add(informationRequirement);
			}
		}
		return decision;
	}

	private DecisionTable parseDecisionTable (Node decisionTableNode){
		DecisionTable decisionTable = new DecisionTable();

		decisionTable.setId(decisionTableNode.getAttributes().getNamedItem("id").getTextContent());
		if(decisionTableNode.getAttributes().getNamedItem("hitPolicy") != null) {
			decisionTable.setHitPolicy(decisionTableNode.getAttributes().getNamedItem("hitPolicy").getTextContent());
		}
		if(decisionTableNode.getAttributes().getNamedItem("aggregation") != null) {
			decisionTable.setAggregation(decisionTableNode.getAttributes().getNamedItem("aggregation").getTextContent());
		}

		NodeList listaInputOutputRuleNodes = decisionTableNode.getChildNodes();

		for(int i= 0; i < listaInputOutputRuleNodes.getLength(); i++) {

			Node inputOutputRuleNode = listaInputOutputRuleNodes.item(i);
			if(inputOutputRuleNode.getNodeName().equals("input")) {
				Input input = parseInput(inputOutputRuleNode);
				decisionTable.getInputs().add(input);
			}

			if(inputOutputRuleNode.getNodeName().equals("output")) {
				Output output = parseOutput(inputOutputRuleNode);
				decisionTable.getOutputs().add(output);
			}
			if(inputOutputRuleNode.getNodeName().equals("rule")) {
				Rule rule = parseRule(inputOutputRuleNode);
				decisionTable.getRules().add(rule);
			}

		}

		return decisionTable;

	}

	private Input parseInput (Node inputNode){

		Input input = new Input();
		if(inputNode.getAttributes().getNamedItem("label") != null) {
			input.setLabel(inputNode.getAttributes().getNamedItem("label").getTextContent());
		}
		input.setId(inputNode.getAttributes().getNamedItem("id").getTextContent());

		for (int i = 0; i< inputNode.getChildNodes().getLength();i++) {

			Node inputExpresionNode = inputNode.getChildNodes().item(i);
			if(inputExpresionNode.getNodeName().equals("inputExpression")) {

				input.setTypeRef(inputExpresionNode.getAttributes().getNamedItem("typeRef").getTextContent());
				for (int j = 0; j< inputExpresionNode.getChildNodes().getLength();j++) {
					if(inputExpresionNode.getChildNodes().item(j).getNodeName().equals("text")) {

						if(inputExpresionNode.getChildNodes().item(j).getChildNodes().getLength()>0) {
							input.setText(inputExpresionNode.getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
						}
					}
				}
			}
		}


		return input;
	}

	private Output parseOutput (Node outputNode){

		Output output = new Output();

		if(outputNode.getAttributes().getNamedItem("label") != null) {
			output.setLabel(outputNode.getAttributes().getNamedItem("label").getTextContent());
		}
		output.setId(outputNode.getAttributes().getNamedItem("id").getTextContent());
		if(outputNode.getAttributes().getNamedItem("name") != null) {
			output.setName(outputNode.getAttributes().getNamedItem("name").getTextContent());
		}
		output.setTypeRef(outputNode.getAttributes().getNamedItem("typeRef").getTextContent());

		return output;
	}

	private Rule parseRule (Node ruleNode){

		Rule rule = new Rule();

		rule.setId(ruleNode.getAttributes().getNamedItem("id").getTextContent());

		NodeList listaInputOutputNodes = ruleNode.getChildNodes();

		for(int i= 0; i < listaInputOutputNodes.getLength(); i++) {

			Node inputOutputNode = listaInputOutputNodes.item(i);
			if(inputOutputNode.getNodeName().equals("inputEntry")) {
				InputEntry inputEntry = parseInputEntry(inputOutputNode);
				rule.getInputEntrys().add(inputEntry);
			}
			if(inputOutputNode.getNodeName().equals("outputEntry")) {
				OutputEntry outputEntry = parseOutputEntry(inputOutputNode);
				rule.getOutputEntrys().add(outputEntry);
			}
		}

		return rule;
	}

	private InputEntry parseInputEntry (Node inputEntryNode){

		InputEntry inputEntry = new InputEntry();

		inputEntry.setId(inputEntryNode.getAttributes().getNamedItem("id").getTextContent());


		for (int i = 0; i< inputEntryNode.getChildNodes().getLength();i++) {

			if(inputEntryNode.getChildNodes().item(i).getNodeName().equals("text")) {
				if(inputEntryNode.getChildNodes().item(i).getChildNodes().getLength()>0) {
					inputEntry.setText(inputEntryNode.getChildNodes().item(i).getChildNodes().item(0).getNodeValue());
				}
			}
		}


		return inputEntry;
	}

	private OutputEntry parseOutputEntry (Node outputEntryNode){

		OutputEntry outputEntry = new OutputEntry();

		outputEntry.setId(outputEntryNode.getAttributes().getNamedItem("id").getTextContent());

		for (int i = 0; i< outputEntryNode.getChildNodes().getLength();i++) {

			if(outputEntryNode.getChildNodes().item(i).getNodeName().equals("text")) {
				if(outputEntryNode.getChildNodes().item(i).getChildNodes().getLength()>0) {
					outputEntry.setText(outputEntryNode.getChildNodes().item(i).getChildNodes().item(0).getNodeValue());
				}
			}
		}

		return outputEntry;
	}

	private String getOutput(List<Output> outputs, List<OutputEntry> outputEntrys) {

		String sentencia = "";

		for(int i= 0; i< outputs.size(); i++) {

			if(outputEntrys.get(i)!= null) {

				String outputText = outputs.get(i).getName();
				String outputEntryText = outputEntrys.get(i).getText();

				if(outputText != null && outputEntryText != null) {

					if(outputEntryText.startsWith("date")) {
						sentencia = sentencia + "(cast(".concat(outputEntryText.replace("date and time","")
								.replace("T"," ").replace("\"","'").concat(", Date, dateformat: 'yyyy-MM-dd HH:mm:ss')))")) + " as " + outputText + "," ;
					}else {
						sentencia = sentencia + outputEntryText.replaceAll("\"", "'") + " as " + outputText + ",";
					}

				}
			}
		}
		if(sentencia.length() > 2) {
			sentencia = sentencia.substring(0, sentencia.length()-1);
		}
		return sentencia;

	}

	private String getInputAny (Integer ruleIndex, List<Integer> sameRuleIndex, List<Rule> rules, List<Input> decisionTableInputs) {

		String sentencia = "";

		sentencia = sentencia + "(" + getInput(decisionTableInputs, rules.get(ruleIndex).getInputEntrys()) +  ")";

		for(Integer sameIndex : sameRuleIndex) {
			sentencia = sentencia + " OR (" + getInput(decisionTableInputs, rules.get(sameIndex).getInputEntrys()) + ") ";
		}

		return sentencia;
	}


	private String getInput(List<Input> inputs, List<InputEntry> inputEntrys) {

		String sentencia = "";

		for(int i= 0; i< inputs.size(); i++) {

			if(inputEntrys.get(i)!= null) {

				String inputText = inputs.get(i).getText();
				String typeRef = inputs.get(i).getTypeRef();
				String inputEntryText = inputEntrys.get(i).getText();

				if(inputText != null && inputEntryText != null) {

					if(typeRef.equals("integer") || typeRef.equals("long") || typeRef.equals("double") ) {

						if(inputEntryText.contains("[") || inputEntryText.contains("]")  ) {

							String [] parts = inputEntryText.split("\\.\\.");
							String ini = parts[0].substring(1);
							String fin = parts[1].substring(0, parts[1].length()-1);

							if (inputEntryText.startsWith("[") && inputEntryText.endsWith("]")) {
								sentencia = sentencia +  inputText + " >= " + ini + " and " + inputText + " <= " + fin + " and ";
							} else if (inputEntryText.startsWith("]") && inputEntryText.endsWith("]")) {
								sentencia = sentencia +  inputText + " > " + ini + " and " + inputText + " <= " + fin + " and ";
							} else if (inputEntryText.startsWith("]") && inputEntryText.endsWith("[")) {
								sentencia = sentencia +  inputText + " < " + ini + " and " + inputText + " > " + fin + " and ";
							} else if (inputEntryText.startsWith("[") && inputEntryText.endsWith("[")) {
								sentencia = sentencia +  inputText + " >= " + ini + " and " + inputText + " < " + fin + " and ";
							}

						}else {

							if (inputEntryText.trim().matches("[0-9]*")) {
								sentencia = sentencia + inputText + " = " + inputEntryText + " and ";
							}
							else {
								sentencia = sentencia + inputText + " " + inputEntryText + " and ";
							}
						}
					}

					if(typeRef.equals("boolean")) {
						if (inputEntryText.equals("true")) {
							sentencia = sentencia + inputText + " and ";
						} else {
							sentencia = sentencia + "Not " + inputText + " and ";
						}
					}

					if(typeRef.equals("date")) {

						if(inputEntryText != null && !inputEntryText.trim().equals("")) {

							String valor = inputEntryText;
							valor = valor.replaceAll("T", " ");

							if (valor.startsWith("[")) { // between
								String[] aux = valor.split("\"");
								sentencia = sentencia + inputText + ".between(cast(" + "'" + aux[1] + "'" + ", Date, dateformat: 'yyyy-MM-dd HH:mm:ss'),(cast("+"'"+aux[3]+"'"+", Date, dateformat: 'yyyy-MM-dd HH:mm:ss'))" + " and ";
							} else if (valor.startsWith(">")) {
								String[] aux = valor.split("\"");
								sentencia = sentencia + inputText + ".after(cast(" + "'" + aux[1] + "'" + ", Date, dateformat: 'yyyy-MM-dd HH:mm:ss'))" + " and ";
							} else if (valor.startsWith("<")) {
								// < date and time("2021-04-11T00:00:00")
								String[] aux = valor.split("\"");
								sentencia = sentencia + inputText + ".before(cast(" + "'" + aux[1] + "'" + ", Date, dateformat: 'yyyy-MM-dd HH:mm:ss'))" + " and ";
							} else {
								String[] aux = valor.split("\"");
								sentencia = sentencia + inputText + " = "+"(cast(" + "'" + aux[1] + "'" + ", Date, dateformat: 'yyyy-MM-dd HH:mm:ss'))" + " and ";
							}
						}

					}


					if(typeRef.equals("string")) {

						String valor = inputEntryText;

						valor = valor.replace("\"", "'");
						valor = valor.replace(" ", "");
						String[] aux;
						if(valor.startsWith("not")) {
							aux = valor.split("not");
							String aux2 = "";
							for (String s : aux) {
								aux2 += s;
							}
							aux = aux2.split(",");
						}else {
							aux = valor.split(",");
						}


						if (valor.startsWith("not") && aux.length > 0) {

							sentencia = sentencia + inputText + " not in (";
							for (int j = 0; j < aux.length; j++) {
								if (j + 1 >= aux.length) {
									sentencia += aux[j];
								} else {
									sentencia += aux[j] + ",";
								}
							}
							sentencia += ")";
							sentencia = sentencia + " and ";
						}

						else if (valor.startsWith("'") && aux.length > 0) {

							sentencia = sentencia + inputText +  " in (";
							for (int j = 0; j < aux.length; j++) {
								if (j + 1 >= aux.length) {
									sentencia += aux[j];
								} else
									sentencia += aux[j] + ",";
							}
							sentencia += ")";
							sentencia = sentencia + " and ";
						}

						else if(valor.trim().length() == 0)
						{

						}
						else {
							sentencia = sentencia + inputText + " = " + valor + " and ";
						}
					}
				}
			}
		}
		if(sentencia.length() > 2) {
			sentencia = sentencia.substring(0, sentencia.length()-4);
		}
		return sentencia;

	}

	// Crea el esquema de la tabla es generico para cualquiera de los tipos de archivos.
	public ArrayList<String> crearTablaParse(List<Decision> decisions) throws IOException {

		String ruta = "C:\\EPL\\outEPL\\";

		HashMap<String, String> createsMap = new HashMap<String, String>();
		HashMap<String, String> rulesMap = new HashMap<String, String>();

		for (Decision decision : decisions) {

			if(decision.getRequirements().size() <= 0) {

				String create = "";

				create += "create schema " + decision.getName() + "(" + "\n";
				create += "id Integer," + "\n"; // identificador predeterminado de la tabla

				for(int i = 0; i<decision.getDecisionTables().get(0).getInputs().size(); i++) {
					if(i<decision.getDecisionTables().get(0).getInputs().size() - 1) {
						create += decision.getDecisionTables().get(0).getInputs().get(i).getText() + " " + decision.getDecisionTables().get(0).getInputs().get(i).getTypeRef() + ",\n";
					}else {
						create += decision.getDecisionTables().get(0).getInputs().get(i).getText() + " " + decision.getDecisionTables().get(0).getInputs().get(i).getTypeRef() + "\n";
					}
				}
				create += ");\n";
				create += "\n";
				create += "\n";

				createsMap.put(decision.getName(), create);
			}

		}

		for (Decision decision : decisions) {

			String rules = "";

			String nombreTablaFrom = decision.getName();

			if(decision.getRequirements().size() > 0) {
				nombreTablaFrom = "";
				for(InformationRequirement informationRequirement : decision.getRequirements()) {
					String ref = informationRequirement.getHref();
					ref = ref.substring(1);
					String nameTablaRef = "";
					for(Decision decisionRef : decisions) {
						if(decisionRef.getId().equals(ref)) {
							nameTablaRef = decisionRef.getName();
							break;
						}
					}

					nombreTablaFrom = nombreTablaFrom + nameTablaRef + ".std:lastevent()" + ", ";

				}
				//System.out.println(nombreTablaFrom);
				nombreTablaFrom = nombreTablaFrom.substring(0, nombreTablaFrom.length()-2);

			}

			for (DecisionTable decisionTable : decision.getDecisionTables()) {

				if(decisionTable.getHitPolicy() == null || decisionTable.getHitPolicy().equals("UNIQUE")) {

					int rulesCount = 0;
					for(Rule rule: decisionTable.getRules()) {

						rules += "@Name('"+ decision.getName() +"_Rule" + rulesCount + "') \n";

						rules += "insert into " + decision.getName() + "_Output" + "\n";

						rules += "Select id," + "current_timestamp.format() as " +  "date_"+ decision.getName()+", " + getOutput(decisionTable.getOutputs(), rule.getOutputEntrys()) + "\n";

						rules += "from " + nombreTablaFrom + "\n";

						rules += "Where " + getInput(decisionTable.getInputs(), rule.getInputEntrys()) + "\n";

						rules += ";";
						rules += "\n";
						rules += "\n";


						rulesCount ++;
					}
				}

				else if(decisionTable.getHitPolicy().equals("RULE ORDER")) {

					int rulesCount = 0;
					int priority = decisionTable.getRules().size()-1;
					for(Rule rule: decisionTable.getRules()) {

						rules += "@Name('" + decision.getName() +"_Rule" + rulesCount + "') \n";

						rules += "@Priority(" + priority + ") \n";

						rules += "insert into " + decision.getName() + "_Output" + "\n";

						rules += "Select id, "+ "current_timestamp.format() as " +  "date_"+ decision.getName()+", " + getOutput(decisionTable.getOutputs(), rule.getOutputEntrys()) + "\n";

						rules += "from " + nombreTablaFrom + "\n";

						rules += "Where " + getInput(decisionTable.getInputs(), rule.getInputEntrys()) + "\n";

						rules += ";";
						rules += "\n";
						rules += "\n";


						rulesCount ++;
						priority --;
					}
				}

				else if(decisionTable.getHitPolicy().equals("FIRST")) {

					int rulesCount = 0;
					int priority = decisionTable.getRules().size()-1;
					for(Rule rule: decisionTable.getRules()) {

						rules += "@Name('" + decision.getName() +"_Rule" + rulesCount + "') \n";

						rules += "@drop \n";

						rules += "@Priority(" + priority + ") \n";

						rules += "insert into " + decision.getName() + "_Output" + "\n";

						rules += "Select id, " + "current_timestamp.format() as " +  "date_"+ decision.getName()+", " + getOutput(decisionTable.getOutputs(), rule.getOutputEntrys()) + "\n";

						rules += "from " + nombreTablaFrom + "\n";

						rules += "Where " + getInput(decisionTable.getInputs(), rule.getInputEntrys()) + "\n";

						rules += ";";
						rules += "\n";
						rules += "\n";


						rulesCount ++;
						priority --;
					}

				}

				else if(decisionTable.getHitPolicy().equals("COLLECT")) {

					if(decisionTable.getOutputs().size() == 1 &&
							(
									decisionTable.getOutputs().get(0).getTypeRef().equals("integer")
											||decisionTable.getOutputs().get(0).getTypeRef().equals("long")
											||decisionTable.getOutputs().get(0).getTypeRef().equals("double")
							)
					) {

						int rulesCount = 0;
						for(Rule rule: decisionTable.getRules()) {

							rules += "@Name('" + decision.getName() +"_Rule" + rulesCount + "') \n";

							rules += "insert into " + decision.getName() + "_Output" + "\n";

							rules += "Select id," + getOutput(decisionTable.getOutputs(), rule.getOutputEntrys()) + "\n";

							rules += "from " + nombreTablaFrom + "\n";

							rules += "Where " + getInput(decisionTable.getInputs(), rule.getInputEntrys()) + "\n";

							rules += ";";
							rules += "\n";
							rules += "\n";


							rulesCount ++;
						}

						rules += "@Name(" + decisionTable.getHitPolicy() + decision.getName() +")" + "\n";
						rules += "Insert into Result_output"+"\n";
						rules +=  "Select id, " + "current_timestamp.format() as " +  "date_"+ decision.getName()+ ", " + decisionTable.getAggregation() +"("+ decisionTable.getOutputs().get(0).getName() +")" + " "+ "as" + " " + "results"+ decisionTable.getAggregation() + "\n";
						rules +=  "From" + " " + decision.getName() + "_Output" + "\n";
						rules += "Group by id";
						rules += ";";
						rules += "\n";
						rules += "\n";
					}else {
						System.out.println("No corresponde");
						return;
					}

				}


				else if(decisionTable.getHitPolicy().equals("ANY")) {

					int rulesCount = 0;

					List<Integer> rulesOff = new ArrayList<Integer>();


					for(int h=0; h<decisionTable.getRules().size(); h++) {

						Rule rule = decisionTable.getRules().get(h);

						if(rulesOff.contains(h)) {
							continue;
						}

						List<Integer> sameOutputRules = checkRulesSameOutput(h, decisionTable.getRules());
						rulesOff.addAll(sameOutputRules);

						rules += "@Name('" + decision.getName()+"_Rule" + rulesCount + "') \n";

						rules += "insert into " + decision.getName() + "_Output" + "\n";

						rules += "Select id, "+ "current_timestamp.format() as " +  "date_"+ decision.getName()+", " + getOutput(decisionTable.getOutputs(), rule.getOutputEntrys()) + "\n";

						rules += "from " + nombreTablaFrom + "\n";

						rules += "Where " + getInputAny(h, sameOutputRules, decisionTable.getRules(), decisionTable.getInputs()) + "\n";

						rules += ";";
						rules += "\n";
						rules += "\n";


						rulesCount ++;
					}
				}
			}


			rulesMap.put(decision.getName() + "Rules", rules);
		}

		Map.Entry<String,String> ArchivoUnificado = createsMap.entrySet().iterator().next();
		String NameArchivo= ArchivoUnificado.getKey();

		ArrayList<String> rutasFicheros = new ArrayList<>();

		File ficheroUnificado = new File (ruta +"DMN"+ NameArchivo +".txt");
		BufferedWriter bws = new BufferedWriter(new FileWriter(ficheroUnificado));

		for(String createName : createsMap.keySet()) {

			File fichero = new File(ruta + createName + ".txt");

			fichero.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));

			bw.write(createsMap.get(createName));
			bws.write(createsMap.get(createName));
			rutasFicheros.add(ruta +"DMN"+ NameArchivo +".txt");
			bw.close();
		}

		for(String ruleName : rulesMap.keySet()) {

			File fichero = new File(ruta + ruleName + ".txt");

			fichero.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.write(rulesMap.get(ruleName));
			bws.write(rulesMap.get(ruleName));
			bw.close();

		}
		bws.close();

		return rutasFicheros;
	}

	private List<Integer> checkRulesSameOutput (Integer indexRule, List<Rule> rules){

		List<Integer> rulesSameOutput = new ArrayList<Integer>();
		List<String> outputsEntry = new ArrayList<String>();

		for(OutputEntry outputEntry : rules.get(indexRule).getOutputEntrys()) {
			outputsEntry.add(outputEntry.getText());
		}

		int index = 0;
		for(Rule rule: rules) {
			if(index == indexRule) {
				index++;
				continue;
			}

			if(outputsEntry.size() == rule.getOutputEntrys().size()) {
				boolean contieneTodos = true;
				for(int i=0;i<rule.getOutputEntrys().size(); i++) {
					if(outputsEntry.get(i) == null && rule.getOutputEntrys().get(i).getText() == null ) {
						continue;
					}
					if(outputsEntry.get(i) != null && rule.getOutputEntrys().get(i).getText() == null ) {
						contieneTodos = false;
						break;
					}
					if(outputsEntry.get(i) == null && rule.getOutputEntrys().get(i).getText() != null ) {
						contieneTodos = false;
						break;
					}
					if(!outputsEntry.get(i).equals(rule.getOutputEntrys().get(i).getText())){
						contieneTodos = false;
						break;
					}
				}
				if(contieneTodos) {
					rulesSameOutput.add(index);
				}
			}
			index ++;
		}
		return rulesSameOutput;

	}
}
