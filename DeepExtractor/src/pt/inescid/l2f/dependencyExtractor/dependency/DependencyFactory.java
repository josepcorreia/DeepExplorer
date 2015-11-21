package pt.inescid.l2f.dependencyExtractor.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;

import java.util.HashMap;

public class DependencyFactory {
	private HashMap<String,DependencyType> _dependenciesMap;
	private NE _ne;
	
	public DependencyFactory(DeepStorage _storage){
		_dependenciesMap = new HashMap<String,DependencyType>();
		
		_dependenciesMap.put("CDIR", new Cdir(_storage));
		_dependenciesMap.put("CINDIR", new Cindir(_storage));
		_dependenciesMap.put("COMPL", new Compl(_storage));
		_dependenciesMap.put("MOD", new Mod(_storage));
		_dependenciesMap.put("SUBJ", new Subj(_storage));
		
		_ne = new NE();
	}

    /**
     * Getter.
     * @return the HashMap with the different objects that analyse the XIP dependencies
     */
	public HashMap<String,DependencyType> getDependenciesMap(){
		return _dependenciesMap;
	}

    /**
     * Getter.
     * @return the ne object
     */
	public NE getNE(){
		return _ne;
	}
}
