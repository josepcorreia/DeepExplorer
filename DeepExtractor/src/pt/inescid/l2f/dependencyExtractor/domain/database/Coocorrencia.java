package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Coocorrencia extends RelationalElement{

	public Coocorrencia(Connection conn) {
		super(conn);
	}

	public boolean insertCoocorrencia(long idpalavra1, long idpalavra2,String propriedade, String tipodepedencia){
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("insert into  Co-ocorrencia values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");

			preparedStatement.setLong(1, idpalavra1);
			preparedStatement.setLong(2, idpalavra1);
			preparedStatement.setString(3, propriedade);
			preparedStatement.setString(4, tipodepedencia);
			preparedStatement.setString(5, tipodepedencia);
			preparedStatement.setInt(6, 1);
			//medidas inicializadas a 0
			preparedStatement.setDouble(7, 0.0);
			preparedStatement.setDouble(8, 0.0);
			preparedStatement.setDouble(9, 0.0);
			preparedStatement.setDouble(10, 0.0);
			preparedStatement.setDouble(11, 0.0);
			preparedStatement.setDouble(12, 0.0);
			
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
