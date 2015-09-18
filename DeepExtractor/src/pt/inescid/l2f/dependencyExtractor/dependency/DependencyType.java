package pt.inescid.l2f.dependencyExtractor.dependency;

import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.dependencyExtractor.domain.Coocorrence;
import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
import pt.inescid.l2f.dependencyExtractor.domain.Sentence;
import pt.inescid.l2f.dependencyExtractor.domain.Word;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

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

	public void getDependencyInformation(Dependency dep, HashMap<String, String> namedEnteties, Sentence sentence){
		String depname = dep.getName();
		String prop = getProperty(dep);


		//para completar a propriedade inicial
        for (XIPNode node : dep.getNodes()){
			String pos = getPOS(node);

			if(!prop.isEmpty())
				prop += "_";
			prop += pos;
		}

        String depProp = depname+" "+prop;


        if(!_depPropTable.containsKey(depProp)) {
            return;
        }

        String newDepProp[] = _depPropTable.get(depProp).split(" ");
        depname = newDepProp[0];
        prop = newDepProp[1];

        ArrayList<Word> words = new ArrayList<Word>();
        for (XIPNode node : dep.getNodes()){
            String pos = getPOS(node);
            words.add(getWord(node, pos, depname,depProp,namedEnteties));
        }

		if(words.size()== 2){
			_storage.checkCoocorrence(new Coocorrence(words.get(0), words.get(1), prop, depname), sentence);
		}
		else{
			String path = new File("src/out/depError.txt").getAbsolutePath();
			
			BufferedWriter writer = null;
	        try {
	            writer = new BufferedWriter(new FileWriter(path, true));
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
		String prop = "";
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
	protected Word getWord(XIPNode node, String pos, String depname, String prop, HashMap<String, String> namedEnteties) {
		String lemma = CheckNomedEntity(node, namedEnteties);

        if (pos.equals("TOP")) {
            lemma = "Frase";
        } else {
            if (lemma.equals("noNE")) {
                for (Token token : node.getTokens()) {
                    lemma = "";
                    if (!lemma.isEmpty()) {
                        lemma += " " + token.getLemmas().element();
                    } else {
                        lemma = token.getLemmas().element();
                    }
                }
            }
        }
		return _storage.checkWord(lemma, pos, depname, prop);
	}


	private HashMap<String, String> getDepPropTable() {
		HashMap<String, String> depPropTable = new HashMap<String, String>();
		
		try {
			String path = new File("src/resources/dep_prop.txt").getAbsolutePath();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			
			String line;
		    while ((line = buffer.readLine()) != null) {
                if(line.contains("##") || line.equals("")) {
                    continue;
                }

                String aux[] = line.split(",");

                depPropTable.put(aux[0], aux[1]);
		    }

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return depPropTable;
	}
	
	private void depInformation() {
        for (Entry<String, String> entry : _depPropTable.entrySet()) {
            String split[] =  entry.getValue().split(" ");
            String depname = split[0];
            String prop = split[1];

            RelationalFactory.getDependency().insertNew(depname);
            RelationalFactory.getProperty().checkProperty(prop, depname);

        }
    }
}