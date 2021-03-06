package pt.inescid.l2f.dependencyExtractor.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;

public class Mod extends DependencyType{

	public Mod(DeepStorage _storage) {
		super(_storage);
	}

    /**
     * Get the XIP dependency's Mod property
     *
     * @param  dep  Xip Dependency (XIPAPI)
     *
     * @result MOD's property
     */
	@Override
	protected String getProperty(Dependency dep) {
		String prop = "";
		for (Feature f : dep.getFeatures()){
			//Propriedade
			String aux = f.getName();
			if (aux.equals("PRE")||aux.equals("POST")){
				prop=aux;
			}
			if (aux.equals("FOCUS")){

				if(!prop.equals(""))
					prop+="_";

				prop+= aux;
			}
		}
		return prop;
	}
}
