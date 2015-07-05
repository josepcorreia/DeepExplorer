package pt.inescid.l2f.connection.database;

import java.sql.Connection;

import pt.inescid.l2f.connection.ConnectionSQLite;

public abstract class RelationalElement {

	protected static Connection newConnetion(){
		return ConnectionSQLite.getConnectionSQLite();
	}
	
	protected static void closeConnetion(Connection connection){
		ConnectionSQLite.closeConnection(connection);
	}
	
}
