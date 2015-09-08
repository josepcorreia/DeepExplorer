package pt.inescid.l2f.connection.database;

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

	public Long insertNewWord(Word  word){

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
			System.out.println("Inserir Nova Palavra: "+ lemma);
			System.out.println(e.getMessage());

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

	//verifica se uma palavra existe na bd
	public Long wordExists(Word word) throws WordNotExist{
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

		throw new WordNotExist();
	}
}