package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.ConnectionSQLite;

import java.sql.Connection;
import java.sql.SQLException;

public class RelationalFactory {
	private static Connection _connection;
	private static WordTable _word;
	private static WordBelongsTable _wordBelongs;
	private static CoocorrenceTable _coo;
	private static PropertyTable _prop;
	private static CorpusTable _corpus;
	private static DependencyTable _dependency;
    private static FileTable _file;
    private static SentenceTable _sentenceTable;
    private static ExemplifiesTable _exemplifiesTable;


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
        _wordBelongs = new WordBelongsTable(_connection,corpusName);
		_coo = new CoocorrenceTable(_connection,corpusName);
		_prop = new PropertyTable(_connection);
		_dependency = new DependencyTable(_connection);
		_file = new FileTable(_connection,corpusName);
        _sentenceTable = new SentenceTable(_connection, corpusName);
        _exemplifiesTable = new ExemplifiesTable(_connection, corpusName);
	}

	public static WordTable getWord() {
		return _word;
	}

    public static WordBelongsTable getWordBelongs() {
        return _wordBelongs;
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
		return _dependency;
	}

    public static FileTable getFile(){
        return _file;
    }

    public static SentenceTable getSentence(){
        return _sentenceTable;
    }

    public static ExemplifiesTable getExemplifies(){
        return _exemplifiesTable;
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