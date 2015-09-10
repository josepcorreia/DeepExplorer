package pt.inescid.l2f.dependencyExtractor.domain;

public class WordBelongs {
	private Word _word;
	private String _depName;
	private String _prop;

	public WordBelongs(Word word, String depName, String prop) {
		super();
		this._word = word;
		this._depName = depName;
		this._prop = prop;
	}

	public Long getWordId() {
		return _word.getWordId();
	}

	public String getDepName() {
		return _depName;
	}

	public String getProp() {
		return _prop;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WordBelongs)) return false;

		WordBelongs that = (WordBelongs) o;

		if (_word != null ? !_word.equals(that._word) : that._word != null) return false;
		if (_depName != null ? !_depName.equals(that._depName) : that._depName != null) return false;
		return !(_prop != null ? !_prop.equals(that._prop) : that._prop != null);

	}

	@Override
	public int hashCode() {
		int result = _word != null ? _word.hashCode() : 0;
		result = 31 * result + (_depName != null ? _depName.hashCode() : 0);
		result = 31 * result + (_prop != null ? _prop.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "WordBelongs [idPalavra=" + _word.getWordId() + ", depName="
				+ _depName + ", prop=" + _prop + "]";
	}
}
