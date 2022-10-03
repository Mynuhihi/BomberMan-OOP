package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Objects;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected double x;
    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected double y;
    protected double width;
    protected double height;

    protected Image img;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity(double xUnit, double yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        width = Sprite.SCALED_SIZE;
        height = Sprite.SCALED_SIZE;
        this.img = img;
    }

    public Entity() {

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }

    public abstract void update();

    public abstract void handleCollision(Entity other);

    public void checkCollision(Entity other, boolean isBlocked) {
        double dx = (other.x + other.width / 2) - (x + width / 2);
        double dy = (other.y + other.height / 2) - (y + height / 2);
        double intersectX = Math.abs(dx) - (width + other.width) / 2;
        double intersectY = Math.abs(dy) - (height + other.height) / 2;

        if (intersectX < 0 && intersectY < 0) {
            if (isBlocked) {
                if (intersectX > intersectY && dx > 0) other.x -= intersectX;
                else if (intersectX > intersectY && dx < 0) other.x += intersectX;
                else if (intersectX < intersectY && dy > 0) other.y -= intersectY;
                else other.y += intersectY;
            }

            this.handleCollision(other);
            other.handleCollision(this);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
