 package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.xipapi.domain.Dependency;

public class COMPL extends DependencyType{

	public COMPL() {
		super();
	}
	@Override
	protected String getProperty(Dependency dep) {
		String prop = "SEM_PROP";
		return prop;
	}
	
	
}
