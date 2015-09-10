package pt.inescid.l2f.dependencyExtractor.domain;

public class Exemplifies {
	private Coocorrence _coocorrence;
    private Sentence _sentence;



    public Exemplifies(Sentence sentence, Coocorrence coocorrence) {
        this._sentence= sentence;
        this._coocorrence = coocorrence;
    }

    public int getSentenceNumber() {
        return _sentence.getSentenceNumber();
    }

    public String getFilename() {
        return _sentence.getFilename();
    }

    public Long getIdPalavra1() {
        return _coocorrence.getWordId1();
    }

    public Long getIdPalavra2() {
        return _coocorrence.getWordId2();
    }

    public String getDependency() {
        return _coocorrence.getDependency();
    }

    public String getProperty() {
        return _coocorrence.getProperty();
    }

    public Coocorrence getCoocorrence(){
        return _coocorrence;
    }

    public Sentence getSentence(){
        return _sentence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exemplifies)) return false;

        Exemplifies that = (Exemplifies) o;

        if (_coocorrence != null ? !_coocorrence.equals(that._coocorrence) : that._coocorrence != null) return false;
        return !(_sentence != null ? !_sentence.equals(that._sentence) : that._sentence != null);

    }

    @Override
    public int hashCode() {
        int result = _coocorrence != null ? _coocorrence.hashCode() : 0;
        result = 31 * result + (_sentence != null ? _sentence.hashCode() : 0);
        return result;
    }
}
