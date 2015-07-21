package pt.inescid.l2f.connection.database;

import java.sql.Connection;

import pt.inescid.l2f.connection.ConnectionSQLite;

public abstract class RelationalElement {
	private Connection _connection = null;
	
	public RelationalElement(Connection connection) {
		_connection = connection;
    }

	protected Connection getConnetion(){
		return _connection;	
	}
}
