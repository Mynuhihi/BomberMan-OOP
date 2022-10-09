package uet.oop.bomberman.entities.bomb;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomb extends Entity {
    public static final int ANIMATE_TIME = 48;

    public enum BOMB_STATUS {
        ACTIVE, EXPLORED, DELETED
    }

    private BOMB_STATUS status = BOMB_STATUS.ACTIVE;
    private FlameList flames;
    private int animate = 0;

    /**
     * Khoi tao flame khi tao bomb nhung flame phai duoc kich hoat thi moi hoat dong.
     */
    public Bomb(int xUnit, int yUnit, int length) {
        super(xUnit, yUnit, null);
        flames = new FlameList(xUnit, yUnit, length);
    }

    /**
     * Kiem tra trang thai:
     * ACTIVE (binh thuong) -> render animation
     * EXPLORED (sau khi no) -> render flameList.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (status == BOMB_STATUS.ACTIVE) {
            img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, ANIMATE_TIME).getFxImage();
            gc.drawImage(img, x, y);
        } else if (status == BOMB_STATUS.EXPLORED) {
            flames.render(gc);
        }
    }

    /**
     * sau moi lan update bien animate tang them 1 (tang them 60 trong 1 giay)
     * Kiem tra trang thai:
     * ACTIVE (binh thuong) -> sau 2 giay (animate = 120) no: goi ham explore()
     * EXPLORED (sau khi no) -> update flameList, sau khi het thoi gian animation cua flame thi xoa: goi ham delete().
     */
    @Override
    public void update() {
        animate++;
        if (status == BOMB_STATUS.ACTIVE) {
            if (animate >= 120) explore();
        } else if (status == BOMB_STATUS.EXPLORED) {
            flames.update();
            if (animate >= Flame.ANIMATE_TIME) delete();
        }
    }

    /**
     * Xu li va cham:
     * Khi bomb chua no (ACTIVE) neu va cham voi Flame thi no: goi ham explore().
     */
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

    /**
     * Bom no:
     * Dat trang thai da no (EXPLORED)
     * Dat lai bien animate (de bat dau tinh tu luc no)
     * Kich hoat flameList (flameList sau khi kich hoat thi moi render).
     */
    private void explore() {
        status = BOMB_STATUS.EXPLORED;
        animate = 0;
        flames.active();
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
