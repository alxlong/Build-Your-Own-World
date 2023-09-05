package byow.Core;

import javax.sound.sampled.*;
import java.io.*;

// Source: https://www.youtube.com/watch?v=3q4f6I5zi2w
// used to play sound from sound files
public class Music {
    public static Clip playSound(String fileName) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        File soundFile = new File(fileName);
        AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audio);

        clip.start();
        return clip;
    }
    // used to pause existing played sound
    public static void stopSound(Clip clip) {
        clip.close();
    }
}
