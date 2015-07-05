package pt.inescid.l2f.connection.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Propriedade extends RelationalElement{

	public static void checkProperty(String prop, String dep){
		if(!propertyExists(prop, dep)){
			insertPropriedade(prop,dep);
		}
	}
	public static void insertPropriedade(String propriedade, String tipodepedencia){
		Connection connection = newConnetion();
		
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
				
				if(connection != null)
					connection.close();
				
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}

	public static boolean propertyExists(String prop, String dep){
		Connection connection = newConnetion();
		
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
	          if(connection != null)
					connection.close();
	          
	       }catch(SQLException se){
	       }// do nothing
	    }//end finally try
		return true;
	}
}
