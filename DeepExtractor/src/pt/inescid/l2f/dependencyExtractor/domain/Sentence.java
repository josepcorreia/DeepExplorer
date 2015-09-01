package pt.inescid.l2f.dependencyExtractor.domain;

public class Sentence {
	private int _sentenceNumber;
	private String _filename;
	private String _sentence_text;


	public Sentence(int sentenceNumber, String filename, String sentence) {
		_sentenceNumber = sentenceNumber;
        _filename = filename;
        _sentence_text = sentence;
    }

    public Sentence(int sentenceNumber, String filename) {
        _sentenceNumber = sentenceNumber;
        _filename = filename;
    }

    public void setSentenceText(String text){
        _sentence_text = text;
    }

    public int getSentenceNumber() {
        return _sentenceNumber;
    }

    public String getFilename() {
        return _filename;
    }

    public String getSentenceText() {
        return _sentence_text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sentence)) return false;

        Sentence sentence = (Sentence) o;

        if (_sentenceNumber != sentence._sentenceNumber) return false;
        return !(_filename != null ? !_filename.equals(sentence._filename) : sentence._filename != null);

    }

    @Override
    public int hashCode() {
        int result = _sentenceNumber;
        result = 31 * result + (_filename != null ? _filename.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "_sentenceNumber=" + _sentenceNumber +
                ", _filename='" + _filename + '\'' +
                ", _sentence='" + _sentence_text + '\'' +
                '}';
    }
}


