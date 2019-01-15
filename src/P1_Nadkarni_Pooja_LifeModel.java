import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 * Pooja Nadkarni
 * Period 1
 * 2/10/18
 * 
 * This lab took me around an hour.
 * 
 * This program went well for me. I found it simple to use the syntax for a 2-dimensional array
 * to complete the program. The rules of Life were easy to follow, and I could easily program
 * the necessary components to complete this program. 
 * 
 */
public class P1_Nadkarni_Pooja_LifeModel extends GridModel <Boolean> {
	ArrayList <GenerationListener> genListener;
	int gen = 0;
	
	public P1_Nadkarni_Pooja_LifeModel(Boolean[][] grid) {
		super(grid);
		genListener = new ArrayList <GenerationListener>();
	}
	public void addGenerationListener(GenerationListener l) {
		genListener.add(l);
	}
	
	public void removeGenerationListener(GenerationListener l) {
		genListener.remove(l);
	}

	public int getNumRows() {
		return super.getNumRows();
	}
	
	public int getNumCols() {
		return super.getNumCols();
	}
	
	public Boolean getValueAt(int row, int col) {
		return super.getValueAt(row, col);
	}
	
	public void setValueAt(int row, int col, boolean val) {
		super.setValueAt(row, col, val);
	}
	
	
	public void setGrid(Boolean[][] grid) {
		super.setGrid(grid);
	}
	
	public void setGeneration(int gen) {
		for(int i = 0; i < genListener.size(); i++) {
			genListener.get(i).generationChanged(gen - 1, gen);
		}
		this.gen = gen;
	}

	public int getGeneration() {
		return gen;
	}

	public int rowCount(int row) {
		if(row < 0 || row >= super.getNumCols()) {
			return -1;
		}
		int total = 0;
		for(int i = 0; i < super.getNumCols(); i++) {
			if((boolean) super.getValueAt(row, i)) {
				total++;
			}
		}
		return total;
	}
	
	public int colCount(int col) {
		if(col < 0 || col >= super.getNumRows()) {
			return -1;
		}
		int total = 0;
		for(int i = 0; i < super.getNumRows(); i++) {
			if((boolean) super.getValueAt(i, col)) {
				total++;
			}
		}
		return total;
	}
	
	public int totalCount() {
		int total = 0;
		for(int i = 0; i < super.getNumRows(); i++) {
			total += rowCount(i);
		}
		return total;
	}
	
	public void runLife(int numGenerations) {
		for(int i = 0; i < numGenerations; i++) {
			nextGeneration();
		}
	}
	

	public void nextGeneration() {
		for(int i = 0; i < super.getNumRows(); i++) {
			for(int m = 0; m < super.getNumCols(); m++) {
				int numCells = numAdjCells(i, m);
				if((boolean) super.getValueAt(i, m)) {
					if(numCells < 2 || numCells >= 4) {
						super.setValueAt(i, m, false);
					} else {
						super.setValueAt(i, m, true);
					}
				} else {
					if(numCells == 3) {
						super.setValueAt(i, m, true);
					} else {
						super.setValueAt(i, m, false);
					}
				}
			}
		}
		setGeneration(gen + 1);
	}
	
	private int numAdjCells(int row, int col){
		int totalAround = 0;
		int[][] span = {{1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 1}, {0, -1}};
		for(int i = 0; i < span.length; i++) {
			if(span[i][0] + row >= 0 && span[i][0] + row < super.getNumRows() && 
					span[i][1] + col >= 0 && span[i][1] + col < super.getNumCols()) {
				if(super.getValueAt(span[i][0] + row, span[i][1] + col)) {
					totalAround++;
				}
			}
		}
		return totalAround;
	}
}
