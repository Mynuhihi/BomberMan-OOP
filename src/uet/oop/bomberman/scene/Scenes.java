package uet.oop.bomberman.scene;

import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import uet.oop.bomberman.BombermanGame;

public abstract class Scenes extends Scene {
    public Scenes(Parent root) {
        super(root, BombermanGame.STAGE_WIDTH, BombermanGame.STAGE_HEIGHT);
    }

    public abstract void show();

    public static void renderText(Canvas canvas, String text, double x, double y, double scale, Color color1, Color color2) {
        double x1, x2, y1, y2;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);

        x1 = x * scale;
        x2 = (x - 1) * scale;
        y1 = y * scale;
        y2 = (y - 1) * scale;

        fill(gc, text, x1, x2, y1, y2, color1, color2);
    }

    public static void renderTextXCenter(Canvas canvas, String text, double y, double scale, Color color1, Color color2) {
        double x1, x2, y1, y2;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);

        x1 = canvas.getWidth() / 2;
        x2 = canvas.getWidth() / 2 - scale;
        y1 = y * scale;
        y2 = (y - 1) * scale;

        fill(gc, text, x1, x2, y1, y2, color1, color2);
    }

    public static void renderTextYCenter(Canvas canvas, String text, double x, double scale, Color color1, Color color2) {
        double x1, x2, y1, y2;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);

        x1 = x * scale;
        x2 = (x - 1) * scale;
        y1 = canvas.getHeight() / 2;
        y2 = canvas.getHeight() / 2 - scale;

        fill(gc, text, x1, x2, y1, y2, color1, color2);
    }

    public static void renderTextCenter(Canvas canvas, String text, double scale, Color color1, Color color2) {
        double x1, x2, y1, y2;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        x1 = canvas.getWidth() / 2;
        x2 = canvas.getWidth() / 2 - scale;
        y1 = canvas.getHeight() / 2;
        y2 = canvas.getHeight() / 2 - scale;

        fill(gc, text, x1, x2, y1, y2, color1, color2);
    }

    private static void fill(GraphicsContext gc, String text, double x1, double x2, double y1, double y2, Color color1, Color color2) {
        gc.setFill(color2);
        gc.fillText(text, x1, y1);
        gc.setFill(color2);
        gc.fillText(text, x1, y2);
        gc.setFill(color2);
        gc.fillText(text, x2, y1);
        gc.setFill(color1);
        gc.fillText(text, x2, y2);
    }
}
