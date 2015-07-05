package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.xipapi.domain.Dependency;

public class Cindir extends DependencyType{

	public Cindir() {
		super();
	}

	@Override
	protected String getProperty(Dependency dep) {
		String prop = "SEM_PROP";
		return prop;
	}
	
}
