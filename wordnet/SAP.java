public class SAP {

    private final Digraph G;

   // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new NullPointerException();
        this.G = new Digraph(G);
    }

   // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // if ((v == null) || (w == null)) throw new NullPointerException();
        if ((v < 0) || (w < 0) || (v >= G.V()) || (w >= G.V())) 
            throw new IndexOutOfBoundsException();
        int minLength = -1;
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                int length = bfdpV.distTo(i) + bfdpW.distTo(i);
                if ((minLength == -1) || (length < minLength)) minLength = length;
            }
        }
        return minLength;
    }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // if ((v == null) || (w == null)) throw new NullPointerException();
        if ((v < 0) || (w < 0) || (v >= G.V()) || (w >= G.V())) 
            throw new IndexOutOfBoundsException();
        int ancestor = -1;
        int minLength = -1;
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                int length = bfdpV.distTo(i) + bfdpW.distTo(i);
                if ((minLength == -1) || (length < minLength)) {
                    minLength = length;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (Integer n : v)
            for (Integer m : w)
                if ((n < 0) || (m < 0) || (n >= G.V()) || (m >= G.V()))
                    throw new IndexOutOfBoundsException();
        int minLength = -1;
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                int length = bfdpV.distTo(i) + bfdpW.distTo(i);
                if ((minLength == -1) || (length < minLength)) minLength = length;
            }
        }
        return minLength;
    }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (Integer n : v)
            for (Integer m : w)
                if ((n < 0) || (m < 0) || (n >= G.V()) || (m >= G.V()))
                    throw new IndexOutOfBoundsException();
        int ancestor = -1;
        int minLength = -1;
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                int length = bfdpV.distTo(i) + bfdpW.distTo(i);
                if ((minLength == -1) || (length < minLength)) {
                    minLength = length;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

   // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length   = sap.length(v, w);
        //     int ancestor = sap.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }
        Bag<Integer> v = new Bag<Integer>();
        v.add(4);
        v.add(8);
        Bag<Integer> w = new Bag<Integer>();
        w.add(9);
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
