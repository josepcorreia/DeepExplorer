package pt.inescid.l2f.dependencyExtractor.domain.dependency;

public class Mod extends DependencyType{

	public Mod() {
		super();
	}
	
	@Override
	protected String getTracos(String prop, String pos) {
		if(prop.contains("SEM_PROP")){
			prop = "";
			prop += pos;
		}else {
			prop += "_" + pos;
		}
		return prop;
	}
}
