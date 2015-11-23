package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.exception.DatabaseException;
import pt.inescid.l2f.connection.exception.WordNotExist;
import pt.inescid.l2f.dependencyExtractor.domain.Word;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WordTable extends RelationalTable {
	private String _corpusName;


	public WordTable(Connection _connection, String corpusName) {
		super(_connection);
		_corpusName = corpusName;
	}

	//In SQL, single quotes will be escaped by using double single quotes. ' --> ''


    /**
     * Add a word to the database (table Palavra)
     *
     * @param word - object Word
     *
     * @return the word's id
	 *
	 * @throws DatabaseException in the case of a problem in the database/database's connection
     */
	public Long insertNewWord(Word  word) throws DatabaseException {

		Connection connection = getConnetion();

		String lemma = word.getLemma().replace("'","''");
		String pos = word.getPOS();
		Long id = (long)0;

		Statement stmt = null;
		try {
			stmt = connection.createStatement();

			//Insert into Palavra (idPalavra, palavra, classe)
			String query = "INSERT INTO Palavra VALUES( NULL, '"+ lemma + "' , '" + pos + "' );";

			stmt.executeUpdate(query);

			String sql = "SELECT idPalavra FROM Palavra WHERE palavra = '"+ lemma + "' AND classe = '" + pos +"' LIMIT 1";

			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				id = rs.getLong("idPalavra");
			}
			rs.close();

			//System.out.println("Record is inserted into Palavra table!");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DatabaseException("Palavra", "insertNewWord (word:" + lemma + ", pos:" +pos+")");

		} finally {
			//finally block used to close resources
		       try{
		          if(stmt!=null)
		             stmt.close();

		       }catch(SQLException se){
		       }// do nothing
		}//end finally try

		return id;
  }

    /**
     * Verify if a word exists in the database (table Palavra)
     *
     * @param word - object Word
     *
     * @return the word's id
     * @throws WordNotExist if this word do not exists in the database
	 * @throws DatabaseException in the case of a problem in the database/database's connection
     */
	public Long wordExists(Word word) throws WordNotExist, DatabaseException {
		Connection connection = getConnetion();

		String lemma = word.getLemma().replace("'","''");
		String pos = word.getPOS();

		Statement stmt = null;

		try{
			stmt = connection.createStatement();

			String sql = "SELECT idPalavra FROM Palavra WHERE palavra ='"+ lemma + "' AND classe =\'" + pos +"' LIMIT 1";

			ResultSet rs = stmt.executeQuery(sql);


			if(rs.next()){
				Long id = rs.getLong("idPalavra");
				rs.close();
				return id;
			}

			//caso nao exista
			rs.close();

	    }catch(SQLException se){
	       //Handle errors for JDBC
	       se.printStackTrace();
			throw new DatabaseException("Palavra", "wordExists (word:" + lemma + ", pos:" +pos+")");
	    }finally{
	       //finally block used to close resources
	       try{
	          if(stmt!=null)
	             stmt.close();

	       }catch(SQLException se){
	       }// do nothing
	    }//end finally try

		throw new WordNotExist();
	}
}