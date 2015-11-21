package pt.inescid.l2f.connection.database;

import java.sql.*;

public class CorpusTable extends RelationalTable {

	
	public CorpusTable(Connection _connection) {
		super(_connection);
	}


	/**
	 * Add new Corpus in the database (table Corpus)
	 *
	 * @param name - corpus' name
	 * @param source - corpus' source
	 * @param year - corpus' year
	 * @param type - corpus' type
	 * @param update - corpus' state
	 *
     * @return true if the corpus was correctly inserted
	 */
	public boolean insertNew(String name, String  source, String year, String type, Boolean update){
		Connection connection = getConnetion();
		
		PreparedStatement preparedStatement = null;
		Statement stmt = null;
			
		try {
			stmt = connection.createStatement();
			String sql = "SELECT EXISTS(SELECT 1 FROM Corpus WHERE nome = '"+ name + "' LIMIT 1)";
			ResultSet rs = stmt.executeQuery(sql);

			if(rs.next()){
				if(rs.getInt(1)==1){
					rs.close(); 
					return true;
				}
			}
			rs.close();
			
			preparedStatement = connection.prepareStatement("insert into  Corpus values (?, ?, ?, ? , ?)");

			preparedStatement.setString(1, name);
			preparedStatement.setString(2, source);
			preparedStatement.setString(3, year);
			preparedStatement.setString(4, type);
			preparedStatement.setBoolean(5, update);

		
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into Corpus table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return false;

		} finally {

			if (preparedStatement != null) {
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
		}
	  return true;
	} 
}
