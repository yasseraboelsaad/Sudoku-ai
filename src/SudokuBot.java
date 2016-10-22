import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SudokuBot {

	private static String[][] grid = new String[9][9];

	public static void readInput(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			String line = br.readLine();
			int counter = 0;
			while (line != null) {
				grid[counter]= line.split(" ");
				line = br.readLine();
				counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

	public static void main(String[] args) {
		try {
			readInput("input_example.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i =0;i<9;i++){
			for(int j=0;j<9;j++){
				System.out.print(grid[i][j]+ " ");
			}
			System.out.println();
		}
	}
}
