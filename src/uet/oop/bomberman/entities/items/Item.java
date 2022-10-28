package uet.oop.bomberman.entities.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.scene.GameScene;
import uet.oop.bomberman.sounds.Sound;

public class Item extends Entity {
    public enum ITEM_STATUS {
        BRICK, ITEM, DELETED
    }

    public ITEM_STATUS status = ITEM_STATUS.BRICK;

    public Item(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        if (status == ITEM_STATUS.DELETED) return;
        if (!GameScene.getMap().containsKey((int) x / 48, (int) y / 48)) status = ITEM_STATUS.ITEM;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (status == ITEM_STATUS.ITEM) {
            gc.drawImage(img, x, y);
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (other instanceof Bomber && status == ITEM_STATUS.ITEM) delete();
    }

    public void delete() {
        MediaPlayer mediaPlayer = Sound.getItemSound.getMediaPlayer();
        Sound.playSound(mediaPlayer, 200, 0.1);
        MediaPlayer powerUpSound = Sound.powerUpSound.getMediaPlayer();
        GameScene.setSoundtrack(powerUpSound);
        status = ITEM_STATUS.DELETED;
    }

    public ITEM_STATUS getStatus() {
        return status;
    }
}
