package uet.oop.bomberman.entities.map;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

public class Map {
    private final HashMap<Integer, MapEntity> map = new HashMap<>(400);

    private int hash(int xUnit, int yUnit) {
        return xUnit * 100 + yUnit;
    }

    public void add(MapEntity entity) {
        int xUnit = (int) (entity.getX() / Sprite.SCALED_SIZE);
        int yUnit = (int) (entity.getY() / Sprite.SCALED_SIZE);
        if (!map.containsKey(hash(xUnit, yUnit))) {
            map.put(hash(xUnit, yUnit), entity);
        }
    }
    public boolean containsKey(int xUnit, int yUnit) {
        return map.containsKey(hash(xUnit, yUnit));
    }

    public void render(GraphicsContext gc) {
        for (MapEntity e : map.values()) {
            e.render(gc);
        }
    }

    public void update() {
        Iterator<MapEntity> it = map.values().iterator();
        while (it.hasNext()) {
            MapEntity entity = it.next();
            if (entity instanceof Brick) {
                Brick brick = (Brick) entity;
                if (brick.getStatus() == Brick.BRICK_STATUS.DELETED) it.remove();
                brick.update();
            }
        }
    }

    public List<MapEntity> getMap() {
        return new ArrayList<>(map.values());
    }
}
