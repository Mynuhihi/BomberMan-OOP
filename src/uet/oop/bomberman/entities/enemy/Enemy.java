package uet.oop.bomberman.entities.enemy;

import uet.oop.bomberman.scene.GameScene;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.bomb.*;
import uet.oop.bomberman.entities.map.Wall;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Enemy extends Entity {
    protected static final int ANIMATE_TIME = 32;
    protected static final int KILL_TIME = 120;

    public enum ENEMY_DIRECTION {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    public enum ENEMY_STATUS {
        INACTIVE, ACTIVE, KILLED, DELETED
    }

    protected ENEMY_STATUS status = ENEMY_STATUS.ACTIVE;
    protected ENEMY_DIRECTION direction = ENEMY_DIRECTION.LEFT;
    protected int score = 0;
    protected double speedLevel = 1;
    protected int animate = 0;
    protected int changeDirectionTimeMin = 120;
    protected int changeDirectionTimeMax = 240;
    private int changeDirectionTime = 0;

    public Enemy(double x, double y) {
        super(x, y, null);
    }

    @Override
    public void update() {
        animate++;

        if (status == ENEMY_STATUS.ACTIVE) {
            if (animate >= changeDirectionTime) {
                direction = calculateDirection();
                changeDirectionTime = changeDirectionTimeMin + (int) (Math.random() * 1000) % (changeDirectionTimeMax - changeDirectionTimeMin);
            }
            move();
        } else if (status == ENEMY_STATUS.KILLED) {
            if (animate >= KILL_TIME) delete();
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (status == ENEMY_STATUS.ACTIVE) {
            if (other instanceof Flame) {
                if (((Flame) other).getStatus() == Flame.FLAME_STATUS.ACTIVE)
                    kill();
            }
            if (other instanceof Bomb) {
                if (((Bomb) other).getStatus() == Bomb.BOMB_STATUS.ACTIVE)
                    direction = calculateRandomDirection();
            }
            if (other instanceof Wall) {
                if (direction == ENEMY_DIRECTION.UP || direction == ENEMY_DIRECTION.DOWN
                        || direction == ENEMY_DIRECTION.LEFT || direction == ENEMY_DIRECTION.RIGHT)
                    direction = calculateRandomDirection();
            }
        }
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
    }

    public void delete() {
        status = ENEMY_STATUS.DELETED;
        GameScene.addScore(score);
    }

    public abstract ENEMY_DIRECTION calculateDirection();

    public ENEMY_DIRECTION calculateRandomDirection() {
        int calc = (int) (Math.round(Math.random() * 1000)) % 8;
        if (calc == 0) return ENEMY_DIRECTION.UP;
        else if (calc == 1) return ENEMY_DIRECTION.DOWN;
        else if (calc == 2) return ENEMY_DIRECTION.LEFT;
        else if (calc == 3) return ENEMY_DIRECTION.RIGHT;
        else if (calc == 4) return ENEMY_DIRECTION.UP_LEFT;
        else if (calc == 5) return ENEMY_DIRECTION.UP_RIGHT;
        else if (calc == 6) return ENEMY_DIRECTION.DOWN_LEFT;
        else return ENEMY_DIRECTION.DOWN_RIGHT;
    }

    public ENEMY_DIRECTION calculateDirectionToBomber() {
        double bomberX = GameScene.getBomber().getX();
        double bomberY = GameScene.getBomber().getY();

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

    public double calcBomberDistance() {
        double bomberX = GameScene.getBomber().getX();
        double bomberY = GameScene.getBomber().getY();
        return Math.sqrt(Math.pow(x - bomberX, 2) + Math.pow(y - bomberY, 2));
    }

    public ENEMY_STATUS getStatus() {
        return status;
    }
}
