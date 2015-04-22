package pt.inescid.l2f.connection;

//Classes necessárias para a conexão à base de dados
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionMySQL {

  private String status = "Not connected"; 
  private Connection connection = null; 
  
  public ConnectionMySQL() { } 
  
  //Método de Conexão// 
  public void makeConnectionMySQL() { 
	  
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

		  	} catch (ClassNotFoundException e) { 
		  		System.out.println("The driver does not found."); 
		  		connection = null; 
		  	} catch (SQLException e) {
		  		System.out.println("Could not connect to the database."); 
		  		connection = null; 
		  	} 
	  } 
  
  public String getStatusConnection() { 
	  return status; 
  } 
  
  public Connection getConnection() { 
	  return connection; 
  } 
  
  public boolean CloseConnection() { 
	  try { 
		this.connection.close(); 
		return true; 
	    } catch (SQLException e) { 
		 return false; 
	    } 
  } 
  
  public void RestartConnection() { 
	  this.CloseConnection();
	  this.makeConnectionMySQL(); 
  } 

  public boolean insertCorpus(String nome, String  fonte, String ano, String genero, Boolean update){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Corpus values (?, ?, ?, ? , ?)");
 
			preparedStatement.setString(1, nome);
			preparedStatement.setString(2, fonte);
			preparedStatement.setString(3, ano);
			preparedStatement.setString(4, genero);
			preparedStatement.setBoolean(5, update);
 
		
			preparedStatement.executeUpdate();
 
			System.out.println("Record is inserted into Corpus table!");
 
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
			return false;
 
		} finally {
 
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	  return true;
  } 
  public boolean insertPalavra(long id, String  palavra, String lema, String classe, String categoria, String corpus){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Palavra values (?, ?, ?, ? , ? , ? , ?)");

			preparedStatement.setLong(1, id);
			preparedStatement.setString(2, palavra);
			preparedStatement.setString(3, lema);
			preparedStatement.setString(4, classe);
			preparedStatement.setString(5, categoria);
			preparedStatement.setInt(6, 1);
			preparedStatement.setString(7, corpus);
		
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Palavra table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return false;

		} finally {

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	  return true;
  }
  public boolean insertDepedencia(String tipodepedencia){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Dependencia values (?)");

			preparedStatement.setString(1, tipodepedencia);
			
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Depedencia table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return false;

		} finally {

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	  return true;
  }
  public boolean insertPropriedade(String propriedade, String tipodepedencia){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Propriedade values (?, ?)");

			preparedStatement.setString(1, propriedade);
			preparedStatement.setString(2, tipodepedencia);
			
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Propriedade table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return false;

		} finally {

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	  return true;
 }
  
  public boolean insertCoocorrencia(long idpalavra1, long idpalavra2,String propriedade, String tipodepedencia){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Co-ocorrencia values (?, ?)");

			preparedStatement.setLong(1, idpalavra1);
			preparedStatement.setLong(2, idpalavra1);
			preparedStatement.setString(3, propriedade);
			preparedStatement.setString(4, tipodepedencia);
			preparedStatement.setInt(5, 1);
			//medidas inicializadas a 0
			preparedStatement.setDouble(6, 0.0);
			preparedStatement.setDouble(7, 0.0);
			preparedStatement.setDouble(8, 0.0);
			preparedStatement.setDouble(9, 0.0);
			preparedStatement.setDouble(10, 0.0);
			preparedStatement.setDouble(11, 0.0);
			
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Propriedade table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return false;

		} finally {

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	  return true;
 }  
 
}
