package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int HUD_FRAME_PAUSE = 10;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Menu menu = new Menu();
        menu.drawMenu();
        Clip menuSound = Music.playSound("Music/Subwoofer Lullaby.wav");

        // initialize world
        TETile[][] finalWorldFrame = new TETile[Engine.WIDTH][Engine.HEIGHT];

        // keeps track of all user inputs for a single session
        String userInput = "";

        while (true) {
            // stores character user has entered, appended to other tokens to keep
            // track of user inputs
            char token = 0;
            Coordinate mouse = new Coordinate(0,0);

            // await next key press or mouse click
            while (StdDraw.hasNextKeyTyped() || StdDraw.isMousePressed()) {
                if (StdDraw.hasNextKeyTyped()) {
                    token = menu.promptUserInput();
                }
                if (StdDraw.isMousePressed()) {
                    mouse = menu.promptMouseInput();
                }
            }

            // if N is pressed, erase current file, prompt user, and create new world
            // also plays sound when selected and when new world is created
            if (token == 'N' || menu.isInMouseRange(mouse, menu.getNRange())) {
                Menu.clearSave();
                Music.stopSound(menuSound);
                Music.playSound("Music/Select.wav");

                // accounts for both mouse and keyboard inputs
                if (menu.isInMouseRange(mouse, menu.getNRange())) {
                    userInput = 'N' + menu.handleN();
                } else {
                    userInput = token + menu.handleN();
                }

                WorldGenerator newWorld = new WorldGenerator(userInput.substring(1, userInput.length() - 1));
                finalWorldFrame = newWorld.getTiles();
                ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                Music.playSound("Music/LoadWorld.wav");

                // loads world from existing save file through interactWithInputString
                // if no save file exit the program
            } else if (token == 'L' || menu.isInMouseRange(mouse, menu.getLRange())) {
                Music.stopSound(menuSound);
                Music.playSound("Music/Select.wav");

                if (menu.isInMouseRange(mouse, menu.getLRange())) {
                    userInput += "L" ;
                } else {
                    userInput += token;
                }

                if (Menu.load().length() == 0) {
                    System.exit(0);
                }

                finalWorldFrame = interactWithInputString(userInput);
                ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                Music.playSound("Music/LoadWorld.wav");

                // quits in menu
                // if preceded by ':' save and quit the world
                // otherwise, if world is loaded pressing "Q" will not quit the program
            } else if (token == 'Q' || menu.isInMouseRange(mouse, menu.getQRange())) {
                if (userInput.length() == 0) {
                    System.exit(0);
                } else if (userInput.toCharArray()[userInput.length() - 1] == ':') {
                    userInput += token;
                    Menu.save(userInput);
                    System.exit(0);
                }

            } else if (token == ':') {
                userInput += token;

                // movement keys for avatar
                // plays sound for each movement
            } else if (token == 'W') {
                userInput += token;
                WorldGenerator.avatar.moveUp();
                Music.playSound("Music/Landing.wav");

            } else if (token == 'A') {
                userInput += token;
                WorldGenerator.avatar.moveLeft();
                Music.playSound("Music/Landing.wav");

            } else if (token == 'S') {
                userInput += token;
                WorldGenerator.avatar.moveDown();
                Music.playSound("Music/Landing.wav");

            } else if (token == 'D') {
                userInput += token;
                WorldGenerator.avatar.moveRight();
                Music.playSound("Music/Landing.wav");

            } else if (token == 'F') {
                if (WorldGenerator.lanterns.on) {
                    userInput += token;
                    WorldGenerator.lanterns.turnOffAll();
                    Music.playSound("Music/LanternOff.wav");
                } else {
                    userInput += token;
                    WorldGenerator.lanterns.turnOnAll();
                    Music.playSound("Music/LanternOn.wav");
                }
            } else {
                continue;
            }

            // initialize HUD
            HUD hud = new HUD(finalWorldFrame);

            // displays loaded/generated/updated world resulted from each key press / mouse click
            while (!StdDraw.hasNextKeyTyped()) {
                hud.renderHUD();
                // pause to eliminate flickering
                StdDraw.pause(HUD_FRAME_PAUSE);
                ter.renderFrame(finalWorldFrame);
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    // when provided with a string of valid input chars, generates a world to
    // reflect said inputs
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        String seed = null;
        String movements = null;

        if (Character.toUpperCase(input.toCharArray()[0]) == 'L') {
            String save = Menu.load();
            String updatedSave = save + input;

            seed = getSeed(updatedSave);
            movements = getMovements(updatedSave);
        } else if (Character.toUpperCase(input.toCharArray()[0]) == 'N') {
            Menu.clearSave();
            seed = getSeed(input);
            movements = getMovements(input);
        }

        WorldGenerator newWorld = new WorldGenerator(seed);

        for (char move : movements.toCharArray()) {
            move = Character.toUpperCase(move);
            if (move == 'W') {
                WorldGenerator.avatar.moveUp();
            } else if (move == 'A') {
                WorldGenerator.avatar.moveLeft();
            } else if (move == 'S') {
                WorldGenerator.avatar.moveDown();
            } else if (move == 'D') {
                WorldGenerator.avatar.moveRight();
            }
        }

        char[] tokenArray = input.toCharArray();
        // if ':Q' is the last token, save into file
        if (input.length() > 1 && tokenArray[tokenArray.length - 2] == ':') {
            Menu.save(input);
        }

        return newWorld.getTiles();
    }
    // parses save file to extract the seed (numeric input) for world generation
    public String getSeed(String userInput) {
        String seed = "";

        for (char token : userInput.toCharArray()) {
            if (Character.isDigit(token)) {
                seed += token;
            }
        }

        return seed;
    }
    // parses save file to extract the movements (W, A, S, D) for world generation
    public String getMovements(String userInput) {
        String movements = "";

        for (char token : userInput.toCharArray()) {
            token = Character.toUpperCase(token);
            if (token == 'W' || token == 'A' || token == 'S' || token == 'D') {
                movements += token;
            }
        }

        // if userInput started with N, ignore the first character which will always be
        // the 'S' that marks the end of seed generation (should NOT be registered as a movement)
        if (Character.toUpperCase(userInput.toCharArray()[0]) == 'N') {
            movements = movements.substring(1, movements.length());
        }

        return movements;
    }
    // checks the last state of the lamps
    // if even number of F's in save file, lamps are on
    // if odd number of F's in save file, lamps are off
    public static boolean getLampsSaveState() {
        String saveState = Menu.load();
        int fCounter = 0;

        for (char input : saveState.toCharArray()) {
            if (input == 'F') {
                fCounter++;
            }
        }

        Coordinate switchedLantern = LanternGenerator.loadLantern();

        // if no lantern is switched, proceed
        if (switchedLantern.getX() == -1) {
            return fCounter % 2 == 0;
        }

        for (Lantern lantern : LanternGenerator.lanterns){
            if (lantern.getPosition().equals(switchedLantern)) {
                lantern.switchLight();
                Avatar.previousLantern = lantern;
            }
        }

        return fCounter % 2 == 0;
    }
}
