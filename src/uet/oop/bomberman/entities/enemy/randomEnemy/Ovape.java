package uet.oop.bomberman.entities.enemy.randomEnemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.enemy.BrickPass;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.graphics.Sprite;

public class Ovape extends RandomEnemy implements BrickPass {
    public Ovape(double x, double y) {
        super(x, y);
        speedLevel = 1.5;
        changeDirectionTimeMin = 60;
        changeDirectionTimeMax = 180;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == Enemy.ENEMY_STATUS.ACTIVE) {
            if (direction == Enemy.ENEMY_DIRECTION.UP_LEFT || direction == Enemy.ENEMY_DIRECTION.DOWN_LEFT
                    || direction == Enemy.ENEMY_DIRECTION.UP || direction == Enemy.ENEMY_DIRECTION.LEFT)
                img = Sprite.movingSprite(Sprite.ovape_left1, Sprite.ovape_left2, Sprite.ovape_left3, Sprite.ovape_left2, animate, ANIMATE_TIME).getFxImage();
            if (direction == Enemy.ENEMY_DIRECTION.UP_RIGHT || direction == Enemy.ENEMY_DIRECTION.DOWN_RIGHT
                    || direction == Enemy.ENEMY_DIRECTION.DOWN || direction == Enemy.ENEMY_DIRECTION.RIGHT)
                img = Sprite.movingSprite(Sprite.ovape_right1, Sprite.ovape_right2, Sprite.ovape_right3, Sprite.ovape_right2, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == Enemy.ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.ovape_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, KILL_TIME).getFxImage();
            super.render(gc);
        }
    }
}
