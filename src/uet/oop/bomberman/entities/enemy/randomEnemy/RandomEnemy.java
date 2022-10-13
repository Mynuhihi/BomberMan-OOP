package uet.oop.bomberman.entities.enemy.randomEnemy;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemy.Enemy;

public abstract class RandomEnemy extends Enemy {
    public RandomEnemy(double x, double y) {
        super(x, y);
    }

    @Override
    public void handleCollision(Entity other) {
        super.handleCollision(other);
    }

    @Override
    public ENEMY_DIRECTION calculateDirection() {
        animate = 0;
        return calculateRandomDirection();
    }
}
