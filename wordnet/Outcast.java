public class Outcast {

    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        int maxDist = -1;
        String outcast = null;
        for (int i = 0; i < nouns.length; i++) {
            int sumDist = 0;
            for (int j = 0; j < nouns.length; j++) {
                sumDist += wordnet.distance(nouns[i], nouns[j]);
            }
            // StdOut.println("sumDist: " + sumDist);
            if (sumDist > maxDist) {
                maxDist = sumDist;
                outcast = nouns[i];
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
