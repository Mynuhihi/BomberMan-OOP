package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.bomb.*;
import uet.oop.bomberman.entities.enemy.*;
import uet.oop.bomberman.entities.enemy.chaseEnemy.*;
import uet.oop.bomberman.entities.enemy.randomEnemy.*;
import uet.oop.bomberman.entities.map.*;
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
    public static final double STAGE_WIDTH = 32 * Sprite.SCALED_SIZE;
    public static final double STAGE_HEIGHT = 13 * Sprite.SCALED_SIZE + 47;

    private GraphicsContext gc;
    private Canvas canvas;

    private static Bomber bomber;
    private static List<Enemy> enemy = new ArrayList<>();
    private static Map mapEntities = new Map(); // cai grass de lam background

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

        bomber.addControl(scene);

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
                if (line.charAt(j) == '#') {
                    mapEntities.add(new Wall(j, i, Sprite.wall.getFxImage()));
                } else if (line.charAt(j) == '*') {
                    mapEntities.add(new Brick(j, i, Sprite.brick.getFxImage()));
                } else if (line.charAt(j) == 'p') {
                    bomber = new Bomber(j, i, Sprite.player_right.getFxImage());
                } else if (line.charAt(j) == '1') {
                    enemy.add(new Balloom(j, i));
                } else if (line.charAt(j) == '2') {
                    enemy.add(new Oneal(j, i));
                } else if (line.charAt(j) == '3') {
                    enemy.add(new Doll(j, i));
                } else if (line.charAt(j) == '4') {
                    enemy.add(new Minvo(j, i));
                } else if (line.charAt(j) == '5') {
                    enemy.add(new Kondoria(j, i));
                } else if (line.charAt(j) == '6') {
                    enemy.add(new Ovape(j, i));
                }
            }
        }

        scanner.close();
    }

    public void checkAllCollisions() {
        // Kiem tra va cham all enemy voi all brick/wall, bomber, all bomb (enemy khong di qua bomb duoc)
        for (Entity e : enemy) {
            for (Entity map : mapEntities.getMap()) {
                map.checkCollision(e, !(e instanceof BrickPass && map instanceof Brick));
            }
            for (Bomb bomb : bomber.getBombList()) {
                bomb.checkCollision(e, true);
            }
            bomber.checkCollision(e, false);
        }
        // Kiem tra va cham all brick/wall voi bomber
        for (Entity entity : mapEntities.getMap()) {
            entity.checkCollision(bomber, true);
        }
        // Kiem tra va cham all flame voi all enemy, all brick/wall, all bomb, bomber
        for (Flame fl : bomber.getFlameList()) {
            for (Entity e : mapEntities.getMap()) {
                e.checkCollision(fl, false);
            }
            for (Entity e : enemy) {
                fl.checkCollision(e, false);
            }
            for (Bomb b : bomber.getBombList()) {
                fl.checkCollision(b, false);
            }
            bomber.checkCollision(fl, false);
        }
    }

    public void update() {
//        mapEntities.forEach(Entity::update);
        mapEntities.update();
        enemy.forEach(Entity::update);
        bomber.update();
    }

    public void render() {
        // Set background cho graphicsContext
        gc.setFill(Color.rgb(80, 160, 0));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Ve cac entity
//        mapEntities.forEach(g -> g.render(gc));
        mapEntities.render(gc);
        bomber.render(gc);
        enemy.forEach(g -> g.render(gc));
    }

    public static Entity getBomber() {
        return bomber;
    }

    public static Map getMap() {
        return mapEntities;
    }
}