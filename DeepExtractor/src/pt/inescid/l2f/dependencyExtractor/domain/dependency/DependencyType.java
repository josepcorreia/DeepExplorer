package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.sql.Connection;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.xipapi.domain.Dependency;


public abstract class DependencyType{
	protected Palavra palavra;
	protected Coocorrencia coocorrencia;
	
	DependencyType(Connection conn){
		palavra = new Palavra(conn);
		coocorrencia = new Coocorrencia(conn);
	}
	
	public abstract void getDepedencyInformation(Dependency dep);
}
