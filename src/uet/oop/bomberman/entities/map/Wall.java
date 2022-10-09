package uet.oop.bomberman.entities.map;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Wall extends MapEntity {

    public Wall(double x, double y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }

    @Override
    public void handleCollision(Entity other) {

    }
}
