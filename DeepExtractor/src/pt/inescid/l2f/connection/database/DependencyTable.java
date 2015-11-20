package pt.inescid.l2f.connection.database;

import java.sql.*;

public class DependencyTable extends RelationalTable {


	public DependencyTable(Connection _connection) {
		super(_connection);
	}

    /**
     * Add new Dependency in the database (table Dependencia)
     *
     * @param dependencyName - dependency name
     *
     * @result true if the Dependency was correctly inserted
     */
	public boolean insertNew(String dependencyName){
		Connection connection = getConnetion();
		
		PreparedStatement preparedStatement = null;
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			String sql = "SELECT EXISTS(SELECT 1 FROM Dependencia WHERE tipoDep = '"+ dependencyName  + "' LIMIT 1)";
			ResultSet rs = stmt.executeQuery(sql);

			if(rs.next()){
				if(rs.getInt(1)==1){
					rs.close(); 
					return true;
				}
			}
			rs.close();
			
			preparedStatement = connection.prepareStatement("insert into  Dependencia values (?)");
			preparedStatement.setString(1, dependencyName);
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Depedencia table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return false;

		} finally {
			try {

				if (preparedStatement != null) 
					preparedStatement.close();
				
				if (stmt != null) 
					stmt.close();

				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			
		}
	  return true;
}

}
