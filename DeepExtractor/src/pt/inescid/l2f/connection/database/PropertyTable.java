package pt.inescid.l2f.connection.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PropertyTable extends RelationalElement{

	public PropertyTable(Connection _connection) {
		super(_connection);
	}
	
	public void checkProperty(String prop, String dep){
		if(!propertyExists(prop, dep)){
			insertPropriedade(prop,dep);
		}
	}
	public void insertPropriedade(String propriedade, String tipodepedencia){
		Connection connection = getConnetion();
		
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into Propriedade values (?, ?)");

			preparedStatement.setString(1, propriedade);
			preparedStatement.setString(2, tipodepedencia);
			
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("|| propriedade");
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
