package pt.inescid.l2f.dependencyExtractor.domain.database;

import java.sql.Connection;

public class RelationalFactory {
	private static Palavra _palavra;
	private static Coocorrencia _coo;
	private static Propriedade _prop;
	private static Corpus _corpus;
	private static Dependencia _dependencia;
		
	public RelationalFactory(Connection conn, String corpusName){
		_corpus = new Corpus(conn);
		_palavra = new Palavra(conn, corpusName);
		_coo = new Coocorrencia(conn, corpusName);
		_prop = new Propriedade(conn); 
		_dependencia = new Dependencia(conn);
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
	 
}
