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
	private static Stack stack = new Stack();
	private static Queue queue = new LinkedList<>();

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
					System.out.println("stars");
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
							System.out.println("row");
							System.out
									.println("[" + i + "]" + "[" + j + "]"
											+ " with " + "[" + i + "]" + "["
											+ j2 + "]");
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
							System.out.println("column");
							System.out
									.println("[" + j + "]" + "[" + i + "]"
											+ " with " + "[" + j3 + "]" + "["
											+ i + "]");
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
							if (grid[i][j]
									.equals(grid[boxRowOffset + k][boxColOffset
											+ m])) {
								System.out.println("box");
								System.out.println("[" + i + "]" + "[" + j
										+ "]" + " with " + "["
										+ (boxRowOffset + k) + "]" + "["
										+ (boxColOffset + m) + "]");
								return false;
							}
						}
					}
				}

			}
		}

		return true;
	}

	public static ArrayList<String> possibleMoves(int x, int y,
			String[][] newGrid) {
		ArrayList<String> possibleMoves = new ArrayList<String>(Arrays.asList(
				"1", "2", "3", "4", "5", "6", "7", "8", "9"));
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

	public static boolean DFS (int i, int j, String[][] grid) {
		
		if (!grid[i][j].equals("*")) {
			if (i == 8 && j == 8) {
				return checkGoal(grid);
			}
			if (j == 8) {
				DFS(i + 1 , 0, grid);
			} else {
				DFS(j + 1, i, grid);
			}
		}
		
		return false;

		
	}
	
	public static void solveDepthFirst(int x, int y, String[][] newGrid) {
		if (!newGrid[y][x].equals("*")) {
			if (x == 8 && y == 8) {
				return;
			}
			if (x == 8) {
				solveDepthFirst(0, y + 1, newGrid);
			} else {
				solveDepthFirst(x + 1, y, newGrid);
			}

		} else {
			ArrayList<String> possibleMoves = possibleMoves(y, x, newGrid);
			if (possibleMoves.isEmpty()) {
				if (x == 0) {
					newGrid[y - 1][8] = stack.pop() + "";
				} else {
					newGrid[y][x - 1] = stack.pop() + "";
				}
				if (x == 8 && y == 8) {
					grid = newGrid;
					return;
				}
				if (x == 8) {
					solveDepthFirst(0, y + 1, newGrid);
				} else {
					solveDepthFirst(x + 1, y, newGrid);
				}
			}
			for (int i = possibleMoves.size() - 1; i >= 0; i--) {
				stack.push(possibleMoves.get(i));
			}
			while (!stack.isEmpty()) {
				newGrid[y][x] = stack.pop() + "";
				if (x == 8 && y == 8) {
					grid = newGrid;
					return;
				}
				if (x == 8) {
					solveDepthFirst(0, y + 1, newGrid);
				} else {
					solveDepthFirst(x + 1, y, newGrid);
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			readInput("GoalCheck.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(checkGoal(grid));
		// solveDepthFirst(0, 0, grid);
		// writeResult();
	}
}