import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/******************************************************************************
 * Author: Zhu Yu Date: 2016-12-01 Purpose: Compilation: javac
 * PercolationStats.java Execution: java PercolationStats [n] [trials]
 * Dependencies: None
 *
 ******************************************************************************/

public class PercolationStats {
    private double[] probalities;
    private int m;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0)
            throw new IllegalArgumentException();
        if (trials <= 0)
            throw new IllegalArgumentException();

        this.m = trials;
        this.probalities = new double[trials];
        for (int i = 0; i < trials; i++) {
            int total = 0;
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    total++;
                }
            }
            this.probalities[i] = (double) total / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.probalities);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.probalities);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double stddev = stddev();
        return mean - 1.96 * stddev / Math.sqrt(this.m);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double stddev = stddev();
        return mean + 1.96 * stddev / Math.sqrt(this.m);
    }

    // test client (described below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }

        int n = Integer.parseInt(args[0]);
        int trivals = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trivals);
        System.out.println("mean\t = " + ps.mean());
        System.out.println("stddev\t = " + ps.stddev());
        System.out.println("95% confidence interval\t = " + ps.confidenceLo()
                + ", " + ps.confidenceHi());
    }
}
