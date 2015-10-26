package pt.inescid.l2f.dependencyExtractor.domain;

import pt.inescid.l2f.connection.database.*;
import pt.inescid.l2f.connection.exception.CoocorrenceNotExist;
import pt.inescid.l2f.connection.exception.WordNotExist;
import pt.inescid.l2f.connection.exception.WordNotExistCorpus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class DeepStorage {
	private HashMap<String, Word> wordsMap;
	private HashMap<WordBelongs, Integer> wordBelongsMap;
	private HashMap<Coocorrence, Integer> coocorrenceMap;
	private HashMap<Sentence, String> sentenceMap;
	private HashSet<Exemplifies> exemplifiesSet;
	
	public DeepStorage() {
		this.wordsMap = new HashMap<String, Word>();
		this.wordBelongsMap = new HashMap<WordBelongs, Integer>();
		this.coocorrenceMap = new HashMap<Coocorrence, Integer>();
        this.sentenceMap = new HashMap<Sentence,String>();
        this.exemplifiesSet = new HashSet<Exemplifies>();
	}
	
	public Word checkWord(String lemma, String pos, String dep, String prop){
        String key = lemma +"_"+ pos;
        Word word;

        if(wordsMap.containsKey(key)){
            word = wordsMap.get(key);
		}else{
            word = new Word(lemma,pos);
            wordsMap.put(key, word);
		}

        checkWordBelongs(word, dep, prop);
        return word;
	}
    public void checkWordBelongs(Word word, String depName,  String prop){
        WordBelongs wb = new WordBelongs(word, depName, prop);

        if(wordBelongsMap.containsKey(wb)){
            int freq = wordBelongsMap.get(wb) + 1;
            wordBelongsMap.put(wb, freq);
        }else{
            wordBelongsMap.put(wb,1);
        }
    }
	public void checkCoocorrence(Coocorrence coocorrence, Sentence sentence){
        if(coocorrenceMap.containsKey(coocorrence)){
			int freq = coocorrenceMap.get(coocorrence) + 1;
			coocorrenceMap.put(coocorrence, freq);
		} else{
			coocorrenceMap.put(coocorrence, 1);
		}
        checkSetence(sentence, coocorrence);
	}

    public void checkSetence(Sentence sentence, Coocorrence coocorrence){
        if(!sentenceMap.containsKey(sentence)){
            sentenceMap.put(sentence, sentence.getSentenceText());
        }
        Exemplifies ex = new Exemplifies(sentence, coocorrence);

        if(!exemplifiesSet.contains(ex)){
            exemplifiesSet.add(ex);
        }
    }

	
	public void storeInDatabase(){

		WordTable worddb = RelationalFactory.getWord();
		WordBelongsTable wordBelongsdb = RelationalFactory.getWordBelongs();
		CooccurrenceTable coo = RelationalFactory.getCoocorrence();
        SentenceTable sentenceDB = RelationalFactory.getSentence();
		ExemplifiesTable exemplifiesDB = RelationalFactory.getExemplifies();

        for (Entry<String, Word>  entry : wordsMap.entrySet() ) {

            Word word = entry.getValue();

            try {
                Long id = worddb.wordExists(word);
                word.setWordId(id);

            } catch (WordNotExist e) {
                Long id = RelationalFactory.getWord().insertNewWord(word);
                word.setWordId(id);
            }
        }

		for (Entry<WordBelongs, Integer>  entry : wordBelongsMap.entrySet() ) {
			WordBelongs wb = entry.getKey();
			int freq = entry.getValue();
			
			try {
				if(wordBelongsdb.wordExistsCorpus(wb.getWordId(), wb.getDepName(), wb.getProp())){
					wordBelongsdb.uptadeFrequency(wb.getWordId(), wb.getDepName(), wb.getProp(), freq);
				}
			} catch (WordNotExistCorpus e) {
				wordBelongsdb.insertWordCorpus(wb.getWordId(), wb.getDepName(), wb.getProp(), freq);
			}	
		}
		
		for (Entry<Coocorrence, Integer>  entry : coocorrenceMap.entrySet() ) {
			Coocorrence coocorrence = entry.getKey();
			int freq = entry.getValue();
			
			Long id1 = coocorrence.getWordId1();
			Long id2 = coocorrence.getWordId2();
			String prop = coocorrence.getProperty();
			String dep = coocorrence.getDependency();
			
			try {
				if(coo.coocorrenceExists(id1, id2, prop, dep)){
					coo.uptadeFrequency(id1, id2, prop, dep, freq);
				}
			} catch (CoocorrenceNotExist e) {
				coo.insertCoocorrence(id1, id2, prop, dep, freq);
			}
			
		}

       for( Exemplifies ex : exemplifiesSet){

           Sentence sentence = ex.getSentence();
           Coocorrence coocorrence = ex.getCoocorrence();

           if(coo.getCoocorrenceFrequency(coocorrence) < 16) {
               sentence.setSentenceText(sentenceMap.get(sentence));
               if(!sentenceDB.sentenceExists(sentence)){
                   sentenceDB.insertNewSentence(sentence);
               }
               exemplifiesDB.insertNewSetenceExample(ex);
           }
       }

	}
	
	public void cleanMaps(){
		this.wordsMap = new HashMap<String,Word>();
		this.wordBelongsMap = new HashMap<WordBelongs, Integer>();
		this.coocorrenceMap = new HashMap<Coocorrence, Integer>();
        this.sentenceMap = new HashMap<Sentence,String>();
        this.exemplifiesSet = new HashSet<Exemplifies>();
	}
	
	public void printSizes(){
		System.out.println("WOrd: " + wordsMap.size());
		System.out.println("WOrdBelongs: " + wordBelongsMap.size());
		System.out.println("COO: " + coocorrenceMap.size());
        System.out.println("frase: " + sentenceMap.size());
        System.out.println("exemplifica: " + exemplifiesSet.size());
	}

	public void commit() {
		RelationalFactory.commit();
		
	}
}
