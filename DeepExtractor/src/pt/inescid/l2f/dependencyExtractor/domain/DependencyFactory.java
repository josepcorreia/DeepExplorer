package pt.inescid.l2f.dependencyExtractor.domain;

import java.sql.Connection;
import java.util.HashMap;

import pt.inescid.l2f.dependencyExtractor.domain.dependency.Attrib;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Cdir;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Mod;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.Subj;

public class DependencyFactory {
	private HashMap<String,DependencyType> _dependenciesMap;
	
	public DependencyFactory(Connection conn){
		_dependenciesMap = new HashMap<String,DependencyType>();
		
		_dependenciesMap.put("ATTRIB", new Attrib(conn));
		_dependenciesMap.put("CDIR", new Cdir(conn));
		_dependenciesMap.put("MOD", new Mod(conn));
		_dependenciesMap.put("SUBJ", new Subj(conn));
	}	
	
	public HashMap<String,DependencyType> getDependenciesMap(){
		return _dependenciesMap;
	}
}
