package pt.inescid.l2f.dependencyExtractor.domain;

public class Word {
	private String _lemma;
	private String _pos;
	private long _count;
	
	public Word(String lemma, String pos) {
		this._lemma = lemma;
		this._pos = pos;
		this._count = 1;
	}

	public String getLemma() {
		return _lemma;
	}

	public String getPOS() {
		return _pos;
	}

	public long getCount() {
		return _count;
	}

	public void incCount() {
		++this._count;
	}

	//auto-generated
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
}


