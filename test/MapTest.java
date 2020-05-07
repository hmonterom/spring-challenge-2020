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
        Map map = new Map(35,12,rows);
        String concatenatedRows = concatenateRows(rows);
        Assertions.assertEquals(map.toString(), concatenatedRows);
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
