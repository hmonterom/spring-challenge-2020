import java.util.*;
import java.util.stream.Stream;

/**
 * Grab the pellets as fast as you can!
 **/
class Player {
    private static Map map;
    private static Map pelletsMap;
    private static int x, y;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        map = readFirstTurnInput(in);

        // game loop
        while (true) {
            readTurnInput(in);

            String action = decideAction();
            executeAction(action);

        }
    }


    private static String decideAction() {
        // MOVE <pacId> <x> <y>
        String action = moveRandomly();
//        String action = moveToAdjacentPellet();
//        if (action == null) {
//            action = moveRandomly();
//        }
        return action;
    }

    private static String moveRandomly() {
        List<Cell> visitableCells = map.getVisitableCells(x, y);
        debug("visitable cells: " + visitableCells.size());
        Random rand = new Random();
        int index = rand.nextInt(visitableCells.size());
        return moveTo(visitableCells.get(index));
    }

    private static String moveTo(Cell cell) {
        return "MOVE 0 " + cell.x + " " + cell.y;
    }

    private static String moveTo(int x, int y) {
        return "MOVE 0 " + x + " " + y;
    }

    private static void readTurnInput(Scanner in) {
        int myScore = in.nextInt();
        int opponentScore = in.nextInt();
        int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight
        for (int i = 0; i < visiblePacCount; i++) {
            int pacId = in.nextInt(); // pac number (unique within a team)
            boolean mine = in.nextInt() != 0; // true if this pac is yours
            int newX = in.nextInt(); // position in the grid
            int newY = in.nextInt(); // position in the grid
            x = newX;
            debug("x:" + x);
            y = newY;
            debug("y:" + y);
            String typeId = in.next(); // unused in wood leagues
            int speedTurnsLeft = in.nextInt(); // unused in wood leagues
            int abilityCooldown = in.nextInt(); // unused in wood leagues
        }
        map.resetPellets();
        int visiblePelletCount = in.nextInt(); // all pellets in sight
        for (int i = 0; i < visiblePelletCount; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            int value = in.nextInt(); // amount of points this pellet is worth
            map.addPelet(x, y, value);
        }
    }

    private static Map readFirstTurnInput(Scanner in) {
        int width = in.nextInt(); // size of the grid
        int height = in.nextInt(); // top left corner is (x=0, y=0)
        List<String> rows = new ArrayList<>();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String row = in.nextLine(); // one line of the grid: space " " is floor, pound "#" is wall
            rows.add(row);
        }
        return new Map(width, height, rows);
    }

    private static void executeAction(String action) {
        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");
        System.out.println(action);
    }

    private static void debug(String line) {
        System.err.println(line);
    }

}

class Map {
    int width, height;
    Cell[][] cells;

    public Map(int width, int height, List<String> rows) {
        this(width, height);
        this.cells = createGridFromStringList(rows);
    }

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private Cell[][] createGridFromStringList(List<String> rows) {
        Cell[][] result = new Cell[width][height];
        for (int i = 0; i < height; i++) {
            char[] row = rows.get(i).toCharArray();
            for (int j = 0; j < width; j++) {
                if (j < row.length) {
                    result[j][i] = new Cell(rows.get(i).charAt(j), j, i);
                } else {
                    result[j][i] = new Cell(Cell.FREE, j, i);
                }
            }
        }
        return result;
    }

    public void resetPellets() {
        cellsStream().forEach(cell -> cell.resetPelet());
    }

    private Stream<Cell> cellsStream() {
        Stream<Cell[]> rowStream = Arrays.stream(cells);
        return rowStream.flatMap(cells -> Arrays.stream(cells));
    }

    Cell up(Cell actual) {
        return this.cells[actual.x][actual.y - 1];
    }

    Cell down(Cell actual) {
        return this.cells[actual.x][actual.y + 1];
    }

    Cell left(Cell actual) {
        return this.cells[actual.x - 1][actual.y];
    }

    Cell right(Cell actual) {
        return this.cells[actual.x][actual.y - 1];
    }

    Cell up(int x, int y) {
        return this.cells[x][y - 1];
    }

    Cell down(int x, int y) {
        return this.cells[x][y + 1];
    }

    Cell left(int x, int y) {
        return this.cells[x - 1][y];
    }

    Cell right(int x, int y) {
        return this.cells[x][y - 1];
    }

    public List<Cell> getVisitableCells(int x, int y) {
        List<Cell> result = new ArrayList<>();
        Cell up = up(x, y);
        if (up.isVisitable()) {
            result.add(up);
        }
        Cell left = left(x, y);
        if (left.isVisitable()) {
            result.add(left);
        }
        Cell down = down(x, y);
        if (up.isVisitable()) {
            result.add(down);
        }
        Cell right = right(x, y);
        if (right.isVisitable()) {
            result.add(right);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                stringBuilder.append(cells[j][i]);
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public void addPelet(int x, int y, int value) {
        cells[x][y].addPelet(value);
    }
}

class Cell {
    public static final char FREE = ' ';
    final int x, y;
    private final char space;
    private int peletValue = 0;

    public Cell(char value, int x, int y) {
        this.x = x;
        this.y = y;
        this.space = value;
    }

    @Override
    public String toString() {
        return String.valueOf(space);
    }

    public void addPelet(int value) {
        this.peletValue = value;
    }

    public void resetPelet() {
        this.peletValue = 0;
    }

    public boolean isPelet() {
        return peletValue > 1;
    }

    public boolean isVisitable() {
        return space == FREE;
    }
}
