import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapTest {

    @Test
    public void testBuilderMap1() throws Exception {
        List<String> rows = readRowsFromFile("test-resources\\Map1.txt");
        Map map = new Map(33,12,rows);
        String concatenatedRows = concatenateRows(rows);
        Assertions.assertEquals(map.toString(), concatenatedRows);
    }

    @Test
    public void testBuilderMap2() throws Exception {
        List<String> rows = readRowsFromFile("test-resources\\Map2.txt");
        Map map = new Map(35, 12, rows);
        String concatenatedRows = concatenateRows(rows);
        Assertions.assertEquals(map.toString(), concatenatedRows);
    }

    public static Map givenASimpleMap() throws FileNotFoundException {
        MapTest test = new MapTest();
        List<String> rows = test.readRowsFromFile("test-resources\\Map3.txt");
        return new Map(10, 10, rows);
    }

    @Test
    public void testDirections() throws Exception {
        List<String> rows = readRowsFromFile("test-resources\\Map0.txt");
        Map map = new Map(3, 3, rows);
        Cell center = map.getCell(1, 1);
        Assertions.assertEquals('U', map.up(center).toString().charAt(0));
        Assertions.assertEquals('D', map.down(center).toString().charAt(0));
        Assertions.assertEquals('L', map.left(center).toString().charAt(0));
        Assertions.assertEquals('R', map.right(center).toString().charAt(0));
    }

    @Test
    public void testVisitableCells() throws Exception {
        Map map = givenASimpleMap();
        List<Cell> visitableCells;
        visitableCells = map.getVisitableCells(1, 5);
        Assertions.assertEquals(1, visitableCells.size());
        visitableCells = map.getVisitableCells(1, 1);
        Assertions.assertEquals(2, visitableCells.size());
        visitableCells = map.getVisitableCells(2, 2);
        Assertions.assertEquals(3, visitableCells.size());
        visitableCells = map.getVisitableCells(4, 5);
        Assertions.assertEquals(4, visitableCells.size());
    }

    private String concatenateRows(List<String> rows) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String row : rows) {
            stringBuilder.append(row);
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private List<String> readRowsFromFile(String filename) throws FileNotFoundException {
        List<String> result = new ArrayList<>();
        File text = new File(filename);

        //Creating Scanner instnace to read File in Java
        Scanner scnr = new Scanner(text);

        //Reading each line of file using Scanner class
        int lineNumber = 1;
        while(scnr.hasNextLine()){
            result.add(scnr.nextLine());
        }
        return result;
    }
}
