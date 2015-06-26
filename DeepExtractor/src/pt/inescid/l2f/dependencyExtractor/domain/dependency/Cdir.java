package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.xipapi.domain.Dependency;

public class Cdir extends DependencyType{

	public Cdir() {
		super();
	}
	
	@Override
	protected String getProperty(Dependency dep) {
		String prop = "SEM_PROP";
		return prop;
	}
}
