/******************************************************************************
 *  Author: Zhu Yu
 *  Date: 2016-12-01
 *  Purpose:
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation
 *  Dependencies: None
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // sites storage
    private boolean[] sites;
    // index of sites
    private int n;
    // UF common object
    private WeightedQuickUnionUF uf;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;

        // set two virtual site for check percolates quickly
        this.sites = new boolean[n*n+2];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            {
                int site = i*n+j+1;
                this.sites[site] = false;
            }

        // set virtual sites to open
        this.sites[0] = true;
        this.sites[n*n+1] = true;

        // new uf
        this.uf = new WeightedQuickUnionUF(n*n+2);
    }

    // check if index is out of bounds?
    private void checkIndexValid(int row, int col) {
        if (row <= 0 || row > this.n) throw new IndexOutOfBoundsException("row index "+row+" out of bounds");
        if (col <= 0 || col > this.n) throw new IndexOutOfBoundsException("col index "+col+" out of bounds");
    }

    // change row, col pair to site index
    private int getSite(int row, int col) {
        checkIndexValid(row, col);
        return (row-1)*this.n+(col-1)+1;
    }

    // connect adjacent sites
    private void connectAdjacentSites(int row, int col) {
        int cs = getSite(row, col);
        int ls = -1;
        // up
        int uprow = row-1;
        if (uprow == 0) {
            ls = 0;
            this.uf.union(cs, ls);
        }
        else {
            if (isOpen(uprow, col)) {
                ls = getSite(uprow, col);
                this.uf.union(cs, ls);
            }
        }
        // down
        int downrow = row+1;
        if (downrow > this.n) {
            ls = this.n*this.n+1;
            this.uf.union(cs, ls);
        }
        else {
            if (isOpen(downrow, col)) {
                ls = getSite(downrow, col);
                this.uf.union(cs, ls);
            }
        }
        // left
        int leftcol = col-1;
        if (leftcol >= 1 && isOpen(row, leftcol)) {
            ls = getSite(row, leftcol);
            this.uf.union(cs, ls);
        }
        // right
        int rightcol = col+1;
        if (rightcol <= this.n && isOpen(row, rightcol)) {
            ls = getSite(row, rightcol);
            this.uf.union(cs, ls);
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int site = getSite(row, col);

        if (!this.sites[site]) {
            this.sites[site] = true;
            // connect adjacent sites
            connectAdjacentSites(row, col);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        int site = getSite(row, col);

        return this.sites[site];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }
        
        int site = getSite(row, col);
        
        if (row == 1) {
            // for top row, just open is full
            return true; 
        }

        return this.uf.connected(0, site);
    }

    // does the system percolate?
    public boolean percolates() {
        return this.uf.connected(0, this.n*this.n+1);
    }

    // test client
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();         // n-by-n percolation system
        Percolation perc = new Percolation(n);
        int ti = 0;
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            ti++;
            if (ti == 4) {
                System.out.println("3,1 full: "+perc.isFull(3, 1));
            }
        }
    }
}
