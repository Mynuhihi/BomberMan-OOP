package uet.oop.bomberman.scene;

import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.IO;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.bomb.*;
import uet.oop.bomberman.entities.enemy.*;
import uet.oop.bomberman.entities.enemy.chaseEnemy.*;
import uet.oop.bomberman.entities.enemy.randomEnemy.*;
import uet.oop.bomberman.entities.map.*;
import uet.oop.bomberman.entities.map.Map;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.items.BombItem;
import uet.oop.bomberman.items.FlameItem;
import uet.oop.bomberman.items.Item;
import uet.oop.bomberman.items.SpeedItem;
import uet.oop.bomberman.sounds.Sound;

import java.io.*;
import java.util.*;

public class GameScene extends Scenes {
    private double CANVAS_WIDTH;
    private double CANVAS_HEIGHT;

    private Camera camera = new ParallelCamera();
    private Canvas scoreboard;
    private GraphicsContext gc;
    private Canvas canvas;

    private static Bomber bomber;
    private static List<Enemy> enemyList = new ArrayList<>();
    private static List<Item> itemList = new ArrayList<>();
    private static Map map = new Map();

    private Timer timer = new Timer();
    private int time = 200;

    private MediaPlayer soundtrack = Sound.gameSound.getMediaPlayer();

    public GameScene(Group root) {
        super(root);
        enemyList = new ArrayList<>();
        map = new Map();

        try {
            String level = getLevelPath();
            createMap(level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        IO.readBomberData(bomber);

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        scoreboard = new Canvas(BombermanGame.STAGE_WIDTH, 51);
        root.getChildren().addAll(canvas, scoreboard);

        bomber.addControl(this);
        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                soundtrack.stop();
                bomber.stopSound();
                BombermanGame.setScene(new MenuScene(new Group()));
            }
        });

        this.setCamera(camera);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time--;
            }
        }, 1000, 1000);

        soundtrack.play();
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
                } else if (line.charAt(j) == 'b') {
                    map.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    itemList.add(new BombItem(j, i, Sprite.powerup_bombs.getFxImage()));
                } else if (line.charAt(j) == 'f') {
                    map.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    itemList.add(new FlameItem(j, i, Sprite.powerup_flames.getFxImage()));
                } else if (line.charAt(j) == 's') {
                    map.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    itemList.add(new SpeedItem(j, i, Sprite.powerup_speed.getFxImage()));
                }
            }
        }

        scanner.close();
    }

    public void checkAllCollisions() {
        // Kiem tra va cham all enemy voi all brick/wall, bomber.txt, all bomb (enemy khong di qua bomb duoc)
        for (Enemy e : enemyList) {
            for (Entity map : map.getMap()) {
                map.checkCollision(e, !(e instanceof BrickPass && map instanceof Brick));
            }
            if (e.getStatus() == Enemy.ENEMY_STATUS.ACTIVE) {
                for (Bomb bomb : bomber.getBombList()) {
                    bomb.checkCollision(e, true);
                }
            }
            bomber.checkCollision(e, false);
        }
        // Kiem tra va cham all brick/wall voi bomber.txt
        for (Entity entity : map.getMap()) {
            entity.checkCollision(bomber, true);
        }
        // Kiem tra va cham all flame voi all enemy, all brick/wall, all bomb, bomber.txt
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
        for (Item item : itemList) {
            bomber.checkCollision(item, false);
            for (Enemy enemy : enemyList) {
                item.checkCollision(enemy, true);
            }
        }
    }

    public void update() {
        if (time == 0) timer.cancel();

        map.update();
        bomber.update();
        if (bomber.getStatus() == Bomber.BOMBER_STATUS.DEAD) gameOver();

        Iterator<Enemy> it = enemyList.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (e.getStatus() == Enemy.ENEMY_STATUS.DELETED) it.remove();
            e.update();
        }
        if (enemyList.size() == 0) nextLevel();
        for (Item item: itemList) {
            item.update();
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

        itemList.forEach(g -> g.render(gc));
        map.render(gc);
        bomber.render(gc);
        enemyList.forEach(g -> g.render(gc));

        renderScoreboard();
    }

    public void renderScoreboard() {
        scoreboard.getGraphicsContext2D().setFill(Color.rgb(180, 180, 180));
        scoreboard.getGraphicsContext2D().fillRect(0, 0, scoreboard.getWidth(), scoreboard.getHeight());

        Scenes.renderTextCenter(scoreboard, String.format("TIME %3d   SCORE %7d   LEFT %2d", time, bomber.getScore(), bomber.getLife()), 3, Color.WHITE, Color.BLACK);

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
        return bomber.getScore();
    }

    public static void addScore(int score) {
        bomber.setScore(bomber.getScore() + score);
    }

    @Override
    public void show() {
        render();
        update();
        checkAllCollisions();
    }

    public void nextLevel() {
        try {
            bomber.stopSound();
            soundtrack.stop();
            if (bomber.getScore() > BombermanGame.HIGH_SCORE) {
                BombermanGame.HIGH_SCORE = bomber.getScore();
                IO.writeToFile(bomber.getScore(), "res/data/highscore.txt");
            }
            if (BombermanGame.CURRENT_LEVEL == BombermanGame.MAX_LEVEL) {
                IO.writeBomberData(bomber);
                BombermanGame.setScene(new WinScene(new Group()));
            } else {
                BombermanGame.CURRENT_LEVEL++;
                IO.writeToFile(BombermanGame.CURRENT_LEVEL, "res/data/curlevel.txt");
                IO.writeBomberData(bomber);
                BombermanGame.setScene(new LevelScene(new Group()));
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public void gameOver() {
        try {
            bomber.stopSound();
            soundtrack.stop();
            if (bomber.getScore() > BombermanGame.HIGH_SCORE) {
                BombermanGame.HIGH_SCORE = bomber.getScore();
                IO.writeToFile(bomber.getScore(), "res/data/highscore.txt");
            }
            IO.newGame();
            BombermanGame.setScene(new GameOverScene(new Group()));
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public String getLevelPath() {
        if (1 <= BombermanGame.CURRENT_LEVEL && BombermanGame.CURRENT_LEVEL <= BombermanGame.MAX_LEVEL) {
            return "res/levels/Level" + BombermanGame.CURRENT_LEVEL + ".txt";
        } else return null;
    }
}
