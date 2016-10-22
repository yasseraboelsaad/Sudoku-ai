import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SudokuBot {

	private static String[][] grid = new String[9][9];
	private static ArrayList<String> placements = new ArrayList<String>();

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
	
	public static void writeResult(){
		BufferedWriter writer = null;
        try {
            File file = new File("solution.txt");
            writer = new BufferedWriter(new FileWriter(file));
            for(int i =0;i<9;i++){
    			for(int j=0;j<9;j++){
    				writer.write(grid[i][j]+ " ");
    			}
    			writer.write('\n');
    		}
            for(int i=0;i<placements.size();i++){
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
	
	public static void solve(){
		for(int i =0;i<9;i++){
			for(int j=0;j<9;j++){
				if(grid[i][j].equals("*")){
					grid[i][j] = "8";
					placements.add(i+" "+j+" "+ 8);
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			readInput("input_example.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		solve();
		writeResult();
	}
}
