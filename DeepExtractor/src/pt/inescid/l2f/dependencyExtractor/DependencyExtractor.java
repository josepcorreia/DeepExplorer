package pt.inescid.l2f.dependencyExtractor;

import pt.inescid.l2f.dependencyExtractor.domain.DependencyFactory;
import pt.inescid.l2f.dependencyExtractor.domain.database.Coocorrencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Corpus;
import pt.inescid.l2f.dependencyExtractor.domain.database.Dependencia;
import pt.inescid.l2f.dependencyExtractor.domain.database.Palavra;
import pt.inescid.l2f.dependencyExtractor.domain.database.Propriedade;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.dependencyExtractor.domain.measures.AssociationMeasures;
import pt.inescid.l2f.dependencyExtractor.domain.word.WordCount;
import pt.inescid.l2f.xipapi.domain.Dependency;

import pt.inescid.l2f.xipapi.domain.XIPNode;
import pt.inescid.l2f.xipapi.domain.XipDocument;

import java.sql.Connection;
import java.util.HashMap;



public class DependencyExtractor {
	private DependencyFactory _dfactory;
	private String _corpusName;
	private Palavra _palavra;
	private Coocorrencia _coo;
	private Propriedade _prop;
	private Corpus _corpus;
	private Dependencia _dependencia;
	private WordCount _wordCount;

	public DependencyExtractor(Connection conn, String corpusName){
		_corpusName = corpusName;
		_corpus = new Corpus(conn);
		_palavra = new Palavra(conn, corpusName);
		_coo = new Coocorrencia(conn, corpusName);
		_prop = new Propriedade(conn); 
		_dfactory = new DependencyFactory(corpusName, _palavra, _coo, _prop);
		_wordCount = new WordCount(_palavra);
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
			//String Frase =  "";		
			
			//número da frase, sentence.getSentenceNumber() não está a funcinar
			sentenceNumber = sentence.getNodes().get(0).getSentenceNumber();
			
			/*for (XIPNode node : sentence.getNodes()) {
				sentenceNumber = node.getSentenceNumber();
				_wordCount.checkNode(node);
			}	
				
			//System.out.println("Frase n :" + sentenceNumber);
			 */
			for (Dependency dependency : document.getSentenceDependecies(sentenceNumber)) {
			
				if(map.containsKey(dependency.getName())){
					map.get(dependency.getName()).getDepedencyInformation(dependency);
				}
			}
			
			
		}
	}

	public void CalculateAssociationMeasures(){
		_coo.UpdateMeasures(_palavra);
	}
}
