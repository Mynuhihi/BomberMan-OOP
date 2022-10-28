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
import uet.oop.bomberman.entities.items.BombItem;
import uet.oop.bomberman.entities.items.FlameItem;
import uet.oop.bomberman.entities.items.Item;
import uet.oop.bomberman.entities.items.SpeedItem;
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
    private static Map map = new Map();
    private static Item item;
    private static Portal portal;

    private int time = 200;
    private boolean isStopped = false;

    private static MediaPlayer soundtrack;

    public GameScene(Group root) {
        super(root);
        init();

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        scoreboard = new Canvas(BombermanGame.STAGE_WIDTH, 51);
        root.getChildren().addAll(canvas, scoreboard);

        bomber.addControl(this);
        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (!isStopped) {
                    soundtrack.stop();
                    bomber.stopSound();
                    BombermanGame.setScene(new MenuScene(new Group()));
                }
            }
        });

        this.setCamera(camera);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time--;
                if (time == 0) timer.cancel();
            }
        }, 1000, 1000);


    }

    private void init() {
        enemyList = new ArrayList<>();
        map = new Map();
        portal = null;
        item = null;

        try {
            String level = getLevelPath();
            createMap(level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        IO.readBomberData(bomber);

        soundtrack = Sound.gameSound.getMediaPlayer();
        soundtrack.setVolume(0.1);
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
                    item = new BombItem(j, i, Sprite.powerup_bombs.getFxImage());
                } else if (line.charAt(j) == 'f') {
                    map.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    item = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
                } else if (line.charAt(j) == 's') {
                    map.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    item = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
                } else if (line.charAt(j) == 'x') {
                    map.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    portal = new Portal(j, i, Sprite.portal.getFxImage());
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
            item.checkCollision(e, !(e instanceof BrickPass));
            portal.checkCollision(e, !(e instanceof BrickPass));
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
        bomber.checkCollision(item, false);
    }

    public void update() {
        map.update();
        bomber.update();

        Iterator<Enemy> it = enemyList.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (e.getStatus() == Enemy.ENEMY_STATUS.DELETED) it.remove();
            e.update();
        }

        item.update();

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
        gc.setFill(Color.rgb(56, 135, 0));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        portal.render(gc);
        item.render(gc);
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

    @Override
    public void show() {
        if (!isStopped) {
            render();
            update();
            checkAllCollisions();

            if (bomber.getStatus() == Bomber.BOMBER_STATUS.DEAD) gameOver();
            if (enemyList.size() == 0 && isBomberInPortal()) nextLevel();
        }
    }

    public boolean isBomberInPortal() {
        double bomberXCenter = bomber.getX() + 18;
        double bomberYCenter = bomber.getY() + 18;
        double portalXCenter = portal.getX() + 24;
        double portalYCenter = portal.getY() + 24;
        double distance = Math.sqrt(Math.pow(bomberXCenter - portalXCenter, 2) + Math.pow(bomberYCenter - portalYCenter, 2));
        return distance < Sprite.SCALED_SIZE / 4.0;
    }

    public void nextLevel() {
        try {
            isStopped = true;
            bomber.stopSound();
            soundtrack.stop();

            MediaPlayer portalSound = Sound.nextLevelSound.getMediaPlayer();
            Sound.playSound(portalSound, 3000, 0.1);

            if (bomber.getScore() > BombermanGame.HIGH_SCORE) {
                BombermanGame.HIGH_SCORE = bomber.getScore();
                IO.writeToFile(bomber.getScore(), "res/data/highscore.txt");
            }
            if (BombermanGame.CURRENT_LEVEL == BombermanGame.MAX_LEVEL) {
                IO.writeBomberData(bomber);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        BombermanGame.setScene(new WinScene(new Group()));
                    }
                }, 3000);
            } else {
                BombermanGame.CURRENT_LEVEL++;
                IO.writeToFile(BombermanGame.CURRENT_LEVEL, "res/data/curlevel.txt");
                IO.writeBomberData(bomber);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        BombermanGame.setScene(new LevelScene(new Group()));
                    }
                }, 3000);
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public void gameOver() {
        try {
            isStopped = true;
            bomber.stopSound();
            soundtrack.stop();

            MediaPlayer bomberDeadSound = Sound.deadSound.getMediaPlayer();
            Sound.playSound(bomberDeadSound, 3000, 0.1);

            if (bomber.getScore() > BombermanGame.HIGH_SCORE) {
                BombermanGame.HIGH_SCORE = bomber.getScore();
                IO.writeToFile(bomber.getScore(), "res/data/highscore.txt");
            }
            IO.newGame();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timer.cancel();
                    BombermanGame.setScene(new GameOverScene(new Group()));
                }
            }, 3000);
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public String getLevelPath() {
        if (1 <= BombermanGame.CURRENT_LEVEL && BombermanGame.CURRENT_LEVEL <= BombermanGame.MAX_LEVEL) {
            return "res/levels/Level" + BombermanGame.CURRENT_LEVEL + ".txt";
        } else return null;
    }

    public static Bomber getBomber() {
        return bomber;
    }

    public static Map getMap() {
        return map;
    }

    public static Portal getPortal() {
        return portal;
    }

    public static List<Enemy> getEnemyList() {
        return enemyList;
    }

    public static int getScore() {
        return bomber.getScore();
    }

    public static void addScore(int score) {
        bomber.setScore(bomber.getScore() + score);
    }

    public static void setSoundtrack(MediaPlayer player) {
        soundtrack.stop();
        soundtrack = player;
        soundtrack.setVolume(0.1);
        soundtrack.play();
    }
}
