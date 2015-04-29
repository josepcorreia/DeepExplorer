package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.util.ArrayList;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class Mod extends DependencyType{

	public Mod(Palavra palavra, Coocorrencia coo, Propriedade prop) {
		super(palavra, coo, prop);
	}

	@Override
	public void getDepedencyInformation(Dependency dep) {
		long wordId1 = 0;
		long wordId2 = 0;
		String prop = "";
		String depname = dep.getName();
		
		for (Feature f : dep.getFeatures()){
			//Propriedade
			prop = f.getName();
		}
		_propriedade.checkProperty(prop, depname);
			//ArrayList<Long> a = new ArrayList<Long>();
			ArrayList<String> a = new ArrayList<String>();
			//os dois nós da dependencia
			
			for (XIPNode node : dep.getNodes()){
				String pos = node.getName();
				String word = "";
				for (Token token : node.getTokens()){
					if(!word.isEmpty()){
						word = word + " ";
					}
					word  = word + token.getLemmas().element();		
				}
				a.add(word);
				//long wordId = super._palavra.checkWord(word, pos, "categoria");
				//a.add(wordId);
			}
			if(a.size()!= 2){
				System.out.println(depname + "_" + prop);
				System.out.println(a);
				System.out.println(dep.getNodes().size());
				System.out.println("####");
			}
			//super._coocorrencia.checkCoocorrence(wordId1, wordId2, prop, depname);

	}

}
