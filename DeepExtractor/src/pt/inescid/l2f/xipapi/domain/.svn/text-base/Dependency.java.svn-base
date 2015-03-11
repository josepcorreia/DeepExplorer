package pt.inescid.l2f.xipapi.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Dependency {

	//##############################################################################
	//   ###########################################################################
	//      ATTRIBUTES
	//   ###########################################################################
	//##############################################################################

	/** The dependency name */
	private String name;

	/** Filipe Carapinha [May/2012]*/
	private int senteceNumber;

	/** The dependency nodes: the arguments of the dependency */
	private Collection<XIPNode> nodes;

	/** The dependency features */
	private Collection<Feature> features;

	/** ?????????????????????? */      //<------------------   ??????????????????
	private Collection<Anaphor> anaphors;


	//##############################################################################
	//   ###########################################################################
	//      CONSTUCTORS
	//   ###########################################################################
	//##############################################################################

	/**
	 * Constructor.
	 * 
	 * @param name   the dependency name
	 */
	public Dependency(String name) {
		//super();                // <------------------- não é disparate??????
		this.name = name;
		this.nodes    = new ArrayList<XIPNode>();
		this.features = new ArrayList<Feature>();
		this.anaphors = new ArrayList<Anaphor>();
	}

	/**
	 * Constructor. Filipe Carapinha [2012/May]
	 * 
	 * @param name   the dependency name
	 * @param sentenceNumber	the sentence number
	 */
	public Dependency(String name, int sentenceNumber) {
		//super();                // <------------------- não é disparate??????
		this.name = name;
		this.senteceNumber = sentenceNumber;
		this.nodes    = new ArrayList<XIPNode>();
		this.features = new ArrayList<Feature>();
		this.anaphors = new ArrayList<Anaphor>();
	}


	//##############################################################################
	//   ###########################################################################
	//      GETTERS AND SETTERS
	//   ###########################################################################
	//##############################################################################

	/**
	 * Getter.
	 * @return the dependency name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter.
	 * @return the dependency nodes: the arguments of the dependency
	 */
	public Collection<XIPNode> getNodes() {
		return nodes;
	}

	/**
	 * Getter.
	 * @return the dependency features
	 */
	public Collection<Feature> getFeatures() {
		return features;
	}

	/**
	 * Getter.
	 * @return the the list of anaphora that are contained within this dependency.
	 */
	public Collection<Anaphor> getAnaphoras(){
		return this.anaphors;
	}

	/**
	 * Getter
	 * @return the sentence number of the sentence which this dependency belongs.
	 */
	public int getSentenceNumber(){
		return this.senteceNumber;
	}

	/**
	 * Setter.
	 * @param name the dependency name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Setter.
	 * @param node a new argument of the dependency
	 */
	public void addNode(XIPNode node) {
		nodes.add(node);
	}

	/**
	 * Setter.
	 * @param feature a new feature of the dependency
	 */
	public void addFeatures(Feature feature) {
		features.add(feature);
	}

	/**
	 * Setter.
	 *@param anaphor ??????????????????????????????
	 */
	public void addAnaphor(Anaphor anaphor){
		this.anaphors.add(anaphor);
	}



	//##############################################################################
	//   ###########################################################################
	//      CONVERTERS
	//   ###########################################################################
	//##############################################################################

	/**
	 * 
	 * @return the string representing the dependency (its name)
	 */
	public String toString() {
		return this.getName();
	}



	/**
	 * XML Output function.
	 * 
	 * Filipe Carapinha [2012/Sep]
	 */
	public void exportXMLDOM(Element parent, Document doc){
		int i = 0;
		Element thisNode = doc.createElement("DEPENDENCY");
		thisNode.setAttribute("name", this.getName());

		for(Feature f : this.getFeatures()){
			f.exportXMLDOM(thisNode, doc);
		}
		String aux = "";
		for(XIPNode n : this.getNodes()){
			aux = n.getSentence();
			aux = aux.replaceAll("\\s+", " ").trim();

			Element param = doc.createElement("PARAMETER");
			param.setAttribute("ind", "" + i);
			param.setAttribute("num", n.getNodeNumber());
			param.setAttribute("word", aux);
			++i;
			thisNode.appendChild(param);
		}

		for(Anaphor a : anaphors){
			a.exportXMLDOM(thisNode, doc);
		}


		parent.appendChild(thisNode);

	}

	/**
	 * 
	 * @return String that represents a Dependency instance.
	 */
	public String printDependency(){
		String res = "";
		String aux;
		res += this.getName();

		for(Feature f : this.getFeatures()){
			res += "_" + f.getName();
		}

		res += "(";
		for(XIPNode n : this.getNodes()){
			aux = n.getSentence();
			aux = aux.replaceAll("\\s+", " ");
			aux = aux.trim();			
			res += aux + ",";
		}
		res = res.replaceAll(",$", ")");
		return res;
	}

}
