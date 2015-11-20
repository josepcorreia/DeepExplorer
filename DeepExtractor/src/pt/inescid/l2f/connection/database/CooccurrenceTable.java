package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.exception.CooccurrenceNotExist;
import pt.inescid.l2f.dependencyExtractor.domain.Cooccurrence;
import pt.inescid.l2f.measures.AssociationMeasures;

import java.sql.*;
import java.util.HashMap;


public class CooccurrenceTable extends RelationalTable {
	private String _corpusName;

	public CooccurrenceTable(Connection _connection, String corpusName) {
		super(_connection);
		_corpusName = corpusName;
	}

    /**
     * Get the number of rows that cooccurence table has in the database
     *
     * @result - return the number of rows that cooccurence table
     */
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

    /**
     * Add a cooccurrence to the database (table Coocorrencia)
     *
     * @param wordId1 first word's Id
     * @param wordId2 second word's Id
     * @param property cooccurrence's property
     * @param dependencyName cooccurrence's dependency
     * @param freq cooccurrence's frequency
     *
     */
	public void insertCooccurrence(long wordId1, long wordId2, String property, String dependencyName, int freq){
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

    /**
     * Verify if a cooccurrence exists in the database (table Coocorrencia)
     *
     * @param wordId1  - first word's Id
     * @param wordId2 - second word's Id
     * @param prop - cooccurrence's property
     * @param dep - cooccurrence's dependency
     *
     * @result true if this cooccurence exists
     * @throws CooccurrenceNotExist if this cooccurence do not exists
     */
	public boolean cooccurrenceExists(long wordId1, long wordId2, String prop, String dep) throws CooccurrenceNotExist {
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

		throw new CooccurrenceNotExist();
	}

    /**
     * Get the number of cooccurrences with a certain dependency-property pattern
     *
     * @param prop - cooccurrence's property
     * @param dep - cooccurrence's dependency
     * @return number of cooccurences with a certain dependency-property pattern
     */
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

    /**
     * Get the frequency of a cooccurrence
     *
     * @param cooccurrence - cooccurrence's property
     * @return the frequency of a cooccurrence
     */
	public int getCoocorrenceFrequency(Cooccurrence cooccurrence){
		long wordId1 = cooccurrence.getWordId1();
		long wordId2 = cooccurrence.getWordId2();
		String prop = cooccurrence.getProperty();
		String dep = cooccurrence.getDependency();

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

    /**
     * Update the cooccurrence's frequency
     *
     * @param wordId1 first word's Id
     * @param wordId2 second word's Id
     * @param prop cooccurrence's property
     * @param dep cooccurrence's dependency
     * @param freq cooccurrence's frequency
     */
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


    /**
     * Update the cooccurrence's fequency
     *
     * @param wordId1 first word's Id
     * @param wordId2 second word's Id
     * @param prop - cooccurrence's property
     * @param dep - cooccurrence's dependency
     * @param pmi - cooccurrence's value of pmi
     * @param dice - cooccurrence's value of dice
     * @param logDice -  cooccurrence's value of logDice
     * @param chisquarePearson - cooccurrence's value of chisquarePearson
     * @param loglikelihood - cooccurrence's value of loglikelihood
     * @param significance - cooccurrence's value of significance
     * @throws SQLException
     */
	public String uptadeAssociationMeasures(long wordId1, long wordId2, String prop, String dep,
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

    /**
     * Get all the cooccurrences stored in the database, and for each one, it is calculated the six systems' measures
     *  - the calculation of association measures is performed by set of 2000 cooccurrences
     *
     */
	public void updateMeasures(){
		Connection connection = getConnetion(); 
		Statement s;
        WordBelongsTable wordBelongsTable = RelationalFactory.getWordBelongs();
        ExemplifiesTable exemplifiesTable = RelationalFactory.getExemplifies();

        //
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

                    //the necessary information for the measures's calculation
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

                    //Hashmap: Word's frequency for each dependency-property pattern
                    // (Measures that use this information: pmi)
                    if(totalWordsDeps.containsKey(depProp)){
						numberWordDep = totalWordsDeps.get(depProp);
					}
					else{
						numberWordDep = wordBelongsTable.getDepNumberWords(dep, prop);
                        totalWordsDeps.put(depProp, numberWordDep);
                    }

                    //Hashmap:  Cooccurence's frequency for each dependency-property pattern
                    // (Measures that use this information: chisquarePerason and Significance)
                    if(totalCoocorrenceDeps.containsKey(depProp)){
                        totalCoocorenceDep = totalCoocorrenceDeps.get(depProp);
                    }
                    else{
                        totalCoocorenceDep = getDepNumberCoocorrences(dep, prop);
                        totalCoocorrenceDeps.put(depProp, totalCoocorenceDep);
                    }

                    double pmi = AssociationMeasures.PMI(numberWordDep, depfreq, word1freq, word2freq);

                    double dice = AssociationMeasures.Dice(depfreq, word1freq, word2freq);

                    double logDice = AssociationMeasures.LogDice(depfreq, word1freq, word2freq);

                    double chisquarePearson = AssociationMeasures.chiSquarePearson(totalCoocorenceDep, depfreq, word1CococorrenceFreq, word2CococorrenceFreq);

					double loglikelihood = AssociationMeasures.logLikelihood(numberWordDep, depfreq, word1freq, word2freq);

                    double significance = AssociationMeasures.significance(word1freq,word2freq,depfreq, totalCoocorenceDep);

					s.addBatch(uptadeAssociationMeasures(word1, word2, prop, dep, pmi, dice, logDice, chisquarePearson,
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