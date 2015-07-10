package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;

public class Mod extends DependencyType{

	public Mod(DeepStorage _storage) {
		super(_storage);
	}
	
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
				prop+="_" + aux;
			}
		}
		return prop;
	}
}
