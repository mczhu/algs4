public class Board {
    private final int[][] blocks;
    private final int N;

    private int[] getBlank() {
        // return the indices of the blank block
        int[] blank = new int[2];
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0) {
                    blank[0] = i;
                    blank[1] = j;
                }                
            }
        return blank;
    }

    private Board exchange(int oldX, int oldY, int newX, int newY) {
        Board newBoard = new Board(blocks);
        newBoard.blocks[oldX][oldY] = blocks[newX][newY];
        newBoard.blocks[newX][newY] = blocks[oldX][oldY];;
        return newBoard;
    }

    private Board makeNeighborBoard(int dx, int dy) {
        int[] blank = getBlank();
        int newX = blank[0] + dx;
        int newY = blank[1] + dy;
        if ((newX < 0) || (newX >= N) || (newY <0) || (newY >=N)) 
            return null;
        Board neighbor = exchange(blank[0], blank[1], newX, newY);
        return neighbor;
    }

    public Board(int[][] blocks) {
        // construct a board from an N-by-N array of blocks
        // (where blocks[i][j] = block in row i, column j)
        this.N = blocks.length;
        this.blocks = new int[N][N];
        // defensive copy
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
    }

    public int dimension() {
        // board dimension N
        return N;
    }

    public int hamming() {
        // number of blocks out of place
        int hamming = 0;
        int count = 1;
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++)  {
                if ((blocks[i][j] != 0) && (blocks[i][j] != count)) {
                    hamming++;
                }
                count++;
            }
        return hamming;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        int manhattan = 0;
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++)
                if (blocks[i][j] != 0) {
                    int goalX = (blocks[i][j] - 1) / N;
                    int goalY = (blocks[i][j] - 1) % N;
                    manhattan += Math.abs(i - goalX) + Math.abs(j - goalY);
                }
        return manhattan;
    }

    public boolean isGoal() {
        // is this board the goal board?
        int[][] goalBlocks = new int[N][N];
        int counter = 1;
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) 
                goalBlocks[i][j] = counter++;
        goalBlocks[N-1][N-1] = 0;
        
        Board goalBoard = new Board(goalBlocks);
        return this.equals(goalBoard);
    }

    public Board twin() {
        // a board that is obtained by exchanging two adjacent blocks in the same row
        int[] blank = getBlank();
        Board twinBoard;
        if (blank[0] == 0)
            // exchange the second row first element
            twinBoard = exchange(1,0,1,1);
        else
            // exchange the first row first element
            twinBoard = exchange(0,0,0,1);
        return twinBoard;
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.N != that.N) return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.blocks[i][j] != that.blocks[i][j])
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards
        Queue<Board> neighbors = new Queue<Board>();
        Board left = makeNeighborBoard(-1,0);
        if (left != null)
            neighbors.enqueue(left);
        Board right = makeNeighborBoard(1,0);
        if (right != null)
            neighbors.enqueue(right);
        Board up = makeNeighborBoard(0,-1);
        if (up != null)
            neighbors.enqueue(up);
        Board down = makeNeighborBoard(0,1);
        if (down != null)
            neighbors.enqueue(down);
        return neighbors;
    }

    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    public static void main(String[] args) {
        // unit tests (not graded)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // Test toString()
        StdOut.println(initial);
        StdOut.println("goal? "+initial.isGoal());

        for (Board neighbor : initial.neighbors()) {
            StdOut.println(neighbor);
        }
        StdOut.println(initial.twin());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
    } 
}
