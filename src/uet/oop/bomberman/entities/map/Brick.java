package uet.oop.bomberman.entities.map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.graphics.Sprite;

public class Brick extends MapEntity {
    private final int BROKEN_ANIMATE_TIME = 15;

    public enum BRICK_STATUS {
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
            img = Sprite.movingSprite(Sprite.brick_exploded1, Sprite.brick_exploded2, Sprite.brick_exploded3, Sprite.brick_exploded4, Sprite.brick_exploded5, animate, BROKEN_ANIMATE_TIME).getFxImage();
            gc.drawImage(img, x, y);
        }
    }

    @Override
    public void update() {
        if (status == BRICK_STATUS.BROKEN) {
            animate++;
            if (animate >= BROKEN_ANIMATE_TIME) delete();
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (status == BRICK_STATUS.ACTIVE) {
            if (other instanceof Flame) {
                Flame fl = (Flame) other;
                if (fl.getStatus() == Flame.FLAME_STATUS.ACTIVE)
                    status = BRICK_STATUS.BROKEN;
            }
        }
    }

    public void delete() {
        status = BRICK_STATUS.DELETED;
    }

    public BRICK_STATUS getStatus() {
        return status;
    }
}
