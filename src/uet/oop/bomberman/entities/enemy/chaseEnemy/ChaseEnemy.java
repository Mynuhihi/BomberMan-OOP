package uet.oop.bomberman.entities.enemy.chaseEnemy;

import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.graphics.Sprite;

public abstract class ChaseEnemy extends Enemy {
    protected double normalSpeedLevel = 1;
    protected double chasingSpeedLevel = 1.5;
    protected double chasingDistance = 4;

    public ChaseEnemy(double x, double y) {
        super(x, y);
    }

    public ENEMY_DIRECTION calculateDirection() {
        animate = 0;
        if (chasingDistance > 0 && calcBomberDistance() > chasingDistance * Sprite.SCALED_SIZE) {
            speedLevel = normalSpeedLevel;
            return calculateRandomDirection();
        } else {
            speedLevel = chasingSpeedLevel;
            return calculateDirectionToBomber();
        }
    }
}
