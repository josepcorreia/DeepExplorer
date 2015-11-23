package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.exception.DatabaseException;

import java.sql.*;

public class PropertyTable extends RelationalTable {

	public PropertyTable(Connection _connection) {
		super(_connection);
	}

    /**
     * If the Property does not exist, it is inserted the new one
     *
     * @param prop - property
     * @param dep - property's dependency name
     *
	 * @throws DatabaseException in the case of a problem in the database/database's connection
	 */
	public void checkProperty(String prop, String dep) throws DatabaseException {
		if(!propertyExists(prop, dep)){
			insertProperty(prop, dep);
		}
	}

    /**
     * Add a Property to the database (table Propriedade)
     *
     * @param prop - property
     * @param dep - property's dependency name
     *
	 * @throws DatabaseException in the case of a problem in the database/database's connection
	 */
	public void insertProperty(String prop, String dep) throws DatabaseException {
		Connection connection = getConnetion();
		
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into Propriedade values (?, ?)");

			preparedStatement.setString(1, prop);
			preparedStatement.setString(2, dep);
			
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DatabaseException("Propriedade", "insertProperty");


		} finally {
			try {

				if (preparedStatement != null) 
					preparedStatement.close();
				
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}

    /**
     * Verify if a Property exists in the database (table Propriedade)
     *
     * @param prop - property
     * @param dep - property's dependency name
     *
     * @return true if the property already exists
	 *
	 * @throws DatabaseException in the case of a problem in the database/database's connection
	 */
	public boolean propertyExists(String prop, String dep) throws DatabaseException {
		Connection connection = getConnetion();
		
		Statement stmt = null;
	
		try{
			stmt = connection.createStatement();
			String sql = "SELECT 1 FROM Propriedade WHERE nomeProp='"+ prop + 
															"' AND tipoDep='" + dep + 
															"' LIMIT 1";
			ResultSet rs = stmt.executeQuery(sql);
			if(!rs.next()){
				rs.close();
				return false;
			}
	
			rs.close(); 	

    
	    }catch(SQLException se){
	       //Handle errors for JDBC
	    	se.printStackTrace();
			throw new DatabaseException("Propriedade", "propertyExists");
	     
	    }finally{
	       //finally block used to close resources
	       try{
	          if(stmt!=null)
	             stmt.close();
	          
	       }catch(SQLException se){
	       }// do nothing
	    }//end finally try
		return true;
	}
}
