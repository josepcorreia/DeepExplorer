package pt.inescid.l2f.connection.database;

import pt.inescid.l2f.connection.ConnectionSQLite;

import java.sql.Connection;
import java.sql.SQLException;

public class RelationalFactory {
	private static Connection _connection;
	private static WordTable _word;
	private static WordBelongsTable _wordBelongs;
	private static CooccurrenceTable _coo;
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
		_coo = new CooccurrenceTable(_connection,corpusName);
		_prop = new PropertyTable(_connection);
		_dependency = new DependencyTable(_connection);
		_file = new FileTable(_connection,corpusName);
        _sentenceTable = new SentenceTable(_connection, corpusName);
        _exemplifiesTable = new ExemplifiesTable(_connection, corpusName);
	}

	/**
	 * Getter.
	 * @return  the word object which correspond to the table Word (Palavra)
	 */
	public static WordTable getWord() {
		return _word;
	}

    /**
     * Getter.
     * @return  the wordBelongs object which correspond to the table Belongs (Pertence)
     */
    public static WordBelongsTable getWordBelongs() {
        return _wordBelongs;
    }

    /**
     * Getter.
     * @return  the cooccurence object which correspond to the table Coocorrence (Coocorrencia)
     */
	public static CooccurrenceTable getCoocorrence() {
		return _coo;
	}

    /**
     * Getter.
     * @return  the property object which correspond to the table Property (Propriedade)
     */
	public static PropertyTable getProperty() {
		return _prop;
	}

    /**
     * Getter.
     * @return  the corpus object which correspond to the table Corpus
     */
	public static CorpusTable getCorpus() {
		return _corpus;
	}

    /**
     * Getter.
     * @return  the dependency object which correspond to the table Dependency (Dependencia)
     */
	public static DependencyTable getDependency() {
		return _dependency;
	}

    /**
     * Getter.
     * @return  the file object which correspond to the table File (Ficheiro)
     */
    public static FileTable getFile(){
        return _file;
    }

    /**
     * Getter.
     * @return  the sentence object which correspond to the table Setence (Frase)
     */
    public static SentenceTable getSentence(){
        return _sentenceTable;
    }

    /**
     * Getter.
     * @return  the exemplifies object which correspond to the table Exemplifies (Exemplifica)
     */
    public static ExemplifiesTable getExemplifies(){
        return _exemplifiesTable;
    }

    /**
     * Close the database connection
     */
	public static void closeConnection() {
		ConnectionSQLite.closeConnection(_connection);
	}

    /**
     * Commits the information in the database
     */
	public static void commit() {
		try {
			_connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}