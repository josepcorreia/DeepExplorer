package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;


public class Mod extends DependencyType{

	public Mod(Palavra palavra, Coocorrencia coo, Propriedade prop) {
		super(palavra, coo, prop);
	}

}
