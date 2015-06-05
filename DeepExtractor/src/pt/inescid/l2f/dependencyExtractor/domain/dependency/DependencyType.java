package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.util.ArrayList;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;


public abstract class DependencyType{
	protected Palavra _palavra;
	protected Coocorrencia _coocorrencia;
	protected Propriedade _propriedade;
	
	DependencyType(Palavra palavra, Coocorrencia coo, Propriedade prop){
		_palavra = palavra ;
		_coocorrencia = coo;
		_propriedade = prop;
	}
	
	public void getDepedencyInformation(Dependency dep){
	
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
				String pos = node.getName();
				String word = "";
				for (Token token : node.getTokens()){
					if(!word.isEmpty()){
						word = word + " ";
					}
					word  = word + token.getLemmas().element();		
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
