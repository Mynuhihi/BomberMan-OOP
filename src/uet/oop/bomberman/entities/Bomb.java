package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sun.java2d.pipe.SpanClipRenderer;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;


public class Bomb extends Entity {
    int animate = 0;

    public void setImage(Image image) {
        this.img = image;
    }
    public Bomb(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void render(GraphicsContext gc) {
        img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 18).getFxImage();
        super.render(gc);
    }

    @Override
    public void update() {
        animate++;
    }

    @Override
    public void handleCollision(Entity other) {

    }
}
