package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;

public class Avatar {
    private Coordinate position;
    // keeps track of the Avatar's current tile ID
    private TETile avatarTile;
    // keeps track of the previous tile the avatar was; used for movement
    private TETile previousTile;
    // maps back to the TETile[][] representing the game/world
    private TETile[][] tiles;
    // keeps track of the previous lantern so that it will be
    // reverted to the original orange color once a new lantern is traversed
    public static Lantern previousLantern;
    public Avatar(TETile[][] tiles, TETile avatarTile) {
        this.tiles = tiles;
        this.avatarTile = avatarTile;
        position = setSpawnPoint(tiles);
    }
    // ensures that avatar is randomly generated within the bounds of a random room
    // also makes sure that if avatar is spawned near a light tile, it will match the
    // corresponding dimness
    // also makes sure that if it is spawned on top of a lantern it will be switched
    public Coordinate setSpawnPoint(TETile[][] tiles) {
        ArrayList<Coordinate> possibleSpawnPoints = Hallway.entryPoints;
        Coordinate spawn;

        int randomIndex = WorldGenerator.random.nextInt(possibleSpawnPoints.size());
        spawn = possibleSpawnPoints.get(randomIndex);

        int spawnX = spawn.getX();
        int spawnY = spawn.getY();

        if (tiles[spawnX][spawnY].equals(Tileset.DIM1)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR_DIM1;
            avatarTile = Tileset.AVATAR_DIM1;
            setPreviousTile();

        } else if (tiles[spawnX][spawnY].equals(Tileset.DIM2)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR_DIM2;
            avatarTile = Tileset.AVATAR_DIM2;
            setPreviousTile();

        } else if (tiles[spawnX][spawnY].equals(Tileset.DIM3)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR_DIM3;
            avatarTile = Tileset.AVATAR_DIM3;
            setPreviousTile();
        } else if (tiles[spawnX][spawnY].equals(Tileset.LANTERN)) {
            checkLantern(spawnX, spawnY);
            System.out.println(previousLantern.isColorSwitched());

            tiles[spawnX][spawnY] = Tileset.AVATAR_LANTERN_SWITCH;
            avatarTile = Tileset.AVATAR_LANTERN_SWITCH;
            setPreviousTile();

        } else if (tiles[spawnX][spawnY].equals(Tileset.OFF_LANTERN)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR_LANTERN_OFF;
            avatarTile = Tileset.AVATAR_LANTERN_OFF;
            setPreviousTile();

        } else if (tiles[spawnX][spawnY].equals(Tileset.FLOOR)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR;
            avatarTile = Tileset.AVATAR;
            setPreviousTile();

        } else if (tiles[spawnX][spawnY].equals(Tileset.DIM1_SWITCH)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR_DIM1_SWITCH;
            avatarTile = Tileset.AVATAR_DIM1_SWITCH;
            setPreviousTile();

        } else if (tiles[spawnX][spawnY].equals(Tileset.DIM2_SWITCH)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR_DIM2_SWITCH;
            avatarTile = Tileset.AVATAR_DIM2_SWITCH;
            setPreviousTile();

        } else if (tiles[spawnX][spawnY].equals(Tileset.DIM3_SWITCH)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR_DIM3_SWITCH;
            avatarTile = Tileset.AVATAR_DIM3_SWITCH;
            setPreviousTile();

        } else if (tiles[spawnX][spawnY].equals(Tileset.LANTERN_SWITCH)) {
            tiles[spawnX][spawnY] = Tileset.AVATAR_LANTERN_SWITCH;
            avatarTile = Tileset.AVATAR_LANTERN_SWITCH;
            setPreviousTile();
        }

        return spawn;
    }
    // movement functions; used to ensure that the avatar and the tiles it traverses are properly
    // updated
    public void moveUp() {
        int currentX = position.getX();
        int currentY = position.getY();

        if (!(tiles[currentX][currentY + 1].equals(Tileset.WALL))) {
            checkLantern(currentX, currentY + 1);
            setPreviousTile();
            tiles[currentX][currentY] = previousTile;

            setPreviousTile();
            position.setY(currentY + 1);
            changeAvatar(new Coordinate(currentX, currentY + 1));
            tiles[currentX][currentY + 1] = avatarTile;
        }
    }
    public void moveDown() {
        int currentX = position.getX();
        int currentY = position.getY();

        if (!(tiles[currentX][currentY - 1].equals(Tileset.WALL))) {
            checkLantern(currentX, currentY - 1);
            setPreviousTile();
            tiles[currentX][currentY] = previousTile;

            setPreviousTile();
            position.setY(currentY - 1);
            changeAvatar(new Coordinate(currentX, currentY - 1));
            tiles[currentX][currentY - 1] = avatarTile;
        }
    }
    public void moveLeft() {
        int currentX = position.getX();
        int currentY = position.getY();

        if (!(tiles[currentX - 1][currentY].equals(Tileset.WALL))) {
            checkLantern(currentX - 1, currentY);
            setPreviousTile();
            tiles[currentX][currentY] = previousTile;

            position.setX(currentX - 1);
            changeAvatar(new Coordinate(currentX - 1, currentY));
            tiles[currentX - 1][currentY] = avatarTile;
        }
    }
    public void moveRight() {
        int currentX = position.getX();
        int currentY = position.getY();

        if (!(tiles[currentX + 1][currentY].equals(Tileset.WALL))) {
            checkLantern(currentX + 1, currentY);
            setPreviousTile();
            tiles[currentX][currentY] = previousTile;

            position.setX(currentX + 1);
            changeAvatar(new Coordinate(currentX + 1, currentY));
            tiles[currentX + 1][currentY] = avatarTile;
        }
    }
    // checks if lantern is traversed (shares the same position as avatar)
    // if so, switch and make sure that the proper tiles are updated
    public void checkLantern(int x, int y) {
        Coordinate position = new Coordinate(x, y);

        if (tiles[x][y].equals(Tileset.LANTERN)) {
            if (LanternGenerator.alreadySwitched()) {
                previousLantern.switchOff();
                previousLantern.turnOn();
            }

            for (Lantern lantern : LanternGenerator.lanterns) {
                if (lantern.getPosition().equals(position)) {
                    previousLantern = lantern;

                    if (avatarTile.equals(Tileset.AVATAR_DIM1)) {
                        avatarTile = Tileset.AVATAR_DIM1_SWITCH;
                    }

                    lantern.switchLight();
                }
            }
        }
    }
    // updates the appearance of avatar if specific tiles are traversed
    public void changeAvatar(Coordinate nextCoordinate) {
        if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.DIM1)) {
            avatarTile = Tileset.AVATAR_DIM1;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.DIM2)) {
            avatarTile = Tileset.AVATAR_DIM2;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.DIM3)) {
            avatarTile = Tileset.AVATAR_DIM3;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.LANTERN)) {
            avatarTile = Tileset.AVATAR_LANTERN_SWITCH;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.FLOOR)) {
            avatarTile = Tileset.AVATAR;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.OFF_LANTERN)) {
            avatarTile = Tileset.AVATAR_LANTERN_OFF;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.DIM1_SWITCH)) {
            avatarTile = Tileset.AVATAR_DIM1_SWITCH;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.DIM2_SWITCH)) {
            avatarTile = Tileset.AVATAR_DIM2_SWITCH;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.DIM3_SWITCH)) {
            avatarTile = Tileset.AVATAR_DIM3_SWITCH;

        } else if (tiles[nextCoordinate.getX()][nextCoordinate.getY()].equals(Tileset.LANTERN_SWITCH)) {
            avatarTile = Tileset.AVATAR_LANTERN_SWITCH;
        }
    }
    // keeps track of previous tile to make sure that after it is traversed,
    // it will be properly reverted/updated
    public void setPreviousTile() {
        if (avatarTile.equals(Tileset.AVATAR_DIM1)) {
            previousTile = Tileset.DIM1;

        } else if (avatarTile.equals(Tileset.AVATAR_DIM2)) {
            previousTile = Tileset.DIM2;

        } else if (avatarTile.equals(Tileset.AVATAR_DIM3)) {
            previousTile = Tileset.DIM3;

        } else if (avatarTile.equals(Tileset.AVATAR)) {
            previousTile = Tileset.FLOOR;

        } else if (avatarTile.equals(Tileset.AVATAR_LANTERN_OFF)) {
            previousTile = Tileset.OFF_LANTERN;

        } else if (avatarTile.equals(Tileset.AVATAR_DIM1_SWITCH)) {
            previousTile = Tileset.DIM1_SWITCH;

        } else if (avatarTile.equals(Tileset.AVATAR_DIM2_SWITCH)) {
            previousTile = Tileset.DIM2_SWITCH;

        } else if (avatarTile.equals(Tileset.AVATAR_DIM3_SWITCH)) {
            previousTile = Tileset.DIM3_SWITCH;

        } else if (avatarTile.equals(Tileset.AVATAR_LANTERN_SWITCH)) {
            previousTile = Tileset.LANTERN_SWITCH;
        }
    }
    public Coordinate getPosition() {
        return position;
    }
    public void setAvatarTile(TETile tile) {
        avatarTile = tile;
    }

}
