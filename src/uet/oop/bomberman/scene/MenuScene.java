package uet.oop.bomberman.scene;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MenuScene extends Scenes {
    public enum MENU_BUTTON {
        START, CONTINUE
    }

    private MENU_BUTTON currentButton = MENU_BUTTON.CONTINUE;
    private double scale = BombermanGame.STAGE_WIDTH / 256;
    private GraphicsContext gc;
    private Canvas canvas;

    public MenuScene(Group root) {
        super(root);
        canvas = new Canvas(256 * scale, 240 * scale);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE)
                BombermanGame.setScene(new LevelScene(new Group(), 1));
            if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.A) nextButton();
        });
    }

    @Override
    public void show() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        try {
            FileInputStream reader = new FileInputStream("res/bomberman.png");
            Image bg = new Image(reader);
            gc.drawImage(bg, 0, 0, canvas.getWidth(), canvas.getHeight());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        double xButton = 0, y = 155;
        if (currentButton == MENU_BUTTON.START) xButton = 66;
        else if (currentButton == MENU_BUTTON.CONTINUE) xButton = 130;
        Scenes.renderText(canvas,">", xButton, y, scale, Color.WHITE, Color.GRAY);

        Scenes.renderText(canvas,"START\tCONTINUE", 72, y, scale, Color.WHITE, Color.GRAY);
        Scenes.renderText(canvas,"TOP\t" + BombermanGame.HIGH_SCORE, 72, y + 16, scale, Color.WHITE, Color.GRAY);
        Scenes.renderTextXCenter(canvas,"TM AND © 1987 HUDSON SOFT", y + 32, scale, Color.WHITE, Color.GRAY);
        Scenes.renderTextXCenter(canvas,"LICENSED BY", y + 48, scale, Color.WHITE, Color.GRAY);
        Scenes.renderTextXCenter(canvas,"NINTENDO OF AMERICA INC.", y + 64, scale, Color.WHITE, Color.GRAY);

        try {
            gc.setFont(Font.loadFont(new FileInputStream("res/font/Kongtext.ttf"), 8 * scale));
        } catch (FileNotFoundException e) {
            System.exit(1);
        }
    }

    public void nextButton() {
        if (currentButton == MENU_BUTTON.START) currentButton = MENU_BUTTON.CONTINUE;
        else if (currentButton == MENU_BUTTON.CONTINUE) currentButton = MENU_BUTTON.START;
    }
}
