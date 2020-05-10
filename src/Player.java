import java.util.*;
import java.util.stream.Stream;

/**
 * Grab the pellets as fast as you can!
 **/
class Player {
    private static GameMap map;
    private static int x, y;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        map = readFirstTurnInput(in);

        // game loop
        while (true) {
            readTurnInput(in);

            Cell here = map.getCell(x, y);
            String action = decideAction(here);
            executeAction(action);

        }
    }


    private static String decideAction(Cell here) {
        // MOVE <pacId> <x> <y>
        String action = null;
        Cell nearestPellet = map.findNearestPellet(here);
        action = moveTo(nearestPellet);
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
            if (mine) {
                x = newX;
                debug("x:" + x);
                y = newY;
                debug("y:" + y);
            }
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

    private static GameMap readFirstTurnInput(Scanner in) {
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
        return new GameMap(width, height, rows);
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

class GameMap {
    final int width;
    final int height;
    Cell[][] cells;

    public GameMap(int width, int height, List<String> rows) {
        this(width, height);
        this.cells = createGridFromStringList(rows);
    }

    public GameMap(int width, int height) {
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

    public void addPelet(int x, int y, int value) {
        cells[x][y].addPelet(value);
    }

    Cell getCell(int x, int y) {
        return cells[x][y];
    }

    Cell up(Cell actual) {
        return up(actual.x, actual.y);
    }

    Cell down(Cell actual) {
        return down(actual.x, actual.y);
    }

    Cell left(Cell actual) {
        return left(actual.x, actual.y);
    }

    Cell right(Cell actual) {
        return right(actual.x, actual.y);
    }

    Cell up(int x, int y) {
        if (y > 0) {
            return this.cells[x][y - 1];
        } else {
            return this.cells[x][height - 1];
        }
    }

    Cell down(int x, int y) {
        if (y < height - 1) {
            return this.cells[x][y + 1];
        } else {
            return this.cells[x][0];
        }
    }

    Cell left(int x, int y) {
        if (x > 0) {
            return this.cells[x - 1][y];
        } else {
            return this.cells[width - 1][y];
        }
    }

    Cell right(int x, int y) {
        if (x < width - 1) {
            return this.cells[x + 1][y];
        } else {
            return this.cells[0][y];
        }
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
        if (down.isVisitable()) {
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

    public Cell findNearestPellet(Cell here) {
        PathFinder pathFinder = new PathFinder(this);
        return pathFinder.findNearestPellet(here);
    }

    public List<Cell> getVisitableCells(Cell current) {
        return getVisitableCells(current.x, current.y);
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
        return peletValue > 0;
    }

    public boolean isVisitable() {
        return space == FREE;
    }

}


class PathFinder {
    private static final int MAX_DISTANCE = 9999;
    final GameMap map;
    final Map<Cell, Integer> visited = new HashMap<>();
    int nearestPelletDistance;

    public PathFinder(GameMap map) {
        this.map = map;
    }

    private QueueNode findNearestPellet(Cell here, int distance) {
        System.err.println("nearestPelletDistance." + nearestPelletDistance);
        if (distance >= nearestPelletDistance) {
            return null;
        }
        if (visited.containsKey(here)) {
            Integer previousDistance = visited.get(here);
            if (distance >= previousDistance) {
                return null;
            }
        }
        visited.put(here, distance);
        System.err.println("here is " + here.x + "." + here.y);
        System.err.println("distance." + distance);
        if (here.isPelet()) {
            nearestPelletDistance = distance;
            System.err.println("here is pelet at " + distance);
            return new QueueNode(here, distance);
        }

        List<Cell> visitableCells = map.getVisitableCells(here);
        List<QueueNode> nearestCells = new LinkedList<>();
        for (Cell cell : visitableCells) {
            QueueNode nearestPellet = findNearestPellet(cell, distance + 1);
            if (nearestPellet != null) {
                nearestCells.add(nearestPellet);
            }
        }

        QueueNode result = null;

        for (QueueNode node : nearestCells) {
            if (node != null) {
                if (result == null) {
                    result = node;
                } else {
                    if (node.distance < result.distance) {
                        nearestPelletDistance = node.distance;
                        result = node;
                    }
                }
            }
        }

        if (result != null) {
            System.err.println("pelet at " + result.distance + " returned");
        }
        return result;
    }

    public Cell findNearestPellet(Cell cell) {
        nearestPelletDistance = MAX_DISTANCE;
        QueueNode nearestNode = findNearestPellet(cell, 0);
        if (nearestNode == null) {
            return null;
        }
        return nearestNode.cell;
    }
}

class QueueNode {
    final Cell cell;
    final int distance;

    public QueueNode(Cell cell, int distance) {
        this.cell = cell;
        this.distance = distance;
    }
}