package pt.inescid.l2f.dependencyExtractor.domain;

import pt.inescid.l2f.connection.database.*;
import pt.inescid.l2f.connection.exception.CooccurrenceNotExist;
import pt.inescid.l2f.connection.exception.DatabaseException;
import pt.inescid.l2f.connection.exception.WordNotExist;
import pt.inescid.l2f.connection.exception.WordNotExistCorpus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class DeepStorage {
	private HashMap<String, Word> wordsMap;
	private HashMap<WordBelongs, Integer> wordBelongsMap;
	private HashMap<Cooccurrence, Integer> coocorrenceMap;
	private HashMap<Sentence, String> sentenceMap;
	private HashSet<Exemplifies> exemplifiesSet;

	/**
	 * Constructor.
	 *
	 */
	public DeepStorage() {
		this.wordsMap = new HashMap<String, Word>();
		this.wordBelongsMap = new HashMap<WordBelongs, Integer>();
		this.coocorrenceMap = new HashMap<Cooccurrence, Integer>();
        this.sentenceMap = new HashMap<Sentence,String>();
        this.exemplifiesSet = new HashSet<Exemplifies>();
	}

    /**
     * Checks if the word exists in the word's Map.
     *
     * @param lemma - word's lemma
     * @param pos - word's POS
     * @param dep - cooccurrence's dependency name where the word occurs
     * @param prop - cooccurrence's property where the word occurs
     *
     * @return the WOrd Object
     */
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

    /**
     * Checks if the word exists in the wordBelongs' Map.
     *
     * @param word - Word object
     * @param depName - cooccurrence's dependency name where the word occurs
     * @param prop - cooccurrence's property where the word occurs
     *
     */
    public void checkWordBelongs(Word word, String depName,  String prop){
        WordBelongs wb = new WordBelongs(word, depName, prop);

        if(wordBelongsMap.containsKey(wb)){
            int freq = wordBelongsMap.get(wb) + 1;
            wordBelongsMap.put(wb, freq);
        }else{
            wordBelongsMap.put(wb,1);
        }
    }

    /**
     * Checks if the cooccurrence already exists in the cooccurrence's Map.
     *
     * @param cooccurrence - cooccurrence object
     * @param sentence - sentence object
     *
     */
	public void checkCooccurrence(Cooccurrence cooccurrence, Sentence sentence){
        if(coocorrenceMap.containsKey(cooccurrence)){
			int freq = coocorrenceMap.get(cooccurrence) + 1;
			coocorrenceMap.put(cooccurrence, freq);
		} else{
			coocorrenceMap.put(cooccurrence, 1);
		}
        checkSetence(sentence, cooccurrence);
	}

    /**
     * Checks if the sentence already exists in the sentence's Map and in the exemplifies' Map.
     *
     * @param sentence - sentence object
     * @param cooccurrence - cooccurrence object
     *
     */
    public void checkSetence(Sentence sentence, Cooccurrence cooccurrence){
        if(!sentenceMap.containsKey(sentence)){
            sentenceMap.put(sentence, sentence.getSentenceText());
        }
        Exemplifies ex = new Exemplifies(sentence, cooccurrence);

        if(!exemplifiesSet.contains(ex)){
            exemplifiesSet.add(ex);
        }
    }

    /**
     * Store the information present in the maps (wordsMap, wordBelongsMap,
     * coocorrenceMap, sentenceMap, exemplifiesSet) in the database
     *
	 * @throws DatabaseException in the case of a problem in the database/database's connection
     */
	public void storeInDatabase() throws DatabaseException {

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
		
		for (Entry<Cooccurrence, Integer>  entry : coocorrenceMap.entrySet() ) {
			Cooccurrence cooccurrence = entry.getKey();
			int freq = entry.getValue();
			
			Long id1 = cooccurrence.getWordId1();
			Long id2 = cooccurrence.getWordId2();
			String prop = cooccurrence.getProperty();
			String dep = cooccurrence.getDependency();
			
			try {
				if(coo.cooccurrenceExists(id1, id2, prop, dep)){
					coo.uptadeFrequency(id1, id2, prop, dep, freq);
				}
			} catch (CooccurrenceNotExist e) {
				coo.insertCooccurrence(id1, id2, prop, dep, freq);
			}
			
		}

       for( Exemplifies ex : exemplifiesSet){

           Sentence sentence = ex.getSentence();
           Cooccurrence cooccurrence = ex.getCoocorrence();

           if(coo.getCoocorrenceFrequency(cooccurrence) < 16) {
               sentence.setSentenceText(sentenceMap.get(sentence));
               if(!sentenceDB.sentenceExists(sentence)){
                   sentenceDB.insertNewSentence(sentence);
               }
               exemplifiesDB.insertNewSetenceExample(ex);
           }
       }

	}

    /**
     * Clean the information present in the maps (wordsMap, wordBelongsMap,
     * coocorrenceMap, sentenceMap, exemplifiesSet)
     *
     */
	public void cleanMaps(){
		this.wordsMap = new HashMap<String,Word>();
		this.wordBelongsMap = new HashMap<WordBelongs, Integer>();
		this.coocorrenceMap = new HashMap<Cooccurrence, Integer>();
        this.sentenceMap = new HashMap<Sentence,String>();
        this.exemplifiesSet = new HashSet<Exemplifies>();
	}

    /**
     * Print the size of the maps (wordsMap, wordBelongsMap,
     * coocorrenceMap, sentenceMap, exemplifiesSet)
     *
     */
	public void printSizes(){
		System.out.println("WOrd: " + wordsMap.size());
		System.out.println("WOrdBelongs: " + wordBelongsMap.size());
		System.out.println("COO: " + coocorrenceMap.size());
        System.out.println("frase: " + sentenceMap.size());
        System.out.println("exemplifica: " + exemplifiesSet.size());
	}

    /**
     * commit the information to the database
     *
     */
	public void commit() {
		RelationalFactory.commit();
		
	}
}
