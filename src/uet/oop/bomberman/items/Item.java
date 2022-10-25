package uet.oop.bomberman.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.scene.GameScene;

public class Item extends Entity {
    public enum ITEM_STATUS {
        BRICK, ITEM, DELETE
    }

    private ITEM_STATUS status = ITEM_STATUS.BRICK;

    public Item(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        if (status == ITEM_STATUS.DELETE) {
            return;
        }
        if (!GameScene.getMap().containsKey((int) (x), (int) (y))) {
            status = ITEM_STATUS.ITEM;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(status == ITEM_STATUS.ITEM) {
            gc.drawImage(img, x, y);
        }
    }

    public void detele() {
        status = ITEM_STATUS.DELETE;

    }
    public ITEM_STATUS getStatus() {
        return status;
    }

    @Override
    public void handleCollision(Entity other) {
        if (other instanceof Bomber && status == ITEM_STATUS.ITEM) {
            detele();
        }
    }


}
