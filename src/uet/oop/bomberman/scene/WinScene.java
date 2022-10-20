package uet.oop.bomberman.scene;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WinScene extends Scenes {
    private double scale = BombermanGame.STAGE_WIDTH / 256;
    private GraphicsContext gc;
    private Canvas canvas;

    public WinScene(Group root) {
        super(root);

        canvas = new Canvas(256 * scale, 240 * scale);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
    }

    @Override
    public void show() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.rgb(0, 0, 0));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < 15; i++) {
            gc.drawImage(Sprite.brick_exploded.getFxImage(), Sprite.SCALED_SIZE * i, BombermanGame.STAGE_HEIGHT * 4 / 5);
        }

        int y = 34;
        Scenes.renderTextXCenter(canvas, "CONGRATULATIONS", y, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "    YOU HAVE SUCCEED IN HELPING", 0, y + 26, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "  BOMBERMAN TO BECOME A HUMAN", 0, y + 42, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "  BEING", 0, y + 58, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "    MAYBE YOU CAN RECOGNIZE HIM", 0, y + 74, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas, "  IN ANOTHER HUDSON SOFT GAME", 0, y + 90, scale, Color.WHITE, Color.GRAY);
        Scenes.renderTextXCenter(canvas, "GOODBYE", y + 106, scale, Color.WHITE, Color.GRAY);

        try {
            gc.setFont(Font.loadFont(new FileInputStream("res/font/Kongtext.ttf"), 8 * scale));
        } catch (FileNotFoundException e) {
            System.exit(1);
        }
    }
}
