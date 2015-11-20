package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.dependencyExtractor.domain.Exemplifies;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExemplifiesTable extends RelationalTable {
	private String _corpusName;


	public ExemplifiesTable(Connection _connection, String corpusName) {
		super(_connection);
		_corpusName = corpusName;
	}

	//In SQL, single quotes will be escaped by using double single quotes. ' --> ''


    /**
     * Add a corpus' sentence that exemplifies a cooccurence to the database (table Exemplifica)
     *
     * @param exemplifies - object Exemplifies
     *
     */
  public void insertNewSetenceExample(Exemplifies exemplifies){
      //table = Exemplifica
	   Connection connection = getConnetion();

	  Statement stmt = null;

		try{
			stmt = connection.createStatement();

			String sql = "INSERT INTO Exemplifica (numeroFrase, nomeFicheiro, idPalavra1 , " +
                                                "idPalavra2 , nomeProp, tipoDep, nomeCorpus) VALUES(" +
												exemplifies.getSentenceNumber() + " , '" +
												exemplifies.getFilename() + "' , " +
												exemplifies.getIdPalavra1() + " , " +
                                                exemplifies.getIdPalavra2() + " , '" +
                                                exemplifies.getProperty()  + "' , '" +
                                                exemplifies.getDependency()  + "' , '" +
												_corpusName + "');";
			stmt.executeUpdate(sql);

	    }catch(SQLException se){
	    	System.out.println("|| Exemplifica: nova entrada");
			System.out.println(se.getMessage());
	    }finally{
	       //finally block used to close resources
	       try{
	          if(stmt!=null)
	             stmt.close();

	       }catch(SQLException se){
	       }// do nothing
	    }//end finally try
	}

}