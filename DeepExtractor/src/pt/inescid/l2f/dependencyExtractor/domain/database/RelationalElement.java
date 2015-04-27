package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;

public abstract class RelationalElement {
	protected Connection connection;
	
	RelationalElement(Connection conn){
		connection=conn;
	}
	
}
