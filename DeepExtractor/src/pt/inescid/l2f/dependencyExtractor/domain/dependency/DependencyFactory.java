package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.util.HashMap;

public class DependencyFactory {
	private HashMap<String,DependencyType> _dependenciesMap;
	private NE _ne;
	
	public DependencyFactory(String corpusName){
		_dependenciesMap = new HashMap<String,DependencyType>();
		
		_dependenciesMap.put("CDIR", new Cdir());
		_dependenciesMap.put("CINDIR", new Cindir());
		_dependenciesMap.put("COMPL", new COMPL());
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
