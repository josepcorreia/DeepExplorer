package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Propriedade extends RelationalElement{

	public Propriedade(Connection conn) {
		super(conn);
	}

	public boolean insertPropriedade(String propriedade, String tipodepedencia){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Propriedade values (?, ?)");

			preparedStatement.setString(1, propriedade);
			preparedStatement.setString(2, tipodepedencia);
			
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Propriedade table!");

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
