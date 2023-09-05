package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.FileWriter;
import java.io.IOException;

public class Lantern {
    private Coordinate position;
    private Room room;
    private final int RADIUS_1 = 1;
    private final int RADIUS_2 = 2;
    private final int RADIUS_3 = 3;
    private boolean switchColor;
    public Lantern(Room room) {
        switchColor = false;
        this.room = room;
        position = setPosition(room);
    }
    // ensures that lantern is generated in the bounds of a random room
    public Coordinate setPosition(Room room) {
        Coordinate lampPosition;
        Coordinate insideCoordinate = new Coordinate(room.getCoordinate().getX() + 1, room.getCoordinate().getY() + 1);

        int randomLength = WorldGenerator.random.nextInt(room.getLength() - 2);
        int randomWidth = WorldGenerator.random.nextInt(room.getWidth() - 2);

        lampPosition = new Coordinate(insideCoordinate.getX() + randomLength,
                                    insideCoordinate.getY() + randomWidth);

        return lampPosition;
    }
    // updates lantern tile and adjacent tiles to be lit
    public void turnOn() {
        if (switchColor) {
            WorldGenerator.tiles[position.getX()][position.getY()] = Tileset.LANTERN_SWITCH;
            lightPerimeter(Tileset.DIM1_SWITCH, RADIUS_1);
            lightPerimeter(Tileset.DIM2_SWITCH,RADIUS_2);
            lightPerimeter(Tileset.DIM3_SWITCH,RADIUS_3);
        } else {
            WorldGenerator.tiles[position.getX()][position.getY()] = Tileset.LANTERN;
            lightPerimeter(Tileset.DIM1, RADIUS_1);
            lightPerimeter(Tileset.DIM2,RADIUS_2);
            lightPerimeter(Tileset.DIM3,RADIUS_3);
        }
    }
    // updates lantern tile and adjacent tiles to be off
    public void turnOff() {
        WorldGenerator.tiles[position.getX()][position.getY()] = Tileset.OFF_LANTERN;
        lightPerimeter(Tileset.FLOOR, RADIUS_1);
        lightPerimeter(Tileset.FLOOR,RADIUS_2);
        lightPerimeter(Tileset.FLOOR,RADIUS_3);
    }
    // used to switch color of light when avatar traverses on top of a lantern
    public void switchLight() {
        this.switchColor = true;
        turnOn();
        saveLantern();
    }
    // constructs the perimeter of the light with each
    // tile reflecting its corresponding dimness
    public void lightPerimeter(TETile tile, int radius) {
        //get starting coordinate
        Coordinate start = new Coordinate(position.getX() - radius, position.getY() - radius);

        lightRight(start, radius * 2 + 1, tile);
        lightUp(start, radius * 2 + 1, tile);
        lightLeft(start, radius * 2 + 1, tile);
        lightDown(start, radius * 2 + 1, tile);
    }
    // following methods used to construct light perimeter
    public void lightRight(Coordinate coordinate, int distance, TETile tile) {
        for (int i = 0; i < distance - 1; i++) {
            if (checkRoomBounds(coordinate, room)) {
                WorldGenerator.tiles[coordinate.getX()][coordinate.getY()] = tile;
            }
            coordinate.setX(coordinate.getX() + 1);
        }
    }
    public void lightUp(Coordinate coordinate, int distance, TETile tile) {
        for (int i = 0; i < distance - 1; i++) {
            if (checkRoomBounds(coordinate, room)) {
                WorldGenerator.tiles[coordinate.getX()][coordinate.getY()] = tile;
            }
            coordinate.setY(coordinate.getY() + 1);
        }
    }
    public void lightLeft(Coordinate coordinate, int distance, TETile tile) {
        for (int i = 0; i < distance - 1; i++) {
            if (checkRoomBounds(coordinate, room)) {
                WorldGenerator.tiles[coordinate.getX()][coordinate.getY()] = tile;
            }
            coordinate.setX(coordinate.getX() - 1);
        }
    }
    public void lightDown(Coordinate coordinate, int distance, TETile tile) {
        for (int i = 0; i < distance - 1; i++) {
            if (checkRoomBounds(coordinate, room)) {
                WorldGenerator.tiles[coordinate.getX()][coordinate.getY()] = tile;
            }
            coordinate.setY(coordinate.getY() - 1);
        }
    }
    // used to make sure lantern is in bounds of the room randomly chosen to be generated in
    public static boolean checkRoomBounds(Coordinate coordinate, Room room) {
        Coordinate roomCoordinate = room.getCoordinate();
        return coordinate.getX() > roomCoordinate.getX() && coordinate.getX() < roomCoordinate.getX() + room.getLength() - 1 &&
                coordinate.getY() > roomCoordinate.getY() && coordinate.getY() < roomCoordinate.getY() + room.getWidth() - 1;
    }
    public Coordinate getPosition() {
        return position;
    }
    public Room getRoom() {
        return  room;
    }
    public boolean isColorSwitched() {
        return switchColor;
    }
    public void switchOff() {
        switchColor = false;
    }
    // Sources: https://www.youtube.com/watch?v=kjzmaJPoaNc
    //          https://www.geeksforgeeks.org/filewriter-class-in-java/
    // saves lantern to file so that if traversed, the color remains switched when a
    // world is saved and loaded
    public void saveLantern() {
        try {
            FileWriter fileWriter = new FileWriter("LanternSave.txt", false);
            fileWriter.write(Integer.toString(position.getX()));
            fileWriter.write('\n');
            fileWriter.write(Integer.toString(position.getY()));
            fileWriter.close();
        }
        catch (IOException exception) {
            exception.getStackTrace();
        }
    }
}
