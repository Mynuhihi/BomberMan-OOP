package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;

public class Balloom extends Enemy {
    public static final int ANIMATE_TIME = 36;
    private int animate = 0;
    private int changeDirectionTime = 120;

    public Balloom(double x, double y) {
        super(x, y);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (direction == ENEMY_DIRECTION.LEFT || direction == ENEMY_DIRECTION.UP)
                img = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, Sprite.balloom_left2, animate, ANIMATE_TIME).getFxImage();
            if (direction == ENEMY_DIRECTION.RIGHT || direction == ENEMY_DIRECTION.DOWN)
                img = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, Sprite.balloom_right2, animate, ANIMATE_TIME).getFxImage();
            super.render(gc);
        } else if (status == ENEMY_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.balloom_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 120).getFxImage();
            super.render(gc);
        }
    }

    @Override
    public void update() {
        animate++;
        if (animate >= 60000) animate = 0;

        if (status == ENEMY_STATUS.ACTIVE) {
            if (animate >= changeDirectionTime) {
                calculateDirection();
                animate = 0;
            }
            move();
        } else if (status == ENEMY_STATUS.KILLED) {
            if (animate >= 120) delete();
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
        int calc = (int) (Math.round(Math.random() * 1000)) % 4;
        if (calc == 0) direction = ENEMY_DIRECTION.UP;
        else if (calc == 1) direction = ENEMY_DIRECTION.DOWN;
        else if (calc == 2) direction = ENEMY_DIRECTION.LEFT;
        else if (calc == 3) direction = ENEMY_DIRECTION.RIGHT;

        changeDirectionTime = 60 + (int) (Math.round(Math.random() * 1000)) % 120;
    }
}
