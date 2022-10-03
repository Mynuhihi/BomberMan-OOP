package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends Entity {
    enum BOMBER_STATUS {
        SPAWN, ACTIVE, KILLED, DEAD
    }

    static class Move {
        private boolean up = false;
        private boolean down = false;
        private boolean left = false;
        private boolean right = false;
        private boolean check = false;
    }

    private BOMBER_STATUS status = BOMBER_STATUS.SPAWN;
    private Move move = new Move();
    private int lives = 3;
    private double speedItemBuff;
    private int animate = 0;

    public Bomber(double x, double y, Image img) {
        super(x, y, img);
        width = Sprite.SCALED_SIZE * 0.75;
        speedItemBuff = 1;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == BOMBER_STATUS.SPAWN) {
            img = Sprite.movingSprite(Sprite.player_right, Sprite.blank, animate, 10).getFxImage();
            if (move.up) img = Sprite.movingSprite(Sprite.player_up_1, Sprite.blank, Sprite.player_up_2, Sprite.blank, animate, 20).getFxImage();
            if (move.down) img = Sprite.movingSprite(Sprite.player_down_1, Sprite.blank, Sprite.player_down_2, Sprite.blank, animate, 20).getFxImage();
            if (move.left) img = Sprite.movingSprite(Sprite.player_left_1, Sprite.blank, Sprite.player_left_2, Sprite.blank, animate, 20).getFxImage();
            if (move.right) img = Sprite.movingSprite(Sprite.player_right_1, Sprite.blank, Sprite.player_right_2, Sprite.blank, animate, 20).getFxImage();
            gc.drawImage(img, x, y);
        } else if (status == BOMBER_STATUS.ACTIVE) {
            if (move.up) img = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, animate, 18).getFxImage();
            if (move.down) img = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, animate, 18).getFxImage();
            if (move.left) img = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, animate, 18).getFxImage();
            if (move.right) img = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, animate, 18).getFxImage();
            gc.drawImage(img, x, y);
        } else if (status == BOMBER_STATUS.KILLED) {
            img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 24).getFxImage();
            gc.drawImage(img, x, y);
        }
    }

    @Override
    public void update() {
        animate++;
        if (status == BOMBER_STATUS.SPAWN) {
            if (animate >= 120) status = BOMBER_STATUS.ACTIVE;
        }
        if (status == BOMBER_STATUS.ACTIVE || status == BOMBER_STATUS.SPAWN) {
            double speed = 0.8 * Sprite.SCALED_SIZE / Sprite.DEFAULT_SIZE;
            if (move.up) y -= speed * speedItemBuff;
            if (move.down) y += speed * speedItemBuff;
            if (move.left) x -= speed * speedItemBuff;
            if (move.right) x += speed * speedItemBuff;
            if (move.check) {
                Bomb bomb = new Bomb(getXTile(), getYTile(), Sprite.bomb.getFxImage());
                bomb.check = true;
                BombermanGame.getBombLists().add(bomb);
            }
        } else if (status == BOMBER_STATUS.KILLED) {
            if (animate >= 24) respawn();
        }
        if (move.check) {
            Bomb bomb = new Bomb(getXTile(), getYTile(), Sprite.bomb.getFxImage());
            bomb.check = true;
            BombermanGame.getBombLists().add(bomb);
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (status == BOMBER_STATUS.ACTIVE) {
            if (other instanceof Enemy) kill();
        }
    }

    public void addControl(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.W) move.up = true;
            if (event.getCode() == KeyCode.S) move.down = true;
            if (event.getCode() == KeyCode.A) move.left = true;
            if (event.getCode() == KeyCode.D) move.right = true;
            if (event.getCode() == KeyCode.SPACE) move.check = true;
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.W) move.up = false;
            if (event.getCode() == KeyCode.S) move.down = false;
            if (event.getCode() == KeyCode.A) move.left = false;
            if (event.getCode() == KeyCode.D) move.right = false;
            if (event.getCode() == KeyCode.SPACE) move.check = false;
            if (event.getCode() == KeyCode.R) kill();
        });
    }

    public void kill() {
        status = BOMBER_STATUS.KILLED;
        animate = 0;
    }

    public void respawn() {
        status = BOMBER_STATUS.SPAWN;
        animate = 0;

        int randomX = 1 + (int) (Math.random() * 1000) % 29;
        int randomY = 1 + (int) (Math.random() * 1000) % 11;

        x = Sprite.SCALED_SIZE * randomX;
        y = Sprite.SCALED_SIZE * randomY;
        lives --;
        //if (lives == 0) dead();
    }

    public void dead() {
        status = BOMBER_STATUS.DEAD;
        System.exit(0);
    }
}
