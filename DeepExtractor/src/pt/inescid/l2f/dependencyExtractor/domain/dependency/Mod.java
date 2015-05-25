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

	/*@Override
	public void getDepedencyInformation(Dependency dep) {
		long wordId1 = 0;
		long wordId2 = 0;
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
			//os dois nós da dependencia
			int i = 0;
			for (XIPNode node : dep.getNodes()){
				String pos = node.getName();
				String word = "";
				for (Token token : node.getTokens()){
					if(!word.isEmpty()){
						word = word + " ";
					}
					word  = word + token.getLemmas().element();		
					System.out.println(node.getNodeNumber() + " " + word);
				i = token.getSentenceNumber();
				}
				a.add(super._palavra.checkWord(word, pos, "categoria"));
			}
			if (i == 3)
				System.exit(0);
			if(a.size()== 2){
				super._coocorrencia.checkCoocorrence(a.get(0), a.get(1), prop, depname);
			}
			else{
				//System.out.println("Depedencia com erro MOD");
				//System.out.println(dep.printDependency());
				//System.out.println(dep.getSentenceNumber());
				//System.out.println("####");
				
			}
		
	}*/

}
