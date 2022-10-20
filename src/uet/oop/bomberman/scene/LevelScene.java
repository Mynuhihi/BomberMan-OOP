package uet.oop.bomberman.scene;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LevelScene extends Scenes {
    private double scale = BombermanGame.STAGE_WIDTH / 256;
    private GraphicsContext gc;
    private Canvas canvas;
    private int level;
    private int count = 0;

    public LevelScene(Group root, int level) {
        super(root);
        this.level = level;

        canvas = new Canvas(256 * scale, 240 * scale);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
    }

    @Override
    public void show() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.rgb(0, 0, 0));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Scenes.renderTextYCenter(canvas, String.format("STAGE %2d", level), 80, scale, Color.WHITE, Color.GRAY);

        try {
            gc.setFont(Font.loadFont(new FileInputStream("res/font/Kongtext.ttf"), 8 * scale));
        } catch (FileNotFoundException e) {
            System.exit(1);
        }

        count++;
        if (count == 180) {
            BombermanGame.setScene(new GameScene(new Group()));
        }
    }
}
