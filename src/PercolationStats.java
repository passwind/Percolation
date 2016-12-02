import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/******************************************************************************
 *  Author: Zhu Yu
 *  Date: 2016-12-01
 *  Purpose:
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats [n] [trials]
 *  Dependencies: None
 *
 *  This program takes the name of a file as a command-line argument.
 *  From that file, it
 *  TODO:
 *    - Reads the grid size n of the percolation system.
 *    - Creates an n-by-n grid of sites (intially all blocked)
 *    - Reads in a sequence of sites (row i, column j) to open.
 *
 *  After each site is opened, it draws full sites in light blue,
 *  open sites (that aren't full) in white, and blocked sites in black,
 *  with with site (1, 1) in the upper left-hand corner.
 *
 ******************************************************************************/

public class PercolationStats {
	private double[] _probabilities;
	private int _m;
	
	// perform trials independent experiments on an n-by-n grid
	public PercolationStats(int n, int trials) {
		this._m=trials;
		this._probabilities=new double[trials];
		for(int i=0;i<trials;i++) {
			int total=0;
			Percolation p=new Percolation(n);
			while(!p.percolates()){
				int row=(int) (StdRandom.uniform(n)+1);
				int col=(int) (StdRandom.uniform(n)+1);
				if(p.isFull(row, col)){
					p.open(row, col);
					total++;
				}
			}
			this._probabilities[i]=(double)total/(double)(n*n);
		}
	}
	
    // sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(this._probabilities);
	}
	
    // sample standard deviation of percolation threshold
	public double stddev() {
		return StdStats.stddev(this._probabilities);
	}
	
    // low  endpoint of 95% confidence interval
	public double confidenceLo() {
		double mean=mean();
		double stddev=stddev();
		return mean-1.96*stddev/Math.sqrt(this._m);
	}
	
    // high endpoint of 95% confidence interval
	public double confidenceHi() {
		double mean=mean();
		double stddev=stddev();
		return mean+1.96*stddev/Math.sqrt(this._m);
	}

    // test client (described below)
	public static void main(String[] args) {
		System.out.println(args);
		int[] params=StdIn.readAllInts();
		int n=params[0];
		int trials=params[1];
		System.out.println(n+" - "+trials);
		
		PercolationStats ps=new PercolationStats(n,trials);
		System.out.println("mean\t = "+ps.mean());
		System.out.println("stddev\t = "+ps.stddev());
		System.out.println("95% confidence interval\t = "+ps.confidenceLo()+", "+ps.confidenceHi());
	}
}
