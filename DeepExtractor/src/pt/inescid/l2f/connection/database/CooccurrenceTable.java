package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.exception.CoocorrenceNotExist;
import pt.inescid.l2f.dependencyExtractor.domain.Coocorrence;
import pt.inescid.l2f.measures.AssociationMeasures;

import java.sql.*;
import java.util.HashMap;


public class CooccurrenceTable extends RelationalTable {
	private String _corpusName;

	public CooccurrenceTable(Connection _connection, String corpusName) {
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

    public long getDepNumberCoocorrences(String dep, String prop){
        //total number of coocorrences in database for dep x and prop y
        Connection connection = getConnetion();

        Statement stmt = null;
        long total = 0;

        try{
            stmt = connection.createStatement();
            String sql = "SELECT sum(frequencia) as total FROM Coocorrencia Where tipoDep = '"+ dep +"' and nomeProp = '" + prop+ "';";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                total = rs.getLong("total");
            }

            rs.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            System.out.println("|| COO: Numero total de coocorrencias");
            se.printStackTrace();

        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();

            }catch(SQLException se){
            }// do nothing
        }//end finally try

        return total;
    }

	public int getCoocorrenceFrequency(Coocorrence coocorrence){
		long wordId1 = coocorrence.getWordId1();
		long wordId2 = coocorrence.getWordId2();
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
			System.out.println("Frequencia duma dependencia");
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

	public String uptadeAssociationMeasure(long wordId1, long wordId2, String prop, String dep,
                                                                                    double pmi,
                                                                                    double dice,
                                                                                    double logDice,
                                                                                    double chisquarePearson,
                                                                                    double loglikelihood,
                                                                                    double significance
                                                                                    ) throws SQLException{

		String sql = "UPDATE Coocorrencia "+ 
				"SET PMI = '" + pmi + "' , Dice = '" + dice + "' , LogDice = '" + logDice +
                                                            "' , ChiPearson = '" + chisquarePearson +
                                                            "' , LogLikelihood = '" + loglikelihood +
                                                            "' , Significance = '" + significance +
				"' WHERE idPalavra1 = " + wordId1+
				" and idPalavra2 = "+ wordId2 +
				" and nomeProp = '" + prop +
				"' and tipoDep = '"+ dep +
				"' and nomeCorpus = '" +  _corpusName + "'";
		return sql;
	}

	public void updateMeasures(){
		Connection connection = getConnetion(); 
		Statement s;
        WordBelongsTable wordBelongsTable = RelationalFactory.getWordBelongs();
        ExemplifiesTable exemplifiesTable = RelationalFactory.getExemplifies();

		int intervall = 2000;
		int totalrows =  getNumberRows();

        int alreadyDone = 0;

		HashMap<String, Long> totalWordsDeps = new HashMap<String, Long>();
        HashMap<String, Long> totalCoocorrenceDeps = new HashMap<String, Long>();
        HashMap<String, Long> totalSentencesDeps = new HashMap<String, Long>();

		int index = 0;
		while(index < totalrows){
			Statement stmt = null;

			try{

				stmt = connection.createStatement();
				String sql = "SELECT * FROM Coocorrencia LIMIT " + index + "," +  intervall +  ";" ;
				ResultSet rs = stmt.executeQuery(sql);
				s = connection.createStatement();

				
				while(rs.next()){

					long word1 = rs.getLong("idPalavra1");
					long word2 = rs.getLong("idPalavra2");
                    long depfreq = rs.getLong("frequencia");

                    String dep = rs.getString("tipoDep");
					String prop =  rs.getString("nomeProp");
                    String depProp = dep + "_" + prop;

                    long word1freq = wordBelongsTable.getWordFrequency(word1, dep, prop);
                    long word2freq = wordBelongsTable.getWordFrequency(word2, dep, prop);

                    long word1CococorrenceFreq = wordBelongsTable.getWordFrequency(word1, dep, prop);
                    long word2CococorrenceFreq = wordBelongsTable.getWordFrequency(word2, dep, prop);


                    long numberWordDep = 0;
                    long totalCoocorenceDep = 0;
                    long totalSentencesDep = 0;

                    //HAshmap WORDS (others measures)
                    if(totalWordsDeps.containsKey(depProp)){
						numberWordDep = totalWordsDeps.get(depProp);
					}
					else{
						numberWordDep = wordBelongsTable.getDepNumberWords(dep, prop);
                        totalWordsDeps.put(depProp, numberWordDep);
                    }

                    //HAshmap COOCORRENCES (chisquare perason)
                    if(totalCoocorrenceDeps.containsKey(depProp)){
                        totalCoocorenceDep = totalCoocorrenceDeps.get(depProp);
                    }
                    else{
                        totalCoocorenceDep = getDepNumberCoocorrences(dep, prop);
                        totalCoocorrenceDeps.put(depProp, totalCoocorenceDep);
                    }

                    //HAshmap SENTENCES (significance)
                    if(totalSentencesDeps.containsKey(depProp)){
                        totalSentencesDep = totalSentencesDeps.get(depProp);
                    }
                    else{
                        totalSentencesDep = exemplifiesTable.getNumberOfSentencesDep(dep, prop);
                        totalSentencesDeps.put(depProp, totalSentencesDep);
                    }

                    double pmi = AssociationMeasures.PMI(numberWordDep, depfreq, word1freq, word2freq);

                    double dice = AssociationMeasures.Dice(depfreq, word1freq, word2freq);

                    double logDice = AssociationMeasures.LogDice(depfreq, word1freq, word2freq);

                    double chisquarePearson = AssociationMeasures.chiSquarePearson(totalCoocorenceDep, depfreq, word1CococorrenceFreq, word2CococorrenceFreq);

					double loglikelihood = AssociationMeasures.logLikelihood(numberWordDep, depfreq, word1freq, word2freq);

                    double significance = AssociationMeasures.significance(word1freq,word2freq,depfreq, totalSentencesDep);

					s.addBatch(uptadeAssociationMeasure(word1, word2, prop, dep, pmi, dice, logDice, chisquarePearson,
							loglikelihood, significance));

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

			alreadyDone += intervall;
			if(alreadyDone > totalrows ){
				alreadyDone = totalrows;
			}

			double totalPercentage = ((double)alreadyDone/(double)totalrows)* 100;
            System.out.println(AssociationMeasures.round(totalPercentage, 3) + "%");

		}
	}
}