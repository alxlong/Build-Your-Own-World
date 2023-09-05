package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Menu {
    private final int length = 700;
    private final int width = 700;

    // sets up bounds for menu
    // Source: Lab 13
    public Menu() {
        StdDraw.setCanvasSize(this.length, this.width);

        StdDraw.setXscale(0, this.length);
        StdDraw.setYscale(0, this.width);

        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }
    public void drawMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Papyrus", Font.PLAIN, 100);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.length / 2,(7 * this.width) / 8, "Project 3");

        Font fontSmall = new Font("Papyrus", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.length / 2, (this.width) / 2, "NEW GAME (N)");
        StdDraw.text(this.length / 2, ((this.width) / 2) - 40, "LOAD GAME (L)");
        StdDraw.text(this.length / 2, ((this.width) / 2) - 80, "QUIT (Q)");

        StdDraw.show();
    }
    // gets tile range of N,L, and Q options so that mouse clicks
    // will register the correct menu item
    ArrayList<Integer> getRange(Coordinate coordinate, String option) {
        ArrayList<Integer> ranges = new ArrayList<>();

        int length = option.length() * 30 / 2;
        int width = 30 / 2;

        int minX = coordinate.getX() - length;
        int maxX = coordinate.getX() + length;

        int minY = coordinate.getY() - width;
        int maxY = coordinate.getY() + width;

        ranges.add(minX);
        ranges.add(maxX);
        ranges.add(minY);
        ranges.add(maxY);

        return ranges;
    }
    public ArrayList<Integer> getNRange() {
        return getRange(new Coordinate(this.length / 2, this.width / 2), "NEW GAME (N)");
    }
    public ArrayList<Integer> getLRange() {
        return getRange(new Coordinate(this.length / 2, ((this.width) / 2) - 40), "LOAD GAME (L)");
    }
    public ArrayList<Integer> getQRange() {
        return getRange(new Coordinate(this.length / 2, ((this.width) / 2) - 80), "QUIT (Q)");
    }
    // returns if mouse position is in range of an option
    public boolean isInMouseRange(Coordinate coordinate, ArrayList<Integer> range) {
        int x = coordinate.getX();
        int y = coordinate.getY();

        return x >= range.get(0) && x <= range.get(1) && y >= range.get(2) && y <= range.get(3);
    }
    // returns postion/coordinate of mouse when clicked
    public Coordinate promptMouseInput() {
        Coordinate mousePosition;
        while (true) {
            if (StdDraw.isMousePressed()) {
                int mouseX = (int) Math.min(StdDraw.mouseX(), this.length - 1);
                int mouseY = (int) Math.min(StdDraw.mouseY(), this.width - 1);

                mousePosition = new Coordinate(mouseX, mouseY);
                break;
            }
        }
        return mousePosition;
    }
    public char promptUserInput() {
        char token;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                token = Character.toUpperCase(StdDraw.nextKeyTyped());
                break;
            }
        }

        return token;
    }
    // prompts user to enter seed
    public String promptSeed() {
        String userInput = "";
        char token = 0;

        while (token != 'S') {
            if (StdDraw.hasNextKeyTyped()) {
                token = Character.toUpperCase(StdDraw.nextKeyTyped());

                if (Character.isDigit(token) || token == 'S') {
                    userInput += token;
                    displayToScreen(userInput);
                }
            }
        }

        return userInput;
    }
    public void displayToScreen(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Papyrus", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.length / 2, this.width / 2, s);

        StdDraw.show();
    }
    // displays corresponding screen when N is inputted
    public String handleN() {
        displayToScreen("Enter A Seed! Press S when you're done!");

        return promptSeed();
    }
    // loads the string representing the saved world in "Save.txt"
    public static String load() {
        String save = "";

        try{
            FileReader fileReader = new FileReader("Save.txt");

            int token = fileReader.read();

            while (token != -1) {
                save += (char) token;
                token = fileReader.read();
            }
            fileReader.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return save;
    }
    // Sources: https://www.youtube.com/watch?v=kjzmaJPoaNc
    //          https://www.geeksforgeeks.org/filewriter-class-in-java/
    public static void save(String userInput) {
        try {
            FileWriter fileWriter = new FileWriter("Save.txt", true);
            fileWriter.write(userInput);
            fileWriter.close();
        }
        catch (IOException exception) {
            exception.getStackTrace();
        }
    }
    // clears the save file, used when "N" is pressed/new world is generated
    public static void clearSave() {
        try {
            FileWriter fileWriter = new FileWriter("Save.txt");
            fileWriter.write("");
            fileWriter.close();
        }
        catch (IOException exception) {
            exception.getStackTrace();
        }
    }
}
