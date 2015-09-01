package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.exception.WordNotExist;
import pt.inescid.l2f.connection.exception.WordNotExistCorpus;
import pt.inescid.l2f.dependencyExtractor.domain.Coocorrence;
import pt.inescid.l2f.dependencyExtractor.domain.Exemplifies;
import pt.inescid.l2f.dependencyExtractor.domain.Sentence;
import pt.inescid.l2f.dependencyExtractor.domain.Word;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SentenceDB extends RelationalElement{
	private String _corpusName;


	public SentenceDB(Connection _connection, String corpusName) {
		super(_connection);
		_corpusName = corpusName;
	}

	//In SQL, single quotes will be escaped by using double single quotes. ' --> ''
	public void insertNewSentence(Sentence sentence){

		Connection connection = getConnetion();

		String sentence_text = sentence.getSentenceText().replace("'", "''");
		int sentenceNumber = sentence.getSentenceNumber();
		String filename = sentence.getFilename();

		Statement stmt = null;
		try {

            stmt = connection.createStatement();

			  				//insert into frase values(numeroFrase,nomeFicheiro, frase,nomeCorpus)
			String query = "INSERT INTO Frase VALUES( "+ sentenceNumber + ", '" + filename + "' , '"+ sentence_text +"','"+ _corpusName +"' );";

			stmt.executeUpdate(query);

			//System.out.println("Record is inserted into Frase table!");

		} catch (SQLException e) {
			System.out.println("Inserir Nova Frase: "+ sentenceNumber + " " +filename);
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

    public boolean sentenceExists(Sentence sentence){
        Connection connection = getConnetion();

        Statement stmt = null;

        try{
            stmt = connection.createStatement();

            String sql = "SELECT EXISTS(SELECT 1 FROM Frase WHERE numeroFrase = "+ sentence.getSentenceNumber()+
                                            " AND nomeFicheiro = '"+ sentence.getFilename()+"' LIMIT 1)";

            ResultSet rs = stmt.executeQuery(sql);

            if(rs.next()){
                if(rs.getInt(1)==1){
                    rs.close();
                    return true;
                }
            }
            //caso nao exista
            rs.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            System.out.println("||Sentence Exists? " + sentence.getFilename() + " " + sentence.getSentenceNumber() );
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

	//Insere a coocorrencia na tabela que indica quais as frases que ocorrem numa determinada coocorrencia
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