package pt.inescid.l2f.dependencyExtractor;

import pt.inescid.l2f.dependencyExtractor.domain.DependencyFactory;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.xipapi.domain.Dependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DependencyExtractor {
	private DependencyFactory _dfactory;  

	public DependencyExtractor(){
		_dfactory = new DependencyFactory();
	}

	public void Extract(Collection<Dependency> dep){
		HashMap<String, DependencyType> map = _dfactory.getDependenciesMap(); 

		for (Dependency dependency : dep) {
			if(map.containsKey(dependency.getName())){
				map.get(dependency.getName()).getDepedencyInformation(dependency);
			}
		}
	}
}
