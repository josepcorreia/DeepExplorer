package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.exception.DatabaseException;
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

	/**
	 * Add a word that occurs in a certain corpus to the database (table Pertence)
	 *
	 * @param id -  word's id
     * @param depname - dependency name
     * @param prop - property
     * @param freq - frequency of that word
	 *
	 * @return true if the word was inserted
	 * @throws DatabaseException in the case of a problem in the database/database's connection
     */
  public boolean insertWordCorpus(long id, String depname, String prop, int freq) throws DatabaseException {
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
			System.out.println(se.getMessage());
			throw new DatabaseException("Pertence", "insertWordCorpus");

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

    /**
     * Verify if a word that occurs in a certain corpus exists in the database (table Palavra)
     *
     * @param id -  word's id
     * @param depname - dependency name
     * @param prop - property
     *
     * @return true if this word already occurs in this corpus
     * @throws WordNotExistCorpus if this word do not occurs in this corpus
     */
  public boolean wordExistsCorpus(long id, String depname, String prop) throws WordNotExistCorpus, DatabaseException {
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
			
			rs.close(); 
			
  
	    }catch(SQLException se){
	       //Handle errors for JDBC
	        se.printStackTrace();
			throw new DatabaseException("Pertence", "wordExistsCorpus (wordid "+ id +")");
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

    /**
     * Update the word's frequency
     *
     * @param wordId first word's Id
     * @param prop cooccurrence's property
     * @param dep cooccurrence's dependency
     * @param freq cooccurrence's frequency
	 *
	 * @throws DatabaseException in the case of a problem in the database/database's connection
     */
  public void uptadeFrequency(long wordId, String dep, String prop, int freq) throws DatabaseException {
	  Connection connection = getConnetion();	

	  Statement stmt = null;
		try{
			stmt = connection.createStatement();
			String sql = "UPDATE Pertence " +
					     " SET frequencia = frequencia + "+ freq + " "+
					     " WHERE idPalavra = "+ wordId +
					     				" AND tipoDep = '"+ dep +
					     				"' AND nomeProp = '"+ prop +
					     				"' AND nomeCorpus= '"+ _corpusName + "';";
						
			stmt.executeUpdate(sql);
	
		}catch(SQLException se){
		      //Handle errors for JDBC
			  se.printStackTrace();
			  throw new DatabaseException("Pertence", "uptadeFrequency (wordid "+ wordId +")");
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		         
		      }catch(SQLException se){
		      }// do nothing

		}//end finally try	   
	}

    /**
     * Get the frequency of a word that occurs in a certain corpus
     *
     * @param wordId first word's Id
     * @param prop cooccurrence's property
     * @param dep cooccurrence's dependency
     *
     * @return the frequency of a word
	 *
	 * @throws DatabaseException in the case of a problem in the database/database's connection
     */
  public long getWordFrequency(Long wordId, String dep, String prop) throws DatabaseException {
	  Connection connection = getConnetion();	
	  
	  Statement stmt = null;
  		int freq = 0;
  		
		try{
			stmt = connection.createStatement();
			String sql = "SELECT frequencia " +
					     "FROM Pertence " +
					     "Where idPalavra = "+ wordId +
					     		" and tipoDep = '" + dep +
					     		"' and nomeProp = '"+ prop +
					     		"' and nomeCorpus = '"+ _corpusName +"';";
			ResultSet rs = stmt.executeQuery(sql);

			
			if(rs.next()){
				freq = rs.getInt("frequencia");
			}

			rs.close(); 

		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
			throw new DatabaseException("Pertence", "getWordFrequency (wordid "+ wordId +")");
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

    /**
     * Get the frequency of a group of words with the same dependency-property pattern
     *
     * @param prop cooccurrence's property
     * @param dep cooccurrence's dependency
     *
     * @return the frequency of a group of words with the same dependency-property pattern
	 *
	 * @throws DatabaseException in the case of a problem in the database/database's connection
     */
	public long getDepNumberWords(String dep, String prop) throws DatabaseException {
		Connection connection = getConnetion();

		Statement stmt = null;
		long totalFreq = 0;
		try{
			stmt = connection.createStatement();

			String sql = "Select sum(frequencia) as total " +
					"from Pertence " +
					"WHERE tipoDep = '" + dep +
					"' and nomeProp = '"+ prop +
					"' and nomeCorpus = '"+ _corpusName +"'";

			ResultSet rs = stmt.executeQuery(sql);


			if(rs.next()){
				totalFreq = rs.getLong("total");
			}

			rs.close();

		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
			throw new DatabaseException("Pertence", "getDepNumberWords");


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