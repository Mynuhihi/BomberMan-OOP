package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends Entity {
    static class Move {
        private boolean up = false;
        private boolean down = false;
        private boolean left = false;
        private boolean right = false;
        private boolean check = false;
    }

    private final Move move = new Move();
    private double speedItemBuff;
    private int animate = 0;

    public Bomber(double x, double y, Image img) {
        super(x, y, img);
        width = Sprite.SCALED_SIZE * 0.75;
        speedItemBuff = 1;
    }

    @Override
    public void update() {
        animate++;
        double speed = 0.8 * Sprite.SCALED_SIZE / Sprite.DEFAULT_SIZE;
        if (move.up) {
            y -= speed * speedItemBuff;
            img = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, animate, 18).getFxImage();
        }
        if (move.down) {
            y += speed * speedItemBuff;
            img = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, animate, 18).getFxImage();
        }
        if (move.left) {
            x -= speed * speedItemBuff;
            img = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, animate, 18).getFxImage();
        }
        if (move.right) {
            x += speed * speedItemBuff;
            img = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, animate, 18).getFxImage();
        }
        if (move.check) {
            Bomb bomb = new Bomb(getXTile(), getYTile(), Sprite.bomb.getFxImage());
            bomb.check = true;
            BombermanGame.getBombLists().add(bomb);
        }
    }

    @Override
    public void handleCollision(Entity other) {

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
        });
    }

    public void setSpeedItemBuff(double speedItemBuff) {
        this.speedItemBuff = speedItemBuff;
    }
}
