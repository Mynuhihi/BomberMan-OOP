package uet.oop.bomberman.entities.enemy.chaseEnemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.enemy.BrickPass;
import uet.oop.bomberman.graphics.Sprite;

public class Kondoria extends ChaseEnemy implements BrickPass {
    public Kondoria(double x, double y) {
        super(x, y);
        normalSpeedLevel = 0.8;
        chasingSpeedLevel = 0.8;
        chasingDistance = 0;
        changeDirectionTimeMin = 15;
        changeDirectionTimeMax = 75;
        score = 1000;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (direction == ENEMY_DIRECTION.UP_LEFT || direction == ENEMY_DIRECTION.DOWN_LEFT
                    || direction == ENEMY_DIRECTION.UP || direction == ENEMY_DIRECTION.LEFT)
                img = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, Sprite.kondoria_left2, animate, ANIMATE_TIME).getFxImage();
            if (direction == ENEMY_DIRECTION.UP_RIGHT || direction == ENEMY_DIRECTION.DOWN_RIGHT
                    || direction == ENEMY_DIRECTION.DOWN || direction == ENEMY_DIRECTION.RIGHT)
                img = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, Sprite.kondoria_right2, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.kondoria_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, Sprite.mob_dead4, animate, KILL_TIME).getFxImage();
            super.render(gc);
        }
    }
}
