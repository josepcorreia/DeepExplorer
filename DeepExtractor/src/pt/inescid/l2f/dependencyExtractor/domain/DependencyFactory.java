package pt.inescid.l2f.dependencyExtractor.domain;

import java.util.HashMap;

import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Attrib;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Cdir;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Mod;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.NE;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Subj;

public class DependencyFactory {
	private HashMap<String,DependencyType> _dependenciesMap;
	private NE _ne;
	
	public DependencyFactory(String corpusName){
		_dependenciesMap = new HashMap<String,DependencyType>();
		
		//_dependenciesMap.put("ATTRIB", new Attrib(palavra, coo, prop));
		_dependenciesMap.put("CDIR", new Cdir());
		_dependenciesMap.put("CINDIR", new Cdir());
		_dependenciesMap.put("MOD", new Mod());
		_dependenciesMap.put("SUBJ", new Subj());
		
		_ne = new NE(); 
	}	
	
	public HashMap<String,DependencyType> getDependenciesMap(){
		return _dependenciesMap;
	}
	
	public NE NE(){
		return _ne;
	}
}
