import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Mat {
    ArrayList<String> mat;
    boolean[][] cellFilled;
    HashMap<Integer, Integer> rowCoords;
    HashMap<Integer, Integer> colCoords;

    final int MAT_ROWS = 3;
    final int MAT_COLUMNS = 5;
    final int CELL_LENGTH = 18;

    public Mat() {
        mat = new ArrayList<>();
        rowCoords = new HashMap<>();
        colCoords = new HashMap<>();
        cellFilled = new boolean[MAT_ROWS][MAT_COLUMNS];

        try {
            BufferedReader matReadeer = new BufferedReader(new FileReader("mat.txt"));
            String line;
            while ((line = matReadeer.readLine()) != null) {
                mat.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        initializeCoords();

    }

    public boolean addCard(Bird bird, Game.Habitat habitat) {

        //ArrayList<Game.Habitat> habts = bird.getHabitat();
        boolean inserted = false;
        int row = (habitat.ordinal());
        int col = (getLeftMostEmptyCell(habitat));
        if (col >= 0 && cellFilled[row][col] == false) {
            insertCardAt(rowCoords.get(row), colCoords.get(col), bird.getName());
            cellFilled[row][col] = true;
            return true;
        }
        return false;
    }

    public boolean emptyCellExists(Bird bird) {
        ArrayList<Game.Habitat> habts = bird.getHabitat();
        for(int i = 0; i < habts.size(); i++) {
            int row = rowCoords.get(habts.get(i).ordinal());
            for (int j = 0; j < MAT_COLUMNS; j++) {
                if (cellFilled[i][j] == false) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean emptyCellExists(Game.Habitat habt) {
        for (int j = 0; j < MAT_COLUMNS; j++) {
            if (cellFilled[habt.ordinal()][j] == false) {
                return true;
            }
        }
        return false;
    }

    public int getLeftMostEmptyCell(Game.Habitat habt) {
        int row = habt.ordinal();
        for(int i = 0; i < MAT_COLUMNS; i++)
            if(cellFilled[row][i] == false)
                return i;
        return -1;
    }

    public void insertCardAt(int y, int x, String s) {
        int length = Math.min(s.length(), CELL_LENGTH);
        char[] row = mat.get(y).toCharArray();
        for(int i = x; i < x+length; i++)
            row[i] = s.charAt(i-x);
        mat.set(y,String.valueOf(row));
    }

    private void initializeCoords() {

        rowCoords.put(0, 2);
        rowCoords.put(1, 7);
        rowCoords.put(2, 12);

        colCoords.put(0, 18);
        colCoords.put(1, 39);
        colCoords.put(2, 60);
        colCoords.put(3, 81);
        colCoords.put(4, 102);

    }

    public String toString() {
        String out = "";
        for(int i = 0; i < mat.size(); i++) {
            out += mat.get(i) + "\n";
        }
        return out;
    }
}
