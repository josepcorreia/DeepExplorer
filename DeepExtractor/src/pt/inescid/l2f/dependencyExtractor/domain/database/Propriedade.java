package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Propriedade extends RelationalElement{

	public Propriedade(Connection conn) {
		super(conn);
	}

	public void checkProperty(String prop, String dep){
		if(!propertyExists(prop, dep)){
			insertPropriedade(prop,dep);
		}
	}
	public void insertPropriedade(String propriedade, String tipodepedencia){
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

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("Record is inserted into Propriedade table!");
	}

	public boolean propertyExists(String prop, String dep){
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
