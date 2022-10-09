package uet.oop.bomberman.entities.bomb;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BombList {
    private final HashMap<Integer, Bomb> bombList = new HashMap<>();

    private int hash(int xUnit, int yUnit) {
        return xUnit * 100 + yUnit;
    }

    public void addBomb(int xUnit, int yUnit, int length) {
        if (!bombList.containsKey(hash(xUnit, yUnit))) {
            Bomb bomb = new Bomb(xUnit, yUnit, length);
            bombList.put(hash(xUnit, yUnit), bomb);
        }
    }

    public void render(GraphicsContext gc) {
        for (Bomb bomb : bombList.values()) {
            bomb.render(gc);
        }
    }

    public void update() {
        Iterator<Bomb> it = bombList.values().iterator();
        while (it.hasNext()) {
            Bomb bomb = it.next();
            if (bomb.getStatus() == Bomb.BOMB_STATUS.DELETED) it.remove();
            bomb.update();
        }
    }

    public int size() {
        return bombList.size();
    }

    public List<Bomb> getBombList() {
        return new ArrayList<>(bombList.values());
    }

    public List<Flame> getFlameList() {
        List<Flame> flameList = new ArrayList<>();
        for (Bomb bomb : bombList.values()) {
            flameList.addAll(bomb.getFlameList());
        }
        return flameList;
    }
}
