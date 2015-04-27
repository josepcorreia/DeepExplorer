package pt.inescid.l2f.dependencyExtractor;

import pt.inescid.l2f.connection.ConnectionMySQL;
import pt.inescid.l2f.dependencyExtractor.domain.DependencyFactory;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.xipapi.domain.Dependency;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;


public class DependencyExtractor {
	private DependencyFactory _dfactory;
	private Connection _connection;

	public DependencyExtractor(Connection conn){
		_dfactory = new DependencyFactory(conn);
		_connection=conn;
	}

	public void Extract(Collection<Dependency> dep, ConnectionMySQL c_mysql){
		HashMap<String, DependencyType> map = _dfactory.getDependenciesMap(); 
 
		for (String depname : map.keySet()) {
			//c_mysql.insertDepedencia(depname);
		}
		
		for (Dependency dependency : dep) {
			if(map.containsKey(dependency.getName())){
				map.get(dependency.getName()).getDepedencyInformation(dependency);
			}
		}
	}

	public void CorpusInformation(ConnectionMySQL c_mysql) {
		//c_mysql.insertCorpus("CETEMPúblico", "Público", "2000", "Noticíario", false);
	}
}
