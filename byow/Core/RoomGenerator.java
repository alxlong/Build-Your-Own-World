package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;

public class RoomGenerator {
    private final int minRooms = 10;
    private final int maxRooms = 15;
    private int numRooms;
    private TETile[][] tiles;
    private ArrayList<Room> rooms;
    public RoomGenerator (TETile[][] tiles) {
        this.numRooms = WorldGenerator.random.nextInt(maxRooms - minRooms) + minRooms;
        this.tiles = tiles;
        rooms = new ArrayList<>();
    }
    // generates numRoom rooms and constructs them
    public void generateRooms() {
        for (int i = 0; i < numRooms; i++) {
            Room newRoom = new Room();
            rooms.add(newRoom);
        }
        buildRooms();
    }
    public void buildRooms() {
        for (int i = 0; i < rooms.size(); i++) {
            Room currentRoom = rooms.get(i);

            // keeps randomzing coordinates until valid one is created (no collisions and intersections)
            do {
                currentRoom.randomizeCoordinate();
            } while (checkCollisions(currentRoom) || checkIntersection(currentRoom));

            int roomLength = currentRoom.getLength();
            int roomWidth = currentRoom.getWidth();

            // build room in order of right up left down
            tiles[currentRoom.getCoordinate().getX()][currentRoom.getCoordinate().getY()] = Tileset.WALL;
            buildRight(currentRoom, roomLength);
            buildUp(currentRoom, roomWidth);
            buildLeft(currentRoom, roomLength);
            buildDown(currentRoom, roomWidth);

            fillTile(currentRoom);
        }
    }
    public boolean checkCollisions(Room room) {
        // check possible length for collisions
        for (int i = 0; i < room.getLength(); i++) {
            if (tiles[room.getCoordinate().getX() + i][room.getCoordinate().getY()].equals(Tileset.WALL)
                    || tiles[room.getCoordinate().getX() + i][room.getCoordinate().getY() + room.getWidth() - 1].equals(Tileset.WALL)) {
                return true;
            }
        }
        //check possible width for collisions
        for (int i = 0; i < room.getWidth(); i++) {
            if (tiles[room.getCoordinate().getX()][room.getCoordinate().getY() + i].equals(Tileset.WALL)
                    || tiles[room.getCoordinate().getX() + room.getLength() - 1][room.getCoordinate().getY() + i].equals(Tileset.WALL)) {
                return true;
            }
        }

        return false;
    }
    public boolean checkIntersection(Room possibleRoom) {
        //check if possibleRoom is inside a room or if room is inside possibleRoom
        for (Room room : rooms) {
            if (isInside(possibleRoom, room) || isInside(room, possibleRoom)) {
                return true;
            }
        }
        return false;
    }
    public boolean isInside(Room innerRoom, Room outerRoom) {
        // insideCoordinate is the bottom left coordinate of the inside of the room
        Coordinate insideCoordinate = new Coordinate(outerRoom.getCoordinate().getX() + 1, outerRoom.getCoordinate().getY() + 1);
        // go through inside of outerRoom
        // if same coordinate as innerRoom return true
        for (int i = 0; i < outerRoom.getWidth() - 2; i++) {
            for (int j = 0; j < outerRoom.getLength() - 2; j++) {
                Coordinate currentCoordinate = new Coordinate(insideCoordinate.getX() + j, insideCoordinate.getY() + i);
                if (currentCoordinate.equals(innerRoom.getCoordinate())) {
                    return true;
                }
            }
        }
        return false;
    }
    // build the right, left, up, and down walls of the room respectively
    public void buildRight(Room room, int distance) {
        int newX = room.getCoordinate().getX();
        for (int i = 0; i < distance - 1; i++) {
            newX++;
            tiles[newX][room.getCoordinate().getY()] = Tileset.WALL;
        }
        room.getCoordinate().setX(newX);
    }
    public void buildLeft(Room room, int distance) {
        int newX = room.getCoordinate().getX();
        for (int i = 0; i < distance - 1; i++) {
            newX--;
            tiles[newX][room.getCoordinate().getY()] = Tileset.WALL;
        }
        room.getCoordinate().setX(newX);
    }
    public void buildUp(Room room, int distance) {
        int newY = room.getCoordinate().getY();
        for (int i = 0; i < distance - 1; i++) {
            newY++;
            tiles[room.getCoordinate().getX()][newY] = Tileset.WALL;
        }
        room.getCoordinate().setY(newY);
    }

    public void buildDown(Room room, int distance) {
        int newY = room.getCoordinate().getY();
        for (int i = 0; i < distance - 1; i++) {
            newY--;
            tiles[room.getCoordinate().getX()][newY] = Tileset.WALL;
        }
        room.getCoordinate().setY(newY);
    }
    // fills inside of room will FLOOR
    public void fillTile(Room room) {
        Coordinate insideCoordinate = new Coordinate(room.getCoordinate().getX() + 1, room.getCoordinate().getY() + 1);

        for (int i = 0; i < room.getWidth() - 2; i++) {
            for (int j = 0; j < room.getLength() - 2; j++) {
                tiles[insideCoordinate.getX() + j][insideCoordinate.getY() + i] = Tileset.FLOOR;
            }
        }
    }
    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
