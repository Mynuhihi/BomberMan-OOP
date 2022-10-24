package uet.oop.bomberman.scene;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sounds.Sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class WinScene extends Scenes {
    private double scale = BombermanGame.STAGE_WIDTH / 256;
    private GraphicsContext gc;
    private Canvas canvas;
    private Timer timer = new Timer();
    private MediaPlayer mediaPlayer = Sound.winSound.getMediaPlayer();

    public WinScene(Group root) {
        super(root);

        canvas = new Canvas(256 * scale, 240 * scale);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        mediaPlayer.play();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mediaPlayer.stop();
                BombermanGame.setScene(new MenuScene(new Group()));
            }
        }, 14000);
    }

    @Override
    public void show() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < 15; i++) {
            gc.drawImage(Sprite.brick_exploded.getFxImage(), Sprite.SCALED_SIZE * i, BombermanGame.STAGE_HEIGHT * 4 / 5);
        }

        int y = 28;
        Scenes.renderTextXCenter(canvas, "CONGRATULATIONS", y, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "    YOU HAVE SUCCEED IN HELPING", 0, y + 26, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "  BOMBERMAN TO BECOME A HUMAN", 0, y + 42, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "  BEING", 0, y + 58, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "    MAYBE YOU CAN RECOGNIZE HIM", 0, y + 74, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "  IN ANOTHER HUDSON SOFT GAME", 0, y + 90, scale, Color.WHITE, Color.GRAY);
        Scenes.renderTextXCenter(canvas, "GOOD BYE", y + 116, scale, Color.WHITE, Color.GRAY);

        try {
            gc.setFont(Font.loadFont(new FileInputStream("res/font/Kongtext.ttf"), 8 * scale));
        } catch (FileNotFoundException e) {
            System.exit(1);
        }
    }
}
