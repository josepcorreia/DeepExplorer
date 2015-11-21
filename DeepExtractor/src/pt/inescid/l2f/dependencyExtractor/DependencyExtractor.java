package pt.inescid.l2f.dependencyExtractor;

import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.dependencyExtractor.domain.DeepStorage;
import pt.inescid.l2f.dependencyExtractor.domain.Sentence;
import pt.inescid.l2f.dependencyExtractor.dependency.DependencyFactory;
import pt.inescid.l2f.dependencyExtractor.dependency.DependencyType;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.XIPNode;
import pt.inescid.l2f.xipapi.domain.XipDocument;

import java.util.HashMap;
import java.util.Vector;

public class DependencyExtractor {
	private DependencyFactory _dependencyFactory;

    /** where the information is temporarily stored*/
    private DeepStorage _storage;

	public DependencyExtractor(){
		_storage = new DeepStorage();
		_dependencyFactory = new DependencyFactory(_storage);
	}

	/**
	 * Extract te information in a certain XIP document
	 *
	 * @param  document  XipDocument (XIPAPI), XML File from XIP
	 * @param  filename - filename
	 */
	public void Extract(XipDocument document, String filename){
		HashMap<String, DependencyType> map = _dependencyFactory.getDependenciesMap(); 
		
		int sentenceNumber = 0;
		for (XIPNode sentenceNode : document.getSentences()) {
					
			//número da frase, sentence.getSentenceNumber() não está a funcinar
			sentenceNumber = sentenceNode.getNodes().get(0).getSentenceNumber();

			String sentence_text = sentenceNode.getSentence();

            Sentence sentence = new Sentence(sentenceNumber, filename, sentence_text);

			Vector<Dependency> deps = document.getSentenceDependecies(sentenceNumber);
			for (Dependency dependency : deps) {
				if("getNE".equals(dependency.getName())){
					_dependencyFactory.getNE().getDependencyInformation(dependency);
				}
			}
			
			for (Dependency dependency : deps) {
			
				if(map.containsKey(dependency.getName())){
					map.get(dependency.getName()).getDependencyInformation(dependency, _dependencyFactory.getNE().getnamedEntetiesNodes(), sentence);
				}
			}
			
			_dependencyFactory.getNE().ClearNamedEntetiesNodes();

			/*if(sentenceNumber==1){
                _storage.printSizes();
                _storage.storeInDatabase();
                _storage.commit();
                _storage.cleanMaps();
                return;
			}*/

		}

        //store in the database
		//_storage.printSizes();
		_storage.storeInDatabase();
		_storage.cleanMaps();

        //insert filename in the database (It means that the file has already been analyzed)
		RelationalFactory.getFile().insertNewFile(filename);
        //commit the information to the database
        _storage.commit();
	}

}
