package uet.oop.bomberman.entities.enemy.chaseEnemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.graphics.Sprite;

public class Minvo extends ChaseEnemy {
    public Minvo(double x, double y) {
        super(x, y);
        normalSpeedLevel = 1.5;
        chasingSpeedLevel = 2;
        chasingDistance = 8;
        changeDirectionTimeMin = 60;
        changeDirectionTimeMax = 120;
        score = 800;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (direction == ENEMY_DIRECTION.UP_LEFT || direction == ENEMY_DIRECTION.DOWN_LEFT
                    || direction == ENEMY_DIRECTION.UP || direction == ENEMY_DIRECTION.LEFT)
                img = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left1, Sprite.minvo_left3, animate, ANIMATE_TIME).getFxImage();
            if (direction == ENEMY_DIRECTION.UP_RIGHT || direction == ENEMY_DIRECTION.DOWN_RIGHT
                    || direction == ENEMY_DIRECTION.DOWN || direction == ENEMY_DIRECTION.RIGHT)
                img = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right1, Sprite.minvo_right3, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.minvo_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, Sprite.mob_dead4, animate, KILL_TIME).getFxImage();
            super.render(gc);
        }
    }
}
