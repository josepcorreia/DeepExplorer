package pt.inescid.l2f.dependencyExtractor;

import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
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
	
	public void Extract(XipDocument document){
		HashMap<String, DependencyType> map = _dependencyFactory.getDependenciesMap(); 
		
		int sentenceNumber = 0;
		for (XIPNode sentence : document.getSentences()) {
					
			//número da frase, sentence.getSentenceNumber() não está a funcinar
			sentenceNumber = sentence.getNodes().get(0).getSentenceNumber();
			
			String Frase = "";
				for (XIPNode node : sentence.getNodes()) {
					Frase += node.getSentence();
				
				}
			//	System.out.println("Frase:" + Frase);
			
			Vector<Dependency> deps = document.getSentenceDependecies(sentenceNumber);
			for (Dependency dependency : deps) {
				if("NE".equals(dependency.getName())){
					_dependencyFactory.NE().getDepedencyInformation(dependency);
				}
			}
			
			for (Dependency dependency : deps) {
			
				if(map.containsKey(dependency.getName())){
					map.get(dependency.getName()).getDepedencyInformation(dependency, _dependencyFactory.NE().getNamedEnteties());
				}
			}
			
			_dependencyFactory.NE().ClearNamedEnteties();
			
			/*if(sentenceNumber==4){
				
				return;
			}*/
			
		}

		_storage.storeInDatabase();
		_storage.printCenas();
		_storage.cleanMaps();
	}

	public void CalculateAssociationMeasures(){
		RelationalFactory.getCoocorrencia().UpdateMeasures();
	}
}
