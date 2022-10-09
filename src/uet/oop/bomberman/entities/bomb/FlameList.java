package uet.oop.bomberman.entities.bomb;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.BombermanGame;

import java.util.ArrayList;
import java.util.List;

public class FlameList {
    private final List<Flame> flames = new ArrayList<>();

    public FlameList(int xUnit, int yUnit, int length) {
        flames.add(new Flame(xUnit, yUnit, Flame.FLAME_TYPE.CENTER));
        for (int i = 1; i <= length; i++) {
            if (i == length) flames.add(new Flame(xUnit, yUnit - length, Flame.FLAME_TYPE.UP));
            else flames.add(new Flame(xUnit, yUnit - i, Flame.FLAME_TYPE.VERTICAL));
            if (BombermanGame.getMap().containsKey(xUnit, yUnit - i)) break;
        }
        for (int i = 1; i <= length; i++) {
            if (i == length) flames.add(new Flame(xUnit, yUnit + length, Flame.FLAME_TYPE.DOWN));
            else flames.add(new Flame(xUnit, yUnit + i, Flame.FLAME_TYPE.VERTICAL));
            if (BombermanGame.getMap().containsKey(xUnit, yUnit + i)) break;
        }
        for (int i = 1; i <= length; i++) {
            if (i == length) flames.add(new Flame(xUnit - length, yUnit, Flame.FLAME_TYPE.LEFT));
            else flames.add(new Flame(xUnit - i, yUnit, Flame.FLAME_TYPE.HORIZONTAL));
            if (BombermanGame.getMap().containsKey(xUnit - i, yUnit)) break;
        }
        for (int i = 1; i <= length; i++) {
            if (i == length) flames.add(new Flame(xUnit + length, yUnit, Flame.FLAME_TYPE.RIGHT));
            else flames.add(new Flame(xUnit + i, yUnit, Flame.FLAME_TYPE.HORIZONTAL));
            if (BombermanGame.getMap().containsKey(xUnit + i, yUnit)) break;
        }
    }

    public void active() {
        for (Flame flame : getFlameList()) flame.active();
    }

    public void render(GraphicsContext gc) {
        for (Flame flame : getFlameList()) flame.render(gc);
    }

    public void update() {
        for (Flame flame : getFlameList()) flame.update();
    }

    public List<Flame> getFlameList() {
        return flames;
    }
}
