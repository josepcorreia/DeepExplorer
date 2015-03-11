package pt.inescid.l2f.slotFilling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Stores information about a Organization entity type.
 * 
 * @author Filipe Carapinha [2012/May]
 * @version 1.0
 */
public class Organization implements Comparable<Organization>{


	//##############################################################################
	//  ###########################################################################
	//     ATTRIBUTES
	//  ###########################################################################
	//##############################################################################

	private String name;

	public Collection<String> alternateNames;

	public Collection<GenericContainer> foundedBy;
	public Collection<GenericContainer> owners;
	public Collection<GenericContainer> employees;
	public Collection<GenericContainer> clients;
	public Collection<GenericContainer> members;
	public Collection<GenericContainer> alumni;

	//##############################################################################
	//  ###########################################################################
	//     CONSTUCTORS
	//  ###########################################################################
	//##############################################################################

	/**
	 * Constructor
	 * 
	 * @param name 	String that represents the organization name.
	 */
	public Organization(String name){
		this.name = name;

		this.alternateNames = null;
		this.foundedBy = null;
		this.owners = null;
		this.employees = null;
		this.clients = null;
	}

	//##############################################################################
	//  ###########################################################################
	//     GETTERS AND SETTERS
	//  ###########################################################################
	//##############################################################################

	/**
	 * Adds a new name to the alternative names Array. 
	 * This method does not guarantee uniqueness of the elements.
	 * 
	 * @param name 	String that represents the organization name.
	 */
	public void addName(String name){
		if(this.alternateNames == null)
			this.alternateNames = new ArrayList<String>();

		this.alternateNames.add(name);
	}

	/**
	 * Adds a new set of establishment information to the founded information Array.
	 * 
	 * @param founder 	GenericContainer that represents the organization establishment information.
	 */
	public void addFounder(GenericContainer founder){
		if(this.foundedBy == null)
			this.foundedBy = new ArrayList<GenericContainer>();

		for(GenericContainer c : this.foundedBy){
			if(c.equals(founder))
				return;
		}
		this.foundedBy.add(founder);
	}

	/**
	 * Adds a new set of ownership information to the owners information Array. 
	 * 
	 * @param owner 	GenericContainer that represents the organization ownership information.
	 */
	public void addOwner(GenericContainer owner){
		if(this.owners == null)
			this.owners = new ArrayList<GenericContainer>();
		for(GenericContainer c : this.owners){
			if(c.equals(owner))
				return;
		}
		this.owners.add(owner);
	}

	/**
	 * Adds a new set of worker information to the workes information Array. 
	 * 
	 * @param worker 	GenericContainer that represents the organization worker information.
	 */
	public void addEmployee(GenericContainer worker){
		if(this.employees == null)
			this.employees = new ArrayList<GenericContainer>();

		for(GenericContainer c : this.employees){
			if(c.equals(worker))
				return;
		}


		this.employees.add(worker);
	}

	/**
	 * Adds a new set of client information to the clients information Array. 
	 * 
	 * @param client 	GenericContainer that represents the organization client information.
	 */
	public void addClient(GenericContainer client){
		if(this.clients == null)
			this.clients = new ArrayList<GenericContainer>();

		for(GenericContainer c : this.clients){
			if(c.equals(client))
				return;
		}

		this.clients.add(client);
	}

	/**
	 * Adds a new set of Member information to the clients information Array. 
	 * 
	 * @param member 	GenericContainer that represents the organization member information.
	 */
	public void addMember(GenericContainer member){
		if(this.members == null)
			this.members = new ArrayList<GenericContainer>();

		for(GenericContainer c : this.members){
			if(c.equals(member))
				return;
		}

		this.members.add(member);
	}

	/**
	 * Adds a new set of alumni information to the clients information Array. 
	 * 
	 * @param alumni 	GenericContainer that represents the organization alumni information.
	 */
	public void addAlumni(GenericContainer alumni){
		if(this.alumni == null)
			this.alumni = new ArrayList<GenericContainer>();

		for(GenericContainer c : this.alumni){
			if(c.equals(alumni))
				return;
		}

		this.alumni.add(alumni);
	}

	/**
	 * 	Returns a String that represents the longest name of the Organization.
	 * @return String that represents the longest name of the Organization.
	 */
	public String getName(){
		return name;
	}


	//##############################################################################
	//  ###########################################################################
	//     CONVERTERS
	//  ###########################################################################
	//##############################################################################
	public void sort(){
		if(this.alternateNames != null)
			Collections.sort((List<String>)this.alternateNames);
	}

	/**
	 * XML Output function.
	 * 
	 * Filipe Carapinha [2012/Sep]
	 */
	public void exportXMLDOM(Element parent, Document doc){
		Element thisNode = doc.createElement("FRAME");
		thisNode.setAttribute("type", "ORGANIZATION");
		thisNode.setAttribute("name", this.name);

		if(this.alternateNames != null){
			Element node = doc.createElement("ALTERNATE-NAMES");
			for(String s : this.alternateNames){
				Element subnode = doc.createElement("NAME");
				subnode.setTextContent(s);
				node.appendChild(subnode);
			}
			thisNode.appendChild(node);
		}


		if(this.foundedBy != null){
			Element node = doc.createElement("FOUNDEDBY");
			for(GenericContainer s : this.foundedBy){
				Element subnode = doc.createElement("ELEM");
				s.exportXMLDOMContents(subnode, doc);
				node.appendChild(subnode);
			}
			thisNode.appendChild(node);
		}

		if(this.owners != null){
			Element node = doc.createElement("OWNERS");
			for(GenericContainer s : this.owners){
				Element subnode = doc.createElement("ELEM");
				s.exportXMLDOMContents(subnode, doc);
				node.appendChild(subnode);
			}
			thisNode.appendChild(node);
		}

		if(this.employees != null){
			Element node = doc.createElement("EMPLOYEES");
			for(GenericContainer s : this.employees){
				Element subnode = doc.createElement("ELEM");
				s.exportXMLDOMContents(subnode, doc);
				node.appendChild(subnode);
			}
			thisNode.appendChild(node);
		}

		if(this.clients != null){
			Element node = doc.createElement("CLIENTS");
			for(GenericContainer s : this.clients){
				Element subnode = doc.createElement("ELEM");
				s.exportXMLDOMContents(subnode, doc);
				node.appendChild(subnode);
			}
			thisNode.appendChild(node);
		}

		if(this.members != null){
			Element node = doc.createElement("FORMAL-MEMBERS");
			for(GenericContainer s : this.members){
				Element subnode = doc.createElement("ELEM");
				s.exportXMLDOMContents(subnode, doc);
				node.appendChild(subnode);
			}
			thisNode.appendChild(node);
		}

		if(this.alumni != null){
			Element node = doc.createElement("ALUMNI");
			for(GenericContainer s : this.alumni){
				Element subnode = doc.createElement("ELEM");
				s.exportXMLDOMContents(subnode, doc);
				node.appendChild(subnode);
			}
			thisNode.appendChild(node);
		}

		parent.appendChild(thisNode);
	}

	/**
	 * Redefinition of equals method. This is used for coreference analysis.
	 * 
	 * @param name	String that represents the name you want to compare.
	 * @return true if the param name meets the conditions of this function, else false.
	 */
	public boolean equals(String name){
		if(this.name.equals(name))
			return true;
		if(this.alternateNames != null && this.alternateNames.contains(name))
			return true;

		String[] t1 = this.name.split("\\s+");
		String[] t2 = name.split("\\s+");
		int l1 = t1.length;
		int l2 = t2.length;	
		String v1 = "";
		String v2 = "";

		if(l1 > l2){
			for(int i = 0; i < l1; ++i){
				v1 += t1[i].substring(0, 1);
			}
			if(v1.equals(name)){
				this.addName(name);
				return true;
			}
			//			System.out.print("V1 ");
			//			System.out.print(v1);
			//			System.out.println(" " + this.name);

		}
		if(l2 > l1){
			for(int i = 0; i < l2; ++i){
				v2 += t2[i].substring(0, 1);
			}
			if(v2.equals(this)){
				this.addName(this.name);
				this.name = name;
				return true;
			}
			//			System.out.print("V2 ");
			//			System.out.print(v2);
			//			System.out.println(" " + name);

		}

		return false;
	}


	/**
	 * Returns a String that is a representation of a Organization entity type. 
	 * This method is used to return a human output for the Slot Filling task.
	 * 
	 * @return	String
	 */
	public String pprintPerson(){
		String out = "";
		out += "------ Organization ------\n";
		out += "NAME: " + this.name + "\n";

		if(this.alternateNames != null){
			out += "\tALTERNATIVE NAMES: \n";
			for(String s : this.alternateNames){
				out += "\t\t" + s + "\n";
			}
		}

		if(this.foundedBy != null)
			for(GenericContainer c : this.foundedBy){
				out += "FOUNDED-BY: \n" + c.pprintGenericContainer();
			}
		if(this.owners != null)
			for(GenericContainer c : this.owners){
				out += "OWNER: \n" + c.pprintGenericContainer();
			}
		if(this.employees != null)
			for(GenericContainer c : this.employees){
				out += "EMPLOYEE: \n" + c.pprintGenericContainer();
			}
		if(this.clients != null)
			for(GenericContainer c : this.clients){
				out += "CLIENT: \n" + c.pprintGenericContainer();
			}
		if(this.members != null)
			for(GenericContainer c : this.members){
				out += "FORMAL-MEMBER: \n" + c.pprintGenericContainer();
			}
		if(this.alumni != null)
			for(GenericContainer c : this.alumni){
				out += "ALUMNI: \n" + c.pprintGenericContainer();
			}

		out += "\n--------------------\n";
		return out;
	}


	public int compareTo(Organization o) {
		return this.name.compareTo(o.getName());
	}

}
