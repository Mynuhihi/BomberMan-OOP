package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import sun.java2d.pipe.SpanClipRenderer;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;


public class Bomb extends Entity {

    boolean check = false;
    long timeNanos = System.nanoTime();
    int animate = 0;

    public void setImage(Image image) {
        this.img = image;
    }
    public Bomb(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        if ((System.nanoTime() - timeNanos)/1000000000 < 2 ) {
            animate++;
            setImage(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 18).getFxImage());
        } else { check = false;
            //BombermanGame.getBombLists().remove();
        }

    }

    @Override
    public void handleCollision(Entity other) {

    }
}
