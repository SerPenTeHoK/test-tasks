public class WordData {
    String word;
    long freq;

    public String getWord() {
        return word;
    }

    public long getFreq() {
        return freq;
    }

    public WordData(String word, long freq) {
        this.word = word;
        this.freq = freq;
    }

    @Override
    public String toString() {
        return  "word='" + word + '\'' +
                ", freq=" + freq;
    }
}