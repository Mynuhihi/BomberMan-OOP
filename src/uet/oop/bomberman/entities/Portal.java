package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.items.Item;
import uet.oop.bomberman.scene.GameScene;

public class Portal extends Entity {
    public enum PORTAL_STATUS {
        INACTIVE, ACTIVE
    }

    public PORTAL_STATUS status = PORTAL_STATUS.INACTIVE;

    public Portal(double x, double y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        if (!GameScene.getMap().containsKey((int) x / 48, (int) y / 48)) status = Portal.PORTAL_STATUS.ACTIVE;
    }

    @Override
    public void handleCollision(Entity other) {

    }

    public PORTAL_STATUS getStatus() {
        return status;
    }
}
