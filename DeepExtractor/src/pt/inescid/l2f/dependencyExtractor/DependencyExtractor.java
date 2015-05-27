package pt.inescid.l2f.dependencyExtractor;

import pt.inescid.l2f.dependencyExtractor.domain.DependencyFactory;
import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Corpus;
import pt.inescid.l2f.dependencyExtractor.domain.database.Dependencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.dependencyExtractor.domain.measures.AssociationMeasures;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;
import pt.inescid.l2f.xipapi.domain.XipDocument;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;


public class DependencyExtractor {
	private DependencyFactory _dfactory;
	private String _corpusName;
	private Palavra _palavra;
	private Coocorrencia _coo;
	private Propriedade _prop;
	private Corpus _corpus;
	private Dependencia _dependencia;

	public DependencyExtractor(Connection conn, String corpusName){
		_corpusName = corpusName;
		_corpus = new Corpus(conn);
		_palavra = new Palavra(conn, corpusName);
		_coo = new Coocorrencia(conn, corpusName);
		_prop = new Propriedade(conn); 
		_dfactory = new DependencyFactory(corpusName, _palavra, _coo, _prop);
		_dependencia = new Dependencia(conn);
	}


	public void CorpusInformation() {
		//depois completar isto
		_corpus.insertNew(_corpusName, "Público", "2000", "Noticíario", false);
	}
	
	public void Extract(XipDocument document){
		HashMap<String, DependencyType> map = _dfactory.getDependenciesMap(); 
		
		//preenche a informação sobre as dependencias detectadas
		for (String depname : map.keySet()) {
			_dependencia.insertNew(depname);
		}
		
		int sentenceNumber = 0;
		for (XIPNode sentence : document.getSentences()) {
			ArrayList<Integer> nodesInSetence = new ArrayList<Integer>();
			String Frase =  "";
			
			for (Token token : sentence.getTokens() ){
				Frase += token.getName() + " "; 
				String lem = token.getLemmas().element();

				sentenceNumber = token.getSentenceNumber();
				
				//para evitar os casos em que no fim de cada frase aparece "outro", e onde o lema é "NOUN" 
				if(!lem.equals("NOUN")){
					Frase += token.getName() + " ";
					nodesInSetence.add(Integer.parseInt(token.getNodeNumber()));
					_palavra.checkWord(lem, token.getPos(), "categoria");
					//System.out.println(token.getNodeNumber() + " " + lem);
				}
			}
			//System.out.println("Frase n :" + sentenceNumber);
			
			for (Dependency dependency : document.getSentenceDependecies(sentenceNumber)) {
			
				if(map.containsKey(dependency.getName())){
					map.get(dependency.getName()).getDepedencyInformation(dependency, nodesInSetence);
				}
			}
						
			//System.out.println("##########");
			
		}
	}
	
	public void CalculateAssociationMeasures(){
		_coo.UpdateMeasures(_palavra);
	}
}
