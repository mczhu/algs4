public class Solver {
    private class Node implements Comparable<Node>{
        private Board board;
        private Node previous;
        private int priority;
        private int moves;
        
        public Node(Board board, Node previous) {
            this.board = board;
            this.previous = previous;
            if (previous == null)
                moves = 0;
            else
                moves = previous.moves + 1;
            priority = board.manhattan() + moves;
        }

        public int compareTo(Node that){
            if (this.priority > that.priority) return +1;
            if (this.priority < that.priority) return -1;
            return 0;
        }
    }
    private MinPQ<Node> leaves;
    private MinPQ<Node> twinLeaves;
    private boolean isSolvable;
    private Node goalNode;

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        leaves = new MinPQ<Node>();
        twinLeaves = new MinPQ<Node>();
        Node nextNode = new Node(initial, null);
        Node nextTwinNode = new Node(initial.twin(), null);
        while (!nextNode.board.isGoal() && !nextTwinNode.board.isGoal()) {
            for (Board neighbor : nextNode.board.neighbors()) {
                if ((nextNode.previous == null) || (!neighbor.equals(nextNode.previous.board))) {
                    // Node newNode = new Node(neighbor, nextNode);
                    // StdOut.println(newNode.board);
                    leaves.insert(new Node(neighbor, nextNode));
                }
            }
            for (Board neighbor : nextTwinNode.board.neighbors()) {
                if ((nextNode.previous == null) || (!neighbor.equals(nextTwinNode.previous.board)))
                    twinLeaves.insert(new Node(neighbor, nextTwinNode));
            }
            nextNode = leaves.delMin();
            nextTwinNode = twinLeaves.delMin();
        }
        if (nextNode.board.isGoal()) {
            goalNode = nextNode;
            isSolvable = true;
        }
        else 
            isSolvable = false;
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return isSolvable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        if (isSolvable)
            return goalNode.moves;
        return -1;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        // TODO:  build a FILO queue that retraces the tree to root
        if (!isSolvable) return null;
        Stack<Board> solution = new Stack<Board>();
        Node retraceNode = goalNode;
        while (retraceNode != null) {
            solution.push(retraceNode.board);
            retraceNode = retraceNode.previous;
        }
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
