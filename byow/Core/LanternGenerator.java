package byow.Core;

import byow.TileEngine.Tileset;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// used initialize lanterns into random rooms in the world
public class LanternGenerator {
    public static ArrayList<Lantern> lanterns;
    private final int LANTERN_NUMBER = 5;
    public boolean on = true;
    public LanternGenerator(ArrayList<Room> rooms) {
        lanterns = new ArrayList<>();

        for (int index : randomRoom(rooms)) {
            Lantern lantern = new Lantern(rooms.get(index));
            lanterns.add(lantern);
        }
    }
    // turns on all lanterns
    public void turnOnAll() {
        for (Lantern lantern : lanterns) {
            lantern.turnOn();
        }
        checkAvatarOn();
        on = true;
    }
    // turns off all lanterns
    public void turnOffAll() {
        for (Lantern lantern : lanterns) {
            lantern.turnOff();
        }
        checkAvatarOff();
        on = false;
    }
    // returns a LANTERN_NUMBER of random room from the world
    // which will contain a lantern
    public ArrayList<Integer> randomRoom(ArrayList<Room> rooms) {
        ArrayList<Integer> roomIndices = new ArrayList<>();

        while (roomIndices.size() != LANTERN_NUMBER) {
            int randomIndex = WorldGenerator.random.nextInt(rooms.size());
            if (roomIndices.contains(randomIndex)) {
                continue;
            }

            roomIndices.add(randomIndex);
        }

        return roomIndices;
    }
    // if avatar is within the proximity of a lamp,
    // and lamp is turned on, change avatar to match corresponding light level
    public void checkAvatarOn() {
        for (Lantern lantern : lanterns) {
            if (WorldGenerator.avatar == null) {
                return;
            }
            // reuse calculateDistance from HallwayGenerator
            int distance = (int) HallwayGenerator.calculateDistance(WorldGenerator.avatar.getPosition(), lantern.getPosition());

            int avatarX = WorldGenerator.avatar.getPosition().getX();
            int avatarY = WorldGenerator.avatar.getPosition().getY();
            Coordinate avatarCoordinate = WorldGenerator.avatar.getPosition();

            int lanternX = lantern.getPosition().getX();
            int lanternY = lantern.getPosition().getY();

            if (distance == 0 && Lantern.checkRoomBounds(avatarCoordinate, lantern.getRoom())) {
                    WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR_LANTERN_SWITCH;
                    WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR_LANTERN_SWITCH);

            } else if (distance == 1 && Lantern.checkRoomBounds(avatarCoordinate, lantern.getRoom())) {
                if (WorldGenerator.tiles[lanternX][lanternY].equals(Tileset.LANTERN_SWITCH)) {
                    WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR_DIM1_SWITCH;
                    WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR_DIM1_SWITCH);
                } else {
                    WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR_DIM1;
                    WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR_DIM1);
                }

            } else if (distance == 2 && Lantern.checkRoomBounds(avatarCoordinate, lantern.getRoom())) {
                if (WorldGenerator.tiles[lanternX][lanternY].equals(Tileset.LANTERN_SWITCH)) {
                    WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR_DIM2_SWITCH;
                    WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR_DIM2_SWITCH);
                } else {
                    WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR_DIM2;
                    WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR_DIM2);
                }

            } else if (distance == 3 && Lantern.checkRoomBounds(avatarCoordinate, lantern.getRoom())) {
                if (WorldGenerator.tiles[lanternX][lanternY].equals(Tileset.LANTERN_SWITCH)) {
                    WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR_DIM3_SWITCH;
                    WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR_DIM3_SWITCH);
                } else {
                    WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR_DIM3;
                    WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR_DIM3);
                }
            }
        }
    }
    // if avatar is within the proximity of a lamp,
    // and lamp is turned off, change avatar to match corresponding light level
    public void checkAvatarOff() {
        if (WorldGenerator.avatar == null) {
            return;
        }

        int avatarX = WorldGenerator.avatar.getPosition().getX();
        int avatarY = WorldGenerator.avatar.getPosition().getY();

        if (onLantern()) {
            WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR_LANTERN_OFF;
            WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR_LANTERN_OFF);
        } else {
            WorldGenerator.tiles[avatarX][avatarY] = Tileset.AVATAR;
            WorldGenerator.avatar.setAvatarTile(Tileset.AVATAR);
        }
    }
    // checks to see if avatar is on top of a lantern
    public boolean onLantern() {
        for (Lantern lantern : lanterns) {
            if (WorldGenerator.avatar.getPosition().equals(lantern.getPosition())) {
                return true;
            }
        }
        return false;
    }
    // ensures that only one lantern is switched at one time
    public static boolean alreadySwitched() {
        int switched = 0;

        for (Lantern lantern : lanterns) {
            if (lantern.isColorSwitched()) {
                switched++;
            }
        }

        if (switched >= 1) {
            return true;
        }
        return false;
    }
    // returns coordinate of the saved switched lantern
    // so that it can remain switched when world is saved and loaded
    public static Coordinate loadLantern() {
        String lanternX = "";
        String lanternY = "";

        try{
            FileReader fileReader = new FileReader("LanternSave.txt");

            int token = fileReader.read();

            // if no lantern is switched return dummy coordinate
            if (token == -1) {
                fileReader.close();
                return new Coordinate(-1, -1);
            }

            while (token != '\n') {
                lanternX += (char)token;
                token = fileReader.read();
            }

            token = fileReader.read();
            while (token != -1) {
                lanternY += (char)token;
                token = fileReader.read();
            }

            fileReader.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return new Coordinate(Integer.parseInt(lanternX), Integer.parseInt(lanternY));
    }
}
