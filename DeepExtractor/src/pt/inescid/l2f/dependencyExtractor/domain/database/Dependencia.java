package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dependencia extends RelationalElement{

	public Dependencia(Connection conn) {
		super(conn);
	}

	public boolean insertNew(String tipodepedencia){
		PreparedStatement preparedStatement = null;
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			String sql = "SELECT EXISTS(SELECT 1 FROM Dependencia WHERE tipoDep = '"+ tipodepedencia  + "' LIMIT 1)";
			ResultSet rs = stmt.executeQuery(sql);

			if(rs.next()){
				if(rs.getInt(1)==1){
					rs.close(); 
					return true;
				}
			}
			rs.close();
			
			preparedStatement = connection.prepareStatement("insert into  Dependencia values (?)");
			preparedStatement.setString(1, tipodepedencia);
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Depedencia table!");

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
