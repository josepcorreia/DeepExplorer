package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.sql.Connection;

import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class Subj extends DependencyType{

	public Subj(Connection conn) {
		super(conn);
	}

	@Override
	public void getDepedencyInformation(Dependency dep) {
		//System.out.println(dep.getName());
		
		for (Feature f : dep.getFeatures()){
			//Propriedade
			//System.out.println("1"+f.getName());
		}
			//os dois nos da dependencia
			for (XIPNode node : dep.getNodes()){
				//System.out.println(node.getName());
				String word = "";
				for (Token token : node.getTokens()){
					if(!word.isEmpty()){
						word = word + " ";
					}
					word  = word + token.getLemmas().element();		
				}
				super.palavra.checkWord(word, node.getName(), "teste");
			}
		
		System.out.println("##############");
	}
}
