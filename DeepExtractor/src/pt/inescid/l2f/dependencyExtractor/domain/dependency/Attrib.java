package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.sql.Connection;

import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class Attrib extends DependencyType{

	public Attrib(Connection conn) {
		super(conn);
	}

	@Override
	public void getDepedencyInformation(Dependency dep) {
//		System.out.println("Attrib");
//		System.out.println(dep.printDependency());
//		//System.out.println("--------");
//		for (XIPNode node : dep.getNodes()){
//			System.out.println(node.getName());
//			System.out.println(" "+node.getSentence());
//			for (Feature f : node.getFeatures()) {
//				System.out.println(" "+" "+ f.getName());
//			}
//			System.out.println("--------");
//			}
//		System.out.println("##############");
	}

}
