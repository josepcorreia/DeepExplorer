package pt.inescid.l2f.connection.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class Palavra extends RelationalElement{
	private long _currentId;
	private String _corpusName;


	public Palavra(Connection _connection, String corpusName) {
		super(_connection);
		_corpusName = corpusName;
	}

	public long checkWord(String  word, String pos, String category, String depname, String prop){
		
		if(!wordExists(word, pos)){
			insertNewPalavra(word, pos, category);
			insertPalavraCorpus(_currentId, depname, prop);
		}else{
			if(wordExistsCorpus(_currentId, depname, prop)){
				uptadeFrequency(_currentId, depname, prop);
			}
			else{
				insertPalavraCorpus(_currentId, depname, prop);
			}
		}
		return _currentId;
	}

	public long getNumberWords(String depname) {
		Connection connection = getConnetion();
		
		Statement stmt = null;
		long totalFreq = 0;
		try{
			stmt = connection.createStatement();
			
			String sql = "Select sum(frequencia) as total " + 
						 "from Pertence " + 
						 "WHERE tipoDep = '" + depname +
						 "' and nomeCorpus = '"+ _corpusName +"'";
			
			ResultSet rs = stmt.executeQuery(sql);

			
			if(rs.next()){
				totalFreq = rs.getLong("total");
			}
			
			rs.close(); 
    
	    }catch(SQLException se){
	       //Handle errors for JDBC
	    	System.out.println("||numero total de palavras");
	       se.printStackTrace();
	    }finally{
	       //finally block used to close resources
	       try{
	          if(stmt!=null)
	             stmt.close();

	       }catch(SQLException se){
	       }// do nothing
	    }//end finally try
		
		return totalFreq;
	}

	
	public void insertNewPalavra(String  word, String pos, String categoria){
		Connection connection = getConnetion();
		
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			
			//Insert into Palavra (idPalavra, palavra, classe)
			String query = "INSERT INTO Palavra VALUES( NULL, '"+ word + "' , '" + pos + "' , '" + categoria+"' );";
						   
			stmt.executeUpdate(query);
			
			String sql = "SELECT idPalavra FROM Palavra WHERE palavra = '"+ word + "' AND classe = '" + pos +"' LIMIT 1";
			
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				_currentId = rs.getLong("idPalavra");
			}
			rs.close();

			//System.out.println("Record is inserted into Palavra table!");
			
		} catch (SQLException e) {
			System.out.println("Inserir Nova Palavra: "+ word);
			System.out.println(e.getMessage());
			
		} finally {
			//finally block used to close resources
		       try{
		          if(stmt!=null)
		             stmt.close();
		          
		       }catch(SQLException se){
		       }// do nothing
		}//end finally try	
  }
	
	//verifica se uma palavra existe na bd e actualiza o atributo currentId, caso a palavra exista
	public boolean wordExists(String  word, String pos){
		Connection connection = getConnetion();
		
		Statement stmt = null;
	
		try{
			stmt = connection.createStatement();
			
			String sql = "SELECT idPalavra FROM Palavra WHERE palavra ='"+ word + "' AND classe =\'" + pos +"' LIMIT 1";
			
			ResultSet rs = stmt.executeQuery(sql);

			
			if(rs.next()){
				_currentId = rs.getLong("idPalavra");
				rs.close(); 
				return true;
			}
			
			//caso nao exista
			rs.close(); 
			return false;
    
	    }catch(SQLException se){
	       //Handle errors for JDBC
	    	System.out.println("||wordExists Palavra " + word);
	       se.printStackTrace();
	    }finally{
	       //finally block used to close resources
	       try{
	          if(stmt!=null)
	             stmt.close();
	          
	       }catch(SQLException se){
	       }// do nothing
	    }//end finally try
 
		return false;
	}
	
	
	//Insere a palavra na tabela que indica quais as palavras que pertencem a um determinado corpus
  public boolean insertPalavraCorpus(long id, String depname, String prop){
	  Connection connection = getConnetion();
	  
	  Statement stmt = null;
		
		try{
			stmt = connection.createStatement();
			
			

			String sql = "INSERT INTO Pertence (idPalavra, nomeCorpus, nomeProp, tipoDep, frequencia) VALUES(" +
												" '" + id + "' , '" +
												_corpusName + "' , '" + 
												prop  + "' , '" + 
												depname  + "' , " +  
												1 + ");";
			stmt.executeUpdate(sql);
  
	    }catch(SQLException se){
	    	System.out.println("|| Palavra insere no corpus");
			System.out.println(se.getMessage());
			return false;
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
  
  public boolean wordExistsCorpus(long id, String depname, String prop){
	  Connection connection = getConnetion();
	  
	  Statement stmt = null;
	
		try{
			stmt = connection.createStatement();
			String sql = "SELECT EXISTS(SELECT 1 FROM Pertence WHERE idPalavra = " + id + 
																" AND tipoDep = '" + depname + 
																"' AND nomeProp = '" + prop +
																"' AND nomeCorpus = '" + _corpusName +"' LIMIT 1)";
			
			
			ResultSet rs = stmt.executeQuery(sql);

			rs.next();
			if(rs.getInt(1)==1){
				rs.close(); 
				return true;
			}
			//caso nao exista
			rs.close(); 
			return false;
  
	    }catch(SQLException se){
	       //Handle errors for JDBC
	    	System.out.println("Pertence ao Coupus?");
	       se.printStackTrace();
	    }finally{
	       //finally block used to close resources
	       try{
	          if(stmt!=null)
	             stmt.close();
	          
	       }catch(SQLException se){
	       }// do nothing
	    }//end finally try

		return false;
	}
  public void uptadeFrequency(long wordId, String depname, String prop){
	  Connection connection = getConnetion();	
	  
	  Statement stmt = null;
		try{
			stmt = connection.createStatement();
			String sql = "UPDATE Pertence " +
					     " SET frequencia = frequencia + 1 " +
					     " WHERE idPalavra = "+ wordId +
					     				" AND tipoDep = '"+ depname +
					     				"' AND nomeProp = '"+ prop +
					     				"' AND nomeCorpus= '"+ _corpusName + "';";
						
			stmt.executeUpdate(sql);
	
		}catch(SQLException se){
		      //Handle errors for JDBC
			System.out.println("|| Update Frequency error");
		      se.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		         
		      }catch(SQLException se){
		      }// do nothing

		}//end finally try	   
	}
  
  public long getWordFrequency(Long id, String depname, String prop){
	  Connection connection = getConnetion();	
	  
	  Statement stmt = null;
  		long freq = 0;
  		
		try{
			stmt = connection.createStatement();
			String sql = "SELECT frequencia " +
					     "FROM Pertence " +
					     "Where idPalavra = "+ id + 
					     		" and tipoDep = '" + depname +  
					     		"' and nomeProp = '"+ prop +
					     		"' and nomeCorpus = '"+ _corpusName +"';";
			ResultSet rs = stmt.executeQuery(sql);

			
			if(rs.next()){
				freq = rs.getLong("frequencia");
			}

			rs.close(); 

		}catch(SQLException se){
		      //Handle errors for JDBC
			System.out.println("word freq");
		      se.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		         
		      }catch(SQLException se){
		      }// do nothing

		}//end finally try	 
		
		return freq;
		
  	}
}