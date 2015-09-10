package pt.inescid.l2f.dependencyExtractor.domain;

public class Coocorrence {
	private String _property;
	private String _depedency;
	private Word _word1;
	private Word _word2;


	public Coocorrence(Word word1, Word word2, String _property, String _depedency) {
		this._word1 = word1;
		this._word2 = word2;
		this._property = _property;
		this._depedency = _depedency;
	}
	public long getWordId1() {
		return _word1.getWordId();
	}
	
	public long getWordId2() {
		return _word2.getWordId();
	}

    public Word getWord1(){
        return _word1;
    }

    public Word getWord2(){
        return _word2;
    }

	public String getProperty() {
		return _property;
	}
	
	public String getDependency() {
		return _depedency;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Coocorrence)) return false;

		Coocorrence that = (Coocorrence) o;

		if (_property != null ? !_property.equals(that._property) : that._property != null) return false;
		if (_depedency != null ? !_depedency.equals(that._depedency) : that._depedency != null) return false;
		if (_word1 != null ? !_word1.equals(that._word1) : that._word1 != null) return false;
		return !(_word2 != null ? !_word2.equals(that._word2) : that._word2 != null);

	}

	@Override
	public int hashCode() {
		int result = _property != null ? _property.hashCode() : 0;
		result = 31 * result + (_depedency != null ? _depedency.hashCode() : 0);
		result = 31 * result + (_word1 != null ? _word1.hashCode() : 0);
		result = 31 * result + (_word2 != null ? _word2.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Coocorrence [idPalavra1=" + _word1.getWordId() + ", idPalavra2="
				+ _word2.getWordId() + ", property=" + _property + ", depedency="
				+ _depedency + "]";
	}

}
