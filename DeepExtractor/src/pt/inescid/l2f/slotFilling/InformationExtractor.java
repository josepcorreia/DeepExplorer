package pt.inescid.l2f.slotFilling;

import pt.inescid.l2f.globalVars.ConstantVals;
import pt.inescid.l2f.slotFilling.domain.GenericContainer;
import pt.inescid.l2f.slotFilling.domain.Person;
import pt.inescid.l2f.slotFilling.domain.TimeNorm;
import pt.inescid.l2f.xipapi.domain.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

/**
 * This class has the main method for the Slot Filling task.
 * 
 * @author Filipe Carapinha [2012/May]
 * @version 1.0
 */
public class InformationExtractor {

	private CoreferenceAnalyzer r = new CoreferenceAnalyzer();
	/** Get CPU time in nanoseconds. */
	public long getCpuTime( ) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		return bean.isCurrentThreadCpuTimeSupported( ) ?
				bean.getCurrentThreadCpuTime( ) : 0L;
	}

	/** Get user time in nanoseconds. */
	public long getUserTime( ) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		return bean.isCurrentThreadCpuTimeSupported( ) ?
				bean.getCurrentThreadUserTime( ) : 0L;
	}

	/** Get system time in nanoseconds. */
	public long getSystemTime( ) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		return bean.isCurrentThreadCpuTimeSupported( ) ?
				(bean.getCurrentThreadCpuTime( ) - bean.getCurrentThreadUserTime( )) : 0L;
	}


	/**
	 * Method that executes the Slot Filling task on a XipDocument.
	 * 
	 * @param document	XipDocument that contains the information this method is going to process.
	 * @param human	flag that indicates if it's suppose to print human output. If true prints human output, else don't print.
	 * @param benchmark flag true - prints user, system and cpu times.
	 */
	public void extract(XipDocument document, boolean human, boolean benchmark) {
		long startCpuTimeNano		= getCpuTime( );
		long startUserTimeNano		= getUserTime();
		long startSystemTimeNano	= getSystemTime();

		//Entidades recolhidas do texto
		int i;
		ArrayList<XIPNode> nodes;
		XIPNode n1, n0, parameter;
		String s1, s0, parameterString, org, profession, place, city, country, domain, degree, anaphora, anaphoraNodeNumber, gender;
		Vector<Integer> personsIndex1, personsIndex2, organizationsIndex;
		Collection<Dependency> dependenciesList;
		int index = 0;
		//Parametros
		TimeNorm tnstart, tnend, tnage, tnduration, tndate;


		//Collection<Dependency> dependencies = document.getDependencies();
		for(int iSentence=0; iSentence < document.getNumberOfSentences(); ++iSentence){
			Vector<Dependency> dependencies = document.getSentenceDependecies(iSentence);
			for (Dependency dep : dependencies){
				try{
					++index;
					if(dep.getName().equals(ConstantVals.affiliation)){
						for(Feature f1 : dep.getFeatures()){
							if(f1.getName().equals(ConstantVals.globalAffiliation)){
								nodes = (ArrayList<XIPNode>)dep.getNodes();
								parameter = nodes.get(1);
								String affiliation = parameter.getSentence();
								affiliation = affiliation.replaceAll("\\s+", " ").trim();
								String name = nodes.get(0).getPersonName();
								name = name.replaceAll("\\s+", " ");
								name = name.trim();

								i = r.findPerson(name);
								if(i != -1){
									r.getPerson(i).addGlobalAffiliation(affiliation);		
								} else {
									Person p = new Person(name);
									p.addGlobalAffiliation(affiliation);
									r.addPerson(p);
								}

								//System.err.println(name + " " + affiliation);
							}
						}
					}
					if(dep.getName().equals(ConstantVals.business)){
						for(Feature f1 : dep.getFeatures()){
							if(f1.getName().equals(ConstantVals.profession)){
								nodes = (ArrayList<XIPNode>)dep.getNodes();
								parameter = nodes.get(1);
								String prof = parameter.getSentence();
								prof = prof.replaceAll("\\s+$", "");
								prof = prof.replaceAll("\\s+", " ");
								String name = nodes.get(0).getPersonName();
								name = name.replaceAll("\\s+", " ");
								name = name.trim();
								gender = "";
								if(nodes.get(0).containsFeature(ConstantVals.femalePerson))
									gender = ConstantVals.femalePerson;
								if(nodes.get(0).containsFeature(ConstantVals.malePerson))
									gender = ConstantVals.malePerson;
								GenericContainer c = new GenericContainer();
								c.setWork(prof, "", "", "", "", null, null, null, null);
								i = r.findPerson(name);

								if(i != -1){
									r.getPerson(i).addWork(c);		
								} else {
									if(gender.equals("")){
										Person p = new Person(name);
										p.addWork(c);
										r.addPerson(p);
									}
									else{
										Person p = new Person(name, gender);
										p.addWork(c);
										r.addPerson(p);
									}
								}
							}
						}
					}
					if(dep.getName().equals(ConstantVals.event)){
						for(Feature f1 : dep.getFeatures()){
							if(f1.getName().equals(ConstantVals.eventLex)){
								nodes = (ArrayList<XIPNode>)dep.getNodes();
								parameter = nodes.get(1);
								parameterString = parameter.getSentence();
								parameterString = parameterString.replaceAll("\\s+$", "");
								parameterString = parameterString.replaceAll("\\s+", " ");
								parameter = nodes.get(0);
								// O index na linha abaixo serve para evitar que se pesquise a lista de dependências de início, evitando assim iterações desnecessárias.
								dependenciesList = document.getDependencysByNodeNumber(parameter.getId(), index);
								personsIndex1 = new Vector<Integer>();
								personsIndex2 = new Vector<Integer>();
								organizationsIndex = new Vector<Integer>();
								tnstart = tnend = tnage = tnduration = tndate = null;
								gender = anaphora = anaphoraNodeNumber = org = profession = s1 = s0 = place = city = country = degree = domain = "";

								for(Dependency d : dependenciesList){
									for(Anaphor an : d.getAnaphoras()){
										anaphoraNodeNumber = an.getNodeNumber();
										anaphora = an.getValue();
									}

									for(Feature f2 : d.getFeatures()){
										nodes = (ArrayList<XIPNode>) d.getNodes();
										n0 = nodes.get(0);
										s0 = n0.getSentence();
										s0 = s0.replaceAll("\\s+$", "");
										s0 = s0.replaceAll("\\s+", " ");

										if(nodes.size() >= 2){
											n1 = nodes.get(1);
											s1 = n1.getSentence();
											s1 = s1.trim();
											s1 = s1.replaceAll("\\s+", " ");
										}

										if(f2.getName().equals(ConstantVals.eventDateStart)){
											if(nodes.get(1).getTimeNormalization() != null){
												tnstart = normalizaData(nodes, s1);
											}else{tnstart = new TimeNorm(s1);}
										}
										if(f2.getName().equals(ConstantVals.eventDate)){
											if(nodes.get(1).getTimeNormalization() != null){
												tndate = normalizaData(nodes, s1);
											}else{tndate = new TimeNorm(s1);}
										}
										if(f2.getName().equals(ConstantVals.eventDateEnd)){
											if(nodes.get(1).getTimeNormalization() != null){
												tnend = normalizaData(nodes, s1);
											}else{tnend = new TimeNorm(s1);}
										}
										if(f2.getName().equals(ConstantVals.eventAge)){
											if(nodes.get(1).getTimeNormalization() != null){
												tnage = normalizaData(nodes, s1);
											}else{tnage = new TimeNorm(s1);}
										}
										if(f2.getName().equals(ConstantVals.eventDuration)){
											if(nodes.get(1).getTimeNormalization() != null){
												tnduration = normalizaData(nodes, s1);
											}else{tnduration = new TimeNorm(s1);}
										}
										if(f2.getName().equals(ConstantVals.eventParticipant) ||
												f2.getName().equals(ConstantVals.eventEmployee) ||
												f2.getName().equals(ConstantVals.eventFormalMember) ||
												f2.getName().equals(ConstantVals.eventFounder) ||
												f2.getName().equals(ConstantVals.eventOwner) ||
												f2.getName().equals(ConstantVals.eventClient)){
											//TODO código para tratar anáfora, ainda não testado pois não existem eventos onde entrem anáforas
											if(!anaphora.equals("") && anaphoraNodeNumber.equals(nodes.get(1).getNodeNumber()))
												s1 = anaphora;
											else{
												s1 = nodes.get(1).getPersonName();
												s1 = s1.replaceAll("\\s+$", "");
												s1 = s1.replaceAll("\\s+", " ");
												//Género.
												if(nodes.get(1).containsFeature(ConstantVals.femalePerson))
													gender = ConstantVals.femalePerson;
												if(nodes.get(1).containsFeature(ConstantVals.malePerson))
													gender = ConstantVals.malePerson;
											}
											i = r.findPerson(s1);
											if(i != -1){
												personsIndex1.add(i);
											} else {
												if(gender.equals(""))
													r.addPerson(new Person(s1));
												else
													r.addPerson(new Person(s1, gender));

												personsIndex1.add(r.getPersons().size() - 1);
											}
										}
										if(f2.getName().equals(ConstantVals.eventPlace)){
											if(nodes.size() >= 2){
												boolean flag = false;
												n1 = nodes.get(1);
												s1 = n1.getSentence();
												s1 = s1.replaceAll("\\s+$", "");
												s1 = s1.replaceAll("\\s+", " ");
												for(Feature f : n1.getFeatures()){
													if(f.getName().equals(ConstantVals.featCity)){
														city = s1;
														flag = true;
														break;
													}
													if(f.getName().equals(ConstantVals.featCountry)){
														country = s1;
														flag = true;
														break;
													}
												}
												if(flag == false)
													place = s1;
											}
											//System.out.println("Encontra City=" + city + " Country=" + country + " Loc=" + place);
										}
										if(f2.getName().equals(ConstantVals.eventAcademic)){
											degree = s1;
										}
										if(f2.getName().equals(ConstantVals.eventDomain)){
											domain = s1;;
										}
										if(f2.getName().equals(ConstantVals.eventChild) ||
												f2.getName().equals(ConstantVals.eventGrandChild) ||
												f2.getName().equals(ConstantVals.eventNephew) ||
												f2.getName().equals(ConstantVals.eventSonInLaw) ||
												f2.getName().equals(ConstantVals.eventGodSon) ||
												f2.getName().equals(ConstantVals.eventGrandNephew) ||
												f2.getName().equals(ConstantVals.eventSibling) ||
												f2.getName().equals(ConstantVals.eventCousin) ||
												f2.getName().equals(ConstantVals.eventBrotherInLaw) ||
												f2.getName().equals(ConstantVals.eventSpouse)){

											if(!anaphora.equals("") && anaphoraNodeNumber.equals(nodes.get(1).getNodeNumber()))
												s1 = anaphora;
											else{
												s1 = nodes.get(1).getPersonName();
												s1 = s1.trim();
												s1 = s1.replaceAll("\\s+", " ");
												//Género.
												if(nodes.get(1).containsFeature(ConstantVals.femalePerson))
													gender = ConstantVals.femalePerson;
												if(nodes.get(1).containsFeature(ConstantVals.malePerson))
													gender = ConstantVals.malePerson;
											}
											i = r.findPerson(s1);
											if(i != -1){
												personsIndex1.add(i);
											} else {
												if(gender.equals(""))
													r.addPerson(new Person(s1));
												else
													r.addPerson(new Person(s1, gender));

												personsIndex1.add(r.getPersons().size() - 1);
											}
										}
										if(f2.getName().equals(ConstantVals.eventParent) ||
												f2.getName().equals(ConstantVals.eventGrandParent) ||
												f2.getName().equals(ConstantVals.eventUncle) ||
												f2.getName().equals(ConstantVals.eventParentInLaw) ||
												f2.getName().equals(ConstantVals.eventGodFahter) ||
												f2.getName().equals(ConstantVals.eventGrandUncle) ||
												f2.getName().equals(ConstantVals.eventBrotherInLaw)){
											if(!anaphora.equals("") && anaphoraNodeNumber.equals(nodes.get(1).getNodeNumber()))
												s1 = anaphora;
											else{
												s1 = nodes.get(1).getPersonName();
												s1 = s1.trim();
												s1 = s1.replaceAll("\\s+", " ");
												//Género.
												if(nodes.get(1).containsFeature(ConstantVals.femalePerson))
													gender = ConstantVals.femalePerson;
												if(nodes.get(1).containsFeature(ConstantVals.malePerson))
													gender = ConstantVals.malePerson;
											}
											i = r.findPerson(s1);
											if(i != -1){
												personsIndex2.add(i);
											} else {
												if(gender.equals(""))
													r.addPerson(new Person(s1));
												else
													r.addPerson(new Person(s1, gender));

												personsIndex2.add(r.getPersons().size() - 1);
											}

										}
										if(f2.getName().equals(ConstantVals.eventOrg)){
											i = r.findOrganization(s1);
											organizationsIndex.add(i);
											org = r.getOrganization(i).getName();
										}
										if(f2.getName().equals(ConstantVals.eventProfession)){
											profession = s1;
										}
									}
								}
								if(parameterString.equals(ConstantVals.eventNascimento)){
									GenericContainer c = new GenericContainer();
									if(tndate == null)
										if(tnstart != null)
											tndate = tnstart;
										else if(tnend != null)
											tndate = tnend;
									c.setBirthOrDeath(place, city, country, tndate);
									genericSetAddPerson("setBirth", personsIndex1, c, r);
								}
								if(parameterString.equals(ConstantVals.eventMorte)){
									GenericContainer c = new GenericContainer();
									if(tndate == null)
										if(tnstart != null)
											tndate = tnstart;
										else if(tnend != null)
											tndate = tnend;
									c.setBirthOrDeath(place, city, country, tndate);
									genericSetAddPerson("setDeath", personsIndex1, c, r);
								}
								if(parameterString.equals(ConstantVals.eventIdade)){
									if(containsType(ConstantVals.eventAge, dependenciesList)){
										genericSetAddPerson("setAge", personsIndex1, tnage, r);
									}
								}
								if(parameterString.equals(ConstantVals.eventResidencia)){
									if(containsType(ConstantVals.eventPlace, dependenciesList)){
										GenericContainer c = new GenericContainer();
										c.setResidence(place, city, country, tnstart, tnend, tnduration);
										genericSetAddPerson("addLocationOfResidence", personsIndex1, c, r);
									}
								}

								if(parameterString.equals(ConstantVals.eventEducacao)){
									GenericContainer c = new GenericContainer();
									c.setEducation(degree, domain, org, place, city, country, tnstart, tnend);
									genericSetAddPerson("addEducation", personsIndex1, c, r);

									GenericContainer alumni = new GenericContainer();
									// Adiciona alumni à organização
									if(!org.equals("")){
										//Organization o = new Organization(org);
										for(int ind = 0; ind < personsIndex1.size(); ++ind){
											alumni.setAlumni(r.getPerson(personsIndex1.get(ind)).getName(), degree, domain, place, city, country, tnstart, tnend);
											int ido = r.findOrganization(org);
											r.getOrganization(ido).addAlumni(alumni);
											alumni = new GenericContainer();
										}
									}

								}

								if(parameterString.equals(ConstantVals.eventTrabalho)){		
									GenericContainer work = new GenericContainer();
									GenericContainer worker = new GenericContainer();
									work.setWork(profession, org, place, city, country, tndate, tnstart, tnend, tnduration);

									// Adiciona trabalhadores à organização
									if(!org.equals("")){
										//Organization o = new Organization(org);
										for(int ind = 0; ind < personsIndex1.size(); ++ind){
											worker.setWorker(profession, r.getPerson(personsIndex1.get(ind)).getName(), place, city, country, tndate, tnstart, tnend, tnduration);
											int ido = r.findOrganization(org);
											r.getOrganization(ido).addEmployee(worker);
											work.setWork(profession, r.getOrganization(ido).getName(), place, city, country, tndate, tnstart, tnend, tnduration);
											worker = new GenericContainer();
										}
									}
									// Adiciona trabalho às pessoas em pessoasIndex1
									genericSetAddPerson("addWork", personsIndex1, work, r);
								}
								if(parameterString.equals(ConstantVals.eventPropriedade)){
									GenericContainer owned = new GenericContainer();
									GenericContainer owner = new GenericContainer();
									owned.setOwnedOrAffiliation(org, place, city, country, tndate, tnstart, tnend, tnduration);


									// Adiciona trabalhadores à organização
									if(!org.equals("")){
										for(int ind = 0; ind < personsIndex1.size(); ++ind){
											owner.setOwnerOrAffiliated(r.getPerson(personsIndex1.get(ind)).getName(), place, city, country, tndate, tnstart, tnend, tnduration);
											int ido = r.findOrganization(org);
											r.getOrganization(ido).addOwner(owner);
											owned.setOwnedOrAffiliation(r.getOrganization(ido).getName(), place, city, country, tndate, tnstart, tnend, tnduration);
											owner = new GenericContainer();
										}
									}
									// Adiciona trabalho às pessoas em pessoasIndex1
									genericSetAddPerson("addOwned", personsIndex1, owned, r);
								}
								if(parameterString.equals(ConstantVals.eventAfiliacao)){
									// TODO não sei se deve ser adicionado affiliado a organização.
									//System.out.println("Estou cá: " + org + " " + place + " " +  tnduration);
									GenericContainer c = new GenericContainer();
									c.setOwnedOrAffiliation(org, place, city, country, tndate, tnstart, tnend, tnduration);
									genericSetAddPerson("addAffiliation", personsIndex1, c, r);

									GenericContainer affiliated = new GenericContainer();
									// Adiciona affiliado à organização
									if(!org.equals("")){
										//Organization o = new Organization(org);
										for(int ind = 0; ind < personsIndex1.size(); ++ind){
											affiliated.setOwnerOrAffiliated(r.getPerson(personsIndex1.get(ind)).getName(), place, city, country, tndate, tnstart, tnend, tnduration);
											int ido = r.findOrganization(org);
											r.getOrganization(ido).addMember(affiliated);
											affiliated = new GenericContainer();
										}
									}
								}
								if(parameterString.equals(ConstantVals.eventFundacao)){
									GenericContainer founded = new GenericContainer();
									GenericContainer founder = new GenericContainer();
									founded.setFounded(org, place, city, country, tnstart);

									// Adiciona trabalhadores à organização
									if(!org.equals("")){
										for(int ind = 0; ind < personsIndex1.size(); ++ind){
											founder.setFounder(r.getPerson(personsIndex1.get(ind)).getName(), place, city, country, tndate);
											int ido = r.findOrganization(org);
											r.getOrganization(ido).addFounder(founder);
											founded.setFounded(r.getOrganization(ido).getName(), place, city, country, tnstart);
											founder = new GenericContainer();
										}
									}
									// Adiciona trabalho às pessoas em pessoasIndex1
									genericSetAddPerson("addFounded", personsIndex1, founded, r);
								}
								if(parameterString.equals(ConstantVals.eventCliente)){	
									GenericContainer c = new GenericContainer();
									c.setClientOf(org, place, city, country, tnstart, tnend, tnduration);
									genericSetAddPerson("addSupplier", personsIndex1, c, r);

									GenericContainer client = new GenericContainer();
									// Adiciona trabalhadores à organização
									if(!org.equals("")){
										for(int ind = 0; ind < personsIndex1.size(); ++ind){
											client.setClient(r.getPerson(personsIndex1.get(ind)).getName(), place, city, country, tnstart, tnend, tnduration);
											int ido = r.findOrganization(org);
											r.getOrganization(ido).addClient(client);
											client = new GenericContainer();
										}
									}
								}
								if(parameterString.equals(ConstantVals.eventParentesco)){
									if(containsType(ConstantVals.eventParent, dependenciesList)){
										genericAddFamilyRelation("addParent", "addChildren", personsIndex1, personsIndex2, r);
									}
									if(containsType(ConstantVals.eventGrandParent, dependenciesList)){
										genericAddFamilyRelation("addGrandParent", "addGrandSon", personsIndex1, personsIndex2, r);
									}
									if(containsType(ConstantVals.eventUncle, dependenciesList)){
										genericAddFamilyRelation("addUncle", "addNephew", personsIndex1, personsIndex2, r);
									}
									if(containsType(ConstantVals.eventParentInLaw, dependenciesList)){
										genericAddFamilyRelation("addParentInLaw", "addSonInLaw", personsIndex1, personsIndex2, r);
									}
									if(containsType(ConstantVals.eventGodFahter, dependenciesList)){
										genericAddFamilyRelation("addGodParent", "addGodSon", personsIndex1, personsIndex2, r);
									}
									if(containsType(ConstantVals.eventGrandUncle, dependenciesList)){
										genericAddFamilyRelation("addGrandUncle", "addGrandUncle", personsIndex1, personsIndex2, r);
									}
									if(containsType(ConstantVals.eventSibling, dependenciesList)){
										for(int ind = 0; ind < personsIndex1.size(); ++ind){
											for(int j = 0; j < personsIndex1.size(); ++j){
												if(ind != j){
													r.getPersons().get(personsIndex1.get(ind)).addSibling(r.getPersons().get(personsIndex1.get(j)));
												}
											}
										}
									}
									if(containsType(ConstantVals.eventCousin, dependenciesList)){
										for(int ind = 0; ind < personsIndex1.size(); ++ind){
											for(int j = 0; j < personsIndex1.size(); ++j){
												if(ind != j){
													r.getPersons().get(personsIndex1.get(ind)).addCousin(r.getPersons().get(personsIndex1.get(j)));
												}
											}
										}
									}
									if(containsType(ConstantVals.eventBrotherInLaw, dependenciesList)){
										// TODO Não existe cunhado
										genericAddFamilyRelation("addSibling", "addSibling", personsIndex1, personsIndex2, r);
									}
									if(containsType(ConstantVals.eventSpouse, dependenciesList)){
										GenericContainer c1 = new GenericContainer();
										GenericContainer c2 = new GenericContainer();
										try {
											c1.setPerson(r.getPersons().get(personsIndex1.get(1)));
											c1.setSpouse(r.getPersons().get(personsIndex1.get(1)).getName(), tndate, tnstart, tnend, tnduration);

											c2.setPerson(r.getPersons().get(personsIndex1.get(0)));
											c2.setSpouse(r.getPersons().get(personsIndex1.get(0)).getName(), tndate, tnstart, tnend, tnduration);

											r.getPersons().get(personsIndex1.get(0)).addSpouse(c1);
											r.getPersons().get(personsIndex1.get(1)).addSpouse(c2);
										} catch (Exception e) {
											System.err.println("SlotFilling: Error in Spouse Event, missing Parameter in dependency.");
											//e.printStackTrace();
										}
									}
								}

							}
						}
					}
				} catch(Exception e){
					System.err.println("SlotFilling: Internal error.");
					e.printStackTrace();
					return;
				}
			}
		}
		document.setPersons(r.getPersons());
		document.setOrganizations(r.getOrganizations());

		if(human){
			r.pprintEntities();
		}

		if(benchmark){
			//CPU
			long endCpuTimeNano = getCpuTime( );
			long elapsedCpuTimeNano = endCpuTimeNano - startCpuTimeNano;
			long cpu = elapsedCpuTimeNano / 1000000;
			//long time = System.currentTimeMillis() - init;
			float secondsCpu = (float) (cpu / 1000.0f) % 60 ;
			int minutesCpu = (int) ((cpu / (1000*60)) % 60);
			int hoursCpu   = (int) ((cpu / (1000*60*60)) % 24);
			//User
			long endUserTimeNano = getUserTime();
			long elapsedUserTimeNano = endUserTimeNano - startUserTimeNano;
			long user = elapsedUserTimeNano / 1000000;
			//long time = System.currentTimeMillis() - init;
			float secondsUser = (float) (user / 1000.0f) % 60 ;
			int minutesUser = (int) ((user / (1000*60)) % 60);
			int hoursUser   = (int) ((user / (1000*60*60)) % 24);

			//System
			long endSystemTimeNano = getSystemTime();
			long elapsedSystemTimeNano = endSystemTimeNano - startSystemTimeNano;
			long system = elapsedSystemTimeNano / 1000000;
			//long time = System.currentTimeMillis() - init;
			float secondsSystem = (float) (system / 1000.0f) % 60 ;
			int minutesSystem = (int) ((system / (1000*60)) % 60);
			int hoursSystem   = (int) ((system / (1000*60*60)) % 24);

			System.err.print("		-> SlotFilling: ALL:" + hoursCpu + "h" + minutesCpu + "m");
			System.err.format("%.3f", secondsCpu);
			System.err.print("s");

			System.err.print(" user:" + hoursUser + "h" + minutesUser + "m");
			System.err.format("%.3f", secondsUser);
			System.err.print("s");

			System.err.print(" system:" + hoursSystem + "h" + minutesSystem + "m");
			System.err.format("%.3f", secondsSystem);

			System.err.println("s");
		}

		return;

	}

	/**
	 * This method is similar to genericSetAdd but works with pairs of indexes. Is used for symmetric and asymmetric relations.
	 * 
	 * @param relationType1 name of the method to invoke on persons1 vector.
	 * @param relationType2 name of the method to invoke on persons2 vector.
	 * @param persons1 vector of indexes of persons.
	 * @param persons2 vector of indexes of persons.
	 * @param r CoreferenceAnalyser.
	 */
	private void genericAddFamilyRelation(String relationType1, String relationType2, Vector<Integer> persons1, Vector<Integer> persons2, CoreferenceAnalyzer r){
		Class<Person> c = Person.class;
		Method m1;
		Method m2;
		try {
			m1 = c.getMethod(relationType1, Person.class);
			m2 = c.getMethod(relationType2, Person.class);
			for(int p1 : persons1){
				for(int p2 : persons2){
					m1.invoke(r.getPersons().get(p1), r.getPersons().get(p2));
					m2.invoke(r.getPersons().get(p2), r.getPersons().get(p1));
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method iterates trough a vector of indexes and invokes another method (relationType) on the Person in the CoreferenceAnalyser.
	 * 
	 * @param relationType name of the method to invoke.
	 * @param persons vector of indexes on the vector of Persons in the CoreferenceAnalyser.
	 * @param argument parameters to pass to the invoked method.
	 * @param r CoreferenceAnalyser.
	 */
	private void genericSetAddPerson(String relationType, Vector<Integer> persons, Object argument, CoreferenceAnalyzer r){
		Class<Person> c = Person.class;
		Method m;
		try {
			m = c.getMethod(relationType, argument.getClass());
			for(int p : persons){
				m.invoke(r.getPerson(p), argument);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	//	private void genericSetAddOrg(String relationType, Vector<Integer> persons, Object argument, CoreferenceAnalyzer r){
	//		Class<Organization> c = Organization.class;
	//		Method m;
	//		try {
	//			m = c.getMethod(relationType, argument.getClass());
	//			for(int o : persons){
	//				m.invoke(r.getOrganization(o), argument);
	//			}
	//		} catch (SecurityException e) {
	//			e.printStackTrace();
	//		} catch (NoSuchMethodException e) {
	//			e.printStackTrace();
	//		} catch (IllegalArgumentException e) {
	//			e.printStackTrace();
	//		} catch (IllegalAccessException e) {
	//			e.printStackTrace();
	//		} catch (InvocationTargetException e) {
	//			e.printStackTrace();
	//		}
	//	}


	/**
	 * Auxiliary function used for testing the existence of a Feature (type) within a list of Dependencies
	 * 
	 * @param type	Feature that is supposed to be found.
	 * @param list	List of Dependencies where the Feature is being searched.
	 * @return	true if the Feature (type) is contained within the list, else false.
	 */
	private boolean containsType(String type, Collection<Dependency>list){
		for(Dependency d : list){
			for(Feature f : d.getFeatures()){
				if(f.getName().equals(type))
					return true;
			}
		}
		return false;
	}

	/**
	 * Auxiliary function user to find a normalysed data for the input node.
	 * 
	 * @param nodes
	 * @param string
	 * @return A Slot Filling representation of a time normalization node.
	 */
	private TimeNorm normalizaData(ArrayList<XIPNode> nodes, String string){
		Feature aux = null;
		String aux2[] = {"","","","","","","",""};
		aux = nodes.get(1).getTimeNormalization().getFeature(ConstantVals.timeValNorm);
		if(aux != null){
			for(String s : aux.getValues()){
				aux2[0] += s;
			}
		}
		aux = nodes.get(1).getTimeNormalization().getFeature(ConstantVals.timeValDelta);
		if(aux != null){
			for(String s : aux.getValues()){
				aux2[1] += s;
			}
		}
		aux = nodes.get(1).getTimeNormalization().getFeature(ConstantVals.timeUmed);
		if(aux != null){
			for(String s : aux.getValues()){
				aux2[2] += s;
			}
		}
		aux = nodes.get(1).getTimeNormalization().getFeature(ConstantVals.timeValNorm1);
		if(aux != null){
			for(String s : aux.getValues()){
				aux2[3] += s;
			}
		}
		aux = nodes.get(1).getTimeNormalization().getFeature(ConstantVals.timeValNorm2);
		if(aux != null){
			for(String s : aux.getValues()){
				aux2[4] += s;
			}
		}
		aux = nodes.get(1).getTimeNormalization().getFeature(ConstantVals.timeValDelta1);
		if(aux != null){
			for(String s : aux.getValues()){
				aux2[5] += s;
			}
		}
		aux = nodes.get(1).getTimeNormalization().getFeature(ConstantVals.timeValDelta2);
		if(aux != null){
			for(String s : aux.getValues()){
				aux2[6] += s;
			}
		}
		aux = nodes.get(1).getTimeNormalization().getFeature(ConstantVals.timeValQuant);
		if(aux != null){
			for(String s : aux.getValues()){
				aux2[7] += s;
			}
		}
		return new TimeNorm(string, 
				aux2[0].replaceAll(" ", ""), 
				aux2[1].replaceAll(" ", ""),
				aux2[2].replaceAll(" ", ""),
				aux2[3].replaceAll(" ", ""),
				aux2[4].replaceAll(" ", ""),
				aux2[5].replaceAll(" ", ""),
				aux2[6].replaceAll(" ", ""),
				aux2[7].replaceAll(" ", ""));

	}
}
