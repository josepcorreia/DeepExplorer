package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.util.ArrayList;

import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class Cdir extends DependencyType{

	@Override
	public void getDepedencyInformation(Dependency dep) {
		System.out.println("Complemento directo");
		System.out.println(dep.getSentenceNumber());
		System.out.println(dep.printDependency());
		System.out.println("--------");
	}

}
