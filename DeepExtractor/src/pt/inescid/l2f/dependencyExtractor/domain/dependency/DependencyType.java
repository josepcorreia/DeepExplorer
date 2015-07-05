package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import pt.inescid.l2f.connection.database.Coocorrencia;
import pt.inescid.l2f.connection.database.Dependencia;
import pt.inescid.l2f.connection.database.Palavra;
import pt.inescid.l2f.connection.database.Propriedade;
import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.dependencyExtractor.domain.Coocorrence;
import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
import pt.inescid.l2f.dependencyExtractor.domain.Word;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;



public abstract class DependencyType{
	private DeepStorage _storage;
	private HashMap<String,String> _depPropTable;

	public DependencyType(){
	}
	
	public DependencyType(DeepStorage storage){
		_storage = storage; 
		_depPropTable = getDepPropTable();
		 depInformation();
	}

	public void getDepedencyInformation(Dependency dep, HashMap<String, String> namedEnteties){
		String depname = dep.getName();
		String prop = getProperty(dep);

		ArrayList<Word> words = new ArrayList<Word>();

		for (XIPNode node : dep.getNodes()){
			String pos = getPOS(node);
			prop = getPropPOS(prop ,pos);
			
			Word word = getWord(node, pos, namedEnteties);
			words.add(word);
		}
		
		String depProp = depname+" "+prop;
		if(!_depPropTable.containsKey(depProp))
			return;
		String newDepProp[] = _depPropTable.get(depProp).split(" ");
		depname = newDepProp[0];  
		prop = newDepProp[1];
		
		RelationalFactory.getPropriedade().checkProperty(prop, depname);
		for (Word w : words) {
			_storage.CheckWord(w, prop, depname);
		}

		if(words.size()== 2){
			_storage.CheckCoocorrence(new Coocorrence(words.get(0).getIdPalavra(), words.get(1).getIdPalavra(), prop, depname));
		}
		else{
			String path = new File("src/out/depError.txt").getAbsolutePath();
			
			BufferedWriter writer = null;
	        try {
	            writer = new BufferedWriter(new FileWriter(path));
	            writer.write("Depedencia com erro " + depname +" na frase " + dep.getSentenceNumber() + "\n");

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                writer.close();
	            } catch (Exception e) {
	            }
	        }
		}
	}
	
	private String CheckNomedEntity(XIPNode node, HashMap<String, String> namedEnteties){
		if(node == null)
			return "noNE";

		if(namedEnteties.containsKey(node.getNodeNumber())){
			return namedEnteties.get(node.getNodeNumber()).toUpperCase();		
		}

		return CheckNomedEntity(node.getParent(), namedEnteties);
	}

	protected String getProperty(Dependency dep) {
		String prop = "SEM_PROP";
		for (Feature f : dep.getFeatures()){
			//Propriedade
			String aux = f.getName();
			if (aux.equals("PRE")||aux.equals("POST")){
				prop=aux;
			}
		}
		return prop;
	}
	protected String getPOS(XIPNode node) {
		String pos = node.getName();

		if(pos.equals("PASTPART")|| pos.equals("VINF") || pos.equals("VF")){
			pos = "VERB";
		}
		
		return pos;
	}
	protected Word getWord(XIPNode node, String pos, HashMap<String, String> namedEnteties) {
		String lemma = CheckNomedEntity(node, namedEnteties); 

		switch (pos) {
			case "TOP":  lemma = "Frase";
				break;
			default: 	
				if(lemma.equals("noNE")){
					for (Token token : node.getTokens()){
						lemma = "";
						if(!lemma.isEmpty()){
							lemma += " " + token.getLemmas().element();
						} 
						else {
							lemma = token.getLemmas().element();						
						}
					}
				}
				break;
			}
		return new Word(lemma, pos);
	}

	protected String getPropPOS(String prop, String pos) {
		if(!prop.contains("SEM_PROP")){
			prop += "_" + pos;
		}
		return prop;
	}

	private HashMap<String, String> getDepPropTable() {
		HashMap<String, String> depPropTable = new HashMap<String, String>();
		
		try {
			String path = new File("src/resources/dep_prop.txt").getAbsolutePath();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			
			String line;
		    while ((line = buffer.readLine()) != null) {
		    	String aux[] = line.split(",");
		    
		    	depPropTable.put(aux[0], aux[1]);
		    }

		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return depPropTable;
	}
	
	private void depInformation(){
		HashSet<String> depnames = new HashSet<String>();
		
		for(Entry<String, String> entry : _depPropTable.entrySet()) {
		
			String depname = entry.getValue().split(" ")[0];
		    depnames.add(depname);
		}

		for(String depname : depnames) {
			RelationalFactory.getDependencia().insertNew(depname);			
		}
	}
}
