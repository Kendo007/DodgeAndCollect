package byow.Core;

import byow.Core.Room;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.Scanner;

import byow.Core.Position;
import edu.princeton.cs.algs4.StdDraw;

import static byow.Core.Engine.*;
import static byow.Core.RandomUtils.*;
import static byow.Core.ShortestPath.getThePath;

public class World {
    /** Percentage of how much of the total world area is covered by rooms.
     *  Increase this to get more rooms
     */
    private static final int roomPercent = 55;
    private static final int roomArea = (HEIGHT * WIDTH) * roomPercent/100;
    /** The minimum width of a room */
    private static final int minWidth = 4;
    /** The maximum width of a room */
    private static final int maxWidth = WIDTH / 6;
    /** The minimum height of a room */
    private static final int minHeight = 4;
    /** The maximum height of a room */
    private static final int maxHeight = HEIGHT / 6;

    private static int[][] enemies;

    private static int enemyCount;

    /** Adds a random room to the given world */

    private static Room addRandomRoom(TETile[][] world, Random r, Room lastRoom) {
        int height = uniform(r, minHeight, maxHeight);
        int width = uniform(r, minWidth, maxWidth);
        int x = uniform(r, 1, WIDTH - width);
        int y = uniform(r, 1, HEIGHT - height);

        if (enemyCount < 9) {
            enemies[enemyCount][0] = x+2;
            enemies[enemyCount][1] = y+2;
            enemyCount++;
        }

        return new Room(new Position(x, y), width, height, world, lastRoom);
    }

    public static void createWorld(TETile[][] world, Random r) {
        int currArea = 0;
        Room lastRoom = null;

        while (currArea < roomArea) {
            System.out.println(currArea);
            lastRoom = addRandomRoom(world, r, lastRoom);
            currArea += lastRoom.getArea();
        }
    }
    public static int startPlaying(TETile[][] world, TERenderer ter) {
        Scanner scan = new Scanner(System.in);
        int tempX = enemies[8][0];
        int tempY = enemies[8][1];

        for (int i = 0; i < 1000; i++) {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(100);
            }
            char ch = StdDraw.nextKeyTyped();

            world[tempX][tempY] = Tileset.FLOOR;
            if (ch == 'W' || ch == 'w') {
                if (world[tempX][tempY+1] == Tileset.TREE) {
                    return 1;
                }
                if (world[tempX][tempY+1] == Tileset.FLOOR) {
                    tempY++;
                }
            } else if (ch == 'a' || ch == 'A') {
                if (world[tempX-1][tempY] == Tileset.TREE) {
                    return 1;
                }
                if (world[tempX-1][tempY] == Tileset.FLOOR) {
                    tempX--;
                }

            } else if (ch == 'd' || ch == 'D') {
                if (world[tempX+1][tempY] == Tileset.TREE) {
                    return 1;
                }
                if (world[tempX+1][tempY] == Tileset.FLOOR) {
                    tempX++;
                }

            } else if (ch == 's' || ch == 'S') {
                if (world[tempX][tempY-1] == Tileset.TREE) {
                    return 1;
                }
                if (world[tempX][tempY-1] == Tileset.FLOOR) {
                    tempY--;
                }
            }
            world[tempX][tempY] = Tileset.SAND;
            for (int j=1; j<8; j+=2) {
                int[] enemyPos = enemies[j];
                world[enemyPos[0]][enemyPos[1]] = Tileset.FLOOR;
                int[] nextMove = getThePath(world, enemyPos[0], tempX, enemyPos[1], tempY);
                if (nextMove[0] == tempX && nextMove[1] == tempY) {
                    return 0;
                }
                enemies[j][0] = nextMove[0];
                enemies[j][1] = nextMove[1];
                world[enemyPos[0]][enemyPos[1]] = Tileset.FLOWER;
            }
            ter.renderFrame(world);
        }

        scan.close();

        return 1;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        enemies = new int[9][2];
        enemyCount = 0;

        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        createWorld(world, new Random());

        for (int i=1; i<8; i+=2) {
            int[] pos = enemies[i];
            world[pos[0]][pos[1]] = Tileset.FLOWER;
        }

        int[] pos = enemies[8];
        world[pos[0]][pos[1]] = Tileset.SAND;

        int[] end = enemies[0];
        world[end[0]][end[1]] = Tileset.TREE;

        ter.renderFrame(world);

        if (startPlaying(world, ter) == 1) {
            System.out.println("YOU WON !!");
        } else {
            System.out.println("GAME OVER !!");
        }
    }
}
