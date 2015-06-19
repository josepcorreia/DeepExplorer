package pt.inescid.l2f.dependencyExtractor.domain;

public class Coocorrence {
	private long _idPalavra1;
	private long _idPalavra2;
	private String _property;
	private String _depedency;
	private long _frequency;

	public Coocorrence(long _idPalavra1, long _idPalavra2, String _property,
			String _depedency) {
		this._idPalavra1 = _idPalavra1;
		this._idPalavra2 = _idPalavra2;
		this._property = _property;
		this._depedency = _depedency;
	}
	public long getIdPalavra1() {
		return _idPalavra1;
	}
	
	public long getIdPalavra2() {
		return _idPalavra2;
	}

	public String getProperty() {
		return _property;
	}
	
	public String getDepedency() {
		return _depedency;
	}

	public long getFreq() {
		return _frequency;
	}
	
	public void setFreq(long freq) {
		this._frequency = freq;
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
		Coocorrence other = (Coocorrence) obj;
		if (_depedency == null) {
			if (other._depedency != null)
				return false;
		} else if (!_depedency.equals(other._depedency))
			return false;
		if (_idPalavra1 != other._idPalavra1)
			return false;
		if (_idPalavra2 != other._idPalavra2)
			return false;
		if (_property == null) {
			if (other._property != null)
				return false;
		} else if (!_property.equals(other._property))
			return false;
		return true;
	}

	


}
