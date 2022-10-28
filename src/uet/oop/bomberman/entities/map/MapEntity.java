package uet.oop.bomberman.entities.map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public abstract class MapEntity extends Entity {
    public MapEntity(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public void render(GraphicsContext gc) {
        super.render(gc);
    }
}
