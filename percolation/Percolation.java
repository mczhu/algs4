public class Percolation {
    private boolean[][] isOpen;
    private int size;
    private WeightedQuickUnionUF wquUF;
    private int xyTo1D(int i, int j) // convert 1-based 2d index to 1-based 1d
    {
        return size*(i-1)+j;
    }
    private boolean isOutOfBound(int i, int j) {
        return i < 1 || i > size || j < 1 || j > size;
    }
    private void connectWith(int i, int j, int x, int y) {
        if (!isOutOfBound(i, j) && isOpen(i, j)) wquUF.union(xyTo1D(x,y), xyTo1D(i, j));
    }
    public Percolation(int N)               // create N-by-N grid, with all sites blocked
    {
        if (N <= 0) throw new IllegalArgumentException("number of rows/columns needs to be greater than 0");
        size = N;
        isOpen = new boolean[size][size];
        for (int i=0; i < size; i++)
            for (int j=0; j < size; j++)
                isOpen[i][j] = false;
        wquUF = new WeightedQuickUnionUF(size*size+1);
        // Connect the first row with the imaginary top element
        for (int i=1; i<=size; i++)
            wquUF.union(0, i);
        // // Connect the bottom row with the imaginary bottom element
        // for (int i=(size-1)*size+1; i<=size*size; i++)
        //     wquUF.union(size*size+1, i);
    }
    public void open(int i, int j)          // open site (row i, column j) if it is not open already
    {
        if (isOutOfBound(i,j)) throw new IndexOutOfBoundsException();
        isOpen[i-1][j-1] = true;
        // Connect with the open neighbors
        connectWith(i-1, j, i, j);
        connectWith(i, j-1, i, j);
        connectWith(i, j+1, i, j);
        connectWith(i+1, j, i, j);
    }
    public boolean isOpen(int i, int j)     // is site (row i, column j) open?
    {
        if (isOutOfBound(i,j)) throw new IndexOutOfBoundsException();
        return isOpen[i-1][j-1];
    }
    public boolean isFull(int i, int j)     // is site (row i, column j) full?
    {
        return isOpen(i, j) && wquUF.connected(xyTo1D(i, j), 0);
    }
    public boolean percolates()             // does the system percolate?
    {
        // Check if any of the bottom row elements is full
        for (int i=1; i<=size; i++){
            if (isFull(size, i)) return true;
        }
        return false;
        // return wquUF.connected(size*size+1, 0);
    }

    public static void main(String[] args)   // test client (optional)
    {
        int N = 10;
        Percolation testPercolation = new Percolation(N);

        // Test xyTo1D()
        StdOut.println(testPercolation.xyTo1D(2, 2)+ "==" + (N+1+1));

        // Test open()
        testPercolation.open(1,1);
        testPercolation.open(2,1);
        StdOut.println("Connected? " +testPercolation.wquUF.connected(1,N+1));
        StdOut.println("Connected to top? " +testPercolation.isFull(2,1));

        // test through connection
        Percolation throughPercolation = new Percolation(N);
        for (int i = 1; i <=N; i++) throughPercolation.open(i, 1);
        StdOut.println("Full? " +throughPercolation.isFull(1,1));
        StdOut.println("Percolate? " +throughPercolation.percolates());
       
        Percolation randomPercolation = new Percolation(N);
       
        Out out = new Out("percolation_test.txt");
        out.println(N);
        StdRandom.setSeed(100);
        while (!randomPercolation.percolates())
            {
                int i = StdRandom.uniform(1, N+1);
                int j = StdRandom.uniform(1, N+1);
                while (randomPercolation.isOpen(i,j))
                    {
                        i = StdRandom.uniform(1, N+1);
                        j = StdRandom.uniform(1, N+1);
                    }
                randomPercolation.open(i,j);
                out.println(i+" "+j);
            }

        // // Test whether each of the bottom row item is full
        // for (int i = 1; i <=N; i++) StdOut.println(N+": "+randomPercolation.isFull(N, i));
    }
}
