package byow.Core;

import java.util.ArrayList;

public class Hallway {
    // keeps track of random coordinates inside room
    public static ArrayList<Coordinate> entryPoints;
    private ArrayList<Room> rooms;
    public Hallway(ArrayList<Room> rooms) {
        this.rooms = rooms;
        entryPoints = generateEntryPoints();
    }
    // generate entry points that correspond to each room
    public ArrayList<Coordinate> generateEntryPoints() {
        ArrayList<Coordinate> entryPoints = new ArrayList<>();

        for (Room room : rooms) {
            entryPoints.add(getEntryPoint(room));
        }
        return entryPoints;
    }
    // puts all possible inside coordinates in array
    // returns random coordinate
    public static Coordinate getEntryPoint(Room room) {
        Coordinate insideCoordinate = new Coordinate(room.getCoordinate().getX() + 1, room.getCoordinate().getY() + 1);

        int randomLength = WorldGenerator.random.nextInt(room.getLength() - 2);
        int randomWidth = WorldGenerator.random.nextInt(room.getWidth() - 2);

        return new Coordinate(insideCoordinate.getX() + randomLength,
                                                    insideCoordinate.getY() + randomWidth);
    }
}
