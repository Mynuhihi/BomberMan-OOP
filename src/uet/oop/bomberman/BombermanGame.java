package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BombermanGame extends Application{
    //kich thuoc stage
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;
    public static final double STAGE_WIDTH = 16 * Sprite.SCALED_SIZE;
    public static final double STAGE_HEIGHT = 13 * Sprite.SCALED_SIZE + 47;

    private GraphicsContext gc;
    private Canvas canvas;
    private static List<Entity> entities = new ArrayList<>();
    private static List<Entity> stillObjects = new ArrayList<>();
    private static List<Entity> bombLists = new ArrayList<>();

    private String level = "res/levels/Level1.txt";

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);

        stage.setTitle("BombermanGame");
        stage.setWidth(STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.getIcons().add(Sprite.balloom_left1.getFxImage());
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
                checkAllCollisions();
            }
        };
        timer.start();

        try {
            createMap(level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Bomber
        Bomber bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);

        bomberman.addControl(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
    }

    public void createMap(String path) throws FileNotFoundException {
        FileInputStream reader = new FileInputStream(path);
        Scanner scanner = new Scanner(reader);
        int lv = scanner.nextInt();
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        String line = scanner.nextLine();

        for (int i = 0; i < row; i++) {
            line = scanner.nextLine();
            for (int j = 0; j < col; j++) {
                Entity object;
                if (line.charAt(j) == '#') {
                    object = new Wall(j, i, Sprite.wall.getFxImage());
                } else if (line.charAt(j) == '*') {
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else {
                    object = new Grass(j, i, Sprite.grass.getFxImage());
                }
                stillObjects.add(object);
            }
        }

        scanner.close();
    }

    public void checkAllCollisions() {
        for (Entity entity : stillObjects) {
            if (entity instanceof Wall) {
                Wall wall = (Wall) entity;
                for (Entity e : entities)
                    wall.checkCollision(e, true);
            }
            if (entity instanceof Brick) {
                Brick brick = (Brick) entity;
                for (Entity e : entities)
                    brick.checkCollision(e, true);
            }
        }
    }

    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        for (Entity entity : bombLists) {
            entities.add(entity);
        }
        bombLists.clear();
    }

    public static List<Entity> getStillObjects() {
        return stillObjects;
    }

    public static List<Entity> getEntities() {
        return entities;
    }

    public static List<Entity> getBombLists() {
        return bombLists;
    }

}
