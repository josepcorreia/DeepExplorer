package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.exception.DatabaseException;

import java.sql.*;

public class FileTable extends RelationalTable {
    private String _corpusName;

	public FileTable(Connection _connection, String corpusName) {
		super(_connection);
        _corpusName =  corpusName;
	}

    /**
     * Verify if a file exists in the database (table Ficheiro)
     *
     * @param fileName - file namename
     *
     * @return true if the file already exists
     * @throws DatabaseException in the case of a problem in the database/database's connection
     */
	public boolean fileExists(String fileName) throws DatabaseException {
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
            throw new DatabaseException("File", "fileExists");
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

    /**
     * Add new File in the database (table Ficheiro)
     *
     * @param fileName - file name
     *
     * @throws DatabaseException in the case of a problem in the database/database's connection
     */
	public void insertNewFile(String fileName) throws DatabaseException {
		Connection connection = getConnetion();

		Statement stmt = null;
			
		try {
			stmt = connection.createStatement();
			
			String sql = "INSERT INTO Ficheiro VALUES ('"+fileName+"' , '"+_corpusName+"')";
		
			stmt.executeUpdate(sql);

			System.out.println("Record is inserted into File table!");

		} catch (SQLException e) {
			e.printStackTrace();
            throw new DatabaseException("File", "insertNewFile");
		} finally {

			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

		}
	}

}
