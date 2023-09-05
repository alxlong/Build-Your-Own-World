package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.math.BigInteger;
import java.util.Random;

public class WorldGenerator {
    // random object for random number generation
    // can replicate worlds w/ same seed
    public static Random random = new Random();
    // keeps track of tiles of the world
    public static TETile[][] tiles;
    // generates rooms
    private RoomGenerator rooms;
    // generates hallways
    private HallwayGenerator hallways;
    public static LanternGenerator lanterns;
    public static Avatar avatar;
    // takes seed as input, outputs world
    public WorldGenerator(String seed) {
        // Source: geeksforgeeks.org/biginteger-class-in-java/
        //         https://www.geeksforgeeks.org/java-string-compareto-method-with-examples/
        BigInteger comparator = new BigInteger(seed);

        // if seed is greater than largest possible long value, set it to long max value
        if (comparator.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            seed = String.valueOf(Long.MAX_VALUE);
        }

        random = new Random(Long.parseLong(seed));
        tiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        rooms = new RoomGenerator(tiles);
        initializeWorld();

        lanterns = new LanternGenerator(rooms.getRooms());

        if (Engine.getLampsSaveState()) {
            lanterns.turnOnAll();
        } else {
            lanterns.turnOffAll();
        }

        avatar = new Avatar(tiles, Tileset.AVATAR);
    }
    // initializes world as blank tiles
    // generate rooms first, hallways last
    public void initializeWorld() {
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        rooms.generateRooms();

        hallways = new HallwayGenerator(rooms.getRooms(), tiles);
        hallways.generateHallways();
    }
    // getter for tiles 2-d array
    public TETile[][] getTiles() {
        return tiles;
    }
}
