package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class HallwayGenerator {
    private Hallway hallway;
    private ArrayList<Coordinate> coordinates;
    // path keeps track of order we want to connect the rooms
    private Queue<Coordinate> path;
    private TETile[][] tiles;
    public HallwayGenerator(ArrayList<Room> rooms, TETile[][] tiles) {
        this.tiles = tiles;
        hallway = new Hallway(rooms);
        coordinates = Hallway.entryPoints;
        path = new Queue<>();
    }
    public void generateHallways() {
        createPaths();
        buildPaths();
    }
    // create paths/order of coordinates/rooms to connect
    public void createPaths() {
        Queue<Coordinate> tempQueue = new Queue<>();

        for (Coordinate coordinate : coordinates) {
            tempQueue.enqueue(coordinate);
        }

        while (!tempQueue.isEmpty()) {
            Coordinate current = tempQueue.dequeue();
            path.enqueue(current);
            Coordinate next = findNearestRoom(current, tempQueue);
            changePriority(tempQueue, next);
        }
    }
    // constructs path based on random room and its adjacent room
    // once path is created go to said adjacent room and repeat the
    // process until all rooms are connected
    public void buildPaths() {
        while (path.size() > 1) {
            Coordinate coordinate = path.dequeue();
            connectPoints(coordinate, path.peek(), Tileset.FLOOR);
        }
    }
    // constructs the walls around the path based on direction of expansion (x or y)
    public void buildHallway(TETile tile, int x, int y, String direction) {
        if (!tiles[x][y].equals(Tileset.FLOOR)) {
            tiles[x][y] = tile;
        }

        if (direction.equals("x")) {
            buildWall(x, y + 1);
            buildWall(x, y - 1);
        }
        if (direction.equals("y")) {
            buildWall(x + 1, y);
            buildWall(x - 1, y);
        }
    }
    // ensures that walls are only created on NOTHING tiles
    // so no walls will be in rooms, as they are composed of floor tiles
    public void buildWall(int x, int y) {
        if (tiles[x][y].equals(Tileset.NOTHING)) {
            tiles[x][y] = Tileset.WALL;
        }
    }
    // connects points in x direction, then y direction
    public void connectPoints(Coordinate pointA, Coordinate pointB, TETile tile) {
        connect(pointA, pointB, tile, "x");
        buildCorners(pointA, pointB);
        connect(pointA, pointB, tile, "y");
    }
    // ensures proper generation of corners for turning hallways
    public void buildCorners(Coordinate pointA, Coordinate pointB) {
        if (pointA.getY() > pointB.getY()) {
            buildWall(pointA.getX() - 1, pointA.getY());
            buildWall(pointA.getX() - 1, pointA.getY() + 1);
            buildWall(pointA.getX() + 1, pointA.getY());
            buildWall(pointA.getX() + 1, pointA.getY() + 1);
        }

        if (pointA.getY() < pointB.getY()) {
            buildWall(pointA.getX() - 1, pointA.getY());
            buildWall(pointA.getX() - 1, pointA.getY() - 1);
            buildWall(pointA.getX() + 1, pointA.getY());
            buildWall(pointA.getX() + 1, pointA.getY() - 1);
        }
    }
    // connects coordinate on x direction or y direction and connects them via
    // the tile passed in the argument
    public void connect(Coordinate pointA, Coordinate pointB, TETile tile, String direction) {
        int dimensionA = 0;
        int dimensionB = 0;

        if (direction.equals("x")) {
            dimensionA = pointA.getX();
            dimensionB = pointB.getX();
        } else if (direction.equals("y")) {
            dimensionA = pointA.getY();
            dimensionB = pointB.getY();
        }

        while (dimensionA != dimensionB) {
            if (dimensionA > dimensionB) {
                if (direction.equals("x")) {
                    buildHallway(tile, dimensionA - 1, pointA.getY(), "x");
                    pointA.setX(dimensionA - 1);
                } else if (direction.equals("y")) {
                    buildHallway(tile, pointA.getX(), dimensionA - 1, "y");
                    pointA.setY(pointA.getY() - 1);
                }
                dimensionA--;
            } else if (dimensionA < dimensionB) {
                if (direction.equals("x")) {
                    buildHallway(tile, dimensionA + 1, pointA.getY(), "x");
                    pointA.setX(dimensionA + 1);
                } else if (direction.equals("y")) {
                    buildHallway(tile, pointA.getX(), dimensionA + 1, "y");
                    pointA.setY(pointA.getY() + 1);
                }
                dimensionA++;
            }
        }
    }
    // calculates distance between two coordinates, used as the mathematical
    // determination for queue order, which is the order in which rooms are built
    public static double calculateDistance(Coordinate to, Coordinate from) {
        return Math.sqrt(Math.abs(to.getY() - from.getY()) * Math.abs(to.getY() - from.getY()) +
                        Math.abs(to.getX() - from.getX()) * Math.abs(to.getX() - from.getX()));
    }
    // finds room w/ coordinate closest to currentCoordinate/room
    // returns most adjacent room
    public Coordinate findNearestRoom(Coordinate currentCoordinate, Queue<Coordinate> queue) {
        if (queue.isEmpty()) {
            return currentCoordinate;
        }
        double smallestDistance = Double.POSITIVE_INFINITY;
        Coordinate nearestRoom = new Coordinate(0,0);

        for (Coordinate coordinate : queue) {
            double currentDistance = calculateDistance(coordinate, currentCoordinate);
            if (currentDistance < smallestDistance) {
                smallestDistance = currentDistance;
                nearestRoom = coordinate;
            }
        }
        return nearestRoom;
    }
    // changes priority of Coordinate in queue
    // used to make sure that the room adjacent to the current room
    // is the next room to be connected
    public void changePriority(Queue<Coordinate> queue, Coordinate coordinate) {
        if (queue.isEmpty()) {
            return;
        }
        while (!queue.peek().equals(coordinate)) {
            queue.enqueue(queue.dequeue());
        }
    }
}
