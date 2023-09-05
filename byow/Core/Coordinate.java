package byow.Core;

import java.util.ArrayList;

public class Coordinate {
    // x coordinate
    private int x;
    // y coordinate
    private int y;
    private ArrayList<Coordinate> surroundings;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    //Sources: https://www.geeksforgeeks.org/overriding-equals-method-in-java/
    //         https://cs61b-2.gitbook.io/cs61b-textbook/12.-exceptions-iterators-object-methods/12.4-object-methods
    // compares x and y of two coordinates
    @Override
    public boolean equals(Object object) {
        Coordinate coordinate = (Coordinate) object;

        return coordinate.getX() == this.getX() && coordinate.getY() == this.getY();
    }
}
