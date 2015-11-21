package pt.inescid.l2f.dependencyExtractor.domain;

public class Cooccurrence {
	private String _property;
	private String _dependency;
	private Word _word1;
	private Word _word2;


    /**
     * Constructor.
     *
     * @param word1 - Word1
     * @param word2 - Word2
     * @param dep  - cooccurrence's dependency name
     * @param prop - cooccurrence's property
     */
	public Cooccurrence(Word word1, Word word2, String prop, String dep) {
		this._word1 = word1;
		this._word2 = word2;
		this._property = prop;
		this._dependency = dep;
	}

    /**
     * Getter
     *
     * @return word1's Id
     */
	public long getWordId1() {
		return _word1.getWordId();
	}

    /**
     * Getter
     *
     * @return word2's Id
     */
	public long getWordId2() {
		return _word2.getWordId();
	}

    /**
     * Getter
     *
     * @return Word1 object
     */
    public Word getWord1(){
        return _word1;
    }

    /**
     * Getter
     *
     * @return Word2 object
     */
    public Word getWord2(){
        return _word2;
    }

    /**
     * Getter
     *
     * @return cooccurrence's property name
     */
	public String getProperty() {
		return _property;
	}

    /**
     * Getter
     *
     * @return cooccurrence's dependency name
     */
	public String getDependency() {
		return _dependency;
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
		if (!(o instanceof Cooccurrence)) return false;

		Cooccurrence that = (Cooccurrence) o;

		if (_property != null ? !_property.equals(that._property) : that._property != null) return false;
		if (_dependency != null ? !_dependency.equals(that._dependency) : that._dependency != null) return false;
		if (_word1 != null ? !_word1.equals(that._word1) : that._word1 != null) return false;
		return !(_word2 != null ? !_word2.equals(that._word2) : that._word2 != null);

	}

	/**
	 * HashCode
	 *
	 * @return the object's HashCode
	 */
	@Override
	public int hashCode() {
		int result = _property != null ? _property.hashCode() : 0;
		result = 31 * result + (_dependency != null ? _dependency.hashCode() : 0);
		result = 31 * result + (_word1 != null ? _word1.hashCode() : 0);
		result = 31 * result + (_word2 != null ? _word2.hashCode() : 0);
		return result;
	}

	/**
	 * toString
	 *
	 * @return the object's string
	 */
	@Override
	public String toString() {
		return "Cooccurrence [idPalavra1=" + _word1.getWordId() + ", idPalavra2="
				+ _word2.getWordId() + ", property=" + _property + ", depedency="
				+ _dependency + "]";
	}

}
