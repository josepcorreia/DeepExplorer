package pt.inescid.l2f.dependencyExtractor.domain;

import java.sql.Connection;
import java.util.HashMap;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Attrib;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Cdir;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Mod;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Subj;

public class DependencyFactory {
	private HashMap<String,DependencyType> _dependenciesMap;
	
	public DependencyFactory(Connection conn, String corpusName){
		_dependenciesMap = new HashMap<String,DependencyType>();
		
		Palavra palavra = new Palavra(conn, corpusName);
		Coocorrencia coo = new Coocorrencia(conn, corpusName);
		Propriedade prop = new Propriedade(conn); 
		
		//_dependenciesMap.put("ATTRIB", new Attrib(palavra, coo, prop));
		//_dependenciesMap.put("CDIR", new Cdir(palavra, coo, prop));
		_dependenciesMap.put("MOD", new Mod(palavra, coo, prop));
		//_dependenciesMap.put("SUBJ", new Subj(palavra, coo, prop));
	}	
	
	public HashMap<String,DependencyType> getDependenciesMap(){
		return _dependenciesMap;
	}
}
