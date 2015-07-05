package pt.inescid.l2f.dependencyExtractor.domain;

public class WordBelongs {
	private Long _idPalavra;
	private String _depName;
	private String _prop;

	public WordBelongs(Long idPalavra, String depName, String prop) {
		super();
		this._idPalavra = idPalavra;
		this._depName = depName;
		this._prop = prop;
	}

	public Long getIdPalavra() {
		return _idPalavra;
	}

	public void setIdPalavra(Long idPalavra) {
		this._idPalavra = idPalavra;
	}

	public String getDepName() {
		return _depName;
	}

	public String getProp() {
		return _prop;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_depName == null) ? 0 : _depName.hashCode());
		result = prime * result
				+ ((_idPalavra == null) ? 0 : _idPalavra.hashCode());
		result = prime * result + ((_prop == null) ? 0 : _prop.hashCode());
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
		WordBelongs other = (WordBelongs) obj;
		if (_depName == null) {
			if (other._depName != null)
				return false;
		} else if (!_depName.equals(other._depName))
			return false;
		if (_idPalavra == null) {
			if (other._idPalavra != null)
				return false;
		} else if (!_idPalavra.equals(other._idPalavra))
			return false;
		if (_prop == null) {
			if (other._prop != null)
				return false;
		} else if (!_prop.equals(other._prop))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WordBelongs [idPalavra=" + _idPalavra + ", depName="
				+ _depName + ", prop=" + _prop + "]";
	}
}
