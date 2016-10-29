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
		if(!gameOver) {
			int[] index = getFirstEmptyCell(newGrid);
			if (index == null) {
				if(checkGoal(newGrid)) {
					grid=deepCopy(newGrid);
					writeResult();
					System.out.println("Done!!");
					gameOver=true;
					return;
				}
				return;
			}
			int x = index[0];
			int y = index[1];
			for (int i = 1; i <= 10; i++) {
				if(i<=9) {
					newGrid[x][y] = i + "";
					dfs(newGrid);
				}else {
					newGrid[x][y] ="*";
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
	        // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
	    }
	    return result;
	}
	
	public static void bfs(String [][] newGrid) {
			Queue<String[][]> queue = new LinkedList<String[][]>();
			queue.add(newGrid);
			while(!queue.isEmpty() && !gameOver) {
				String [][] node = deepCopy((String[][])queue.remove());
				int [] index = getFirstEmptyCell(node);
				if(index!=null) {
					int x = index[0];
					int y = index[1];
					for(int i=1;i<=9;i++) {
						String [][] child=new String[9][9];
						child=deepCopy(node);
						child[x][y]=i+"";
						if(checkGoal(child)) {
							grid=child;
							writeResult();
							System.out.println("Done!!");
							gameOver=true;
							return;
						}
						queue.add(child);
					}
				}
			}
	}
	
	public static int[] getMostConstrained(String [][] newGrid) {
		int [] index = new int[2];
		
		return index;
	}
	
	public static void mostConstrainedCellFirst(String[][] newGrid) {
		
	}

	public static void main(String[] args) {
		try {
			readInput("input_example2.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		bfs(grid);
	}
}
