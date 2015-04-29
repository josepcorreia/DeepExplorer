package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.xipapi.domain.Dependency;


public abstract class DependencyType{
	protected Palavra _palavra;
	protected Coocorrencia _coocorrencia;
	protected Propriedade _propriedade;
	
	DependencyType(Palavra palavra, Coocorrencia coo, Propriedade prop){
		_palavra = palavra ;
		_coocorrencia = coo;
		_propriedade = prop;
	}
	
	public abstract void getDepedencyInformation(Dependency dep);
}
