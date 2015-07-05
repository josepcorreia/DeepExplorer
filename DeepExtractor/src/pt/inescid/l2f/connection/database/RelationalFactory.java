package pt.inescid.l2f.connection.database;

import java.sql.Connection;
import java.sql.SQLException;

import pt.inescid.l2f.connection.ConnectionSQLite;

public class RelationalFactory {
	private Connection _connection;
	private static Palavra _palavra;
	private static Coocorrencia _coo;
	private static Propriedade _prop;
	private static Corpus _corpus;
	private static Dependencia _dependencia;

	public RelationalFactory(String corpusName){
		_connection = ConnectionSQLite.getConnectionSQLite();
		_corpus = new Corpus(_connection);
		_palavra = new Palavra(_connection,corpusName);
		_coo = new Coocorrencia(_connection,corpusName);
		_prop = new Propriedade(_connection); 
		_dependencia = new Dependencia(_connection);
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

	public void closeConnection() {
		ConnectionSQLite.closeConnection(_connection);
	}
}