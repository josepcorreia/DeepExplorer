 package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
import pt.inescid.l2f.xipapi.domain.Dependency;

public class Compl extends DependencyType{

	public Compl(DeepStorage _storage) {
		super(_storage);
	}
	@Override
	protected String getProperty(Dependency dep) {
		String prop = "SEM_PROP";
		return prop;
	}
	
	
}
