import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathFinderTest {

    PathFinder pathFinder;

    @BeforeEach
    public void init() throws Exception {
        pathFinder = new PathFinder(MapTest.givenASimpleMap());
    }

    @Test
    public void returnsOwnCellIfIsPellet() {
        pathFinder.map.addPelet(1, 1, 1);
        Cell own = pathFinder.map.getCell(1, 1);
        Cell result = pathFinder.findNearestPellet(own);
        assertEquals(result, own);
    }

    @Test
    public void ifVisitableIsPelletReturnsVisitable() {
        pathFinder.map.addPelet(1, 2, 1);
        Cell own = pathFinder.map.getCell(1, 1);
        Cell result = pathFinder.findNearestPellet(own);
        assertEquals(result, pathFinder.map.getCell(1, 2));
    }

    @Test
    public void twoCellsTest() {
        pathFinder.map.addPelet(1, 3, 1);
        Cell own = pathFinder.map.getCell(1, 1);
        Cell result = pathFinder.findNearestPellet(own);
        assertEquals(result, pathFinder.map.getCell(1, 3));
    }

    @Test
    public void threeCellsTest() {
        pathFinder.map.addPelet(2, 3, 1);
        Cell own = pathFinder.map.getCell(1, 1);
        Cell result = pathFinder.findNearestPellet(own);
        assertEquals(result, pathFinder.map.getCell(2, 3));
    }

    @Test
    public void twoPelletsTest() {
        pathFinder.map.addPelet(4, 1, 1);
        pathFinder.map.addPelet(4, 7, 1);
        Cell own = pathFinder.map.getCell(4, 5);
        Cell result = pathFinder.findNearestPellet(own);
        assertEquals(result, pathFinder.map.getCell(4, 7));
    }

    @Test
    public void noPelletTest() {
        Cell own = pathFinder.map.getCell(4, 5);
        Cell result = pathFinder.findNearestPellet(own);
        assertEquals(result, null);
    }

}