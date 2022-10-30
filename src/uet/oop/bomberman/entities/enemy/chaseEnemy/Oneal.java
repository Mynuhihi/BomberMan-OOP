package uet.oop.bomberman.entities.enemy.chaseEnemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.graphics.Sprite;

public class Oneal extends ChaseEnemy {
    public Oneal(double x, double y) {
        super(x, y);
        chasingDistance = 6;
        changeDirectionTimeMin = 90;
        changeDirectionTimeMax = 210;
        score = 200;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (direction == ENEMY_DIRECTION.UP_LEFT || direction == ENEMY_DIRECTION.DOWN_LEFT
                    || direction == ENEMY_DIRECTION.UP || direction == ENEMY_DIRECTION.LEFT)
                img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left1, Sprite.oneal_left3, animate, ANIMATE_TIME).getFxImage();
            if (direction == ENEMY_DIRECTION.UP_RIGHT || direction == ENEMY_DIRECTION.DOWN_RIGHT
                    || direction == ENEMY_DIRECTION.DOWN || direction == ENEMY_DIRECTION.RIGHT)
                img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right1, Sprite.oneal_right3, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.oneal_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, Sprite.mob_dead4, animate, KILL_TIME).getFxImage();
            super.render(gc);
        }
    }
}
