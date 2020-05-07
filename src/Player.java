import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Grab the pellets as fast as you can!
 **/
class Player {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        readFirstTurnInput(in);

        // game loop
        //noinspection InfiniteLoopStatement
        while (true) {
            readTurnInput(in);

            String action = decideAction();
            executeAction(action);
        }
    }

    private static void executeAction(String action) {
        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");
        System.out.println(action);
    }

    private static String decideAction() {
        // MOVE <pacId> <x> <y>
        return "MOVE 0 15 10";
    }

    private static void readTurnInput(Scanner in) {
        int myScore = in.nextInt();
        int opponentScore = in.nextInt();
        int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight
        for (int i = 0; i < visiblePacCount; i++) {
            int pacId = in.nextInt(); // pac number (unique within a team)
            boolean mine = in.nextInt() != 0; // true if this pac is yours
            int x = in.nextInt(); // position in the grid
            int y = in.nextInt(); // position in the grid
            String typeId = in.next(); // unused in wood leagues
            int speedTurnsLeft = in.nextInt(); // unused in wood leagues
            int abilityCooldown = in.nextInt(); // unused in wood leagues
        }
        int visiblePelletCount = in.nextInt(); // all pellets in sight
        for (int i = 0; i < visiblePelletCount; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            int value = in.nextInt(); // amount of points this pellet is worth
        }
    }

    private static void readFirstTurnInput(Scanner in) {
        int width = in.nextInt(); // size of the grid
        int height = in.nextInt(); // top left corner is (x=0, y=0)
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String row = in.nextLine(); // one line of the grid: space " " is floor, pound "#" is wall
        }
    }
}