package uet.oop.bomberman.entities.enemy;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.entities.enemy.Enemy.ENEMY_DIRECTION.LEFT;
import static uet.oop.bomberman.entities.enemy.Enemy.ENEMY_DIRECTION.UP;

public abstract class Enemy extends Entity {
    public enum ENEMY_DIRECTION {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
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
        double speed = 0.4 * Sprite.SCALED_SIZE / Sprite.DEFAULT_SIZE;

        if (direction == ENEMY_DIRECTION.UP) y -= speed * speedLevel;
        else if (direction == ENEMY_DIRECTION.DOWN) y += speed * speedLevel;
        else if (direction == ENEMY_DIRECTION.LEFT) x -= speed * speedLevel;
        else if (direction == ENEMY_DIRECTION.RIGHT) x += speed * speedLevel;
        else if (direction == ENEMY_DIRECTION.UP_LEFT) {
            x -= speed * speedLevel;
            y -= speed * speedLevel;
        } else if (direction == ENEMY_DIRECTION.UP_RIGHT) {
            x += speed * speedLevel;
            y -= speed * speedLevel;
        } else if (direction == ENEMY_DIRECTION.DOWN_LEFT) {
            x -= speed * speedLevel;
            y += speed * speedLevel;
        } else if (direction == ENEMY_DIRECTION.DOWN_RIGHT) {
            x += speed * speedLevel;
            y += speed * speedLevel;
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

    public ENEMY_DIRECTION calculateRandomDirection() {
        int calc = (int) (Math.round(Math.random() * 1000)) % 8;
        if (calc == 0) return UP;
        else if (calc == 1) return ENEMY_DIRECTION.DOWN;
        else if (calc == 2) return ENEMY_DIRECTION.LEFT;
        else if (calc == 3) return ENEMY_DIRECTION.RIGHT;
        else if (calc == 4) return ENEMY_DIRECTION.UP_LEFT;
        else if (calc == 5) return ENEMY_DIRECTION.UP_RIGHT;
        else if (calc == 6) return ENEMY_DIRECTION.DOWN_LEFT;
        else return ENEMY_DIRECTION.DOWN_RIGHT;
    }
}
