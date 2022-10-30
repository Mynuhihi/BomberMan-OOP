package uet.oop.bomberman.entities.enemy.randomEnemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.map.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.graphics.Sprite;

public class Doll extends RandomEnemy {
    public Doll(double x, double y) {
        super(x, y);
        speedLevel = 1.75;
        changeDirectionTimeMin = 30;
        changeDirectionTimeMax = 120;
        score = 400;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == Enemy.ENEMY_STATUS.ACTIVE) {
            if (direction == Enemy.ENEMY_DIRECTION.UP_LEFT || direction == Enemy.ENEMY_DIRECTION.DOWN_LEFT
                    || direction == Enemy.ENEMY_DIRECTION.UP || direction == Enemy.ENEMY_DIRECTION.LEFT)
                img = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, Sprite.doll_left2, animate, ANIMATE_TIME).getFxImage();
            if (direction == Enemy.ENEMY_DIRECTION.UP_RIGHT || direction == Enemy.ENEMY_DIRECTION.DOWN_RIGHT
                    || direction == Enemy.ENEMY_DIRECTION.DOWN || direction == Enemy.ENEMY_DIRECTION.RIGHT)
                img = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, Sprite.doll_right2, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == Enemy.ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.doll_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, Sprite.mob_dead4, animate, KILL_TIME).getFxImage();
            super.render(gc);
        }
    }

    @Override
    public void handleCollision(Entity other) {
        super.handleCollision(other);
        if (status == Enemy.ENEMY_STATUS.ACTIVE) {
            if (other instanceof Brick) {
                if (direction == Enemy.ENEMY_DIRECTION.UP || direction == Enemy.ENEMY_DIRECTION.DOWN
                        || direction == Enemy.ENEMY_DIRECTION.LEFT || direction == Enemy.ENEMY_DIRECTION.RIGHT)
                    direction = calculateDirection();
            }
        }
    }
}
