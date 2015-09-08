package pt.inescid.l2f.xipapi.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pt.inescid.l2f.xipapi.exception.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class XIPNode {

	//##############################################################################
	//   ###########################################################################
	//      ATTRIBUTES
	//   ###########################################################################
	//##############################################################################

	/** The id of the node */
	private String id;

	/** The name of the node */
	private String name;

	/** The start-index of the node */
	private String start;

	/** The end-index of the node */
	private String end;

	/** The parent node of the node */
	private XIPNode parentNode;

	/** The node number (a string) of the node */
	private String nodeNumber;

	/** The parent document of the node */   // <------------???????
	private XipDocument parentDocument;

	/** The features of the node */
	private HashMap<String, Feature> features;

	/** The leaf nodes of the node */
	private LinkedList<XIPNode> nodes;

	/** The sentence number of the node */
	private int sentenceNumber;

	/** The time normalization of the node (when a time expression) */
	private TimeNormalization normalizacao;


	//##############################################################################
	//   ###########################################################################
	//      CONSTUCTORS
	//   ###########################################################################
	//##############################################################################

	/**
	 * Constructor.
	 * 
	 * @param parentNode      parent node
	 * @param name            name of the node
	 * @param start           start-index of the node
	 * @param end             end-index of the node
	 * @param nodeNumber      node number (string)
	 * @param sentenceNumber  sentence number
	 * @param nodeNum         node number
	 */
	public XIPNode(XIPNode parentNode, String name, String start, String end,
			String nodeNumber, int sentenceNumber, int nodeNum) {
		this.parentNode     = parentNode;
		this.parentDocument = null;
		this.name           = name;
		this.start          = start;
		this.end            = end;
		this.features       = new HashMap<String, Feature>();
		this.nodes          = new LinkedList<XIPNode>();
		this.normalizacao   = null;
		this.nodeNumber     = nodeNumber;
		this.sentenceNumber = sentenceNumber;
		this.id             = nodeNum + "";
	}


	/**
	 * Constructor.
	 * 
	 * @param parentDocument  parent document
	 * @param name            name of the node
	 * @param start           start-index of the node
	 * @param end             end-index of the node
	 * @param nodeNumber      node number (string)
	 * @param sentenceNumber  sentence number
	 * @param nodeNum         node number
	 */
	public XIPNode(XipDocument parentDocument, String name, String start,
			String end, String nodeNumber, int sentenceNumber, int nodeNum) {
		this.parentNode     = null;
		this.parentDocument = parentDocument;
		this.name           = name;
		this.start          = start;
		this.end            = end;
		this.features       = new HashMap<String, Feature>();
		this.nodes          = new LinkedList<XIPNode>();
		this.nodeNumber     = nodeNumber;
		this.id             = nodeNum + "";
	}


	//##############################################################################
	//   ###########################################################################
	//      GETTERS AND SETTERS
	//   ###########################################################################
	//##############################################################################

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public TimeNormalization getTimeNormalization() {
		return this.normalizacao;
	}

	public Collection<Feature> getFeatures() {
		return features.values();
	}

	public Feature getFeatures(String name) throws FeatureDoesNotExistException {
		Feature feature = features.get(name);
		if (feature == null)
			throw new FeatureDoesNotExistException(name);

		return feature;
	}

	public LinkedList<XIPNode> getNodes() {
		return nodes;
	}




	public void setTimeNormalization(TimeNormalization norm) {
		this.normalizacao = norm;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void addFeatures(Collection<Feature> features) {
		for (Feature feature : features) {
			this.features.put(feature.getName(), feature);
		}
	}

	public void addFeature(Feature feature) {
		this.features.put(feature.getName(), feature);
	}

	public void removeFeature(Feature feature) {
		this.features.remove(feature);
	}





	public boolean containsFeature(String name) {
		return this.features.containsKey(name);
	}


	public void addNode(XIPNode node) {
		this.nodes.add(node);
	}

	public void removeNode(XIPNode node) {
		this.nodes.remove(node);
	}

	public XIPNode getNodeById(String id) throws NodeDoesNotExistException {

		for (XIPNode node : nodes) {
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

		throw new NodeDoesNotExistException(id);

	}

	/**
	 * @param number  Number of the node to return.
	 * @return XIPNode  
	 * @throws NodeDoesNotExistException
	 */
	public XIPNode getNodeByNumber(String number) throws NodeDoesNotExistException {

		for (XIPNode node : nodes) {
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

		throw new NodeDoesNotExistException(number);

	}

	/**
	 *  Returns the node that has his nodeNumber == num.
	 * @param num
	 * @return XIPNode
	 */
	public XIPNode getNode(String num) {
		XIPNode res = null;

		for (XIPNode node : nodes) {
			if (node.getNodeNumber().equals(num))
				res = node;

			if (res != null) 
				return res;

			res = node.getNode(num);
		}

		return res;

	}

	/**
	 * @deprecated
	 * @param num
	 * @param word
	 * @return XIPNode
	 * @throws NodeDoesNotExistException
	 */
	public XIPNode getNodeByNumAndWord(String num, String word)
			throws NodeDoesNotExistException {

		for (XIPNode node : nodes) {
			if (node.getNodeNumber().equals(num) && node.getName().equals(word)) {
				return node;
			} else {
				XIPNode temp;
				try {
					temp = node.getNodeByNumAndWord(num, word);
					return temp;
				} catch (NodeDoesNotExistException e) {
					// continue
				}
			}
		}

		throw new NodeDoesNotExistException(id);
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setParent(XIPNode parent) {
		this.parentNode = parent;
	}

	public XIPNode getParent() {
		return parentNode;
	}

	public XIPNode getParentPhrase() throws NodeHasNoParentPhraseException {
		XIPNode result = null;
		XIPNode temp;
		temp = getParent();

		while(!temp.getName().equals("TOP")) {
			result = temp;
			temp = temp.getParent();
		}

		if(result == null) {
			throw new NodeHasNoParentPhraseException(getId());
		}
		return result;
	}

	/**
	 * @deprecated
	 * @return String
	 */
	public String export() {
		String res = "";
		for (XIPNode node : getNodes()) {
			if (node instanceof Token) {
				res += ((Token) node).toString();
			} else {
				try {
					String anaph = " " + node.getFeatures("anaphora").toString() + " ";
					String ant = node.getFeatures("antecedent").toString();

					res += "<" + node.getName() + " id=" + node.getId() + " "
							+ anaph + ant + ">" + node.export() + "</"
							+ node.getName() + ">";
				} catch (FeatureDoesNotExistException e) {
					res += "<" + node.getName() + " id=" + node.getId() + ">" +
							node.export() + "</" + node.getName() + ">";
				}

			}
		}
		return res;
	}


	public String getSentence() {
		String res = "";
		for (XIPNode node : getNodes()) {
			if (node instanceof Token) {
				res += ((Token) node).toString() + " ";
			} else {
				if(!node.getEnd().equals("-10"))
					res += node.getSentence() + " ";
			}
		}
		return res;
	}


	/**
	 * This method returns a String that contains (if it exists) a person name from the node.
	 * @return String
	 */
	public String getPersonName() {
		String res = "";
		for (XIPNode node : getNodes()) {
			if(this.containsFeature("PROPER") || this.containsFeature("END_PEOPLE") || this.containsFeature("CONT_PEOPLE") || this.containsFeature("START_PEOPLE")) {
				if (node instanceof Token) {
					res += ((Token) node).toString() + " ";
				} else {
					res += node.getPersonName();
				}
			}
		}
		if("".equals(res))
			return getSentence().trim(); //evita preenchimento de nomes com ""
		return res;
	}


	/**
	 * This method returns a list of Tokens contained in the node.
	 * @return LinkedList<Token>
	 */
	public LinkedList<Token> getTokens() {
		LinkedList<Token> tokens = new LinkedList<Token>();
		for (XIPNode node : getNodes()) {
			if (node instanceof Token)
				tokens.add((Token) node);
			else
				tokens.addAll(node.getTokens());
		}
		return tokens;
	}

	public void setNodeNumber(String nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public String getNodeNumber() {
		return nodeNumber;
	}

	public void setSentenceNumber(int sentenceNumber) {
		this.sentenceNumber = sentenceNumber;
	}

	public int getSentenceNumber() {
		return sentenceNumber;
	}

	public XipDocument getParentDocument() {
		return parentDocument;
	}

	/**
	 * 
	 * @return Previous sibling in parent's list of child nodes
	 * @throws NodeHasNoSiblingsException
	 * @throws NodeHasNoPreviousSiblingException
	 */
	public XIPNode getPreviousSibling() throws NodeHasNoSiblingsException,
	NodeHasNoPreviousSiblingException {
		XIPNode parent = this.getParent();
		XIPNode previousSibling = null;
		if (parent == null)
			throw new NodeHasNoSiblingsException(this.getId());

		Iterator<XIPNode> it = parent.getNodes().iterator();
		while (it.hasNext()) {
			XIPNode temp = it.next();
			if (this.equals(temp))
				break;
			else
				previousSibling = temp;
		}

		if (previousSibling == null)
			throw new NodeHasNoPreviousSiblingException(this.getId());

		return previousSibling;
	}

	/**
	 * 
	 * @return Next sibling in parent's list of child nodes
	 * @throws NodeHasNoSiblingsException
	 * @throws NodeHasNoPreviousSiblingException
	 * 
	 */
	public XIPNode getNextSibling() throws NodeHasNoSiblingsException,
	NodeHasNoNextSiblingException {
		XIPNode parent = this.getParent();
		XIPNode nextSibling = null;
		if (parent == null)
			throw new NodeHasNoSiblingsException(this.getId());

		Iterator<XIPNode> it = parent.getNodes().descendingIterator();
		while (it.hasNext()) {
			XIPNode temp = it.next();
			if (this.equals(temp))
				break;
			else
				nextSibling = temp;
		}

		if (nextSibling == null)
			throw new NodeHasNoNextSiblingException(this.getId());

		return nextSibling;
	}


	//##############################################################################
	//   ###########################################################################
	//      CONVERTERS
	//   ###########################################################################
	//##############################################################################

	/**
	 * 
	 * @return the string representing the node (its name)
	 */
	public String toString() {
		return getName();
	}

	/**
	 * XML Output function.
	 * 
	 * Filipe Carapinha [2012/Sep]
	 */
	public void exportXMLDOM(Element parent, Document doc){

		Element thisNode = doc.createElement("NODE");
		thisNode.setAttribute("num", this.getNodeNumber());
		thisNode.setAttribute("tag", this.getName());
		thisNode.setAttribute("start", this.getStart());
		thisNode.setAttribute("end", this.getEnd());

		for(Feature f : this.getFeatures()){
			f.exportXMLDOM(thisNode, doc);
		}

		if (normalizacao != null) {
			normalizacao.exportXMLDOM(thisNode, doc);
		}

		LinkedList<Token> t = new LinkedList<Token>();
		for (XIPNode node : nodes) {
			if (!(node instanceof Token))
				node.exportXMLDOM(thisNode,doc);
			else
				t.add((Token)node);
		}

		// Estou a assumir que quando existe mais do que 1 Token estamos no nó raiz da frase.
		//LinkedList<Token> t = this.getTokens();
		if (t != null && t.size() == 1){
			//res += "<TOKEN end=\"" + this.end + "\" pos=\"" + t.get(0).getPos() + "\" start=\"" + start + "\">";
			t.get(0).exportXMLDOM(thisNode, doc);
			//res += "</TOKEN>";
		}

		parent.appendChild(thisNode);
	}

	/**
	 * This method builds a string with the sentence tree representation.
	 */
	public String buildTree(){
		String res = "";
		res += this.getName() + "{";
		//int size = this.nodes.size();
		for(XIPNode n : this.getNodes()){
			if(!n.getEnd().equals("-10")) // This line is to avoid the final nodes that are not relevant.
				res += n.crawlerPT();
		}
		res += "}";

		res = res.replaceAll("\\{\\s+", "{");

		return res;
	}

	/**
	 * Aux method of buildTree
	 * @return String 
	 */
	private String crawlerPT(){
		String res = "";
		if(this.getNodes().size() == 1 && this.getNodes().get(0) instanceof Token){
			res += " " + this.getSentence().trim();
			return res;
		}

		res += " " + this.getName() + "{";
		for(XIPNode n : this.getNodes()){
			res += n.crawlerPT();
		}
		res += "}";
		return res;
	}

}
