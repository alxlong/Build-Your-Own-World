package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Date;

public class HUD {
    private TETile[][] tiles;
    public HUD(TETile[][] tiles) {
        this.tiles = tiles;
    }
    public void renderHUD() {
        // used to keep track of mouse movements
        // Math.min used to maneuver around original mouse initialization
        // in pixels instead of tiles, ensures that coordinate will not be out of bounds
        // Source: office hours
        int mouseX = (int) Math.min(StdDraw.mouseX(), Engine.WIDTH - 1);
        int mouseY = (int) Math.min(StdDraw.mouseY(), Engine.HEIGHT - 1);

        String description = tiles[mouseX][mouseY].description();

        // display HUD
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, 29, "Tile: " + description);
        StdDraw.textRight(79, 29, "Date/Time: " + new Date());
        StdDraw.line(0, Engine.HEIGHT - 2, Engine.WIDTH, Engine.HEIGHT - 2);

        StdDraw.show();
    }
}
