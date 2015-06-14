package pt.inescid.l2f.connection;

//Classes necessárias para a conexão à base de dados
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException;

public class ConnectionMySQL {

  private static String status = "Not connected"; 

  public ConnectionMySQL() { } 
  
  //Método de Conexão// 
  public static Connection getConnectionMySQL() { 
	  
	  try { 
		  Connection connection;
		  String driverName = "com.mysql.jdbc.Driver";
		  Class.forName(driverName); 
		  
		  // Configuração da conexão à base de dados//
		  	String serverName = "localhost"; 
		  	//String serverName = "81.193.142.31";
		  	//String mydatabase = "db_deep";
		  	String mydatabase = "db_deep_aux"; 	
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
  
  public static String getStatusConnection() { 
	  return status; 
  } 
  
  public static boolean CloseConnection(Connection connection) { 
	  try { 
		connection.close(); 
		return true; 
	    } catch (SQLException e) { 
		 return false; 
	    } 
  } 
  
  public Connection RestartConnection(Connection connection) { 
	  ConnectionMySQL.CloseConnection(connection);
	  return ConnectionMySQL.getConnectionMySQL(); 
  } 
}
