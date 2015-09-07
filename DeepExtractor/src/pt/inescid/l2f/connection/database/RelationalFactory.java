package pt.inescid.l2f.connection.database;

import java.sql.Connection;
import java.sql.SQLException;

import pt.inescid.l2f.connection.ConnectionSQLite;

public class RelationalFactory {
	private static Connection _connection;
	private static WordTable _word;
	private static CoocorrenceTable _coo;
	private static PropertyTable _prop;
	private static CorpusTable _corpus;
	private static DependencyTable _dependencia;
    private static FileTable _ficheiro;
	private static SentenceTable _sentenceDB;

	public RelationalFactory(String corpusName, String dirDB){
		_connection = ConnectionSQLite.getConnectionSQLite(dirDB);
		try {
			_connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_corpus = new CorpusTable(_connection);
		_word = new WordTable(_connection,corpusName);
		_coo = new CoocorrenceTable(_connection,corpusName);
		_prop = new PropertyTable(_connection);
		_dependencia = new DependencyTable(_connection);
		_ficheiro = new FileTable(_connection,corpusName);
        _sentenceDB = new SentenceTable(_connection, corpusName);
	}

	public static WordTable getWord() {
		return _word;
	}

	public static CoocorrenceTable getCoocorrence() {
		return _coo;
	}

	public static PropertyTable getProperty() {
		return _prop;
	}

	public static CorpusTable getCorpus() {
		return _corpus;
	}

	public static DependencyTable getDependency() {
		return _dependencia;
	}

    public static FileTable getFile(){
        return _ficheiro;
    }

    public static SentenceTable getSentence(){
        return _sentenceDB;
    }

	public void closeConnection() {
		ConnectionSQLite.closeConnection(_connection);
	}

	public static void commit() {
		try {
			_connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}