package pt.inescid.l2f.xipapi.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.*;

import pt.inescid.l2f.globalVars.ConstantVals;
import pt.inescid.l2f.slotFilling.domain.*;
import pt.inescid.l2f.xipapi.exception.NodeDoesNotExistException;

public class XipDocument {

	//##############################################################################
	//   ###########################################################################
	//      ATTRIBUTES
	//   ###########################################################################
	//##############################################################################

	/** The document's name */
	private String name;

	/** The ???????????????? */
	private Document document;

	/** The sentences */
	private LinkedList<XIPNode> sentences;

	/** The dependencies */
	private Collection<Dependency> dependencies;

	/** The Organizations that have been identified */
	private Vector<Organization> entitiesOrganization;

	/** The Persons that have been identified */
	private Vector<Person> entitiesPerson;


	//##############################################################################
	//   ###########################################################################
	//      CONSTUCTORS
	//   ###########################################################################
	//##############################################################################

	/**
	 * Constructor.
	 *
	 * @param name      ?????????
	 * @param document  ?????????
	 */
	public XipDocument(String name, Document document) {
		this.name = name;
		this.document = document;
		this.sentences = new LinkedList<XIPNode>();
		this.dependencies = new ArrayList<Dependency>();
		this.entitiesOrganization = new Vector<Organization>();
		this.entitiesPerson = new Vector<Person>();
	}


	/**
	 * Constructor.
	 *
	 * @param document  ?????????
	 */
	public void setDocument(Document document) {
		this.document = document;
	}


	//##############################################################################
	//   ###########################################################################
	//      GETTERS AND SETTERS
	//   ###########################################################################
	//##############################################################################

	public Document getDocument() {
		return document;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void removeSentence(XIPNode sentence) {
		this.sentences.remove(sentence);
	}

	public void addSentence(XIPNode sentence) {
		this.sentences.add(sentence);
	}

	public void addSentences(LinkedList<XIPNode> sentences) {
		this.sentences.addAll(sentences);
	}

	public LinkedList<XIPNode> getSentences() {
		return sentences;
	}

	public String getText() {
		String res = "";
		for (XIPNode sentence : sentences) {
			for (XIPNode node : sentence.getNodes()) {
				res += node.toString() + " ";
			}
		}
		return res;
	}

	public void addDependencies(LinkedList<Dependency> dependencies) {
		this.dependencies.addAll(dependencies);
	}

	public void addDependency(Dependency dependency) {
		dependencies.add(dependency);
	}

	public void removeDependency(Dependency dependency) {
		dependencies.remove(dependency);
	}

	/**
	* This method adds all dependencies in a sorted manner. Adds each dependency just after those from the same sentence.
	* @param dependencies to add
	*
	* Viviana Cabrita [2014/April]
	*/
	public void addAndSortDependencies(LinkedList<Dependency> dependencies) {
		ArrayList<Dependency> newList = new ArrayList<Dependency>(this.dependencies);

		int k = 0;
		for(int i = 0; i < dependencies.size(); i++){
			if(k < newList.size()){
				while(newList.get(k).getSentenceNumber() <= dependencies.get(i).getSentenceNumber()){
					k++;
					if(k == newList.size()){
						break;
					}
				}
				newList.add(k,dependencies.get(i));
			}
			else{
				newList.add(k,dependencies.get(i));
				k++;
			}
		}
		this.dependencies = newList;
	}

	/**
	 * This method returns all the Dependencies that belong to sentence with the input index.
	 *
	 * @param index number of the sentence,
	 * @return Vector of Dependencies.
	 * Filipe Carapinha [2012/May]
	 */
	public Vector<Dependency> getSentenceDependecies(int index){
		Vector<Dependency> vector= new Vector<Dependency>();
		for(Dependency d : this.getDependencies()){
			if(d.getSentenceNumber() == index)
				vector.add(d);
		}
		return vector;
	}

	/**
	 * This method returns the number of sentences within the document.
	 * @return number of sentences.
	 * Filipe Carapinha [2012/May]
	 */
	public int getNumberOfSentences(){
		return this.getSentences().size();
	}

	/**
	 * Checks if a node exists on the document's dependencies
	 *
	 * @param node
	 * @return boolean
	 */
	public boolean existsInDependencies(XIPNode node) {
		for (Dependency dependency : this.dependencies) {
			if (dependency.getNodes().contains(node)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Checks if a node exists on the document's dependencies with this name
	 *
	 * @param node
	 * @param dependencyName
	 * @return boolean
	 */
	public boolean existsInDependency(XIPNode node, String dependencyName) {
		for (Dependency dependency : this.dependencies) {
			if (dependency.getName().equals(dependencyName)) {
				if (dependency.getNodes().contains(node)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the document's dependencies.
	 *
	 * @return Collection<Dependency> dependencies
	 */
	public Collection<Dependency> getDependencies() {
		return dependencies;
	}

	/**
	 * @deprecated Exports the document to a string in the xml format.
	 *
	 * @return xml document
	 */
	public String export(){
		String res = "";

		return res;
	}
	/*	public String export() {
		String res = "";
		for (XIPNode node : sentences) {
			res += "<" + node.getName() + ">" + node.export() + "</" + node.getName() + ">";
			for(XIPNode n : node.getNodes()){
				System.out.println(n.getNodes());
				for(Feature f : n.getFeatures()){
					res += f;
				}
			}

		}
		return res;
	}*/

	/**
	 * Searches a node in the present document, by its id
	 *
	 * @param id
	 * @return id
	 * @throws NodeDoesNotExistException
	 */
	public XIPNode getNodeById(String id) throws NodeDoesNotExistException {

		for (XIPNode node : sentences) {
			if (node.getId().equals(id)) {
				return node;
			} else {
				XIPNode temp;
				try {
					temp = node.getNodeById(id);
					return temp;
				} catch (NodeDoesNotExistException e) {
					// continue
				}

			}
		}

		throw new NodeDoesNotExistException();
	}

	/**
	 *
	 * @param sentenceNumber
	 * @param num
	 * @return XIPNode
	 * @throws NodeDoesNotExistException
	 */
	public XIPNode getNode(int sentenceNumber, String num)
			throws NodeDoesNotExistException {
		XIPNode res = null;

		for (XIPNode node : sentences.get(sentenceNumber).getNodes()) {
			if (node.getNodeNumber().equals(num))
				res = node;

			if (res != null)
				return res;

			res = node.getNode(num);

			/** Filipe Carapinha [May/2012]
			 * this line was missing and causing problems with dependencies
			 *
			 */
			if (res != null)
				return res;
		}

		throw new NodeDoesNotExistException();

	}


	/**
	 * Searches a node in the present document, by its id
	 *
	 * @deprecated
	 * @param num
	 * @param word
	 * @return XIPNode
	 * @throws NodeDoesNotExistException
	 */
	public XIPNode getNodeByNumAndWord(String num, String word)
			throws NodeDoesNotExistException {

		XIPNode temp = null;
		for (XIPNode node : sentences) {
			if (node.getNodeNumber().equals(num) && node.getName().equals(word)) {
				return node;
			} else {
				temp = node.getNodeByNumAndWord(num, word);
			}
		}

		if (temp != null)
			return temp;

		throw new NodeDoesNotExistException();
	}

	public String toString() {
		return this.getName();
	}

	public Collection<Dependency> getDependenciesByNodeId(XIPNode node){
		Collection<Dependency> res = new LinkedList<Dependency>();
		for(Dependency d : this.getDependencies()){
			for(XIPNode n : d.getNodes()){
				try {
					node.getNodeById(n.getId());
					res.add(d);
					break;
				} catch (NodeDoesNotExistException e) {
					//e.printStackTrace();
				}
			}
		}

		return res;
	}
	/**
	 * Filipe Carapinha [2012/May]
	 *
	 *
	 */
	public void setPersons(Vector<Person> v){
		this.entitiesPerson = v;
	}

	public void setOrganizations(Vector<Organization> v){
		this.entitiesOrganization = v;
	}

	/**
	 * @param number
	 * @return XIPNode
	 * @throws NodeDoesNotExistException
	 */
	public XIPNode getNodeByNumber(String number) throws NodeDoesNotExistException {

		for (XIPNode node : sentences) {
			if (node.getNodeNumber().equals(number)) {
				return node;
			} else {
				XIPNode temp;
				try {
					temp = node.getNodeByNumber(number);
					return temp;
				} catch (NodeDoesNotExistException e) {
					// continue
				}

			}
		}

		throw new NodeDoesNotExistException();
	}

	/**
	 * Filipe Carapinha [JavaDoc 2012/May]
	 * @param nodeId
	 * @param anaphor
	 */
	public void addDependencyAnaphor(String nodeId, Anaphor anaphor){
		for(Dependency d : this.dependencies){
			if(!d.getName().contains("ACANDIDATE"))
				for(XIPNode n : d.getNodes()){
					if(n.getId().equals(nodeId)){
						d.addAnaphor(anaphor);
					}
				}
		}
	}

	/**
	 * Filipe Carapinha [JavaDoc 2012/May]
	 * @param nodeID
	 * @param index indice onde a procura deve começar (minimiza o espaço de procura).
	 * @return Dependências que contêm o nó com o ID pedido como primeiro parâmetro.
	 */
	public Collection<Dependency> getDependencysByNodeNumber(String nodeID, int index){
		Collection<Dependency> result = new ArrayList<Dependency>();
		ArrayList<XIPNode> nodes;
		XIPNode n;
		int max = this.dependencies.size();
		Dependency d = null;
		ArrayList<Dependency> array = (ArrayList<Dependency>)this.dependencies;

		for(int i = index; i<max ; ++i){ //Dependency d : this.dependencies){
			d = array.get(i);
			nodes = (ArrayList<XIPNode>)d.getNodes();
			n = nodes.get(0);
			//			for(Feature f : d.getFeatures()){
			//				if(f.getName().equals(ConstantVals.eventLex))
			//					return result;
			//			}
			if(n.getId().equals(nodeID) && d.getName().equals(ConstantVals.event))//Só adiciona event à lista
				result.add(d);
		}

		return result;
	}


	//##############################################################################
	//   ###########################################################################
	//      CONVERTERS
	//   ###########################################################################
	//##############################################################################

	/**
	 * XML Output function.
	 *
	 * Filipe Carapinha [2012/Sep]
	 */
	public String exportXMLDOM(){
		try {

			Collection<Dependency> dep = null;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("XIPRESULT");
			rootElement.setAttribute("math", "0");
			doc.appendChild(rootElement);



			for(XIPNode node: sentences){
				//res += "<LUNIT language=\"Portuguese\">";
				Element lunit = doc.createElement("LUNIT");

				Attr attr = doc.createAttribute("language");
				attr.setValue("Portuguese");
				lunit.setAttributeNode(attr);

				rootElement.appendChild(lunit);
				node.exportXMLDOM(lunit, doc);

				dep = null;
				dep = this.getDependenciesByNodeId(node);

				for(Dependency d : dep){
					d.exportXMLDOM(lunit, doc);
				}

			}

			Element frames = doc.createElement("FRAMES");
			for(Person p : entitiesPerson){
				p.exportXMLDOM(frames, doc);
			}
			//System.out.println();
			for(Organization o : entitiesOrganization){
				o.exportXMLDOM(frames, doc);
			}
			rootElement.appendChild(frames);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
//			<?xml version="1.0" encoding="UTF-8" standalone="no"?>
			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);


		}catch(Exception e){
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Human Output function.
	 *
	 * @param fileName	String that represents the Human version of an XipDocument instance.
	 * Filipe Carapinha [2012/May]
	 */
	public void printDependencies(String fileName) {

		Map<String, String> keywords = new HashMap<String, String>();
		int count_dep = 0;
		String line = "";

		if(fileName != null) {
			try{
				BufferedReader buffer = new BufferedReader(new FileReader(fileName));
				while((line = buffer.readLine()) != null) {
					line = line.replaceAll("^\\s+", "");
					if(!line.equals("") && !line.startsWith("/")) {
						String keyword[] = line.split("[,\\.:]");
						if(!keyword[0].equals("Hidden")) {
							keywords.put(keyword[0], keyword[0]);
						}
					}
				}
				buffer.close();
			}catch(Exception e1){
				System.err.println("AferXIP: file->" + fileName + " not found");
				e1.printStackTrace();
			}
		}
		int previous = 0;
		//Caso em que existem dependências a esconder.
		//Verifica a que frase pertence a dependência através do sentenceNumber.
		if(!keywords.isEmpty()) {
			for(Dependency d : this.getDependencies()){
				++count_dep;
				if(!keywords.containsKey(d.getName())){
					if(previous == d.getSentenceNumber()){
						System.out.println(d.printDependency());

					}else{
						//TODO : imprimir TOP
						System.out.print(previous + ">");
						System.out.println(this.sentences.get(previous).buildTree());
						System.out.println();

						previous = d.getSentenceNumber();
						System.out.println();
						System.out.println(d.printDependency());
					}
				}
				//TODO : imprimir TOP
				if(this.getDependencies().size() == count_dep){
					System.out.print(previous + ">");
					System.out.println(this.sentences.get(previous).buildTree());
					System.out.println();
				}
			}
			//c.c.
		} else {
			for(Dependency d : this.getDependencies()){
				++count_dep;
				if(previous == d.getSentenceNumber()){
					System.out.println(d.printDependency());
				}else{
					//TODO : imprimir TOP
					System.out.print(previous + ">");
					System.out.println(this.sentences.get(previous).buildTree());
					System.out.println();

					previous = d.getSentenceNumber();
					System.out.println();
					System.out.println(d.printDependency());
				}
				//TODO : imprimir TOP
				if(this.getDependencies().size() == count_dep){
					System.out.print(previous + ">");
					System.out.println(this.sentences.get(previous).buildTree());
					System.out.println();
				}
			}
		}
	}
}
