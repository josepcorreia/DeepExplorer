package pt.inescid.l2f.connection.database;

import java.sql.*;

public class FileTable extends RelationalTable {
    private String _corpusName;

	public FileTable(Connection _connection, String corpusName) {
		super(_connection);
        _corpusName =  corpusName;
	}

	public boolean fileExists(String fileName){
        Connection connection = getConnetion();
        Statement stmt = null;

        try {
            stmt = connection.createStatement();

            String sql = "SELECT EXISTS(SELECT 1 FROM Ficheiro " +
                                                "WHERE nome = '"+ fileName +
                                                "' and nomeCorpus = '"+ _corpusName +
                                                "' LIMIT 1)";

            ResultSet rs = stmt.executeQuery(sql);

            if(rs.next()){
                if(rs.getInt(1)==1){
                    rs.close();
                    return true;
                }
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {

            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }
        return false;
    }

	public boolean insertNewFile(String fileName){
		Connection connection = getConnetion();
		
		PreparedStatement preparedStatement = null;
		Statement stmt = null;
			
		try {

			
			preparedStatement = connection.prepareStatement("insert into Ficheiro values (?, ?)");

			preparedStatement.setString(1, fileName);
			preparedStatement.setString(2, _corpusName);
		
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
