package Main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {

    private Clip backgroundMusicClip;
    private float musicVolume = 0.5f; // 0.0 to 1.0

    public AudioManager() {
    }

    /**
     * Load and play background music on loop
     * Supports WAV, AIFF, and AU formats natively
     */
    public void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.out.println("Audio file not found: " + filePath);
                return;
            }

            // Try using AudioInputStream; if MP3, it may fail gracefully
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            if (audioStream == null) {
                System.out.println("Unsupported audio format: " + filePath);
                return;
            }

            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioStream);

            // Set volume
            if (backgroundMusicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(musicVolume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }

            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusicClip.start();
            System.out.println("Background music started: " + filePath);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio format not supported by Java's built-in decoder: " + filePath + ". Please convert to WAV or AIFF format.");
        } catch (IOException | LineUnavailableException e) {
            System.err.println("Failed to play background music: " + e.getMessage());
        }
    }

    /**
     * Stop and close background music
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }
    }

    /**
     * Play a one-shot sound effect (non-looping)
     * Supports WAV, AIFF, and AU formats natively
     */
    public void playSoundEffect(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.out.println("Sound effect not found: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            if (audioStream == null) {
                System.out.println("Unsupported audio format: " + filePath);
                return;
            }

            Clip sfxClip = AudioSystem.getClip();
            sfxClip.open(audioStream);
            sfxClip.start();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio format not supported: " + filePath + ". Please convert to WAV or AIFF format.");
        } catch (IOException | LineUnavailableException e) {
            System.err.println("Failed to play sound effect: " + e.getMessage());
        }
    }

    /**
     * Set background music volume (0.0 to 1.0)
     */
    public void setMusicVolume(float volume) {
        if (volume < 0.0f) volume = 0.0f;
        if (volume > 1.0f) volume = 1.0f;
        musicVolume = volume;

        if (backgroundMusicClip != null && backgroundMusicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(musicVolume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
}