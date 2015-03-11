package pt.inescid.l2f.xipapi.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Feature {

	//##############################################################################
	//   ###########################################################################
	//      ATTRIBUTES
	//   ###########################################################################
	//##############################################################################

	/** The feature name */
	private String name;

	/** The feature values */
	private Collection<String> values;


	//##############################################################################
	//   ###########################################################################
	//      CONSTUCTORS
	//   ###########################################################################
	//##############################################################################

	/**
	 * Constructor.
	 * 
	 */
	public Feature() {
	}


	/**
	 * Constructor.
	 * 
	 * @param name          feature name
	 * @param values        feature values (a collection)
	 */
	public Feature(String name, Collection<String> values) {
		this.name = name;
		this.values = values;
	}


	/**
	 * Constructor.
	 * 
	 * @param name          feature name
	 * @param value         feature value (a single value)
	 */
	public Feature(String name, String value) {
		this.name = name;
		this.values = new ArrayList<String>();
		this.values.add(value);
	}


	/**
	 * Constructor.
	 * 
	 * @param name          feature name
	 */
	public Feature(String name) {
		this.name = name;
		this.values = new ArrayList<String>();
	}


	//##############################################################################
	//   ###########################################################################
	//      GETTERS AND SETTERS
	//   ###########################################################################
	//##############################################################################

	/**
	 * Getter.
	 * @return the attribute name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter.
	 * @return the values
	 */
	public Collection<String> getValues() {
		return this.values;
	}

	/**
	 * Setter.
	 * @param name the name of this feature
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Setter.
	 * @param value a new value of the feature
	 */
	public void addValue(String value) {
		this.values.add(value);
	}




	//##############################################################################
	//   ###########################################################################
	//      CONVERTERS
	//   ###########################################################################
	//##############################################################################

	
	public String toString() {
		return getName() + "='" + getValues() + "'";
	}


	/**
	 * XML Output function.
	 * 
	 * Filipe Carapinha [2012/Sep]
	 */
	public void exportXMLDOM(Element parent, Document doc){
		if(this.getName().equals("ANAPHCOORDAUX")) //TODO: existe devido ao erro do COORD no módulo arm
			return ;


		Element thisNode = doc.createElement("FEATURE");
		thisNode.setAttribute("attribute", this.getName());

		for(String s : values){
			thisNode.setAttribute("value", s.replaceAll("\\s+", " ").trim());
			//TODO: Não deveria ser necessário, mas no caso da Feature COORD não funciona bem sem isto.
			break;
		}
		parent.appendChild(thisNode);

	}
}
