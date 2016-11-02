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
	private static String[][] gridDomain = new String[9][9];
	public static String val = "";
	public static Stack<String> lifo = new Stack();
	public static int prevX = 0;
	public static int prevY = 0;
	private static ArrayList<String> placements = new ArrayList<String>();
	private static boolean gameOver = false;
	private static ArrayList<String[][]> visited = new ArrayList<String[][]>();

	public static void initForwardCheking() {
		for (int k = 0; k < 9; k++) {
			for (int m = 0; m < 9; m++) {
				if (grid[k][m].equals("*")) {
					gridDomain[k][m] = possibleMovesString(k, m, grid)
							.substring(0);
				} else {
					gridDomain[k][m] = "+";
				}
			}
		}
	}

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

	public static boolean checkEligibility(String[][] grid, int x, int y,
			int index) {
		lifo.push(gridDomain[x][y].charAt(index) + "");
		val = lifo.pop();
		if (gridDomain[x][y].length() > 0) {
			excludeFromOtherDomains(x, y, val + "");
		}

		for (int i = 0; i < 9; i++) {
			if (i > y) {
				if ((!grid[x][i].equals("*") && val.equals(grid[x][i]))
						|| (grid[x][i].equals("*") && countOfChar(
								gridDomain[x][i], '@') == gridDomain[x][i]
								.length())) {

					includeBackToOtherDomains(x, y, val);
					System.out.println("row violation");

					grid[x][y] = "*";

					return false;
				}
			}
		}

		for (int j = 0; j < 9; j++) {
			if (j > x) {
				if ((!grid[j][y].equals("*") && val.equals(grid[j][y]))
						|| (grid[j][y].equals("*") && countOfChar(
								gridDomain[j][y], '@') == gridDomain[j][y]
								.length())) {

					includeBackToOtherDomains(x, y, val);
					System.out.println("column violation");

					grid[x][y] = "*";
					return false;

				}
			}
		}

		int boxRowOffset = (x / 3) * 3;
		int boxColOffset = (y / 3) * 3;
		for (int k = 0; k < 3; ++k) {
			for (int m = 0; m < 3; ++m) {
				if ((boxColOffset + m > y && boxRowOffset + k == x)
						|| (boxRowOffset + k > x)) {
					if ((!grid[boxRowOffset + k][boxColOffset + m].equals("*") && val
							.equals(grid[boxRowOffset + k][boxColOffset + m]))
							|| (grid[boxRowOffset + k][boxColOffset + m]
									.equals("*") && countOfChar(
									gridDomain[boxRowOffset + k][boxColOffset
											+ m], '@') == gridDomain[boxRowOffset
									+ k][boxColOffset + m].length())) {

						includeBackToOtherDomains(x, y, val);
						System.out.println("box violation");

						grid[x][y] = "*";

						return false;

					}
				}
			}
		}

		grid[x][y] = val;
		return true;

	}

	public static String possibleMovesString(int x, int y, String[][] newGrid) {
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
		String finalPossibleMoves = "";
		for (String s : possibleMoves) {
			finalPossibleMoves += s;
		}
		// finalPossibleMoves = finalPossibleMoves.substring(0,
		// finalPossibleMoves.length());
		System.out.println(finalPossibleMoves);
		return finalPossibleMoves;
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

		return index;
	}

	public static void mostConstrainedCellFirst(String[][] newGrid) {

	}

	public static void forwardChecking(String[][] newGrid) {
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

			// ArrayList<String> currentPossibleMoves = new ArrayList<String>();
			//
			// for (char c : gridDomain[x][y].toCharArray()) {
			// currentPossibleMoves.add(c + "");
			// }

			if (gridDomain[x][y].length() > 0) {
				for (int i = 0; i < gridDomain[x][y].length(); i++) {

					// lifo.push(gridDomain[x][y].charAt(0) + "");
					if (gridDomain[x][y].charAt(i) != '@') {
						prevX = x;
						prevY = y;
						// newGrid[x][y] = gridDomain[x][y].charAt(0) + "";

						if (checkEligibility(newGrid, x, y, i)) {
							forwardChecking(newGrid);
							// if (gridDomain[x][y].length() <= 0
							// && !checkEligibility(newGrid, x, y)) {
							// newGrid[x][y] = "*";
							// }
						} else {
							newGrid[x][y] = "*";

						}

					}
				}
			} else {
				return;
				// if (!lifo.isEmpty()) {
				// includeBackToOtherDomains(x, y, lifo.pop());
				// } else {
				// grid[x][y] = "*";
				// }
			}
		}
	}

	private static void excludeFromOtherDomains(int x, int y, String number) {
		// For Row Domain Replacement
		for (int i = 0; i < 9; i++) {
			if (!gridDomain[x][i].equals("+")
					&& gridDomain[x][i].contains(number)) {
				gridDomain[x][i] = gridDomain[x][i].replaceFirst(number, "@");

			}
		}
		// For Column Domain Replacement
		for (int j = 0; j < 9; j++) {
			if (!gridDomain[j][y].equals("+")
					&& gridDomain[j][y].contains(number)) {
				gridDomain[j][y] = gridDomain[j][y].replaceFirst(number, "@");
			}
		}

		// For Box Domain Replacement
		int boxRowOffset = (x / 3) * 3;
		int boxColOffset = (y / 3) * 3;
		for (int k = 0; k < 3; ++k) {
			for (int m = 0; m < 3; ++m) {
				if (!gridDomain[boxRowOffset + k][boxColOffset + m].equals("+")
						&& gridDomain[boxRowOffset + k][boxColOffset + m]
								.contains(number)) {
					gridDomain[boxRowOffset + k][boxColOffset + m] = gridDomain[boxRowOffset
							+ k][boxColOffset + m].replaceFirst(number, "@");
				}
			}
		}

	}

	private static void includeBackToOtherDomains(int x, int y, String number) {
		// For Row Domain Addition
		for (int i = 0; i < 9; i++) {
			if (i != y) {
				if (!gridDomain[x][i].equals("+")
						&& !gridDomain[x][i].contains(number)) {
					gridDomain[x][i] = gridDomain[x][i].replaceFirst("@",
							number);
					System.out.println("domain back to row " + val + " [" + x
							+ "]" + "[" + i + "]");

				}
			} else {
				gridDomain[x][i] = gridDomain[x][i].replaceFirst("@", number);
			}
		}
		// For Column Domain Addition
		for (int j = 0; j < 9; j++) {
			if (j != x) {
				if (!gridDomain[j][y].equals("+")
						&& !gridDomain[j][y].contains(number)) {
					gridDomain[j][y] = gridDomain[j][y].replaceFirst("@",
							number);
					System.out.println("domain back to column " + val + " ["
							+ j + "]" + "[" + y + "]");
				}
			} else {
				gridDomain[j][y] = gridDomain[j][y].replaceFirst("@", number);
			}
		}

		// For Box Domain Addition
		int boxRowOffset = (x / 3) * 3;
		int boxColOffset = (y / 3) * 3;
		for (int k = 0; k < 3; ++k) {
			for (int m = 0; m < 3; ++m) {
				if (boxRowOffset + k != x && boxColOffset + m != y) {
					if (!gridDomain[boxRowOffset + k][boxColOffset + m]
							.equals("+")
							&& !gridDomain[boxRowOffset + k][boxColOffset + m]
									.contains(number)) {
						gridDomain[boxRowOffset + k][boxColOffset + m] = gridDomain[boxRowOffset
								+ k][boxColOffset + m]
								.replaceFirst("@", number);
						System.out.println("domain back to box " + val + " ["
								+ (boxRowOffset + k) + "]" + "["
								+ (boxColOffset + m) + "]");

					}
				} else {
					gridDomain[boxRowOffset + k][boxColOffset + m] = gridDomain[boxRowOffset
							+ k][boxColOffset + m].replaceFirst("@", number);
				}
			}
		}

	}

	public static int countOfChar(String s, char x) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (x == s.charAt(i)) {
				count++;
			}
		}
		return count;

	}

	public static void main(String[] args) {
		try {
			readInput("example3.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// dfs(grid);
		initForwardCheking();
		forwardChecking(grid);

		// ArrayList<String> currentPossibleMoves = new ArrayList<String>(
		// Arrays.asList("1", "2", "3"));
		// String s = "1 0";
		// s = s.replace("1 ", "");
		// System.out.println(s);
		// System.out.println(s.indexOf("0"));
		// for (int i = 0; i <= currentPossibleMoves.size(); i++) {
		// if (i < 3) {
		// System.out.println(currentPossibleMoves.get(i));
		// } else {
		// System.out.println(i);
		// }
		//
		// }
	}
}