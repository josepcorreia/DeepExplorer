package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.util.HashMap;

public class DependencyFactory {
	private HashMap<String,DependencyType> _dependenciesMap;
	private NE _ne;
	
	public DependencyFactory(String corpusName){
		_dependenciesMap = new HashMap<String,DependencyType>();
		
		_dependenciesMap.put("CDIR", new Cdir(corpusName));
		_dependenciesMap.put("CINDIR", new Cindir(corpusName));
		_dependenciesMap.put("COMPL", new COMPL(corpusName));
		_dependenciesMap.put("MOD", new Mod(corpusName));
		_dependenciesMap.put("SUBJ", new Subj(corpusName));
		
		_ne = new NE(); 
	}	
	
	public HashMap<String,DependencyType> getDependenciesMap(){
		return _dependenciesMap;
	}
	
	public NE NE(){
		return _ne;
	}
}
