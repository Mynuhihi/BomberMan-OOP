package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.BombList;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.items.BombItem;
import uet.oop.bomberman.entities.items.FlameItem;
import uet.oop.bomberman.entities.items.Item;
import uet.oop.bomberman.entities.items.SpeedItem;
import uet.oop.bomberman.scene.GameScene;
import uet.oop.bomberman.sounds.Sound;

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
    private int animate = 0;

    private int score = 0;
    private int life = 2;
    private int speedLevel = 0;
    private int bombLength = 1;
    private int maxBomb = 1;

    private final MediaPlayer bomberVerticalMoveSound = Sound.verticalMoveSound.getMediaPlayer();
    private final MediaPlayer bomberHorizontalMoveSound = Sound.horizontalMoveSound.getMediaPlayer();
    private final MediaPlayer bomberGetItemSound = Sound.getItemSound.getMediaPlayer();
    private final MediaPlayer bomberKillSound = Sound.killSound.getMediaPlayer();

    public Bomber(double x, double y, Image img) {
        super(x, y, img);
        width = Sprite.SCALED_SIZE * 0.75;
    }

    @Override
    public void render(GraphicsContext gc) {
        animate++;
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

    @Override
    public void update() {
        bombList.update();
        updateSound();

        if (status == BOMBER_STATUS.SPAWN) {
            if (animate >= 60) status = BOMBER_STATUS.ACTIVE;
        }
        if (status == BOMBER_STATUS.ACTIVE || status == BOMBER_STATUS.SPAWN) {
            double speed = 0.8 * Sprite.SCALED_SIZE / Sprite.DEFAULT_SIZE;
            if (move.up) y -= speed * (1 + speedLevel * 0.2);
            if (move.down) y += speed * (1 + speedLevel * 0.2);
            if (move.left) x -= speed * (1 + speedLevel * 0.2);
            if (move.right) x += speed * (1 + speedLevel * 0.2);
        } else if (status == BOMBER_STATUS.KILLED) {
            if (animate >= 24) respawn();
        }
    }

    public void updateSound() {
        if (move.left || move.right) Sound.playSoundLoop(bomberHorizontalMoveSound, 200, 0.1);
        else bomberHorizontalMoveSound.stop();
        if (move.up || move.down) Sound.playSoundLoop(bomberVerticalMoveSound, 200, 0.1);
        else bomberVerticalMoveSound.stop();
    }

    public void stopSound() {
        bomberHorizontalMoveSound.stop();
        bomberVerticalMoveSound.stop();
        bomberGetItemSound.stop();
        bomberKillSound.stop();
    }

    @Override
    public void handleCollision(Entity other) {
        if (status == BOMBER_STATUS.ACTIVE) {
            if (other instanceof Enemy) {
                Enemy enemy = (Enemy) other;
                if (enemy.getStatus() == Enemy.ENEMY_STATUS.ACTIVE) kill();
            }
            if (other instanceof Flame) {
                Flame fl = (Flame) other;
                if (fl.getStatus() == Flame.FLAME_STATUS.ACTIVE) kill();
            }
            if (other instanceof BombItem) {
                BombItem bt = (BombItem) other;
                if (bt.getStatus() == Item.ITEM_STATUS.ITEM) {
                    maxBomb++;
                }
            }
            if (other instanceof FlameItem) {
                FlameItem ft = (FlameItem) other;
                if (ft.getStatus() == Item.ITEM_STATUS.ITEM) {
                    bombLength++;
                }
            }
            if (other instanceof SpeedItem) {
                SpeedItem st = (SpeedItem) other;
                if (st.getStatus() == Item.ITEM_STATUS.ITEM) {
                    speedLevel++;
                }
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

        Sound.playSound(bomberKillSound, 600, 0.1);
    }

    public void respawn() {
        status = BOMBER_STATUS.SPAWN;
        animate = 0;

        life--;
        if (life < 0) dead();
        else {
            x = Sprite.SCALED_SIZE;
            y = Sprite.SCALED_SIZE;
        }
    }

    public void dead() {
        status = BOMBER_STATUS.DEAD;
    }

    void addBomb() {
        if (bombList.size() < maxBomb) {
            int xUnit = (int) Math.round(x / Sprite.SCALED_SIZE);
            int yUnit = (int) Math.round(y / Sprite.SCALED_SIZE);
            int xPortal = (int) Math.round(GameScene.getPortal().x / Sprite.SCALED_SIZE);
            int yPortal = (int) Math.round(GameScene.getPortal().y / Sprite.SCALED_SIZE);

            if (xUnit != xPortal || yUnit != yPortal) {
                bombList.addBomb(xUnit, yUnit, bombLength);
            }
        }
    }

    public List<Bomb> getBombList() {
        return bombList.getBombList();
    }

    public List<Flame> getFlameList() {
        return bombList.getFlameList();
    }

    public BOMBER_STATUS getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public int getBombLength() {
        return bombLength;
    }

    public int getMaxBomb() {
        return maxBomb;
    }

    public void setStatus(BOMBER_STATUS status) {
        this.status = status;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public void setBombLength(int bombLength) {
        this.bombLength = bombLength;
    }

    public void setMaxBomb(int maxBomb) {
        this.maxBomb = maxBomb;
    }
}