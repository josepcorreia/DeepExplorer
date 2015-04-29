package pt.inescid.l2f.dependencyExtractor;

import pt.inescid.l2f.dependencyExtractor.domain.DependencyFactory;
import pt.inescid.l2f.dependencyExtractor.domain.database.Corpus;
import pt.inescid.l2f.dependencyExtractor.domain.database.Dependencia;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.xipapi.domain.Dependency;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;


public class DependencyExtractor {
	private DependencyFactory _dfactory;
	private String _corpusName;

	private Corpus _corpus;
	private Dependencia _dependencia;

	public DependencyExtractor(Connection conn, String corpusName){
		_corpusName = corpusName;
		_dfactory = new DependencyFactory(conn,corpusName);
		_corpus = new Corpus(conn);
		_dependencia = new Dependencia(conn);
		this.CorpusInformation();
	}

	public void Extract(Collection<Dependency> dep){
				
		HashMap<String, DependencyType> map = _dfactory.getDependenciesMap(); 
		
		//preenche a informação sobre as dependencias detectadas
		for (String depname : map.keySet()) {
			_dependencia.insertNew(depname);
		}
		
		for (Dependency dependency : dep) {
			if(map.containsKey(dependency.getName())){
				map.get(dependency.getName()).getDepedencyInformation(dependency);
			}
		}
	}

	public void CorpusInformation() {
		//depois completar isto
		_corpus.insertNew(_corpusName, "Público", "2000", "Noticíario", false);
	}
}
