package uet.oop.bomberman.entities.bomb;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sounds.Sound;

import java.util.List;

public class Bomb extends Entity {
    public static final int ANIMATE_TIME = 60;

    public Bomb(double v, double v1, Object o) {
    }

    public enum BOMB_STATUS {
        ACTIVE, EXPLORED, DELETED
    }

    private BOMB_STATUS status = BOMB_STATUS.ACTIVE;
    private FlameList flames;
    private int animate = 0;

    public Bomb(int xUnit, int yUnit, int length) {
        super(xUnit, yUnit, null);
        MediaPlayer putBombSound = Sound.putBombSound.getMediaPlayer();
        Sound.playSound(putBombSound, 200, 0.1);
        flames = new FlameList(xUnit, yUnit, length);
    }

    @Override
    public void render(GraphicsContext gc) {
        animate++;
        if (status == BOMB_STATUS.ACTIVE) {
            img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, Sprite.bomb_1, animate, ANIMATE_TIME).getFxImage();
            gc.drawImage(img, x, y);
        } else if (status == BOMB_STATUS.EXPLORED) {
            flames.render(gc);
        }
    }

    @Override
    public void update() {
        if (status == BOMB_STATUS.ACTIVE) {
            if (animate >= 120) explore();
        } else if (status == BOMB_STATUS.EXPLORED) {
            flames.update();
            if (animate >= Flame.ANIMATE_TIME) delete();
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (status == BOMB_STATUS.ACTIVE) {
            if (other instanceof Flame) {
                Flame fl = (Flame) other;
                if (fl.getStatus() == Flame.FLAME_STATUS.ACTIVE)
                    explore();
            }
        }
    }

    private void explore() {
        status = BOMB_STATUS.EXPLORED;
        animate = 0;
        flames.active();

        MediaPlayer explore = Sound.exploreSound.getMediaPlayer();
        Sound.playSound(explore, 1200, 0.1);
    }

    public void delete() {
        status = BOMB_STATUS.DELETED;
    }

    public BOMB_STATUS getStatus() {
        return status;
    }

    public List<Flame> getFlameList() {
        return flames.getFlameList();
    }
}
