package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class Brick extends Entity {
    enum BRICK_STATUS {
        ACTIVE, BROKEN, DELETED
    }

    private BRICK_STATUS status = BRICK_STATUS.ACTIVE;
    private int animate = 0;

    public Brick(double x, double y, Image img) {
        super(x, y, img);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == BRICK_STATUS.ACTIVE) {
            gc.drawImage(img, x, y);
        } else if (status == BRICK_STATUS.BROKEN) {
            img = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2, animate, 18).getFxImage();
            gc.drawImage(img, x, y);
        }
    }

    @Override
    public void update() {
        if (status == BRICK_STATUS.BROKEN) {
            animate++;
            if (animate >= 18) delete();
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (other instanceof Bomber) {
            status = BRICK_STATUS.BROKEN;
        }
    }

    public void delete() {
        status = BRICK_STATUS.DELETED;
        width = 0;
        height = 0;
        img = null;
    }
}
