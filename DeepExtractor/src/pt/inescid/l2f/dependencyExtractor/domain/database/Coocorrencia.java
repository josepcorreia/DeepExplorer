package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pt.inescid.l2f.dependencyExtractor.domain.measures.AssociationMeasures;


public class Coocorrencia extends RelationalElement{
	private String _corpusName;
	
	public Coocorrencia(Connection conn, String corpusName) {
		super(conn);
		_corpusName = corpusName;
	}

	public int getNumberRows(){
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
	
	
	
	public void checkCoocorrence(long wordId1, long wordId2, String prop, String dep){
		if(coocorrenceExists(wordId1, wordId2, prop, dep)){
			uptadeFrequency(wordId1, wordId2, prop, dep);
		}else{
			insertCoocorrence(wordId1, wordId2, prop, dep);
		}
	}
	
	public void insertCoocorrence(long wordId1, long wordId2,String property, String dependencyName){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Coocorrencia values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			preparedStatement.setLong(1, wordId1);
			preparedStatement.setLong(2, wordId2);
			preparedStatement.setString(3, property);
			preparedStatement.setString(4, dependencyName);
			preparedStatement.setString(5, _corpusName);
			preparedStatement.setInt(6, 1);
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

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	} 
	
	public boolean coocorrenceExists(long wordId1, long wordId2, String prop, String dep){
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
			return false;

    
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
 
		return true;
	}
	public void uptadeFrequency(long wordId1, long wordId2, String prop, String dep){
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			String sql = "UPDATE Coocorrencia " +
					     "SET frequencia = frequencia + 1 " +
					     "WHERE idPalavra1='"+ wordId1 +"' AND idPalavra2='" + wordId2 + "' AND nomeProp='" + prop + "' AND tipoDep = '" + dep + "' AND nomeCorpus= '"+ _corpusName +"'";
						
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
	
	public int getDependencyFrequency(long wordId1, long wordId2, String prop, String dep){
		Statement stmt = null;
		int freq = 0;
		
		try{
			stmt = connection.createStatement();
			String sql = "SELECT frequencia " + 
						 "FROM Coocorrencia " +
						 "WHERE idPalavra1 = "+ wordId1 + 
						        " and idPalavra2 = " + wordId2 + 
						        " and tipoDep=" + dep + 
						        " and nomeProp = " + prop +
						        " and nomeCorpus = " + _corpusName + ";";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){  
				freq = rs.getInt(1);
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
	
	public void uptadeAssociationMeasure(long wordId1, long wordId2, String prop, String dep, String measure, double value){
		
		
		
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			String sql = "UPDATE Coocorrencia "+ 
						 "SET "+ measure + " = " + value + 
						 " WHERE idPalavra1 = " + wordId1+
						 " and idPalavra2 = "+ wordId2 +
						 " and nomeProp = '" + prop +
						 "' and tipoDep = '"+ dep +
						 "' and nomeCorpus = '" +  _corpusName + "'";
						
			stmt.executeUpdate(sql);
	
		}catch(SQLException se){
		      //Handle errors for JDBC
			System.out.println("Measure");
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
	
	public void UpdateMeasures(Palavra pal){
		int intervall = 1000;
		int totalrows =  getNumberRows();
		long nWords = pal.getNumberWords();
		
		int index = 0;
		while(index < totalrows){
			Statement stmt = null;

			try{
				
				stmt = connection.createStatement();
				String sql = "SELECT * FROM Coocorrencia LIMIT " + index + "," +  intervall +  ";" ;
				ResultSet rs = stmt.executeQuery(sql);

				while(rs.next()){  
					long word1 = rs.getLong("idPalavra1");
					long word2 = rs.getLong("idPalavra2");
					long depfreq = rs.getLong("frequencia");
					String dep = rs.getString("tipoDep");
					String prop =  rs.getString("nomeProp");
					
					long word1freq = pal.getWordFrequency(word1);
					long word2freq = pal.getWordFrequency(word2);
					
					double pmi = AssociationMeasures.PMI(nWords, depfreq, word1freq, word2freq);
				
					
					uptadeAssociationMeasure(word1, word2,prop, dep, "PMI", pmi);
				}
				rs.close(); 



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
	}
}