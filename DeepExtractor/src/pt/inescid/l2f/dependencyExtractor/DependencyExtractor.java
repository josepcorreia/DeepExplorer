package pt.inescid.l2f.dependencyExtractor;

import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
import pt.inescid.l2f.dependencyExtractor.domain.Sentence;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyFactory;
import pt.inescid.l2f.dependencyExtractor.domain.dependency.DependencyType;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.XIPNode;
import pt.inescid.l2f.xipapi.domain.XipDocument;

import java.util.HashMap;
import java.util.Vector;

public class DependencyExtractor {
	private DependencyFactory _dependencyFactory;
	private DeepStorage _storage;

	public DependencyExtractor(){
		_storage = new DeepStorage();
		_dependencyFactory = new DependencyFactory(_storage);
	}
	
	public void Extract(XipDocument document, String filename){
		HashMap<String, DependencyType> map = _dependencyFactory.getDependenciesMap(); 
		
		int sentenceNumber = 0;
		for (XIPNode sentenceNode : document.getSentences()) {
					
			//número da frase, sentence.getSentenceNumber() não está a funcinar
			sentenceNumber = sentenceNode.getNodes().get(0).getSentenceNumber();
			
			String sentence_text = "";
            for (XIPNode node : sentenceNode.getNodes()) {
                sentence_text += node.getSentence();
			}

            Sentence sentence = new Sentence(sentenceNumber, filename, sentence_text);

			Vector<Dependency> deps = document.getSentenceDependecies(sentenceNumber);
			for (Dependency dependency : deps) {
				if("NE".equals(dependency.getName())){
					_dependencyFactory.NE().getDependencyInformation(dependency);
				}
			}
			
			for (Dependency dependency : deps) {
			
				if(map.containsKey(dependency.getName())){
					map.get(dependency.getName()).getDependencyInformation(dependency, _dependencyFactory.NE().getnamedEntetiesNodes(), sentence);
				}
			}
			
			_dependencyFactory.NE().ClearNamedEntetiesNodes();
			/*
			if(sentenceNumber==4){
                _storage.printSizes();
                _storage.storeInDatabase();
                _storage.commit();
                _storage.cleanMaps();
                return;
			}
			*/
		}
		
		//_storage.printSizes();
		_storage.storeInDatabase();
		_storage.commit();
		_storage.cleanMaps();
	}

}
