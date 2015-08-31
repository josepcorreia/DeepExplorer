package pt.inescid.l2f.dependencyExtractor.domain;

public class Sentence {
	private int _sentenceNumber;
	private String _filename;
	private String _sentence;


	public Sentence(int sentenceNumber, String filename, String sentence) {
		_sentenceNumber = sentenceNumber;
        _filename = filename;
        _sentence = sentence;
    }

    public int getSentenceNumber() {
        return _sentenceNumber;
    }

    public String getFilename() {
        return _filename;
    }

    public String getSentence() {
        return _sentence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sentence)) return false;

        Sentence sentence = (Sentence) o;

        if (_sentenceNumber != sentence._sentenceNumber) return false;
        if (_filename != null ? !_filename.equals(sentence._filename) : sentence._filename != null) return false;
        return !(_sentence != null ? !_sentence.equals(sentence._sentence) : sentence._sentence != null);

    }

    @Override
    public int hashCode() {
        int result = _sentenceNumber;
        result = 31 * result + (_filename != null ? _filename.hashCode() : 0);
        result = 31 * result + (_sentence != null ? _sentence.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "_sentenceNumber=" + _sentenceNumber +
                ", _filename='" + _filename + '\'' +
                ", _sentence='" + _sentence + '\'' +
                '}';
    }
}


