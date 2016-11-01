import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class SudokuBot {

	private static String[][] grid = new String[9][9];
	private static ArrayList<String> placements = new ArrayList<String>();
	private static boolean gameOver = false;
	private static ArrayList<String[][]> visited = new ArrayList<String[][]>();

	public static void readInput(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			String line = br.readLine();
			int counter = 0;
			while (line != null) {
				grid[counter] = line.split(" ");
				line = br.readLine();
				counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

	public static void writeResult() {
		BufferedWriter writer = null;
		try {
			File file = new File("solution.txt");
			writer = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					writer.write(grid[i][j] + " ");
				}
				writer.write('\n');
			}
			for (int i = 0; i < placements.size(); i++) {
				writer.write(placements.get(i));
				writer.write('\n');
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public static boolean checkGoal(String[][] grid) {
		// For Full Board Checking
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (grid[i][j].equals("*")) {
					return false;
				}
			}
		}
		// For Row Checking
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				for (int j2 = 0; j2 < 9; j2++) {
					if (j != j2) {
						if (grid[i][j].equals(grid[i][j2])) {
							return false;
						}
					}
				}
			}
		}
		// For Column Checking
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				for (int j3 = 0; j3 < 9; j3++) {
					if (j != j3) {
						if (grid[j][i].equals(grid[j3][i])) {
							return false;
						}
					}
				}
			}
		}
		// For Box Checking
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int boxRowOffset = (i / 3) * 3;
				int boxColOffset = (j / 3) * 3;
				for (int k = 0; k < 3; k++) {
					for (int m = 0; m < 3; m++) {
						if ((boxRowOffset + k) != i || (boxColOffset + m) != j) {
							if (grid[i][j].equals(grid[boxRowOffset + k][boxColOffset + m])) {
								return false;
							}
						}
					}
				}

			}
		}

		return true;
	}

	public static ArrayList<String> possibleMoves(int x, int y, String[][] newGrid) {
		ArrayList<String> possibleMoves = new ArrayList<String>(
				Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
		for (int i = 0; i < 9; i++) {
			if (!newGrid[i][y].equals("*")) {
				possibleMoves.remove(newGrid[i][y]);
			}
		}
		for (int j = 0; j < 9; j++) {
			if (!newGrid[x][j].equals("*")) {
				possibleMoves.remove(newGrid[x][j]);
			}
		}
		if (x <= 2 && y <= 2) {
			// Top left grid
			for (int i = 0; i <= 2; i++) {
				for (int j = 0; j <= 2; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		} else if (x <= 5 && y <= 2) {
			// Top mid grid
			for (int i = 3; i <= 5; i++) {
				for (int j = 0; j <= 2; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		} else if (x <= 8 && y <= 2) {
			// Top right grid
			for (int i = 6; i <= 8; i++) {
				for (int j = 0; j <= 2; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		} else if (x <= 2 && y <= 5) {
			// Mid left grid
			for (int i = 0; i <= 2; i++) {
				for (int j = 3; j <= 5; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		} else if (x <= 5 && y <= 5) {
			// Mid grid
			for (int i = 3; i <= 5; i++) {
				for (int j = 3; j <= 5; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		} else if (x <= 8 && y <= 5) {
			// Mid right grid
			for (int i = 6; i <= 8; i++) {
				for (int j = 3; j <= 5; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		} else if (x <= 2 && y <= 8) {
			// Bottom left grid
			for (int i = 0; i <= 2; i++) {
				for (int j = 6; j <= 8; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		} else if (x <= 5 && y <= 8) {
			// Bottom mid grid
			for (int i = 3; i <= 5; i++) {
				for (int j = 6; j <= 8; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		} else if (x <= 8 && y <= 8) {
			// Bottom right grid
			for (int i = 6; i <= 8; i++) {
				for (int j = 6; j <= 8; j++) {
					if (!newGrid[i][j].equals("*")) {
						possibleMoves.remove(newGrid[i][j]);
					}
				}
			}
		}
		return possibleMoves;
	}

	public static boolean isGameOver(String[][] newGrid) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (newGrid[i][j].equals("*")) {
					return false;
				}
			}
		}
		return true;
	}

	public static int[] getFirstEmptyCell(String[][] currentState) {
		int[] index = new int[2];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (currentState[i][j].equals("*")) {
					index[0] = i;
					index[1] = j;
					return index;
				}
			}
		}
		return null;
	}

	public static void dfs(String[][] newGrid) {
		if (!gameOver) {
			int[] index = getFirstEmptyCell(newGrid);
			if (index == null) {
				if (checkGoal(newGrid)) {
					grid = deepCopy(newGrid);
					writeResult();
					System.out.println("Done!!");
					gameOver = true;
					return;
				}
				return;
			}
			int x = index[0];
			int y = index[1];
			for (int i = 1; i <= 10; i++) {
				if (i <= 9) {
					newGrid[x][y] = i + "";
					dfs(newGrid);
				} else {
					newGrid[x][y] = "*";
				}
			}
		}
	}

	public static String[][] deepCopy(String[][] original) {
		if (original == null) {
			return null;
		}

		final String[][] result = new String[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
			// For Java versions prior to Java 6 use the next:
			// System.arraycopy(original[i], 0, result[i], 0,
			// original[i].length);
		}
		return result;
	}

	public static void bfs(String[][] newGrid) {
		Queue<String[][]> queue = new LinkedList<String[][]>();
		queue.add(newGrid);
		while (!queue.isEmpty() && !gameOver) {
			String[][] node = deepCopy((String[][]) queue.remove());
			int[] index = getFirstEmptyCell(node);
			if (index != null) {
				int x = index[0];
				int y = index[1];
				for (int i = 1; i <= 9; i++) {
					String[][] child = new String[9][9];
					child = deepCopy(node);
					child[x][y] = i + "";
					if (checkGoal(child)) {
						grid = child;
						writeResult();
						System.out.println("Done!!");
						gameOver = true;
						return;
					}
					queue.add(child);
				}
			}
		}
	}

	public static int[] getMostConstrained(String[][] newGrid) {
		int[] index = new int[2];
		int[][] numberOfMoves = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				numberOfMoves[i][j]=10;
			}
		}
		int[] indexOfFirstEmpty = getFirstEmptyCell(newGrid);
		int x=indexOfFirstEmpty[0];
		int y = indexOfFirstEmpty[1];
		numberOfMoves[x][y]=possibleMoves(x, y, newGrid).size();
		int smallestX=x;
		int smallestY=y;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if(newGrid[i][j].equals("*")) {
					if(possibleMoves(i, j, newGrid).size()<possibleMoves(smallestX, smallestY, newGrid).size()) {
						smallestX=i;
						smallestY=j;
					}
				}
			}
		}
		index[0]=smallestX;
		index[1]=smallestY;
		return index;
	}

	public static void mostConstrainedCellFirst(String[][] newGrid) {
		Queue<String[][]> queue = new LinkedList<String[][]>();
		queue.add(newGrid);
		while (!queue.isEmpty() && !gameOver) {
			String[][] node = deepCopy((String[][]) queue.remove());
			int[] index = getMostConstrained(node);
			if (index != null) {
				int x = index[0];
				int y = index[1];
				ArrayList<String> pMoves= possibleMoves(x, y, node); 
				for (int i = 0; i < pMoves.size(); i++) {
					String[][] child = new String[9][9];
					child = deepCopy(node);
					child[x][y] = pMoves.get(i) + "";
					if (checkGoal(child)) {
						grid = child;
						writeResult();
						System.out.println("Done!!");
						gameOver = true;
						return;
					}
					queue.add(child);
				}
			}
		}
	}
	
	public static String[][] getAllDomains(String [][] newGrid){
		String [][] domains = new String[9][9];
		for(int i = 0; i<9;i++) {
			for(int j=0;j<9;j++) {
				if(newGrid[j][i].equals("*")) {
					ArrayList<String> temp = possibleMoves(j, i, newGrid);
					String temp2="";
					for(int k=0;k<temp.size();k++) {
						temp2=temp2+temp.get(k);
						domains[j][i]=temp2;
					}
				}
			}
		}
		return domains;
	}
	
	public static String [][] forwardCheck(int x, int y, String i, String [][] newGrid) {
		String [][] domains = deepCopy(newGrid);
		for(int j=0;j<9;j++) {
			if(newGrid[x][j]!=null && newGrid[x][j].contains(i+"")) {
				domains[x][j]=domains[x][j].replace(i+"", "");
			}
		}
		
		for(int j=0;j<9;j++) {
			if(newGrid[j][y]!=null &&newGrid[j][y].contains(i+"")) {
				domains[j][y]=domains[j][y].replace(i+"", "");
			}
		}
		
		int boxRowOffset = (x / 3)*3;
        int boxColOffset = (y / 3)*3;
        for (int k = 0; k < 3; ++k) // box
            for (int m = 0; m < 3; ++m)
            	if(newGrid[boxRowOffset+k][boxColOffset+m]!=null && newGrid[boxRowOffset+k][boxColOffset+m].contains(i+"")) {
            		domains[boxRowOffset+k][boxColOffset+m]=domains[boxRowOffset+k][boxColOffset+m].replace(i+"", "");
    			}
		
		return domains;
	}
	
	public static boolean nullChecker(String[][] oldDomain, String [][] newDomain) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (oldDomain[i][j]!=null && newDomain[i][j]==null) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void forwardChecking(String [][] newGrid) {
		String [][] domains = deepCopy(getAllDomains(newGrid));
		Queue<String[][]> queue = new LinkedList<String[][]>();
		queue.add(newGrid);
		while (!queue.isEmpty() && !gameOver) {
			String[][] node = deepCopy((String[][]) queue.remove());
			int[] index = getMostConstrained(node);
			if (index != null) {
				int x = index[0];
				int y = index[1];
				ArrayList<String> pMoves= possibleMoves(x, y, node); 
				for (int i = 0; i < pMoves.size(); i++) {
					String[][] newDomain=forwardCheck(x,y, pMoves.get(i), domains);
					if(nullChecker(domains, newDomain)) {
						domains=deepCopy(newDomain);
						String[][] child = new String[9][9];
						child = deepCopy(node);
						child[x][y] = pMoves.get(i) + "";
						if (checkGoal(child)) {
							grid = child;
							writeResult();
							System.out.println("Done!!");
							gameOver = true;
							return;
						}
						queue.add(child);
					}
				}
			}
		}
	}
	
	public static String [][] recurrsiveForwardCheck(int x, int y, String i, String [][] newGrid) {
		String [][] domains = deepCopy(newGrid);
		for(int j=0;j<9;j++) {
			if(newGrid[x][j]!=null && newGrid[x][j].contains(i+"")) {
				domains[x][j]=domains[x][j].replace(i+"", "");
				if(domains[x][j].length()==1) {
					recurrsiveForwardCheck(x, j, domains[x][j], domains);
				}
			}
		}
		
		for(int j=0;j<9;j++) {
			if(newGrid[j][y]!=null &&newGrid[j][y].contains(i+"")) {
				domains[j][y]=domains[j][y].replace(i+"", "");
				if(domains[j][y].length()==1) {
					recurrsiveForwardCheck(j, y, domains[j][y], domains);
				}
			}
		}
		
		int boxRowOffset = (x / 3)*3;
        int boxColOffset = (y / 3)*3;
        for (int k = 0; k < 3; ++k) // box
            for (int m = 0; m < 3; ++m)
            	if(newGrid[boxRowOffset+k][boxColOffset+m]!=null && newGrid[boxRowOffset+k][boxColOffset+m].contains(i+"")) {
            		domains[boxRowOffset+k][boxColOffset+m]=domains[boxRowOffset+k][boxColOffset+m].replace(i+"", "");
            		if(domains[boxRowOffset+k][boxColOffset+m].length()==1) {
    					recurrsiveForwardCheck(boxRowOffset+k, boxColOffset+m, domains[boxRowOffset+k][boxColOffset+m], domains);
    				}
    			}
		
		return domains;
	}
	
	public static void maintainingArcConsistensy(String [][] newGrid) {
		String [][] domains = deepCopy(getAllDomains(newGrid));
		Queue<String[][]> queue = new LinkedList<String[][]>();
		queue.add(newGrid);
		while (!queue.isEmpty() && !gameOver) {
			String[][] node = deepCopy((String[][]) queue.remove());
			int[] index = getMostConstrained(node);
			if (index != null) {
				int x = index[0];
				int y = index[1];
				ArrayList<String> pMoves= possibleMoves(x, y, node); 
				for (int i = 0; i < pMoves.size(); i++) {
					String[][] newDomain=recurrsiveForwardCheck(x,y, pMoves.get(i), domains);
					if(nullChecker(domains, newDomain)) {
						domains=deepCopy(newDomain);
						String[][] child = new String[9][9];
						child = deepCopy(node);
						child[x][y] = pMoves.get(i) + "";
						if (checkGoal(child)) {
							grid = child;
							writeResult();
							System.out.println("Done!!");
							gameOver = true;
							return;
						}
						queue.add(child);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			readInput("example3.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		maintainingArcConsistensy(grid);
	}
}