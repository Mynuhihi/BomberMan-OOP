package uet.oop.bomberman.scene;

import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.bomb.*;
import uet.oop.bomberman.entities.enemy.*;
import uet.oop.bomberman.entities.enemy.chaseEnemy.*;
import uet.oop.bomberman.entities.enemy.randomEnemy.*;
import uet.oop.bomberman.entities.map.*;
import uet.oop.bomberman.entities.map.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.io.*;
import java.util.*;

import static uet.oop.bomberman.BombermanGame.HIGH_SCORE;

public class GameScene extends Scenes {
    private double CANVAS_WIDTH;
    private double CANVAS_HEIGHT;

    private Camera camera = new ParallelCamera();
    private Canvas scoreboard;
    private GraphicsContext gc;
    private Canvas canvas;

    private static Bomber bomber;
    private static List<Enemy> enemyList = new ArrayList<>();
    private static Map map = new Map();

    private boolean pause = false;
    private static int score = 0;
    private Timer timer = new Timer();
    private int time = 200;

    private String level = "res/levels/Level1.txt";

    public GameScene(Group root) {
        super(root);
        enemyList = new ArrayList<>();
        map = new Map();
        score = 0;

        try {
            createMap(level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        scoreboard = new Canvas(BombermanGame.STAGE_WIDTH, 51);
        root.getChildren().addAll(canvas, scoreboard);

        bomber.addControl(this);
        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) BombermanGame.setScene(new MenuScene(new Group()));
            if (event.getCode() == KeyCode.ENTER) pause = !pause;
        });

        this.setCamera(camera);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time--;
            }
        }, 1000, 1000);
    }

    public void createMap(String path) throws FileNotFoundException {
        FileInputStream reader = new FileInputStream(path);
        Scanner scanner = new Scanner(reader);
        int lv = scanner.nextInt();
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        String line = scanner.nextLine();

        CANVAS_HEIGHT = row * Sprite.SCALED_SIZE;
        CANVAS_WIDTH = col * Sprite.SCALED_SIZE;

        for (int i = 0; i < row; i++) {
            line = scanner.nextLine();
            for (int j = 0; j < col; j++) {
                if (line.charAt(j) == '#') {
                    map.add(new Wall(j, i, Sprite.wall.getFxImage()));
                } else if (line.charAt(j) == '*') {
                    map.add(new Brick(j, i, Sprite.brick.getFxImage()));
                } else if (line.charAt(j) == 'p') {
                    bomber = new Bomber(j, i, Sprite.player_right.getFxImage());
                } else if (line.charAt(j) == '1') {
                    enemyList.add(new Balloom(j, i));
                } else if (line.charAt(j) == '2') {
                    enemyList.add(new Oneal(j, i));
                } else if (line.charAt(j) == '3') {
                    enemyList.add(new Doll(j, i));
                } else if (line.charAt(j) == '4') {
                    enemyList.add(new Minvo(j, i));
                } else if (line.charAt(j) == '5') {
                    enemyList.add(new Kondoria(j, i));
                } else if (line.charAt(j) == '6') {
                    enemyList.add(new Ovape(j, i));
                }
            }
        }

        scanner.close();
    }

    public void checkAllCollisions() {
        // Kiem tra va cham all enemy voi all brick/wall, bomber, all bomb (enemy khong di qua bomb duoc)
        for (Entity e : enemyList) {
            for (Entity map : map.getMap()) {
                map.checkCollision(e, !(e instanceof BrickPass && map instanceof Brick));
            }
            for (Bomb bomb : bomber.getBombList()) {
                bomb.checkCollision(e, true);
            }
            bomber.checkCollision(e, false);
        }
        // Kiem tra va cham all brick/wall voi bomber
        for (Entity entity : map.getMap()) {
            entity.checkCollision(bomber, true);
        }
        // Kiem tra va cham all flame voi all enemy, all brick/wall, all bomb, bomber
        for (Flame fl : bomber.getFlameList()) {
            for (Entity e : map.getMap()) {
                e.checkCollision(fl, false);
            }
            for (Entity e : enemyList) {
                fl.checkCollision(e, false);
            }
            for (Bomb b : bomber.getBombList()) {
                fl.checkCollision(b, false);
            }
            bomber.checkCollision(fl, false);
        }
    }

    public void update() {
        if (time == 0) timer.cancel();

        map.update();
        bomber.update();
        if (bomber.getStatus() == Bomber.BOMBER_STATUS.DEAD) {
            updateHighScore();
            BombermanGame.setScene(new GameOverScene(new Group()));
        }

        Iterator<Enemy> it = enemyList.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (e.getStatus() == Enemy.ENEMY_STATUS.DELETED) it.remove();
            e.update();
        }
        if (enemyList.size() == 0) {
            updateHighScore();
            BombermanGame.setScene(new WinScene(new Group()));
        }

        updateCamera();
        updateScoreboard();
    }

    public void updateCamera() {
        camera.setLayoutX(bomber.getX() - BombermanGame.STAGE_WIDTH / 2);
        camera.setLayoutY(bomber.getY() - BombermanGame.STAGE_HEIGHT / 2);

        if (camera.getLayoutX() < 0) camera.setLayoutX(0);
        if (camera.getLayoutX() > CANVAS_WIDTH - BombermanGame.STAGE_WIDTH)
            camera.setLayoutX(CANVAS_WIDTH - BombermanGame.STAGE_WIDTH);
        if (camera.getLayoutY() < -51) camera.setLayoutY(-51);
        if (camera.getLayoutY() > CANVAS_HEIGHT - BombermanGame.STAGE_HEIGHT)
            camera.setLayoutY(CANVAS_HEIGHT - BombermanGame.STAGE_HEIGHT);
    }

    public void updateScoreboard() {
        scoreboard.setLayoutX(camera.getLayoutX());
        scoreboard.setLayoutY(camera.getLayoutY());
    }

    public void render() {
        gc.setFill(Color.rgb(80, 160, 0));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        map.render(gc);
        bomber.render(gc);
        enemyList.forEach(g -> g.render(gc));

        renderScoreboard();
    }

    public void renderScoreboard() {
        scoreboard.getGraphicsContext2D().setFill(Color.rgb(180, 180, 180));
        scoreboard.getGraphicsContext2D().fillRect(0, 0, scoreboard.getWidth(), scoreboard.getHeight());

        Scenes.renderTextCenter(scoreboard, String.format("TIME %3d   SCORE %7d   LEFT %2d", time, score, bomber.getLives()), 3, Color.WHITE, Color.BLACK);

        try {
            scoreboard.getGraphicsContext2D().setFont(Font.loadFont(new FileInputStream("res/font/Kongtext.ttf"), 20));
        } catch (FileNotFoundException e) {
            System.exit(1);
        }
    }

    public static Bomber getBomber() {
        return bomber;
    }

    public static Map getMap() {
        return map;
    }

    public static int getScore() {
        return score;
    }

    public static void addScore(int score) {
        GameScene.score += score;
    }

    @Override
    public void show() {
        if (!pause) {
            render();
            update();
            checkAllCollisions();
        }
    }

    public void updateHighScore() {
        if (score <= HIGH_SCORE) return;
        else {
            HIGH_SCORE = score;
            try {
                FileWriter out = new FileWriter("res/data/highscore.txt");
                out.write(Integer.toString(score));
                out.close();
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }
}
