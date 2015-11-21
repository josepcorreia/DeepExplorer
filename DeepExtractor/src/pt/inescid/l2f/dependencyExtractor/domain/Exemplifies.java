package pt.inescid.l2f.dependencyExtractor.domain;

public class Exemplifies {
	private Cooccurrence _cooccurrence;
    private Sentence _sentence;

    /**
     * Constructor.
     *
     * @param sentence - Sentence
     * @param cooccurrence - Cooccurrence
     */
    public Exemplifies(Sentence sentence, Cooccurrence cooccurrence) {
        this._sentence= sentence;
        this._cooccurrence = cooccurrence;
    }


    /**
     * Getter
     *
     * @return sentenceNumber - number of sentence in the file
     */
    public int getSentenceNumber() {
        return _sentence.getSentenceNumber();
    }

    /**
     * Getter
     *
     * @return filename where this sentence occurs
     */
    public String getFilename() {
        return _sentence.getFilename();
    }

    /**
     * Getter
     *
     * @return Word1
     */
    public Long getIdPalavra1() {
        return _cooccurrence.getWordId1();
    }

    /**
     * Getter
     *
     * @return Word2
     */
    public Long getIdPalavra2() {
        return _cooccurrence.getWordId2();
    }

    /**
     * Getter
     *
     * @return cooccurrence's dependency name
     */
    public String getDependency() {
        return _cooccurrence.getDependency();
    }

    /**
     * Getter
     *
     * @return cooccurrence's property name
     */
    public String getProperty() {
        return _cooccurrence.getProperty();
    }

    /**
     * Getter
     *
     * @return cooccurrence object
     */
    public Cooccurrence getCoocorrence(){
        return _cooccurrence;
    }

    /**
     * Getter
     *
     * @return sentence object
     */
    public Sentence getSentence(){
        return _sentence;
    }

    /**
     * Equals
     *
     * @param o - Object to compare
     * @return true if this object is equals with the Object o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exemplifies)) return false;

        Exemplifies that = (Exemplifies) o;

        if (_cooccurrence != null ? !_cooccurrence.equals(that._cooccurrence) : that._cooccurrence != null) return false;
        return !(_sentence != null ? !_sentence.equals(that._sentence) : that._sentence != null);

    }

    /**
     * HashCode
     *
     * @return the object's HashCode
     */
    @Override
    public int hashCode() {
        int result = _cooccurrence != null ? _cooccurrence.hashCode() : 0;
        result = 31 * result + (_sentence != null ? _sentence.hashCode() : 0);
        return result;
    }
}
