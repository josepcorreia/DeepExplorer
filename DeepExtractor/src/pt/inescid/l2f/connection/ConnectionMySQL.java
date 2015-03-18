package pt.inescid.l2f.connection;

//Classes necessárias para a conexão à base de dados
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException;

public class ConnectionMySQL {

  public static String status = "Not connected"; 
   
  public ConnectionMySQL() { } 
  
  //Método de Conexão// 
  public java.sql.Connection getConnectionMySQL() { 
	  Connection connection = null; 
	  
	  try { 
		
		  String driverName = "com.mysql.jdbc.Driver";
		  Class.forName(driverName); 
		  
		  // Configuração da conexão à base de dados//
		  	String serverName = "localhost"; 
		  	String mydatabase = "db_deep";
		  	String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 
		  	String username = "jcorreia";
		  	String password = "deepexplorer";
		  	
		  	connection = DriverManager.getConnection(url, username, password); 
		  	
		  	//Teste da conexão// 
		  	if (connection != null){ 
		  		status = "DB: Conected with success!";
		  	} else { 
		  		status = "DB: Not connected"; 
		  	} 
		  	return connection; 
		  	} catch (ClassNotFoundException e) { 
		  		System.out.println("The driver does not found."); 
		  		return null; 
		  	} catch (SQLException e) {
		  		System.out.println("Could not connect to the database."); 
		  		return null; 
		  	} 
	  } 
  
  public String getStatusConnection() { 
	  return status; 
  } 
  
  public boolean CloseConnection() { 
	  try { 
		this.getConnectionMySQL().close(); 
		return true; 
	    } catch (SQLException e) { 
		 return false; 
	    } 
  } 
  
  public java.sql.Connection RestartConnection() { 
	  this.CloseConnection();
	  return this.getConnectionMySQL(); 
  } 
}

