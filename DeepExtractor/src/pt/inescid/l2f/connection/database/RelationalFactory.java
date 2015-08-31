package pt.inescid.l2f.connection.database;

import java.sql.Connection;
import java.sql.SQLException;

import pt.inescid.l2f.connection.ConnectionSQLite;

public class RelationalFactory {
	private static Connection _connection;
	private static Palavra _palavra;
	private static Coocorrencia _coo;
	private static Propriedade _prop;
	private static Corpus _corpus;
	private static Dependencia _dependencia;
    private static Ficheiro _ficheiro;
	private static SentenceDB _sentenceDB;

	public RelationalFactory(String corpusName, String dirDB){
		_connection = ConnectionSQLite.getConnectionSQLite(dirDB);
		try {
			_connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_corpus = new Corpus(_connection);
		_palavra = new Palavra(_connection,corpusName);
		_coo = new Coocorrencia(_connection,corpusName);
		_prop = new Propriedade(_connection); 
		_dependencia = new Dependencia(_connection);
		_ficheiro = new Ficheiro(_connection,corpusName);
        _sentenceDB = new SentenceDB(_connection, corpusName);
	}

	public static Palavra getPalavra() {
		return _palavra;
	}

	public static Coocorrencia getCoocorrencia() {
		return _coo;
	}

	public static Propriedade getPropriedade() {
		return _prop;
	}

	public static Corpus getCorpus() {
		return _corpus;
	}

	public static Dependencia getDependencia() {
		return _dependencia;
	}

    public static Ficheiro getFicheiro(){
        return _ficheiro;
    }

    public static SentenceDB getSentenceDB(){
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