package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;

public class Doll extends Enemy {
    private static final int ANIMATE_TIME = 24;
    private int changeDirectionTime = 60;

    public Doll(double x, double y) {
        super(x, y);
        speedLevel = 1.75;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (direction == ENEMY_DIRECTION.UP_LEFT
                    || direction == ENEMY_DIRECTION.DOWN_LEFT
                    || direction == ENEMY_DIRECTION.UP
                    || direction == ENEMY_DIRECTION.LEFT)
                img = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, Sprite.doll_left2, animate, ANIMATE_TIME).getFxImage();
            if (direction == ENEMY_DIRECTION.UP_RIGHT
                    || direction == ENEMY_DIRECTION.DOWN_RIGHT
                    || direction == ENEMY_DIRECTION.DOWN
                    || direction == ENEMY_DIRECTION.RIGHT)
                img = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, Sprite.doll_right2, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.doll_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 120).getFxImage();
            super.render(gc);
        }
    }

    @Override
    public void update() {
        animate++;

        if (status == ENEMY_STATUS.ACTIVE) {
            if (animate >= changeDirectionTime) {
                direction = calculateRandomDirection();
                animate = 0;
                changeDirectionTime = 30 + (int) (Math.random() * 1000) % 60;
            }
            move();
        } else if (status == ENEMY_STATUS.KILLED) {
            if (animate >= 120) delete();
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (other instanceof Wall || other instanceof Brick) {
                if (direction == ENEMY_DIRECTION.UP
                        || direction == ENEMY_DIRECTION.DOWN
                        || direction == ENEMY_DIRECTION.LEFT
                        || direction == ENEMY_DIRECTION.RIGHT)
                    direction = calculateRandomDirection();
            }
        }
    }
}
