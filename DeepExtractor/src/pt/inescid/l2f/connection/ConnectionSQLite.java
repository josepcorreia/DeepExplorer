package pt.inescid.l2f.connection;

//Classes necessárias para a conexão à base de dados
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException;

public class ConnectionSQLite {

  private static String status = "Not connected"; 
  
  //Método de Conexão// 
  public static Connection getConnectionSQLite() { 
	  
	  try { 
		  Connection connection = null;
		  
		  // load the sqlite-JDBC driver using the current class loader
		  String driverName = "org.sqlite.JDBC";
		  Class.forName(driverName); 
	

			 // Configuração da conexão à base de dados//

			String mydatabase = "db_deep.sqlite";
//		  	String mydatabase = "db_deep_aux.sqlite";
			String dir = "/Users/josecorreia/Projects/DB/";
			String path = "jdbc:sqlite:" + dir + mydatabase; 

			// create a database connection
		    connection = DriverManager.getConnection(path);
		 
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
	  ConnectionSQLite.CloseConnection(connection);
	  return ConnectionSQLite.getConnectionSQLite(); 
  } 
}