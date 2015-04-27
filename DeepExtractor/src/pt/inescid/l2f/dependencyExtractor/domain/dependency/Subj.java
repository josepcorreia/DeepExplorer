package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.sql.Connection;

import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class Subj extends DependencyType{

	public Subj(Connection conn) {
		super(conn);
	}

	@Override
	public void getDepedencyInformation(Dependency dep) {
		System.out.println(dep.getName());
		
		for (Feature f : dep.getFeatures()){
		System.out.println(f.getName());
		}
		
		for (XIPNode node : dep.getNodes()){
			System.out.println(node.getName());
			for (Token token : node.getTokens()){
				System.out.println(token.getWord());
				System.out.println(token.getLemmas());
			}
			System.out.println("--------");
			}
		System.out.println("##############");
		System.exit(0);
	}
}
