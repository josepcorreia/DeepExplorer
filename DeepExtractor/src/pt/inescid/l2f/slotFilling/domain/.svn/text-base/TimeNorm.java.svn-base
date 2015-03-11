package pt.inescid.l2f.slotFilling.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Stores Time Normalization information about an entity.
 * 
 * @author Filipe Carapinha [2012/May]
 * @version 1.0
 */
public class TimeNorm{

	//##############################################################################
	//  ###########################################################################
	//     ATTRIBUTES
	//  ###########################################################################
	//##############################################################################
	public String string;
	public String val_norm;
	public String val_delta;
	public String umed;
	public String val_norm1;
	public String val_norm2;
	public String val_delta1;
	public String val_delta2;
	public String val_quant;


	//##############################################################################
	//  ###########################################################################
	//     CONSTUCTORS
	//  ###########################################################################
	//##############################################################################

	/**
	 * Constructor
	 * 
	 * @param string
	 * @param val_norm
	 * @param val_delta
	 * @param umed
	 * @param val_norm1
	 * @param val_norm2
	 * @param val_delta1
	 * @param val_delta2
	 * @param val_quant
	 */
	public TimeNorm(String string, String val_norm, String val_delta, String umed, String val_norm1, String val_norm2, String val_delta1, String val_delta2, String val_quant){
		this.string = string;
		this.val_norm = val_norm;
		this.val_delta = val_delta;
		this.umed = umed;
		this.val_norm1 = val_norm1;
		this.val_norm2 = val_norm2;
		this.val_delta1 = val_delta1;
		this.val_delta2 = val_delta2;
		this.val_quant = val_quant;
	}

	/**
	 * Constructor
	 * 
	 * @param string representation of a date.
	 */
	public TimeNorm(String string){
		this.string = string;
		this.val_norm = "";
		this.val_delta = "";
		this.umed = "";
		this.val_norm1 = "";
		this.val_norm2 = "";
		this.val_delta1 = "";
		this.val_delta2 = "";
		this.val_quant = "";
	}

	//##############################################################################
	//  ###########################################################################
	//     CONVERTERS
	//  ###########################################################################
	//##############################################################################

	/**
	 * XML Output function.
	 * 
	 * Filipe Carapinha [2012/Sep]
	 */	
	public void exportXMLDOM(Element parent, Document doc){
		parent.setAttribute("string", this.string);
		parent.setAttribute("valnorm", this.val_norm);
		parent.setAttribute("valdelta", this.val_delta);
		parent.setAttribute("umed", this.umed);
		parent.setAttribute("valnorm1", this.val_norm1);
		parent.setAttribute("valnorm2", this.val_norm2);
		parent.setAttribute("valdelta1", this.val_delta1);
		parent.setAttribute("valdelta2", this.val_delta2);
		parent.setAttribute("valquant", this.val_quant);
	}

	/**
	 * Returns a String that is a representation of the information stored inside a TimeNorm Object. 
	 * This method is used to return a human output for the Slot Filling tasks.
	 * 
	 * @return	String
	 */
	public String pprintTimeNorm(){
		String out = "";
		if(!this.string.equals(""))
			out += "\t\tSTRING: " + this.string + "\n";
		if(!this.val_norm.equals(""))
			out += "\t\tVAL-NORM: " + this.val_norm + "\n";
		if(!this.val_delta.equals(""))
			out += "\t\tVAL-DELTA: " + this.val_delta + "\n";
		if(!this.umed.equals(""))
			out += "\t\tUMED: " + this.umed + "\n";
		if(!this.val_quant.equals(""))
			out += "\t\tVAL-QUANT: " + this.val_quant + "\n";
		if(!this.val_norm1.equals(""))
			out += "\t\tVAL-NORM1: " + this.val_norm1 + "\n";
		if(!this.val_norm2.equals(""))
			out += "\t\tVAL-NORM2: " + this.val_norm2 + "\n";
		if(!this.val_delta1.equals(""))
			out += "\t\tVAL-DELTA1: " + this.val_delta1 + "\n";
		if(!this.val_delta2.equals(""))
			out += "\t\tVAL-DELTA2: " + this.val_delta2 + "\n";

		return out;
	}

	public boolean equals(TimeNorm t){
		if(this.string.equals(t.string) && this.val_norm.equals(t.val_norm) &&
				this.val_delta.equals(t.val_delta) && this.umed.equals(t.umed) &&
				this.val_quant.equals(t.val_quant) && this.val_norm1.equals(t.val_norm1) &&
				this.val_norm2.equals(t.val_norm2) && this.val_delta1.equals(t.val_delta1) &&
				this.val_delta2.equals(t.val_delta2))
			return true;
		else return false;
	}
}