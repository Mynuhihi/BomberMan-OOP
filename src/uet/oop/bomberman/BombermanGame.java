package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.scene.MenuScene;
import uet.oop.bomberman.scene.Scenes;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class BombermanGame extends Application {
    public static final double STAGE_WIDTH = 720;
    public static final double STAGE_HEIGHT = 675;
    public static int HIGH_SCORE = 0;
    private static Scenes currentScene = new MenuScene(new Group());

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        readHighScore();

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

    public static void readHighScore() {
        try {
            FileReader reader = new FileReader("res/data/highscore.txt");
            Scanner scanner = new Scanner(reader);
            HIGH_SCORE = scanner.nextInt();
            reader.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }
}