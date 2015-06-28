public class BoggleSolver
{
    private final BoggleTrieST<Integer> dictST;

    private class Tile {
        int m;
        int n;
    }

    private int xyToIdx(int m, int n, int nY) {
        return m * nY + n; 
    }

    private Iterable<Tile> neighbors(int m, int n, int nX, int nY) {
        Bag<Tile> nb = new Bag<Tile>();
        for (int dm = -1; dm <= 1; dm++)
            for (int dn = -1; dn <= 1; dn++) {
                if (dm == 0 && dn == 0) continue;
                if ((m + dm) >= 0 && (m + dm) < nX && (n + dn) >= 0 &&
                    (n + dn) < nY) {
                    Tile newNB = new Tile();
                    newNB.m = m + dm;
                    newNB.n = n + dn;
                    nb.add(newNB);
                }
            }
        return nb;
    }

    // private void put(BoggleBoard board, int m, int n, String prefix, boolean[] isVisited,
    //                  SET<String> words) {
    //     // Local copy of isVisited
    //     boolean[] visitedStack = new boolean[board.rows() * board.cols()];
    //     for (int i = 0; i < visitedStack.length; i++) visitedStack[i] = isVisited[i];
    //     // System.out.println(m);
    //     // System.out.println(n);
    //     prefix = prefix + board.getLetter(m, n);
    //     if (board.getLetter(m, n) == 'Q') prefix = prefix + "U";
    //     visitedStack[xyToIdx(m, n, board.cols())] = true;
    //     if (dictST.contains(prefix) && prefix.length() >= 3) words.add(prefix);
    //     if (dictST.hasChildren(prefix)) {
    //         // iterate through neighbors, recursively call put if node not visited
    //         for (Tile nb : neighbors(m, n, board.rows(), board.cols())) {
    //             if (!visitedStack[xyToIdx(nb.m, nb.n, board.cols())])
    //                 put(board, nb.m, nb.n, prefix, visitedStack, words);
    //         }
    //     }
    // }

    private void put(BoggleBoard board, int m, int n, String prefix, Stack<Integer> visitedIdx,
                     SET<String> words) {
        // Local copy of isVisited
        // boolean[] visitedStack = new boolean[board.rows() * board.cols()];
        // for (int i = 0; i < visitedStack.length; i++) visitedStack[i] = isVisited[i];
        // System.out.println(m);
        // System.out.println(n);
        prefix = prefix + board.getLetter(m, n);
        if (board.getLetter(m, n) == 'Q') prefix = prefix + "U";
        visitedIdx.push(xyToIdx(m, n, board.cols()));
        if (dictST.contains(prefix) && prefix.length() >= 3) words.add(prefix);
        if (dictST.hasChildren(prefix)) {
            // iterate through neighbors, recursively call put if node not visited
            for (Tile nb : neighbors(m, n, board.rows(), board.cols())) {
                boolean isVisited = false;
                for (int i : visitedIdx)
                    if (xyToIdx(nb.m, nb.n, board.cols()) == i) {
                        isVisited = true;
                        break;
                    }
                if (!isVisited)
                    put(board, nb.m, nb.n, prefix, visitedIdx, words);
            }
        }
        visitedIdx.pop();
    }

    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        // TODO: Build the trie representation
        dictST = new BoggleTrieST<Integer>();
        for (int i = 0; i < dictionary.length; i++) {
            int points = 0;
            int dictLength = dictionary[i].length();
            if (dictLength >= 3 && dictLength <= 4) points = 1;
            else if (dictLength == 5) points = 2;
            else if (dictLength == 6) points = 3;
            else if (dictLength == 7) points = 5;
            else if (dictLength >= 8) points = 11;
            dictST.put(dictionary[i], points);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> words = new SET<String>();
        for (int m = 0; m < board.rows(); m++)
            for (int n = 0; n < board.cols(); n++) {
                // Nodes that have been visited
                // boolean[] isVisited = new boolean[board.rows() * board.cols()]; 
                // put(board, m, n, "", isVisited, words);
                Stack<Integer> visitedIdx = new Stack<Integer>();
                put(board, m, n, "", visitedIdx, words);
            }
        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        Integer score =  dictST.get(word);
        return (score == null) ? 0 : score; 
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        // StdOut.println("Score = " + solver.scoreOf("AIRCRAFT"));
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
            {
                StdOut.println(word);
                score += solver.scoreOf(word);
            }
        StdOut.println("Score = " + score);
    }
}
