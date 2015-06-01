package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;

public class Subj extends DependencyType{

	public Subj(Palavra palavra, Coocorrencia coo, Propriedade prop) {
		super(palavra,coo, prop);
	}

}
