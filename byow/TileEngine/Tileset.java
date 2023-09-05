package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LANTERN = new TETile('❀', Color.yellow, new Color(143, 79, 17), "lantern");
    public static final TETile OFF_LANTERN = new TETile('❀', Color.white, Color.black, "lantern");
    public static final TETile DIM1 = new TETile('·', new Color(128, 192, 128), new Color(87, 50, 13),
            "floor");
    public static final TETile DIM2 = new TETile('·', new Color(128, 192, 128), new Color(59, 34, 9),
            "floor");
    public static final TETile DIM3 = new TETile('·', new Color(128, 192, 128), new Color(36, 21, 6),
            "floor");
    public static final TETile AVATAR_DIM1 = new TETile('@', Color.white, new Color(87, 50, 13), "you");
    public static final TETile AVATAR_DIM2 = new TETile('@', Color.white, new Color(59, 34, 9), "you");
    public static final TETile AVATAR_DIM3 = new TETile('@', Color.white, new Color(36, 21, 6), "you");
    public static final TETile LANTERN_SWITCH = new TETile('❀', Color.white, new Color(31, 72, 148), "lantern");
    public static final TETile DIM1_SWITCH = new TETile('·', new Color(128, 192, 128), new Color(20, 51, 107),
            "floor");
    public static final TETile DIM2_SWITCH = new TETile('·', new Color(128, 192, 128), new Color(15, 38, 79),
            "floor");
    public static final TETile DIM3_SWITCH = new TETile('·', new Color(128, 192, 128), new Color(8, 21, 43),
            "floor");
    public static final TETile AVATAR_DIM1_SWITCH = new TETile('@', Color.white, new Color(20, 51, 107), "you");
    public static final TETile AVATAR_DIM2_SWITCH = new TETile('@', Color.white, new Color(15, 38, 79), "you");
    public static final TETile AVATAR_DIM3_SWITCH = new TETile('@', Color.white, new Color(8, 21, 43), "you");
    public static final TETile AVATAR_LANTERN_SWITCH = new TETile('▲', Color.white, new Color(31, 72, 148), "you");
    public static final TETile AVATAR_LANTERN_OFF = new TETile('▲', Color.white, Color.black, "you");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
}


