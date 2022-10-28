package uet.oop.bomberman.scene;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.sounds.Sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class LevelScene extends Scenes {
    private double scale = BombermanGame.STAGE_WIDTH / 256;
    private GraphicsContext gc;
    private Canvas canvas;

    public LevelScene(Group root) {
        super(root);

        canvas = new Canvas(256 * scale, 240 * scale);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        MediaPlayer mediaPlayer = Sound.levelSound.getMediaPlayer();
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mediaPlayer.stop();
                BombermanGame.setScene(new GameScene(new Group()));
            }
        }, 3000);
    }

    @Override
    public void show() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Scenes.renderTextYCenter(canvas, String.format("STAGE %2d", BombermanGame.CURRENT_LEVEL), 80, scale, Color.WHITE, Color.GRAY);

        try {
            gc.setFont(Font.loadFont(new FileInputStream("res/font/Kongtext.ttf"), 8 * scale));
        } catch (FileNotFoundException e) {
            System.exit(1);
        }
    }
}
