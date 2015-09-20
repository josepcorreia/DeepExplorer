package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.exception.WordNotExistCorpus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WordBelongsTable extends RelationalTable {
	private String _corpusName;


	public WordBelongsTable(Connection _connection, String corpusName) {
		super(_connection);
		_corpusName = corpusName;
	}

	//In SQL, single quotes will be escaped by using double single quotes. ' --> ''

	
	//Insere a palavra na tabela que indica quais as palavras que pertencem a um determinado corpus
  public boolean insertWordCorpus(long id, String depname, String prop, int freq){
	  Connection connection = getConnetion();
	  
	  Statement stmt = null;
		
		try{
			stmt = connection.createStatement();
			
			

			String sql = "INSERT INTO Pertence (idPalavra, nomeCorpus, nomeProp, tipoDep, frequencia) VALUES(" +
												" '" + id + "' , '" +
												_corpusName + "' , '" + 
												prop  + "' , '" + 
												depname  + "' , " +  
												freq + ");";
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
  
  public boolean wordExistsCorpus(long id, String depname, String prop) throws WordNotExistCorpus{
	  Connection connection = getConnetion();
	  
	  Statement stmt = null;
	
		try{
			stmt = connection.createStatement();
			String sql = "SELECT 1 FROM Pertence WHERE idPalavra = " + id +
																" AND tipoDep = '" + depname + 
																"' AND nomeProp = '" + prop +
																"' AND nomeCorpus = '" + _corpusName +"' LIMIT 1";
			
			
			ResultSet rs = stmt.executeQuery(sql);

			rs.next();
			if(rs.getInt(1)==1){
				rs.close(); 
				return true;
			}
			
			rs.close(); 
			
  
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

		throw new WordNotExistCorpus();
	}
  public void uptadeFrequency(long wordId, String depname, String prop, int freq){
	  Connection connection = getConnetion();	

	  Statement stmt = null;
		try{
			stmt = connection.createStatement();
			String sql = "UPDATE Pertence " +
					     " SET frequencia = frequencia + "+ freq + " "+
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
  		int freq = 0;
  		
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
				freq = rs.getInt("frequencia");
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
	public long getDepNumberWords(String depname, String prop) {
		Connection connection = getConnetion();

		Statement stmt = null;
		long totalFreq = 0;
		try{
			stmt = connection.createStatement();

			String sql = "Select sum(frequencia) as total " +
					"from Pertence " +
					"WHERE tipoDep = '" + depname +
					"' and nomeProp = '"+ prop +
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
}