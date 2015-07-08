package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;

public class Quantd extends DependencyType{

	public Quantd(DeepStorage _storage) {
		super(_storage);
	}
	
	/*
	@Override
	protected String getPropPOS(String prop, String pos) {
		if(prop.contains("SEM_PROP")){
			prop = "";
			prop += pos;
		}else {
			prop += "_" + pos;
		}
		return prop;
	}
	*/
}
