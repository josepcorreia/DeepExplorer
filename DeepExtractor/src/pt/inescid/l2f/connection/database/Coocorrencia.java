package pt.inescid.l2f.connection.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import pt.inescid.l2f.connection.exception.CoocorrenceNotExist;
import pt.inescid.l2f.dependencyExtractor.domain.Coocorrence;
import pt.inescid.l2f.dependencyExtractor.domain.Word;
import pt.inescid.l2f.measures.AssociationMeasures;


public class Coocorrencia extends RelationalElement{
	private String _corpusName;

	public Coocorrencia(Connection _connection, String corpusName) {
		super(_connection);
		_corpusName = corpusName;
	}


	public int getNumberRows(){
		Connection connection = getConnetion();

		Statement stmt = null;
		int rows = 0;

		try{
			stmt = connection.createStatement();
			String sql = "SELECT count(*) FROM Coocorrencia;";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				rows = rs.getInt(1);
			}

			rs.close(); 

		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println("|| COOO - a verificar se so ha 1");
			se.printStackTrace();

		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();

			}catch(SQLException se){
			}// do nothing
		}//end finally try

		return rows;
	}

	public void insertCoocorrence(long wordId1, long wordId2,String property, String dependencyName, int freq){
		Connection connection = getConnetion();

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement("insert into  Coocorrencia values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			preparedStatement.setLong(1, wordId1);
			preparedStatement.setLong(2, wordId2);
			preparedStatement.setString(3, property);
			preparedStatement.setString(4, dependencyName);
			preparedStatement.setString(5, _corpusName);
			preparedStatement.setInt(6, freq);
			//medidas inicializadas a 0
			preparedStatement.setDouble(7, 0.0);
			preparedStatement.setDouble(8, 0.0);
			preparedStatement.setDouble(9, 0.0);
			preparedStatement.setDouble(10, 0.0);
			preparedStatement.setDouble(11, 0.0);
			preparedStatement.setDouble(12, 0.0);

			preparedStatement.executeUpdate();

			//System.out.println("Record is inserted into Coocorrencia table!");

		} catch (SQLException e) {
			System.out.println("|| COOO A INSERIR" + wordId2);
			System.out.println(e.getMessage());

		} finally {

			try {
				if (preparedStatement != null) 
					preparedStatement.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	} 

	public boolean coocorrenceExists(long wordId1, long wordId2, String prop, String dep) throws CoocorrenceNotExist{
		Connection connection = getConnetion();

		Statement stmt = null;

		try{
			stmt = connection.createStatement();
			String sql = "SELECT EXISTS(SELECT 1 FROM Coocorrencia WHERE idPalavra1 ='" + wordId1 + 
					"' AND idPalavra2 ='" + wordId2 + 
					"' AND nomeProp='"+ prop + 
					"' AND tipoDep='" + dep + 
					"' AND nomeCorpus='" + _corpusName + "' LIMIT 1)";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			if(rs.getInt(1)==1){
				rs.close(); 
				return true;
			}

			//caso nao exista
			rs.close(); 

		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println("|| COOO - a verificar se so ha 1");
			se.printStackTrace();

		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
				
			}catch(SQLException se){
			}// do nothing
		}//end finally try

		throw new CoocorrenceNotExist();
	}
	public void uptadeFrequency(long wordId1, long wordId2, String prop, String dep, int freq){
		Connection connection = getConnetion();

		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			String sql = "UPDATE Coocorrencia " +
					"SET frequencia = frequencia + " + freq + " "+
					"WHERE idPalavra1='"+ wordId1 +
					"' AND idPalavra2='" + wordId2 + 
					"' AND nomeProp='" + prop + 
					"' AND tipoDep = '" + dep + 
					"' AND nomeCorpus= '"+ _corpusName +"'";

			stmt.executeUpdate(sql);

		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println("|| COOO frequencia");
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

	public int getDependencyFrequency(Coocorrence coocorrence){
		long wordId1 = coocorrence.getIdPalavra1();
		long wordId2 = coocorrence.getIdPalavra2();
		String prop = coocorrence.getProperty();
		String dep = coocorrence.getDependency();

		Connection connection = getConnetion();

		Statement stmt = null;
		int freq = 0;

		try{
			stmt = connection.createStatement();
			String sql = "SELECT frequencia " + 
					"FROM Coocorrencia " +
					"WHERE idPalavra1 = "+ wordId1 + 
					" and idPalavra2 = " + wordId2 + 
					" and tipoDep= '" + dep + 
					"' and nomeProp = '" + prop +
					"' and nomeCorpus = '" + _corpusName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){  
				freq = rs.getInt("frequencia");
			}

			rs.close(); 

		}catch(SQLException se){
			//Handle errors for JDBC
			System.out.println("Frequencia duma denpedencia");
			se.printStackTrace();

		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
				
			}catch(SQLException se){
			}// do nothing
		}//end finally try

		return freq ;
	}

	public String uptadeAssociationMeasure(long wordId1, long wordId2, String prop, String dep, double pmi, double dice, double logDice) throws SQLException{

		String sql = "UPDATE Coocorrencia "+ 
				"SET PMI = " + pmi + " , Dice = " + dice + " , LogDice = " + logDice + 
				" WHERE idPalavra1 = " + wordId1+
				" and idPalavra2 = "+ wordId2 +
				" and nomeProp = '" + prop +
				"' and tipoDep = '"+ dep +
				"' and nomeCorpus = '" +  _corpusName + "'";
		return sql;
	}

	public void UpdateMeasures(){
		Connection connection = getConnetion(); 
		Statement s;
		
		int intervall = 2000;
		int totalrows =  getNumberRows();

		HashMap<String, Long> nWords = new HashMap<String, Long>();

		int index = 0;
		while(index < totalrows){
			Statement stmt = null;

			try{

				stmt = connection.createStatement();
				String sql = "SELECT * FROM Coocorrencia LIMIT " + index + "," +  intervall +  ";" ;
				ResultSet rs = stmt.executeQuery(sql);
				s = connection.createStatement();
				
				Palavra palavra = RelationalFactory.getPalavra();
				
				while(rs.next()){  
					long word1 = rs.getLong("idPalavra1");
					long word2 = rs.getLong("idPalavra2");
					long depfreq = rs.getLong("frequencia");
					String dep = rs.getString("tipoDep");
					String prop =  rs.getString("nomeProp");
					long nWordDep = 0;

					if(nWords.containsKey(dep)){
						nWordDep = nWords.get(dep); 
					}
					else{
						nWordDep = palavra.getNumberWords(dep);
						nWords.put(dep, nWordDep);
					}

					long word1freq = palavra.getWordFrequency(word1,dep, prop); 

					long word2freq = palavra.getWordFrequency(word2, dep, prop);
					//System.out.println(nWordDep);

					double pmi = AssociationMeasures.PMI(nWordDep, depfreq, word1freq, word2freq);
					double dice = AssociationMeasures.Dice(depfreq, word1freq, word2freq);					
					double logDice = AssociationMeasures.LogDice(depfreq, word1freq, word2freq);

					s.addBatch(uptadeAssociationMeasure(word1, word2,prop, dep, pmi, dice, logDice));

				}
				rs.close(); 
				s.executeBatch();
				s.clearBatch();

			}catch(SQLException se){
				//Handle errors for JDBC
				System.out.println("ERROR: Update measures");
				se.printStackTrace();

			}finally{
				//finally block used to close resources
				try{
					if(stmt!=null)
						stmt.close();
										
				}catch(SQLException se){
				}// do nothing
			}//end finally try

			index = index + intervall;

		}
		try {
			connection.commit();
		} catch (SQLException e) {

		}

	}
}