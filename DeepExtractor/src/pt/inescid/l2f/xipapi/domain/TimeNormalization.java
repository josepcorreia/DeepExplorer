package pt.inescid.l2f.xipapi.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

public class TimeNormalization {

	//##############################################################################
	//   ###########################################################################
	//      ATTRIBUTES
	//   ###########################################################################
	//##############################################################################

	/** The surface-string of the node to be normalized */
	private String name;

	/** The normalization: property-value pairs */
	private Collection<Feature> features;


	//##############################################################################
	//   ###########################################################################
	//      CONSTUCTORS
	//   ###########################################################################
	//##############################################################################

	/**
	 * Constructor.
	 * 
	 * @param name surface-string of the node to be normalized
	 */
	public TimeNormalization(String name) {
		super();
		this.name = name;
		this.features = new ArrayList<Feature>();
	}


	//##############################################################################
	//   ###########################################################################
	//      GETTERS AND SETTERS
	//   ###########################################################################
	//##############################################################################

	/**
	 * Getter.
	 * @return the surface-string of the node to be normalized
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter.
	 * @return normalization: all the property-value pairs
	 */
	public Collection<Feature> getFeatures() {
		return features;
	}

	/**
	 * Getter.
	 * @param name the name of a feature (ex. ValNorm)
	 * @return the feature with 'name' (the argument)]
	 */
	//@author Filipe Carapinha [2010/May]
	public Feature getFeature(String name){
		for(Feature f : features){
			if(f.getName().equals(name))
				return f;
		}
		return null;
	}


	/**
	 * Setter.
	 * @param name surface-string of the node to be normalized
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Setter.
	 * @param feature a new feature (part of the normalization)
	 */
	public void addFeatures(Feature feature) {
		features.add(feature);
	}



	//##############################################################################
	//   ###########################################################################
	//      CONVERTERS
	//   ###########################################################################
	//##############################################################################


	/**
	 * 
	 * @return the string representing the time-normalization (its name)
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
		for(Feature f : features){
			f.exportXMLDOM(parent, doc);
		}
	}
}

