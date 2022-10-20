package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.BombList;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomber extends Entity {
    public enum BOMBER_STATUS {
        SPAWN, ACTIVE, KILLED, DEAD
    }

    static class Move {
        private boolean up = false;
        private boolean down = false;
        private boolean left = false;
        private boolean right = false;
    }

    private BOMBER_STATUS status = BOMBER_STATUS.SPAWN;
    private BombList bombList = new BombList();
    private Move move = new Move();
    private int lives = 3;
    private double speedItemBuff = 1;
    private int bombLength = 2;
    private int maxBomb = 2;
    private int animate = 0;

    public Bomber(double x, double y, Image img) {
        super(x, y, img);
        width = Sprite.SCALED_SIZE * 0.75;
    }

    /**
     * render bombList cung voi bomber bomblist khong phu thoc trang thai bomber.
     */
    @Override
    public void render(GraphicsContext gc) {
        bombList.render(gc);
        if (status == BOMBER_STATUS.SPAWN) {
            img = Sprite.movingSprite(Sprite.player_right, Sprite.blank, animate, 10).getFxImage();
            if (move.up)
                img = Sprite.movingSprite(Sprite.player_up_1, Sprite.blank, Sprite.player_up_2, Sprite.blank, animate, 20).getFxImage();
            if (move.down)
                img = Sprite.movingSprite(Sprite.player_down_1, Sprite.blank, Sprite.player_down_2, Sprite.blank, animate, 20).getFxImage();
            if (move.left)
                img = Sprite.movingSprite(Sprite.player_left_1, Sprite.blank, Sprite.player_left_2, Sprite.blank, animate, 20).getFxImage();
            if (move.right)
                img = Sprite.movingSprite(Sprite.player_right_1, Sprite.blank, Sprite.player_right_2, Sprite.blank, animate, 20).getFxImage();
            gc.drawImage(img, x, y);
        } else if (status == BOMBER_STATUS.ACTIVE) {
            if (move.up)
                img = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, animate, 18).getFxImage();
            if (move.down)
                img = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, animate, 18).getFxImage();
            if (move.left)
                img = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, animate, 18).getFxImage();
            if (move.right)
                img = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, animate, 18).getFxImage();
            gc.drawImage(img, x, y);
        } else if (status == BOMBER_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 24).getFxImage();
            gc.drawImage(img, x, y);
        }
    }

    /**
     * update tuong tu render.
     * Kiem tra trang thai Bomber:
     * SPAWN (moi hoi sinh) -> sau 1.5 giay tro ve binh thuong (ACTIVE)
     * KILLED -> sau 1 thoi gian hoi sinh: goi ham respawn().
     */
    @Override
    public void update() {
        animate++;
        bombList.update();

        if (status == BOMBER_STATUS.SPAWN) {
            if (animate >= 60) status = BOMBER_STATUS.ACTIVE;
        }
        if (status == BOMBER_STATUS.ACTIVE || status == BOMBER_STATUS.SPAWN) {
            double speed = 0.8 * Sprite.SCALED_SIZE / Sprite.DEFAULT_SIZE;
            if (move.up) y -= speed * speedItemBuff;
            if (move.down) y += speed * speedItemBuff;
            if (move.left) x -= speed * speedItemBuff;
            if (move.right) x += speed * speedItemBuff;
        } else if (status == BOMBER_STATUS.KILLED) {
            if (animate >= 24) respawn();
        }
    }

    /**
     * Va cham voi Enemy hoac Flame thi bi kill.
     */
    @Override
    public void handleCollision(Entity other) {
        if (status == BOMBER_STATUS.ACTIVE) {
            if (other instanceof Enemy) {
                Enemy enemy = (Enemy) other;
                if (((Enemy) other).getStatus() == Enemy.ENEMY_STATUS.ACTIVE) kill();
            }
            if (other instanceof Flame) {
                Flame fl = (Flame) other;
                if (fl.getStatus() == Flame.FLAME_STATUS.ACTIVE) kill();
            }
        }
    }

    public void addControl(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.W) move.up = true;
            if (event.getCode() == KeyCode.S) move.down = true;
            if (event.getCode() == KeyCode.A) move.left = true;
            if (event.getCode() == KeyCode.D) move.right = true;
            if (event.getCode() == KeyCode.SPACE) addBomb();
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.W) move.up = false;
            if (event.getCode() == KeyCode.S) move.down = false;
            if (event.getCode() == KeyCode.A) move.left = false;
            if (event.getCode() == KeyCode.D) move.right = false;
            if (event.getCode() == KeyCode.R) kill();
        });
    }

    public void kill() {
        status = BOMBER_STATUS.KILLED;
        animate = 0;
    }

    /**
     * Hoi sinh dat lai trang thai va vi tri
     * Neu het mang thi die.
     */
    public void respawn() {
        status = BOMBER_STATUS.SPAWN;
        animate = 0;

//        int randomX = 1 + (int) (Math.random() * 1000) % 29;
//        int randomY = 1 + (int) (Math.random() * 1000) % 11;
//
//        x = Sprite.SCALED_SIZE * randomX;
//        y = Sprite.SCALED_SIZE * randomY;

//        x = Sprite.SCALED_SIZE;
//        y = Sprite.SCALED_SIZE;

        lives--;
//        if (lives == 0) dead();
    }

    /**
     * die.
     */
    public void dead() {
        status = BOMBER_STATUS.DEAD;
        //TODO
        System.exit(0);
    }

    /**
     * Them bom vao bombList neu chua maxBomb
     * Lam tron vi tri bom.
     */
    void addBomb() {
        if (bombList.size() < maxBomb) {
            int xUnit = (int) Math.round(x / Sprite.SCALED_SIZE);
            int yUnit = (int) Math.round(y / Sprite.SCALED_SIZE);
            bombList.addBomb(xUnit, yUnit, bombLength);
        }
    }

    public List<Bomb> getBombList() {
        return bombList.getBombList();
    }

    public List<Flame> getFlameList() {
        return bombList.getFlameList();
    }
}