package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Palavra extends RelationalElement{
	
	public Palavra(Connection conn) {
		super(conn);
	}

	  public boolean insertPalavra(long id, String  palavra, String lema, String classe, String categoria){
			PreparedStatement preparedStatement = null;
			
			try {
				preparedStatement = super.connection.prepareStatement("insert into  Palavra values (?, ?, ?, ? , ? , ?)");

				preparedStatement.setLong(1, id);
				preparedStatement.setString(2, palavra);
				preparedStatement.setString(3, lema);
				preparedStatement.setString(4, classe);
				preparedStatement.setString(5, categoria);
				preparedStatement.setInt(6, 1);
			
				preparedStatement.executeUpdate();

				System.out.println("Record is inserted into Palavra table!");

			} catch (SQLException e) {

				System.out.println(e.getMessage());
				return false;

			} finally {

				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		  return true;
	  }
	  public boolean insertPalavraCorpus(long id, String corpus){
			PreparedStatement preparedStatement = null;
			
			try {
				preparedStatement = connection.prepareStatement("insert into Fornece values (?, ?)");

				preparedStatement.setLong(1, id);
				preparedStatement.setString(2,corpus);
			
				preparedStatement.executeUpdate();

				System.out.println("Record is inserted into Fornece table!");

			} catch (SQLException e) {

				System.out.println(e.getMessage());
				return false;

			} finally {

				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		  return true;
	  }

}
