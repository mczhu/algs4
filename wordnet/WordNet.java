public class WordNet {

    private final Digraph wordDAG;
    private final SeparateChainingHashST<String, Bag<Integer>> wordHash;
    private final SeparateChainingHashST<Integer, String> idHash;
    private final SAP wordSAP;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new NullPointerException();
        // Construct a hash table with words as keys and synset id as values
        // Construction cost is linear to the size. a second hash table with id as keys
        // and synsets as values (useful in sap)
        wordHash = new SeparateChainingHashST<String, Bag<Integer>>();
        idHash = new SeparateChainingHashST<Integer, String>();
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            String[] words = fields[1].split(" ");
            idHash.put(Integer.parseInt(fields[0]), fields[1]);
            for (int i = 0; i < words.length; i++) {
                // StdOut.println(words);
                Bag<Integer> newBag = new Bag<Integer>();
                if (wordHash.contains(words[i]))
                    newBag = wordHash.get(words[i]);
                newBag.add(Integer.parseInt(fields[0]));
                wordHash.put(words[i], newBag);
            }
        }
        // Build a digraph of synset ids
        wordDAG = new Digraph(idHash.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            for (int i = 1; i < fields.length; i++) {
                wordDAG.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
            }
        }
        DirectedCycle dc = new DirectedCycle(wordDAG);
        if (dc.hasCycle()) throw new IllegalArgumentException();

        Topological top = new Topological(wordDAG);
        int count = 0;
        for (Integer order : top.order()) {
            // StdOut.println("order:"+order);
            if ((wordDAG.outdegree(order) == 0) && count < wordDAG.V()-1)
                throw new IllegalArgumentException();
            count++;
        }
        
        wordSAP = new SAP(wordDAG);
    }


   // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordHash.keys();
    }

   // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new NullPointerException();
        return wordHash.contains(word);
    }

   // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return wordSAP.length(wordHash.get(nounA), wordHash.get(nounB));
    }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return idHash.get(wordSAP.ancestor(wordHash.get(nounA), wordHash.get(nounB)));
    }

   // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        StdOut.println("isNoun:" + wn.isNoun("zebra"));
        StdOut.println("dist:" + wn.distance("zebra", "cat"));
        StdOut.println("dist:" + wn.distance("zebra", "zebra"));
        StdOut.println("common ancestor:" + wn.sap("zebra", "table"));
    }
}
