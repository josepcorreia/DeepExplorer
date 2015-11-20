package pt.inescid.l2f.connection.database;

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
     */
	public void checkProperty(String prop, String dep){
		if(!propertyExists(prop, dep)){
			insertPropriedade(prop,dep);
		}
	}


    /**
     * Add a Property to the database (table Propriedade)
     *
     * @param prop - property
     * @param dep - property's dependency name
     *
     */
	public void insertPropriedade(String prop, String dep){
		Connection connection = getConnetion();
		
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into Propriedade values (?, ?)");

			preparedStatement.setString(1, prop);
			preparedStatement.setString(2, dep);
			
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("|| property");
			System.out.println(e.getMessage());

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
     */
	public boolean propertyExists(String prop, String dep){
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
	    	System.out.println("|| propriedade");
	    	se.printStackTrace();
	     
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
