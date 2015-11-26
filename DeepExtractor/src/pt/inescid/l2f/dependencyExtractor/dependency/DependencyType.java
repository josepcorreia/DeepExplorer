package pt.inescid.l2f.dependencyExtractor.dependency;

import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.connection.exception.DatabaseException;
import pt.inescid.l2f.dependencyExtractor.domain.Cooccurrence;
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
import java.util.Map.Entry;

public abstract class DependencyType{
	/** where the information is temporarily stored*/
    private DeepStorage _storage;

    /** list of dependency-property patterns that are relevant for the system*/
    private HashMap<String,String> _depPropTable;
	
	public DependencyType(){
	}
	
	public DependencyType(DeepStorage storage){
		_storage = storage; 
		_depPropTable = getDepPropTable();
		 depInformation();
	}

    /**
     * Get the list of relevant dependency-property patterns for the system (patterns present in a file)
     *
     *
     * @result list of dependency-property patterns that are relevant for the system
     */
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

    /**
     * Insert the relevants XIPDependency's names and properties in the database
     *
     * @throws DatabaseException in the case of a problem in the database/database's connectione1991
     *
     */
    private void depInformation(){
        for (Entry<String, String> entry : _depPropTable.entrySet()) {
            String split[] =  entry.getValue().split(" ");
            String depname = split[0];
            String prop = split[1];

            try {
                RelationalFactory.getDependency().insertNew(depname);
                RelationalFactory.getProperty().checkProperty(prop, depname);
            } catch (DatabaseException e) {
                System.err.println(e.getMessage());

                //if a problem occurs in the database, the connection is closed and the program exits
                RelationalFactory.closeConnection();
                System.exit(0);
            }
        }
    }

    /**
     * Verify if this XipNode or other parent of this was present in some named entity
     *
     * @param  node  Xip Dependency (XIPAPI)
     * @param  namedEntities - set of name entities in this sentence
     *
     * @result
     */
	private String CheckNamedEntity(XIPNode node, HashMap<String, String> namedEntities){
		if(node == null)
			return "noNE";

		if(namedEntities.containsKey(node.getNodeNumber())){
			return namedEntities.get(node.getNodeNumber()).toUpperCase();
		}

		return CheckNamedEntity(node.getParent(), namedEntities);
	}


    /**
     * Get the XIP dependency's generic property
     *
     * @param  dep - Xip Dependency (XIPAPI)
     *
     * @result
     */
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

    /**
     * Get the XIP dependency's Mod property
     *
     * @param  node - XipNODE (XIPAPI): word
     *
     * @result word's pos
     */
	protected String getPOS(XIPNode node) {
		String pos = node.getName();

		if(pos.equals("PASTPART")|| pos.equals("VINF") || pos.equals("VF")){
			pos = "VERB";
		}
		
		return pos;
	}

    /**
     * Get the object word
     *
     * @param  node - XipNODE (XIPAPI)
     * @param  pos - word's pos
     * @param dep - XIPDependency's name
     * @param prop - XIPDependency's property
     *
     * @result object for this word
     */
	protected Word getWord(XIPNode node, String pos, String dep, String prop, HashMap<String, String> namedEnteties) {
		//check if this XipNode or other parent of this was present in some named entity
        String lemma = CheckNamedEntity(node, namedEnteties);

        if (pos.equals("TOP")) {
            lemma = "Frase";
        } else {
            //if this node has no named entity
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
		return _storage.checkWord(lemma, pos, dep, prop);
	}

    /**
     * Extract te information in a certain XIP dependence
     *
     * @param  dep  Xip Dependency (XIPAPI)
     * @param  namedEnteties - set of name enteties in this sentence
     * @param sentence - Sentence object (with string that represent the sentence)
     */
    public void getDependencyInformation(Dependency dep, HashMap<String, String> namedEnteties, Sentence sentence){
        String depname = dep.getName();
        String prop = getProperty(dep);


        //to complete the initial property
        //result: PROP_POS1_POS2
        for (XIPNode node : dep.getNodes()){
            String pos = getPOS(node);

            if(!prop.isEmpty())
                prop += "_";
            prop += pos;
        }

        //dependency-property pattern -> DEP PROP_POS1_POS2
        String depProp = depname+" "+prop;


        //it is checked if this pattern is present in the list with the relevant system's patterns
        if(!_depPropTable.containsKey(depProp)) {
            return;
        }

        String newDepProp[] = _depPropTable.get(depProp).split(" ");
        depname = newDepProp[0];
        prop = newDepProp[1];

        //two words of the cooccurrence
        ArrayList<Word> words = new ArrayList<Word>();
        for (XIPNode node : dep.getNodes()){
            String pos = getPOS(node);
            words.add(getWord(node, pos, depname,prop,namedEnteties));
        }

        if(words.size()== 2){
            //submit in the storage the cooccurrence
            _storage.checkCooccurrence(new Cooccurrence(words.get(0), words.get(1), prop, depname), sentence);
        }
        else{
            //write in a file in teh case of some error in de XIPDependency
            String path = new File("src/out/depError.txt").getAbsolutePath();

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(path, true));
                writer.write("Dependência com erro " + depname +" na frase " + dep.getSentenceNumber() + "\n");

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
}
