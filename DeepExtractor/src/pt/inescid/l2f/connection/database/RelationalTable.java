package pt.inescid.l2f.connection.database;

import java.sql.Connection;

public abstract class RelationalTable {
	private Connection _connection = null;
	
	public RelationalTable(Connection connection) {
		_connection = connection;
    }

    /**
     * Getter.
     * @return  the connection to teh database
     */
	protected Connection getConnetion(){
		return _connection;	
	}
}
