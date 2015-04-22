package pt.inescid.l2f.dependencyExtractor.domain;

import java.util.HashMap;

import pt.inescid.l2f.dependencyExtractor.domain.dependency.Attrib;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Cdir;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Mod;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Subj;

public class DependencyFactory {
	private HashMap<String,DependencyType> _dependenciesMap;
	
	public DependencyFactory(){
		_dependenciesMap = new HashMap<String,DependencyType>();
		
		_dependenciesMap.put("ATTRIB", new Attrib());
		_dependenciesMap.put("CDIR", new Cdir());
		_dependenciesMap.put("MOD", new Mod());
		_dependenciesMap.put("SUBJ", new Subj());
	}	
	
	public HashMap<String,DependencyType> getDependenciesMap(){
		return _dependenciesMap;
	}
}
