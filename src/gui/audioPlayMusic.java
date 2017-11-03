package gui;

import java.io.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

/**
 *
 * @author Abdikhaliq Timer
 */
public class audioPlayMusic {
    private Clip clip;
    
    public  void startSong() {
       AudioInputStream audioIn = null;
        try {
            //open the sound file through an input stream
            File is = new File("backgroundMusic.wav");
            audioIn = AudioSystem.getAudioInputStream(is);
            clip = AudioSystem.getClip();
            //FloatControl gainControl =(FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            //gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            audioIn.close();
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(audioPlayMusic.class.getName()).log(Level.SEVERE, null, ex); 
        }
    }
    
    public void stopSong() {
        if(clip.isRunning()) {
            clip.stop();
        }
    }
    
    public static void main(String[] args) {
        audioPlayMusic test = new audioPlayMusic();
        test.startSong();
        
    }
}
