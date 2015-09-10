package pt.inescid.l2f.dependencyExtractor.domain;

public class Word {
	private Long _wordId;
	private String _lemma;
	private String _pos;
	
	public Word(String lemma, String pos) {
		this._lemma = lemma;
		this._pos = pos;
	}
	
	public void setWordId(Long id) {
		_wordId = id;
	}
	
	public Long getWordId() {
		return _wordId;
	}

	public String getLemma() {
		return _lemma;
	}

	public String getPOS() {
		return _pos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_lemma == null) ? 0 : _lemma.hashCode());
		result = prime * result + ((_pos == null) ? 0 : _pos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (_lemma == null) {
			if (other._lemma != null)
				return false;
		} else if (!_lemma.equals(other._lemma))
			return false;
		if (_pos == null) {
			if (other._pos != null)
				return false;
		} else if (!_pos.equals(other._pos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Word [idPalavra=" + _wordId + ", lemma=" + _lemma
				+ ", pos=" + _pos + "]";
	}
}


