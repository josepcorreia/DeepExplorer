package pt.inescid.l2f.dependencyExtractor.domain;

public class Exemplifies {
	private int _sentenceNumber;
	private String _filename;

	private Long _idPalavra1;
	private Long _idPalavra2;
	private String _depName;
	private String _prop;

    public Exemplifies(int sentenceNumber, String filename, Long idPalavra1, Long idPalavra2, String depName, String prop) {
        this._sentenceNumber = sentenceNumber;
        this._filename = filename;
        this._idPalavra1 = idPalavra1;
        this._idPalavra2 = idPalavra2;
        this._depName = depName;
        this._prop = prop;
    }

    public Exemplifies(Sentence sentence, Coocorrence coocorrence) {
        this._sentenceNumber = sentence.getSentenceNumber();
        this._filename = sentence.getFilename();
        this._idPalavra1 = coocorrence.getIdPalavra1();
        this._idPalavra2 = coocorrence.getIdPalavra2();
        this._depName = coocorrence.getDependency();
        this._prop = coocorrence.getProperty();
    }

    public int getSentenceNumber() {
        return _sentenceNumber;
    }

    public String getFilename() {
        return _filename;
    }

    public Long getIdPalavra1() {
        return _idPalavra1;
    }

    public Long getIdPalavra2() {
        return _idPalavra2;
    }

    public String getDependency() {
        return _depName;
    }

    public String getProperty() {
        return _prop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exemplifies)) return false;

        Exemplifies that = (Exemplifies) o;

        if (_sentenceNumber != that._sentenceNumber) return false;
        if (_filename != null ? !_filename.equals(that._filename) : that._filename != null) return false;
        if (_idPalavra1 != null ? !_idPalavra1.equals(that._idPalavra1) : that._idPalavra1 != null) return false;
        if (_idPalavra2 != null ? !_idPalavra2.equals(that._idPalavra2) : that._idPalavra2 != null) return false;
        if (_depName != null ? !_depName.equals(that._depName) : that._depName != null) return false;
        return !(_prop != null ? !_prop.equals(that._prop) : that._prop != null);

    }

    @Override
    public int hashCode() {
        int result = _sentenceNumber;
        result = 31 * result + (_filename != null ? _filename.hashCode() : 0);
        result = 31 * result + (_idPalavra1 != null ? _idPalavra1.hashCode() : 0);
        result = 31 * result + (_idPalavra2 != null ? _idPalavra2.hashCode() : 0);
        result = 31 * result + (_depName != null ? _depName.hashCode() : 0);
        result = 31 * result + (_prop != null ? _prop.hashCode() : 0);
        return result;
    }
}
