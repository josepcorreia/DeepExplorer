package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.sql.Connection;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class Subj extends DependencyType{

	public Subj(Palavra palavra, Coocorrencia coo, Propriedade prop) {
		super(palavra,coo, prop);
	}

	@Override
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
			
			//os dois nós da dependencia
			int i = 1;
			for (XIPNode node : dep.getNodes()){
				String pos = node.getName();
				String word = "";
				for (Token token : node.getTokens()){
					if(!word.isEmpty()){
						word = word + " ";
					}
					word  = word + token.getLemmas().element();		
				}
				long wordId = super._palavra.checkWord(word, pos, "categoria");
				if(i==1){
					wordId1 = wordId;
				}else{
					wordId2 = wordId;
				}
				i++;
			}
			
			if(wordId1==0 || wordId2 == 0){
				System.out.println("Depedencia com erro SUBJ");
			}
			
			super._coocorrencia.checkCoocorrence(wordId1, wordId2, prop, depname);
		
		//System.out.println("##############");

	}
}
