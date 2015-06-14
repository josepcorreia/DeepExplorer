package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;
import pt.inescid.l2f.xipapi.domain.XipDocument;
import pt.inescid.l2f.xipapi.exception.FeatureDoesNotExistException;


public abstract class DependencyType{
	protected Palavra _palavra;
	protected Coocorrencia _coocorrencia;
	protected Propriedade _propriedade;

	DependencyType(Palavra palavra, Coocorrencia coo, Propriedade prop){
		_palavra = palavra ;
		_coocorrencia = coo;
		_propriedade = prop;
	}
	DependencyType(){}

	private String CheckNomedEntity(XIPNode node, HashMap<String, String> namedEnteties){
		if(node == null)
			return "noNE";

		if(namedEnteties.containsKey(node.getNodeNumber())){
			return namedEnteties.get(node.getNodeNumber());		
		}

		return CheckNomedEntity(node.getParent(), namedEnteties);
	}
	private String CheckPOS(XIPNode node){
		return "SSS";
	}


	public void getDepedencyInformation(Dependency dep, HashMap<String, String> namedEnteties){

		String prop = "SEM_PROP";
		String depname = dep.getName();

		for (Feature f : dep.getFeatures()){
			//Propriedade
			String aux = f.getName();
			if (aux.equals("PRE")||aux.equals("POST")){
				prop=aux;
			}
		}
		_propriedade.checkProperty(prop, depname);

		ArrayList<Long> a = new ArrayList<Long>();

		for (XIPNode node : dep.getNodes()){
			String word = "";
			String pos = "";

			word = CheckNomedEntity(node, namedEnteties); 
			if(!word.equals("noNE")){
				//System.out.println("AQUIIII");
				//System.out.println(node.getSentence());
				pos = "NAMED_ENTITY";
			}else{
				pos = node.getName();
				
				if(pos.equals("PUNCT")){
					word = "PUNCT";
				}
				else{						
					word="";
					for (Token token : node.getTokens()){
						if(!word.isEmpty()){
							word += " " + token.getLemmas().element();
						} 
						else {
							word = token.getLemmas().element();						
						}
					}

				}
			}
			a.add(_palavra.checkWord(word, pos, "categoria", depname));
			//System.out.println(node.getNodeNumber() + " " + word);
		}

		if(a.size()== 2){
			_coocorrencia.checkCoocorrence(a.get(0), a.get(1), prop, depname);
		}
		else{
			/*System.out.println("Depedencia com erro" + depname +");
				System.out.println(dep.printDependency());
				System.out.println(dep.getSentenceNumber());
				System.out.println("####");*/

		}
	}
}
