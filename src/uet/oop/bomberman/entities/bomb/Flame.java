package uet.oop.bomberman.entities.bomb;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.map.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.map.Wall;
import uet.oop.bomberman.graphics.Sprite;

public class Flame extends Entity {
    public static final int ANIMATE_TIME = 20;

    public enum FLAME_TYPE {
        CENTER, VERTICAL, HORIZONTAL, UP, DOWN, LEFT, RIGHT
    }
    public enum FLAME_STATUS {
        INACTIVE, ACTIVE
    }

    private final FLAME_TYPE type;
    private FLAME_STATUS status = FLAME_STATUS.INACTIVE;
    private int animate = 0;

    public Flame(double xUnit, double yUnit, FLAME_TYPE type) {
        super(xUnit, yUnit, null);
        this.type = type;
    }

    /**
     * Set img phu thuoc vao loai flame (FLAME_TYPE)
     * Neu flame da duoc kich hoat (ACTIVE) thi moi render.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (type == FLAME_TYPE.CENTER)
            img = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, Sprite.bomb_exploded1, Sprite.bomb_exploded, animate, ANIMATE_TIME).getFxImage();
        if (type == FLAME_TYPE.VERTICAL)
            img = Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1, Sprite.explosion_vertical2, Sprite.explosion_vertical1, Sprite.explosion_vertical, animate, ANIMATE_TIME).getFxImage();
        if (type == FLAME_TYPE.HORIZONTAL)
            img = Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1, Sprite.explosion_horizontal2, Sprite.explosion_horizontal1, Sprite.explosion_horizontal, animate, ANIMATE_TIME).getFxImage();
        if (type == FLAME_TYPE.UP)
            img = Sprite.movingSprite(Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1, Sprite.explosion_vertical_top_last2, Sprite.explosion_vertical_top_last1, Sprite.explosion_vertical_top_last, animate, ANIMATE_TIME).getFxImage();
        if (type == FLAME_TYPE.DOWN)
            img = Sprite.movingSprite(Sprite.explosion_vertical_down_last, Sprite.explosion_vertical_down_last1, Sprite.explosion_vertical_down_last2, Sprite.explosion_vertical_down_last1, Sprite.explosion_vertical_down_last, animate, ANIMATE_TIME).getFxImage();
        if (type == FLAME_TYPE.LEFT)
            img = Sprite.movingSprite(Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1, Sprite.explosion_horizontal_left_last2, Sprite.explosion_horizontal_left_last1, Sprite.explosion_horizontal_left_last, animate, ANIMATE_TIME).getFxImage();
        if (type == FLAME_TYPE.RIGHT)
            img = Sprite.movingSprite(Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1, Sprite.explosion_horizontal_right_last2, Sprite.explosion_horizontal_right_last1, Sprite.explosion_horizontal_right_last, animate, ANIMATE_TIME).getFxImage();
        if (status == FLAME_STATUS.ACTIVE) super.render(gc);
    }

    @Override
    public void update() {
        if (status == FLAME_STATUS.ACTIVE) animate++;
    }

    @Override
    public void handleCollision(Entity other) {
        if (status == FLAME_STATUS.ACTIVE) {
            if (other instanceof Wall) delete();
            if (other instanceof Brick) delete();
        }
    }

    public void active() {
        status = FLAME_STATUS.ACTIVE;
    }

    public void delete() {
        status = FLAME_STATUS.INACTIVE;
    }

    public FLAME_STATUS getStatus() {
        return status;
    }
}