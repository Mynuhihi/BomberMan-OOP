package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;

public class Oneal extends Enemy {
    private static final int ANIMATE_TIME = 36;

    public Oneal(double x, double y) {
        super(x, y);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (direction == ENEMY_DIRECTION.LEFT || direction == ENEMY_DIRECTION.UP)
                img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left1, Sprite.oneal_left3, animate, ANIMATE_TIME).getFxImage();
            if (direction == ENEMY_DIRECTION.RIGHT || direction == ENEMY_DIRECTION.DOWN)
                img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right1, Sprite.oneal_right3, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.oneal_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 120).getFxImage();
            super.render(gc);
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (other instanceof Wall || other instanceof Brick) calculateDirection();
            if (other instanceof Bomber) kill();
        }
    }

    public void calculateDirection() {
        if (direction == ENEMY_DIRECTION.UP) direction = ENEMY_DIRECTION.LEFT;
        else if (direction == ENEMY_DIRECTION.LEFT) direction = ENEMY_DIRECTION.DOWN;
        else if (direction == ENEMY_DIRECTION.DOWN) direction = ENEMY_DIRECTION.RIGHT;
        else if (direction == ENEMY_DIRECTION.RIGHT) direction = ENEMY_DIRECTION.UP;
    }
}
