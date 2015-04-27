package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Corpus extends RelationalElement{

	public Corpus(Connection conn) {
		super(conn);
	}

	public boolean insertCorpus(String nome, String  fonte, String ano, String genero, Boolean update){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Corpus values (?, ?, ?, ? , ?)");

			preparedStatement.setString(1, nome);
			preparedStatement.setString(2, fonte);
			preparedStatement.setString(3, ano);
			preparedStatement.setString(4, genero);
			preparedStatement.setBoolean(5, update);

		
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Corpus table!");

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
