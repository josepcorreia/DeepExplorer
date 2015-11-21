package pt.inescid.l2f.dependencyExtractor.domain;

public class Sentence {
	private int _sentenceNumber;
	private String _filename;
	private String _sentence_text;


    /**
     * Constructor.
     *
     * @param sentenceNumber - number of sentence in the file
     * @param filename  - filename where this sentence occurs
     * @param sentence - sentence's text
     */
	public Sentence(int sentenceNumber, String filename, String sentence) {
		_sentenceNumber = sentenceNumber;
        _filename = filename;
        _sentence_text = sentence;
    }

    /**
     * Constructor.
     *
     * @param sentenceNumber - number of sentence in the file
     * @param filename  - filename where this sentence occurs
     */
    public Sentence(int sentenceNumber, String filename) {
        _sentenceNumber = sentenceNumber;
        _filename = filename;
    }

    /**
     * Setter
     *
     * @param text - sentence's text
     */
    public void setSentenceText(String text){
        _sentence_text = text;
    }

    public int getSentenceNumber() {
        return _sentenceNumber;
    }

    /**
     * Getter
     *
     * @return filename where this sentence occurs
     */
    public String getFilename() {
        return _filename;
    }


    /**
     * Getter
     *
     * @return sentence's text
     */
    public String getSentenceText() {
        return _sentence_text;
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
        if (!(o instanceof Sentence)) return false;

        Sentence sentence = (Sentence) o;

        if (_sentenceNumber != sentence._sentenceNumber) return false;
        return !(_filename != null ? !_filename.equals(sentence._filename) : sentence._filename != null);

    }

    /**
     * HashCode
     *
     * @return the object's HashCode
     */
    @Override
    public int hashCode() {
        int result = _sentenceNumber;
        result = 31 * result + (_filename != null ? _filename.hashCode() : 0);
        return result;
    }

    /**
     * toString
     *
     * @return the object's string
     */
    @Override
    public String toString() {
        return "Sentence{" +
                "_sentenceNumber=" + _sentenceNumber +
                ", _filename='" + _filename + '\'' +
                ", _sentence='" + _sentence_text + '\'' +
                '}';
    }
}


