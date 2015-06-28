public class PercolationStats {
    private int site_size;
    private int n_trials;
    private double[] percent_opened;

    public PercolationStats(int N, int T)     // perform T independent experiments on an N-by-N grid
    {
        if (N <=0 || T <=0)  throw new IllegalArgumentException();
        site_size = N;
        n_trials = T;
        percent_opened = new double[n_trials];
        for (int trial=0; trial<n_trials; trial++){
            int n_opened = 0;
            Percolation randomPercolation = new Percolation(N);
            while (!randomPercolation.percolates()){
                int i = StdRandom.uniform(1, N+1);
                int j = StdRandom.uniform(1, N+1);
                while (randomPercolation.isOpen(i,j)){
                    i = StdRandom.uniform(1, N+1);
                    j = StdRandom.uniform(1, N+1);
                }
                randomPercolation.open(i,j);
                n_opened++;
            }
            percent_opened[trial] = ((double)n_opened)/(site_size*site_size);
        }

    }
    public double mean()                      // sample mean of percolation threshold
    {
        return StdStats.mean(percent_opened);
    }
    public double stddev()                    // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(percent_opened);
    }
    public double confidenceLo()              // low  endpoint of 95% confidence interval
    {
        return mean()-1.96*stddev()/Math.sqrt(n_trials);
    }
    public double confidenceHi()              // high endpoint of 95% confidence interval
    {
        return mean()+1.96*stddev()/Math.sqrt(n_trials);
    }
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(N,T);
        StdOut.println("mean                    ="+percStats.mean());
        StdOut.println("stddev                  ="+percStats.stddev());
        StdOut.println("95% confidence interval ="+percStats.confidenceLo()+ " " +percStats.confidenceHi());
    }
}
