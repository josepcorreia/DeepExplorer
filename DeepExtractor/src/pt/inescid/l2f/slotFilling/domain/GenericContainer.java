package pt.inescid.l2f.slotFilling.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Stores generic information about an entity. This class is used in various relation types, it contains dates, places, names and another types of informations. All the setters in this class are destructive methods. 
 * <P> The export methods return a String that is an XML representation of the information contained within an instance of a GenericContainer.
 * 
 * @author Filipe Carapinha [2012/May]
 * @version 1.0
 */
public class GenericContainer {

	//##############################################################################
	//  ###########################################################################
	//     ATTRIBUTES
	//  ###########################################################################
	//##############################################################################
	private String name;
	private Person person;
	private String organization;
	private String city;
	private String country;
	private String place;
	private String profession;
	private String domain;
	private String degree;
	private TimeNorm dateStart;
	private TimeNorm dateEnd;
	private TimeNorm duration;
	private TimeNorm date;

	//##############################################################################
	//  ###########################################################################
	//     CONSTUCTORS
	//  ###########################################################################
	//##############################################################################

	/**
	 * Constructor that builds an empty GenericContainer
	 * 
	 */
	public GenericContainer(){
		this.name = "";
		this.person = null;
		this.place = "";
		this.organization = "";
		this.profession = "";
		this.domain = "";
		this.degree = "";
		this.country = "";
		this.city = "";
		this.dateStart = null;
		this.dateEnd = null;
		this.duration = null;
		this.date = null;
	}

	//##############################################################################
	//  ###########################################################################
	//     GETTERS AND SETTERS
	//  ###########################################################################
	//##############################################################################
	public void setName(String name){
		this.name = name;
	}
	public void setPerson(Person person){
		this.person = person;
	}
	public void setPlace(String place){
		this.place = place;
	}
	public void setOrganization(String organization){
		this.organization = organization;
	}
	public void setProfession(String profession){
		this.profession = profession;
	}
	public void setDomain(String domain){
		this.domain = domain;
	}
	public void setDegree(String degree){
		this.degree = degree;
	}
	public void setCountry(String country){
		this.country = country;
	}
	public void setCity(String city){
		this.city = city;
	}
	public void setDateStart(TimeNorm dateStart){
		this.dateStart = dateStart;
	}
	public void setDateEnd(TimeNorm dateEnd){
		this.dateEnd = dateEnd;
	}
	public void setDate(TimeNorm date){
		this.date = date;
	}
	public void setDuration(TimeNorm duration){
		this.duration = duration;
	}

	public void setBirthOrDeath(String place, String city, String country, TimeNorm time){
		this.place = place;
		this.city = city;
		this.country = country;
		this.date = time;
	}

	public void setSpouse(String name, TimeNorm date, TimeNorm dateStart, TimeNorm dateEnd, TimeNorm duration){
		this.name = name;
		this.date = date;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.duration = duration;
	}

	public void setResidence(String place, String city, String country, TimeNorm dateStart, TimeNorm dateEnd, TimeNorm duration){
		this.place = place;
		this.city = city;
		this.country = country;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.duration = duration;
	}

	public void setEducation(String degree, String domain, String organization, String place, String city, String country, TimeNorm dateStart, TimeNorm dateEnd){
		this.degree = degree;
		this.domain = domain;
		this.organization = organization;
		this.place = place;
		this.city = city;
		this.country = country;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	public void setWork(String profession, String organization, String place, String city, String country, TimeNorm date, TimeNorm dateStart, TimeNorm dateEnd, TimeNorm duration){
		this.profession = profession;
		this.organization = organization;
		this.place = place;
		this.city = city;
		this.country = country;
		this.date = date;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.duration = duration;
	}

	public void setWorker(String profession, String name, String place, String city, String country, TimeNorm date, TimeNorm dateStart, TimeNorm dateEnd, TimeNorm duration){
		this.profession = profession;
		this.name = name;
		this.place = place;
		this.city = city;
		this.country = country;
		this.date = date;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.duration = duration;
	}

	public void setFounded(String organization, String place, String city, String country, TimeNorm date){
		this.organization = organization;
		this.place = place;
		this.city = city;
		this.country = country;
		this.dateStart = date;
	}
	public void setFounder(String name, String place, String city, String country, TimeNorm date){
		this.name = name;
		this.place = place;
		this.city = city;
		this.country = country;
		this.date = date;
	}

	public void setOwnedOrAffiliation(String organization, String place, String city, String country, TimeNorm date, TimeNorm dateStart, TimeNorm dateEnd, TimeNorm duration){
		this.organization = organization;
		this.place = place;
		this.city = city;
		this.country = country;
		this.date = date;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.duration = duration;
	}

	public void setOwnerOrAffiliated(String name, String place, String city, String country, TimeNorm date, TimeNorm dateStart, TimeNorm dateEnd, TimeNorm duration){
		this.name = name;
		this.place = place;
		this.city = city;
		this.country = country;
		this.date = date;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.duration = duration;
	}

	public void setClient(String name, String place, String city, String country, TimeNorm dateStart, TimeNorm dateEnd, TimeNorm duration){
		this.name = name;
		this.place = place;
		this.city = city;
		this.country = country;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.duration = duration;
	}

	public void setAlumni(String name, String degree, String domain, String place, String city, String country, TimeNorm dateStart, TimeNorm dateEnd){
		this.name = name;
		this.degree = degree;
		this.domain = domain;
		this.place = place;
		this.city = city;
		this.country = country;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	public void setClientOf(String organization, String place, String city, String country, TimeNorm dateStart, TimeNorm dateEnd, TimeNorm duration){
		this.organization = organization;
		this.place = place;
		this.city = city;
		this.country = country;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.duration = duration;
	}

	public void setBirthPlace(String place){
		this.place = place;
	}
	public void setDeathPlace(String place){
		this.place = place;
	}

	public void setBirthDate(TimeNorm date){
		this.date = date;
	}
	public void setDeathDate(TimeNorm date){
		this.dateStart = date;
	}



	public String getPlace(){
		return this.place;
	}

	public String getCity(){
		return this.city;
	}
	public String getCoutry(){
		return this.country;
	}

	public String getName(){
		return this.name;
	}

	public Person getPerson(){
		return this.person;
	}

	public String getProfession(){
		return this.profession;
	}

	public String getOrganization(){
		return this.organization;
	}

	public String getDomain(){
		return this.domain;
	}

	public String getDegree(){
		return this.degree;
	}

	public TimeNorm getDate(){
		return this.date;
	}

	public TimeNorm getDateStart(){
		return this.dateStart;
	}

	public TimeNorm getDateEnd(){
		return this.dateEnd;
	}

	public TimeNorm getDuration(){
		return this.duration;
	}



	/**
	 * Returns a String that is a representation of the information stored inside a GenericContainer Object. 
	 * This method is used to return a human output for the Slot Filling tasks.
	 * 
	 * @return	String
	 */
	public String pprintGenericContainer(){
		String out = "";
		if(!this.name.equals(""))
			out += "\tNAME: " + this.name + "\n";
		if(!this.organization.equals(""))
			out += "\tORGANIZATION: " + this.organization + "\n";
		if(!this.profession.equals(""))
			out += "\tPROFESSION: " + this.profession + "\n";
		if(!this.degree.equals(""))
			out += "\tDEGREE: " + this.degree + "\n";
		if(!this.domain.equals(""))
			out += "\tDOMAIN: " + this.domain + "\n";
		if(!this.place.equals(""))
			out += "\tPLACE: " + this.place + "\n";
		if(!this.city.equals(""))
			out += "\tCITY: " + this.city + "\n";
		if(!this.country.equals(""))
			out += "\tCOUNTRY: " + this.country + "\n";
		if(this.date != null)
			out += "\tDATE: \n" + this.date.pprintTimeNorm();
		if(this.dateStart != null)
			out += "\tSTART-DATE: \n" + this.dateStart.pprintTimeNorm();
		if(this.dateEnd != null)
			out += "\tEND-DATE: \n" + this.dateEnd.pprintTimeNorm();
		if(this.duration != null)
			out += "\tDURATION: \n" + this.duration.pprintTimeNorm();

		return out;
	}

	/**
	 * XML Output function.
	 * 
	 * Filipe Carapinha [2012/Sep]
	 */
	public void exportXMLDOMbirthOrDeath(Element parent, Document doc){
		if(!"".equals(this.place))
		{
			Element node = doc.createElement("PLACE");
			node.setAttribute("val", this.place);
			parent.appendChild(node);
		}
		if(!"".equals(this.city))
		{
			Element node = doc.createElement("CITY");
			node.setAttribute("val", this.city);
			parent.appendChild(node);
		}
		if(!"".equals(this.country))
		{
			Element node = doc.createElement("COUNTRY");
			node.setAttribute("val", this.country);
			parent.appendChild(node);
		}
		//TODO verificar se é preciso Start-date e end-date
		if(this.date != null)
		{
			Element node = doc.createElement("DATE");
			this.date.exportXMLDOM(node, doc);
			parent.appendChild(node);
		}
	}

	/**
	 * XML Output function.
	 * 
	 * Filipe Carapinha [2012/Sep]
	 */
	public void exportXMLDOMContents(Element parent, Document doc){
		if(!this.name.equals(""))
		{
			Element node = doc.createElement("PERSON");
			node.setAttribute("name", this.name);
			parent.appendChild(node);
		}		
		if(!this.organization.equals(""))
		{
			Element node = doc.createElement("ORGANIZATION");
			node.setAttribute("name", this.organization);
			parent.appendChild(node);
		}
		if(!this.profession.equals(""))
		{
			Element node = doc.createElement("PROFESSION");
			node.setAttribute("val", this.profession);
			parent.appendChild(node);
		}
		if(!this.degree.equals(""))
		{
			Element node = doc.createElement("DEGREE");
			node.setAttribute("val", this.degree);
			parent.appendChild(node);
		}	
		if(!this.domain.equals(""))
		{
			Element node = doc.createElement("DOMAIN");
			node.setAttribute("val", this.domain);
			parent.appendChild(node);
		}	
		if(!this.place.equals(""))
		{
			Element node = doc.createElement("PLACE");
			node.setAttribute("val", this.place);
			parent.appendChild(node);
		}	
		if(!this.city.equals(""))
		{
			Element node = doc.createElement("CITY");
			node.setAttribute("val", this.city);
			parent.appendChild(node);
		}	
		if(!this.country.equals(""))
		{
			Element node = doc.createElement("COUNTRY");
			node.setAttribute("val", this.country);
			parent.appendChild(node);
		}	
		if(this.date != null) 
		{
			Element node = doc.createElement("DATE");
			this.date.exportXMLDOM(node,doc);
			parent.appendChild(node);
		}	
		if(this.dateStart != null) 
		{
			Element node = doc.createElement("DATE-START");
			this.dateStart.exportXMLDOM(node,doc);
			parent.appendChild(node);
		}
		if(this.dateEnd != null)
		{
			Element node = doc.createElement("DATE-END");
			this.dateEnd.exportXMLDOM(node,doc);
			parent.appendChild(node);
		}
		if(this.duration != null)
		{
			Element node = doc.createElement("DURATION");
			this.duration.exportXMLDOM(node,doc);
			parent.appendChild(node);
		}

	}


	public void merge(GenericContainer c){
		if(this.name.equals(""))
			this.name = c.name;
		if(this.organization.equals(""))
			this.organization = c.organization;
		if(this.profession.equals(""))
			this.profession = c.profession;
		if(this.degree.equals(""))
			this.degree = c.degree;
		if(this.domain.equals(""))
			this.domain = c.domain;
		if(this.place.equals(""))
			this.place = c.place;
		if(this.city.equals(""))
			this.city = c.city;
		if(this.country.equals(""))
			this.country = c.country;
		if(this.date == null)
			this.date = c.date;
		if(this.dateStart == null)
			this.dateStart = c.dateStart;
		if(this.dateEnd == null)
			this.dateEnd = c.dateEnd;
		if(this.duration == null)
			this.duration = c.duration;		
	}

	/**
	 * Method equals for this class
	 * @param c GenericContainer to compare with this.
	 * @return true / false
	 */
	public boolean equals(GenericContainer c){
		if(this.name.equals(c.name) && this.organization.equals(c.organization) &&
				this.profession.equals(c.profession) && this.degree.equals(c.degree) &&
				this.domain.equals(c.domain) && this.place.equals(c.place) &&
				this.city.equals(c.city) && this.country.equals(c.country) &&
				((this.date != null && c.date != null && this.date.equals(c.date)) 
						|| (this.date == null && c.date == null)) && 
						((this.dateStart != null && c.dateStart != null && this.dateStart.equals(c.dateStart)) 
								|| (this.dateStart == null && c.dateStart == null)) &&
								((this.dateEnd != null && c.dateEnd != null && this.dateEnd.equals(c.dateEnd)) 
										||(this.dateEnd == null && c.dateEnd == null)) &&
										((this.duration != null && c.duration != null && this.duration.equals(c.duration)) 
												|| (this.duration == null && c.duration == null)))
			return true;
		else
			return false;
	}

	public boolean sameInfo(GenericContainer c){
		if(this.organization.equals(c.organization) &&
				this.profession.equals(c.profession) && this.degree.equals(c.degree) &&
				this.domain.equals(c.domain) && this.place.equals(c.place) &&
				this.city.equals(c.city) && this.country.equals(c.country) &&
				((this.date != null && c.date != null && this.date.equals(c.date)) 
						|| (this.date == null && c.date == null)) && 
						((this.dateStart != null && c.dateStart != null && this.dateStart.equals(c.dateStart)) 
								|| (this.dateStart == null && c.dateStart == null)) &&
								((this.dateEnd != null && c.dateEnd != null && this.dateEnd.equals(c.dateEnd)) 
										||(this.dateEnd == null && c.dateEnd == null)) &&
										((this.duration != null && c.duration != null && this.duration.equals(c.duration)) 
												|| (this.duration == null && c.duration == null)))
			return true;
		else
			return false;
	}
}
