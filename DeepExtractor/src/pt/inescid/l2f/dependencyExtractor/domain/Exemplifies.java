package pt.inescid.l2f.dependencyExtractor.domain;

public class Exemplifies {
	private Cooccurrence _cooccurrence;
    private Sentence _sentence;



    public Exemplifies(Sentence sentence, Cooccurrence cooccurrence) {
        this._sentence= sentence;
        this._cooccurrence = cooccurrence;
    }

    public int getSentenceNumber() {
        return _sentence.getSentenceNumber();
    }

    public String getFilename() {
        return _sentence.getFilename();
    }

    public Long getIdPalavra1() {
        return _cooccurrence.getWordId1();
    }

    public Long getIdPalavra2() {
        return _cooccurrence.getWordId2();
    }

    public String getDependency() {
        return _cooccurrence.getDependency();
    }

    public String getProperty() {
        return _cooccurrence.getProperty();
    }

    public Cooccurrence getCoocorrence(){
        return _cooccurrence;
    }

    public Sentence getSentence(){
        return _sentence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exemplifies)) return false;

        Exemplifies that = (Exemplifies) o;

        if (_cooccurrence != null ? !_cooccurrence.equals(that._cooccurrence) : that._cooccurrence != null) return false;
        return !(_sentence != null ? !_sentence.equals(that._sentence) : that._sentence != null);

    }

    @Override
    public int hashCode() {
        int result = _cooccurrence != null ? _cooccurrence.hashCode() : 0;
        result = 31 * result + (_sentence != null ? _sentence.hashCode() : 0);
        return result;
    }
}
