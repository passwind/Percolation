/******************************************************************************
 *  Author: Zhu Yu
 *  Date: 2016-12-01
 *  Purpose:
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation input.txt
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

import edu.princeton.cs.algs4.QuickFindUF;

public class Percolation {
    // sites storage
    private boolean[] _sites;
    // index of sites
    private int _n;
    // UF common object
    private QuickFindUF _uf;

    // check if index is out of bounds?
    private void checkIndexValid(int row, int col) {
        if (row <= 0 || row > this._n) throw new IndexOutOfBoundsException("row index "+row+" out of bounds");
        if (col <= 0 || col > this._n) throw new IndexOutOfBoundsException("col index "+col+" out of bounds");
    }

    // change row, col pair to site index
    private int getSite(int row, int col) {
        checkIndexValid(row, col);
        return (row-1)*this._n+(col-1)+1;
    }

    // connect adjacent sites
    private void connectAdjacentSites(int row, int col) {
        int cs=getSite(row,col);
        int ls=-1;
        // up
        int uprow=row-1;
        if(uprow==0) {
            ls=0;
            this._uf.union(cs,ls);
        }
        else {
            if(isOpen(uprow,col)){
                ls=getSite(uprow,col);
                this._uf.union(cs,ls);
            }
        }
        // down
        int downrow=row+1;
        if(downrow>this._n) {
            ls=this._n*this._n+1;
            this._uf.union(cs,ls);
        }
        else {
            if(isOpen(downrow,col)){
                ls=getSite(downrow,col);
                this._uf.union(cs,ls);
            }

        }
        // left
        int leftcol=col-1;
        if(leftcol>=1&&isOpen(row,leftcol)){
            ls=getSite(row,leftcol);
            this._uf.union(cs,ls);
        }
        // right
        int rightcol=col+1;
        if(rightcol<=this._n&&isOpen(row,rightcol)){
            ls=getSite(row,rightcol);
            this._uf.union(cs,ls);
        }
    }

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n<=0) {
            throw new IllegalArgumentException();
        }

        this._n=n;

        // set two virtual site for check percolates quickly
        this._sites=new boolean[n*n+2];
        for (int i=0;i<n;i++)
            for (int j=0;j<n;j++)
            {
                int site=i*n+j+1;
                this._sites[site]=false;
            }

        // set virtual sites to open
        this._sites[0]=true;
        this._sites[n*n+1]=true;

        // new uf
        this._uf=new QuickFindUF(n*n+2);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int site=getSite(row,col);

        if (!this._sites[site]) {
            this._sites[site]=true;
            // connect adjacent sites
            connectAdjacentSites(row,col);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        int site=getSite(row,col);

        return this._sites[site];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        int site=getSite(row,col);

        return !this._sites[site];
    }

    // does the system percolate?
    public boolean percolates() {
        return this._uf.connected(0,this._n*this._n+1);
    }

    // test client
    public static void main(String[] args) {
        // test n=0
        Percolation p=new Percolation(3);

        p.open(1,1);
        p.open(2,1);
        p.open(3,1);
        
        System.out.println("percolates: "+p.percolates());
    }
}
