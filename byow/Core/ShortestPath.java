package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class ShortestPath {

    public static int[] getThePath(TETile[][] world, int x1, int x2, int y1, int y2) {

        int[] next = {x1, y1};

        String move = getNextMove(world, x1, y1, x2, y2);
        switch (move) {
            case "left":
                next[0] = x1-1;
                break;
            case "right":
                next[0] = x1+1;
                break;
            case "up":
                next[1] = y1+1;
                break;
            case "down":
                next[1] = y1-1;
                break;
        }

        return next;
    }

    private static String getNextMove(TETile[][] world, int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        // Check for direct horizontal or vertical move
        if (dx != 0 && isClearPath(world, x1, y1, x2, y1))
            return dx > 0 ? "right" : "left";
        if (dy != 0 && isClearPath(world, x1, y1, x1, y2))
            return dy > 0 ? "up" : "down";

        // Find the shortest diagonal move
        int absDx = Math.abs(dx);
        int absDy = Math.abs(dy);

        if (absDx < absDy)
            return dy > 0 ? "up" : "down";
        if (absDx > absDy)
            return dx > 0 ? "right" : "left";

        // |dx| == |dy|
        if (dx > 0 && dy > 0)
            return "right";
        if (dx < 0 && dy < 0)
            return "left";
        if (dx > 0 && dy < 0)
            return "down";
        return "up";
    }

    private static boolean isClearPath(TETile[][] world, int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        for (int i = 1; i <= steps; i++) {
            int x = x1 + (i * dx) / steps;
            int y = y1 + (i * dy) / steps;


            if (world[x][y] != Tileset.FLOOR)
                return false;
        }

        return true;
    }
}