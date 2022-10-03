package uet.oop.bomberman.entities.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Enemy extends Entity {
    enum ENEMY_DIRECTION {
        UP, DOWN, LEFT, RIGHT
    }

    enum ENEMY_STATUS {
        INACTIVE, ACTIVE, KILLED, DELETED
    }

    protected ENEMY_STATUS status = ENEMY_STATUS.ACTIVE;
    protected ENEMY_DIRECTION direction = ENEMY_DIRECTION.LEFT;
    protected double speedLevel = 1;
    protected int animate = 0;

    public Enemy(double x, double y) {
        super(x, y, null);
    }
    public void move() {
        double speed = 0.5 * Sprite.SCALED_SIZE / Sprite.DEFAULT_SIZE;
        if (direction == ENEMY_DIRECTION.UP) y -= speed * speedLevel;
        if (direction == ENEMY_DIRECTION.DOWN) y += speed * speedLevel;
        if (direction == ENEMY_DIRECTION.LEFT) x -= speed * speedLevel;
        if (direction == ENEMY_DIRECTION.RIGHT) x += speed * speedLevel;
    }

    @Override
    public void update() {
        animate++;
        if (animate >= 60000) animate = 0;

        if (status == ENEMY_STATUS.ACTIVE) {
            move();
        } else if (status == ENEMY_STATUS.KILLED) {
            if (animate >= 120) delete();
        }
    }

    public void kill() {
        status = ENEMY_STATUS.KILLED;
        animate = 0;
        width = 0;
        height = 0;
    }

    public void delete() {
        status = ENEMY_STATUS.DELETED;
    }
}
