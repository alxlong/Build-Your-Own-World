package byow.Core;

public class Room {
    private final int MIN_LENGTH = 4;
    private final int MIN_WIDTH = 4;
    private final int MAX_LENGTH = 10;
    private final int MAX_WIDTH = 10;
    private int length;
    private int width;
    private Coordinate coordinate;
    public Room() {
        this.length = randomizeLength();
        this.width = randomizeWidth();
        this.coordinate = new Coordinate(0, 0);
    }
    public void randomizeCoordinate() {
        int randomX = WorldGenerator.random.nextInt(Engine.WIDTH - MAX_LENGTH);

        // ensures that any room not surpass y = 27 (y bound is 29), so that
        // a 1 tile space above the game is reserved for the HUD (@ y = 29) and another 1 tile space
        // reserved for the divider (@ y = 28)
        int randomY = WorldGenerator.random.nextInt(Engine.HEIGHT - MAX_WIDTH - 2);

        this.coordinate = new Coordinate(randomX, randomY);
    }
    public int randomizeWidth() {
        return WorldGenerator.random.nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH;
    }
    public int randomizeLength() {
        return WorldGenerator.random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;
    }
    public int getLength() {
        return length;
    }
    public int getWidth() {
        return width;
    }
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
