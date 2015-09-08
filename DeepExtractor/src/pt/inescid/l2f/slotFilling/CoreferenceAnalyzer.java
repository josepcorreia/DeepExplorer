package pt.inescid.l2f.slotFilling;

import pt.inescid.l2f.slotFilling.domain.Organization;
import pt.inescid.l2f.slotFilling.domain.Person;

import java.util.Vector;


/**
 *  This class stores the entities that has been discovered by the processing done by the InformationExtractor. It's also responsible for coreference analysis.
 *  
 * @author Filipe Carapinha [JavaDoc 2012/May]
 * @version 1.0
 */
public class CoreferenceAnalyzer {
	
//##############################################################################
//  ###########################################################################
//     ATTRIBUTES
//  ###########################################################################
//##############################################################################
	/** The Organization type entities extracted from the document */
	private Vector<Organization> entitiesOrganization;
	
	/** The Person type entities extracted from the document */
	private Vector<Person> entitiesPerson;

	
//##############################################################################
//  ###########################################################################
//     CONSTUCTORS
//  ###########################################################################
//##############################################################################

	/**
	 * Constructor.
	 * 
	 */
	public CoreferenceAnalyzer(){
		this.entitiesOrganization = new Vector<Organization>();
		this.entitiesPerson = new Vector<Person>();
	}

	/**
	 * 
	 * @param p	 Adds Person p to the Person type entities vector.
	 */
	public void addPerson(Person p){
		entitiesPerson.add(p);
	}

	/**
	 * 
	 * @param o	 Adds Organization o to the Organization type entities vector.
	 */
	public void addOrganization(Organization o){
		entitiesOrganization.add(o);
	}

	/**
	 * Method that search for an Person with a given name in the known persons vector.
	 * 
	 * @param name 	String 
	 * @return i 	index of the vector where the Person is stored. -1 if the Person with the given name does not exists.
	 */
	public int findPerson(String name){
		if(!entitiesPerson.contains(name)){
			for(int i = 0 ; i < entitiesPerson.size() ; ++i){
				if(entitiesPerson.get(i).equals(name))
					return i;
			}
		}
		return -1;
	}

	/**
	 * Method that search for an Organization with a given name in the known organizations vector.
	 * 
	 * @param name 	String 
	 * @return i 	index of the vector where the Organization is stored. -1 if the Organization with the given name does not exists.
	 */
	public int findOrganization(String name){
		if(!entitiesOrganization.contains(name)){
			for(int i = 0 ; i < entitiesOrganization.size() ; ++i){
				if(entitiesOrganization.get(i).equals(name))
					return i;
			}
		}
		entitiesOrganization.add(new Organization(name));
		return entitiesOrganization.size() - 1;
	}

//##############################################################################
//  ###########################################################################
//     GETTERS AND SETTERS
//  ###########################################################################
//##############################################################################
	/**
	 * 
	 * 
	 * @param index index of the Person that is supposed to get from the vector of Persons.
	 * @return Person at that index.
	 */
	public Person getPerson(int index){
		if(index >= entitiesPerson.size() || index < 0)
			return null;
		return entitiesPerson.get(index);
	}

	/**
	 * 
	 * 
	 * @param index index of the Organization that is supposed to get from the vector of Organizations.
	 * @return Organization at that index.
	 */
	public Organization getOrganization(int index){
		if(index >= entitiesOrganization.size() || index < 0)
			return null;
		return entitiesOrganization.get(index);
	}
	
	/**
	 * Returns the Vector stored within the CorefrenceAnalyser. Does not verify if the vector is not initialized.
	 * 
	 * @return Vector stored within the CorefrenceAnalyser.
	 */
	public Vector<Person> getPersons(){
		return this.entitiesPerson;
	}
	
	/**
	 * Returns the Vector stored within the CorefrenceAnalyser. Does not verify if the vector is not initialized.
	 * 
	 * @return Vector stored within the CorefrenceAnalyser.
	 */
	public Vector<Organization> getOrganizations(){
		return this.entitiesOrganization;
	}

	/**
	 * Remove a Person from the vector of Persons.
	 * 
	 * @param index index of the Person that is supposed to be removed.
	 */
	public void removePerson(int index){
		entitiesPerson.remove(index);
	}

	/**
	 * Remove a Organization from the vector of Organizations.
	 * 
	 * @param index index of the Organization that is supposed to be removed.
	 */
	public void removeOrganization(int index){
		entitiesOrganization.remove(index);
	}

//##############################################################################
//  ###########################################################################
//     CONVERTERS
//  ###########################################################################
//##############################################################################
	/**
	 * Vai ser usado para colocas os nomes mais longos.
	 */
	private void consolidate(){
		
	}
	
	
	/**
	 * Prints a String that is a representation of the information stored inside a CoreferenceAnalyser. 
	 * This method is used to return a human output for the Slot Filling tasks.
	 *
	 */
	public void pprintEntities(){
		consolidate();
		System.out.println("\n****************** Slot-Filling ******************\n");
		if(entitiesPerson.isEmpty() && entitiesOrganization.isEmpty())
			return;
		if(!entitiesPerson.isEmpty()){
			System.out.println("********* PERSONS *********");
			for(Person p : entitiesPerson){
				System.out.println(p.pprintPerson());
			}
		}

		if(!entitiesOrganization.isEmpty()){
			System.out.println("********* ORGANIZATIONS *********");
			for(Organization o : entitiesOrganization){
				System.out.println(o.pprintPerson());
			}
		}
	}

}
