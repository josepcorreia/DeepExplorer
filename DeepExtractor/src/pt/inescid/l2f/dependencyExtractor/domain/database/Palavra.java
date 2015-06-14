package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Palavra extends RelationalElement{
	private long _currentId;
	private String _corpusName;
	//private HashMap<String,String> 
	
	public Palavra(Connection conn, String corpusName) {
		super(conn);
		_corpusName = corpusName;
	}

	public long getNumberWords(String depname) {
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

	public long checkWord(String  word, String pos, String category, String depname){
		
		if(pos.equals("PASTPART")|| pos.equals("VINF") || pos.equals("VF")){
			pos = "VERB";
		}

		if(!wordExists(word, pos)){
			insertNewPalavra(word, pos, category);
			insertPalavraCorpus(_currentId, depname);
		}else{
			if(wordExistsCorpus(_currentId, depname)){
				uptadeFrequency(_currentId, depname);
			}
			else{
				insertPalavraCorpus(_currentId, depname);
			}
		}
		return _currentId;
	}
	
	public void insertNewPalavra(String  palavra, String classe, String categoria){
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			String query = "Insert into db_deep_aux.Palavra (palavra, classe) " +
						   "VALUES(\""+ palavra +"\",\"" + classe +"\" );";
						   
			
			stmt.executeUpdate(query);
	
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID();");
			if(rs.next()){
				_currentId = rs.getLong("LAST_INSERT_ID()");
			}
			rs.close();

			//System.out.println("Record is inserted into Palavra table!");
			
		} catch (SQLException e) {
			System.out.println("Inserir Nova Palavra: "+ palavra );
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
		Statement stmt = null;
	
		try{
			stmt = connection.createStatement();
			
			String sql = "SELECT idPalavra FROM Palavra WHERE palavra =\""+ word + "\" AND classe =\"" + pos +"\" LIMIT 1";
			
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
  public boolean insertPalavraCorpus(long id, String depname){
		PreparedStatement preparedStatement = null;
			
		try {
			preparedStatement = connection.prepareStatement("insert into Pertence values (?, ?, ?, ?)");
			preparedStatement.setLong(1, id);
			preparedStatement.setString(2,depname);
			preparedStatement.setString(3,_corpusName);
			preparedStatement.setInt(4,1);
			
			preparedStatement.executeUpdate();
			//System.out.println("Record is inserted into Fornece table!");
		} catch (SQLException e) {
			System.out.println("|| Palavra insere no corpus");
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
  
  public boolean wordExistsCorpus(long id, String depname){
		Statement stmt = null;
	
		try{
			stmt = connection.createStatement();
			String sql = "SELECT EXISTS(SELECT 1 FROM Pertence WHERE idPalavra = " + id + " AND tipoDep = '" + depname + "' AND nomeCorpus = '" + _corpusName +"' LIMIT 1)";
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
	    	System.out.println("|| Palavra");
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
  public void uptadeFrequency(long wordId, String depname){
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			String sql = "UPDATE Pertence " +
					     " SET frequencia = frequencia + 1 " +
					     " WHERE idPalavra = "+ wordId +
					     				" AND tipoDep = '"+ depname +
					     				"' AND nomeCorpus= '"+ _corpusName + "';";
						
			stmt.executeUpdate(sql);
	
		}catch(SQLException se){
		      //Handle errors for JDBC
			System.out.println("|| Palavra");
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
  
  public long getWordFrequency(Long id, String depname){
  		Statement stmt = null;
  		long freq = 0;
  		
		try{
			stmt = connection.createStatement();
			String sql = "SELECT frequencia " +
					     "FROM Pertence " +
					     "Where idPalavra = "+ id + 
					     		" and tipoDep = '" + depname +  
					     		"' and nomeCorpus = '"+ _corpusName +"';";
			ResultSet rs = stmt.executeQuery(sql);

			
			if(rs.next()){
				freq = rs.getInt(1);//mudar
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