package uet.oop.bomberman.sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Sound {
    public static Sound menuSound = new Sound("res/sound/Track 1.wav");
    public static Sound levelSound = new Sound("res/sound/Track 2.wav");
    public static Sound gameSound = new Sound("res/sound/Track 3.wav");
    public static Sound powerUpSound = new Sound("res/sound/Track 4.wav");
    public static Sound nextLevelSound = new Sound("res/sound/Track 5.wav");
    public static Sound bonusLevelSound = new Sound("res/sound/Track 6.wav");
    public static Sound specialSound = new Sound("res/sound/Track 7.wav");
    public static Sound winSound = new Sound("res/sound/Track 8.wav");
    public static Sound deadSound = new Sound("res/sound/Track 9.wav");
    public static Sound gameOverSound = new Sound("res/sound/Track 10.wav");

    public static Sound horizontalMoveSound = new Sound("res/sound/SFX 1.wav");
    public static Sound verticalMoveSound = new Sound("res/sound/SFX 2.wav");
    public static Sound putBombSound = new Sound("res/sound/SFX 3.wav");
    public static Sound exploreSound = new Sound("res/sound/SFX 4.wav");
    public static Sound getItemSound = new Sound("res/sound/SFX 5.wav");
    public static Sound killSound = new Sound("res/sound/SFX 6.wav");
    public static Sound killAllEnemySound = new Sound("res/sound/SFX 7.wav");

    private Media media;

    public Sound(String path) {
        File mediaFile = new File(path);
        media = new Media(mediaFile.toURI().toString());
    }

    public MediaPlayer getMediaPlayer() {
        return new MediaPlayer(media);
    }

    public static void playSound(MediaPlayer player, int millis) {
        player.play();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.stop();
            }
        }, millis);
    }

    public static void playSoundLoop(MediaPlayer player, int millis) {
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setStopTime(Duration.millis(millis));
        player.play();
    }
}
