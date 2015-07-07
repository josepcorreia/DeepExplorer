package pt.inescid.l2f.dependencyExtractor.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import pt.inescid.l2f.connection.database.Coocorrencia;
import pt.inescid.l2f.connection.database.Palavra;
import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.connection.exception.CoocorrenceNotExist;
import pt.inescid.l2f.connection.exception.WordNotExist;
import pt.inescid.l2f.connection.exception.WordNotExistCorpus;

public class DeepStorage {
	private HashMap<Word,Long> wordsMap;
	private HashMap<WordBelongs, Integer> wordBelongsMap;
	private HashMap<Coocorrence, Integer> coocorrenceMap;
	
	public DeepStorage() {
		super();
		this.wordsMap = new HashMap<Word,Long>();
		this.wordBelongsMap = new HashMap<WordBelongs, Integer>();
		this.coocorrenceMap = new HashMap<Coocorrence, Integer>();
	}
	
	public void CheckWord(Word word, String prop, String depName){
		Palavra palavra = RelationalFactory.getPalavra();
		
		if(wordsMap.containsKey(word)){
			Long id = wordsMap.get(word);
			WordBelongs wb = new WordBelongs(id, depName, prop);
			word.setIdPalavra(id);
			
			if(wordBelongsMap.containsKey(wb)){
				int freq = wordBelongsMap.get(wb) + 1;
				wordBelongsMap.put(wb, freq);
			}else{
				wordBelongsMap.put(wb,1);
			}			
		}else{
			try {
				Long id = palavra.wordExists(word);
				word.setIdPalavra(id);
				wordsMap.put(word, id);
				
				WordBelongs wb = new WordBelongs(id, depName, prop);
				wordBelongsMap.put(wb,1);
				
			} catch (WordNotExist e) {
				Long id = RelationalFactory.getPalavra().insertNewPalavra(word);
				word.setIdPalavra(id);
				wordsMap.put(word, id);
				
				WordBelongs wb = new WordBelongs(id, depName, prop);
				wordBelongsMap.put(wb,1);
			}
		}
			
	}

	public void CheckCoocorrence(Coocorrence coocorrence){
		if(coocorrenceMap.containsKey(coocorrence)){
			int freq = coocorrenceMap.get(coocorrence) + 1;
			coocorrenceMap.put(coocorrence, freq);
		} else{
			coocorrenceMap.put(coocorrence, 1);
		}
	}
	
	public void storeInDatabase(){
		Palavra palavra = RelationalFactory.getPalavra();
		Coocorrencia coo = RelationalFactory.getCoocorrencia();
		
		for (Entry<WordBelongs, Integer>  entry : wordBelongsMap.entrySet() ) {
			WordBelongs wb = entry.getKey();
			int freq = entry.getValue();
			
			try {
				if(palavra.wordExistsCorpus(wb.getIdPalavra(), wb.getDepName(), wb.getProp())){
					palavra.uptadeFrequency(wb.getIdPalavra(), wb.getDepName(), wb.getProp(), freq);
				}
			} catch (WordNotExistCorpus e) {
				palavra.insertPalavraCorpus(wb.getIdPalavra(), wb.getDepName(), wb.getProp(), freq);				
			}	
		}
		
		for (Entry<Coocorrence, Integer>  entry : coocorrenceMap.entrySet() ) {
			Coocorrence coocorrence = entry.getKey();
			int freq = entry.getValue();
			
			Long id1 = coocorrence.getIdPalavra1(); 
			Long id2 = coocorrence.getIdPalavra2();
			String prop = coocorrence.getProperty();
			String dep = coocorrence.getDepedency();
			
			try {
				if(coo.coocorrenceExists(id1, id2, prop, dep)){
					coo.uptadeFrequency(id1, id2, prop, dep, freq);
				}
			} catch (CoocorrenceNotExist e) {
				coo.insertCoocorrence(id1, id2, prop, dep, freq);
			}
			
		}
	}
	
	public void cleanMaps(){
		this.wordsMap = new HashMap<Word,Long>();
		this.wordBelongsMap = new HashMap<WordBelongs, Integer>();
		this.coocorrenceMap = new HashMap<Coocorrence, Integer>();
	}
	
	public void printSizes(){
		System.out.println("WOrd: " + wordsMap.size());
		System.out.println("WOrdBelongs: " + wordBelongsMap.size());
		System.out.println("COO: " + coocorrenceMap.size());
	}

	public void commit() {
		RelationalFactory.commit();
		
	}
}
