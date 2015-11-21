package pt.inescid.l2f.dependencyExtractor.domain;

public class WordBelongs {
	private Word _word;
	private String _depName;
	private String _prop;

    /**
     * Constructor.
     *
     * @param word - Object Word
     * @param dep  - cooccurrence's dependency name
     * @param prop - cooccurrence's property
     */
	public WordBelongs(Word word, String dep, String prop) {
		super();
		this._word = word;
		this._depName = dep;
		this._prop = prop;
	}

	/**
	 * Getter
	 *
	 * @return word's Id
	 */
	public Long getWordId() {
		return _word.getWordId();
	}

    /**
     * Getter
     *
     * @return cooccurrence's dependency name where the word occurs
     */
	public String getDepName() {
		return _depName;
	}

    /**
     * Getter
     *
     * @return cooccurrence's property name where the word occurs
     */
	public String getProp() {
		return _prop;
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
		if (!(o instanceof WordBelongs)) return false;

		WordBelongs that = (WordBelongs) o;

		if (_word != null ? !_word.equals(that._word) : that._word != null) return false;
		if (_depName != null ? !_depName.equals(that._depName) : that._depName != null) return false;
		return !(_prop != null ? !_prop.equals(that._prop) : that._prop != null);

	}

    /**
     * HashCode
     *
     * @return the object's HashCode
     */
	@Override
	public int hashCode() {
		int result = _word != null ? _word.hashCode() : 0;
		result = 31 * result + (_depName != null ? _depName.hashCode() : 0);
		result = 31 * result + (_prop != null ? _prop.hashCode() : 0);
		return result;
	}

    /**
     * toString
     *
     * @return the object's string
     */
	@Override
	public String toString() {
		return "WordBelongs [idPalavra=" + _word.getWordId() + ", depName="
				+ _depName + ", prop=" + _prop + "]";
	}
}
