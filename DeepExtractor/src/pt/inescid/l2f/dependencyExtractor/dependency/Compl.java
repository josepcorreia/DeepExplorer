 package pt.inescid.l2f.dependencyExtractor.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
import pt.inescid.l2f.xipapi.domain.Dependency;

public class Compl extends DependencyType{

	public Compl(DeepStorage _storage) {
		super(_storage);
	}

	/**
	 * Get the XIP dependency's COMPL property
	 *
	 * @param  dep - Xip Dependency (XIPAPI)
	 *
	 * @result COMPL's property
	 */
	protected String getProperty(Dependency dep) {
		//neste caso não interessa se a prop é pre ou post
		String prop = "";

		return prop;
	}

}
