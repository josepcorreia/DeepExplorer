package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.inescid.l2f.dependencyExtractor.domain.Word;
import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.dependencyExtractor.domain.database.RelationalFactory;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;
import pt.inescid.l2f.xipapi.domain.XipDocument;
import pt.inescid.l2f.xipapi.exception.FeatureDoesNotExistException;


public abstract class DependencyType{

	public DependencyType(){}

	public void getDepedencyInformation(Dependency dep, HashMap<String, String> namedEnteties){

		String depname = dep.getName();

		String prop = getProperty(dep);


		ArrayList<Word> words = new ArrayList<Word>();

		for (XIPNode node : dep.getNodes()){
			String word = "";
			String pos = getPOS(node);
			String ne = CheckNomedEntity(node, namedEnteties); 
			
			prop = getTracos(prop ,pos);
			
			
			if(!ne.equals("noNE")){
				word = ne;

			}else{
				switch (pos) {
				case "TOP":  word = "Frase";
				break;
				default: 	
					word="";
					for (Token token : node.getTokens()){
						if(!word.isEmpty()){
							word += " " + token.getLemmas().element();
						} 
						else {
							word = token.getLemmas().element();						
						}
					}
					break;
				}
			}
			words.add(new Word(word, pos));
		}
		
		RelationalFactory.getPropriedade().checkProperty(prop, depname);

		for (Word w : words) {
			w.setId(RelationalFactory.getPalavra().checkWord(w.getLemma(), w.getPOS(), "", depname, prop));
		}

		if(words.size()== 2){
			RelationalFactory.getCoocorrencia().checkCoocorrence(words.get(0), words.get(1), prop, depname);
		}
		else{
			System.out.println("Depedencia com erro " + depname +" na frase " + dep.getSentenceNumber());
		}
	}

	protected String getTracos(String prop, String pos) {
		if(!prop.contains("SEM_PROP")){
			prop += "_" + pos;
		}
		return prop;
	}

	protected String CheckNomedEntity(XIPNode node, HashMap<String, String> namedEnteties){
		if(node == null)
			return "noNE";

		if(namedEnteties.containsKey(node.getNodeNumber())){
			return namedEnteties.get(node.getNodeNumber());		
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
}
