package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Palavra extends RelationalElement{
	private long  _idCounter;
	private long _currentId;
	private String _corpusName;
	private long _numberWords;//número total de palavras no corpus
	
	
	
	public Palavra(Connection conn, String corpusName) {
		super(conn);
		_idCounter= getLastId() + 1;
		_corpusName = corpusName;
	}

	public long getNumberWords() {
		Statement stmt = null;
		long totalFreq = 0;
		try{
			stmt = connection.createStatement();
			
			String sql = "Select sum(frequencia) as total from Pertence WHERE nomeCorpus = '"+ _corpusName +"'";
			
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

	private long getLastId() {
		Statement stmt = null;
		long id = 0;
		try{
			stmt = connection.createStatement();
			
			String sql = "SELECT idPalavra FROM Palavra ORDER BY idPalavra DESC LIMIT 1";
			
			ResultSet rs = stmt.executeQuery(sql);

			
			if(rs.next()){
				id = rs.getLong("idPalavra");
			}
			
			rs.close(); 
    
	    }catch(SQLException se){
	       //Handle errors for JDBC
	    	System.out.println("||idPalavra ao inicio");
	       se.printStackTrace();
	    }finally{
	       //finally block used to close resources
	       try{
	          if(stmt!=null)
	             stmt.close();
	       }catch(SQLException se){
	       }// do nothing
	    }//end finally try
		
		return id;
	}

	public long checkWord(String  word, String pos, String category){
		
		if(pos.equals("PASTPART")|| pos.equals("VINF") || pos.equals("VF")){
			pos = "VERB";
		}

		
		if(wordExists(word, pos)){
			//if(wordExistsCorpus(_currentId)){
				uptadeFrequency(_currentId);
			//}else{
			//}
			
		}else{
			insertNewPalavra(word, pos, category);
			insertPalavraCorpus(_currentId, _corpusName);
		}
		return _currentId;
	}
	
	public void insertNewPalavra(String  palavra, String classe, String categoria){
		PreparedStatement preparedStatement = null;
		_currentId = _idCounter++;
		try {
			preparedStatement = super.connection.prepareStatement("insert into Palavra values (?, ?, ?,?)");
			preparedStatement.setLong(1, _currentId);
			preparedStatement.setString(2, palavra);
			preparedStatement.setString(3, classe);
			preparedStatement.setString(4, categoria);
			
			preparedStatement.executeUpdate();

			//System.out.println("Record is inserted into Palavra table!");

		} catch (SQLException e) {
			System.out.println("|| Palavra- inserir "+ palavra );
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
  public boolean insertPalavraCorpus(long id, String corpus){
		PreparedStatement preparedStatement = null;
			
		try {
			preparedStatement = connection.prepareStatement("insert into Pertence values (?, ?, ?)");
			preparedStatement.setLong(1, id);
			preparedStatement.setString(2,corpus);
			preparedStatement.setInt(3,1);
			
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
  
  public boolean wordExistsCorpus(long id){
		Statement stmt = null;
	
		try{
			stmt = connection.createStatement();
			String sql = "SELECT EXISTS(SELECT 1 FROM Pertence WHERE idPalavra =\""+ id + "\" AND nomeCorpus =\"" + _corpusName +"\" LIMIT 1)";
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
  public void uptadeFrequency(long wordId){
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			String sql = "UPDATE Pertence " +
					     " SET frequencia = frequencia + 1 " +
					     " WHERE idPalavra=\""+ wordId +"\" AND nomeCorpus= \""+ _corpusName +"\"";
						
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
  
  public long getWordFrequency(Long id){
  		Statement stmt = null;
  		long freq = 0;
  		
		try{
			stmt = connection.createStatement();
			String sql = "SELECT frequencia " +
					     "FROM Pertence " +
					     "Where idPalavra = "+ id + 
					     		" and nomeCorpus = '"+ _corpusName +"';";
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