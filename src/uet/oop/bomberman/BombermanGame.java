package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.scene.MenuScene;
import uet.oop.bomberman.scene.Scenes;

import java.io.IOException;

public class BombermanGame extends Application {
    public static final double STAGE_WIDTH = 720;
    public static final double STAGE_HEIGHT = 675;
    public static int HIGH_SCORE = 0;
    public static int CURRENT_LEVEL = 1;
    public static int MAX_LEVEL = 4;

    private static Scenes currentScene = new MenuScene(new Group());

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        try {
            HIGH_SCORE = Integer.parseInt(IO.readFromFile("res/data/highscore.txt"));
            CURRENT_LEVEL = Integer.parseInt(IO.readFromFile("res/data/curlevel.txt"));
            MAX_LEVEL = Integer.parseInt(IO.readFromFile("res/data/maxlevel.txt"));
        } catch (IOException e) {
            System.exit(1);
        }

        stage.setTitle("Bomberman");
        stage.sizeToScene();
        stage.getIcons().add(Sprite.balloom_left1.getFxImage());
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                stage.setScene(currentScene);
                currentScene.show();
            }
        };
        timer.start();
    }

    public static void setScene(Scenes scene) {
        BombermanGame.currentScene = scene;
    }
}