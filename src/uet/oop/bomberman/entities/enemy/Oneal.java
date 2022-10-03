package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Oneal extends Enemy {
    private static final int ANIMATE_TIME = 36;

    public Oneal(double x, double y) {
        super(x, y);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (direction == ENEMY_DIRECTION.UP_LEFT
                    || direction == ENEMY_DIRECTION.DOWN_LEFT
                    || direction == ENEMY_DIRECTION.UP
                    || direction == ENEMY_DIRECTION.LEFT)
                img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left1, Sprite.oneal_left3, animate, ANIMATE_TIME).getFxImage();
            if (direction == ENEMY_DIRECTION.UP_RIGHT
                    || direction == ENEMY_DIRECTION.DOWN_RIGHT
                    || direction == ENEMY_DIRECTION.DOWN
                    || direction == ENEMY_DIRECTION.RIGHT)
                img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right1, Sprite.oneal_right3, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.oneal_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 120).getFxImage();
            super.render(gc);
        }
    }

    @Override
    public void update() {
        animate++;

        if (status == ENEMY_STATUS.ACTIVE) {
            if (animate >= 120) {
                direction = calculateDirection();
                animate = 0;
            }
            move();
        } else if (status == ENEMY_STATUS.KILLED) {
            if (animate >= 120) delete();
        }
    }

    @Override
    public void handleCollision(Entity other) {

    }

    public ENEMY_DIRECTION calculateDirection() {
        if (calcBomberDistance() > 4 * Sprite.SCALED_SIZE) {
            speedLevel = 1;
            return calculateRandomDirection();
        } else {
            speedLevel = 1.5;

            double bomberX = BombermanGame.getBomber().getX();
            double bomberY = BombermanGame.getBomber().getY();

            if (x == bomberX) {
                if (y < bomberY) return ENEMY_DIRECTION.DOWN;
                else return ENEMY_DIRECTION.UP;
            } else if (y == bomberY) {
                if (x < bomberX) return ENEMY_DIRECTION.RIGHT;
                else return ENEMY_DIRECTION.LEFT;
            } else if (x > bomberX && y > bomberY) {
                return ENEMY_DIRECTION.UP_LEFT;
            } else if (x > bomberX) {
                return ENEMY_DIRECTION.DOWN_LEFT;
            } else if (y > bomberY) {
                return ENEMY_DIRECTION.UP_RIGHT;
            } else {
                return ENEMY_DIRECTION.DOWN_RIGHT;
            }
        }
    }

    public double calcBomberDistance() {
        double bomberX = BombermanGame.getBomber().getX();
        double bomberY = BombermanGame.getBomber().getY();
        return Math.sqrt(Math.pow(x - bomberX, 2) + Math.pow(y - bomberY, 2));
    }
}
