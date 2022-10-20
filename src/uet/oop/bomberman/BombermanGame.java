package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.scene.MenuScene;
import uet.oop.bomberman.scene.Scenes;

public class BombermanGame extends Application {
    public static final double STAGE_WIDTH = 720;
    public static final double STAGE_HEIGHT = 675;
    private static Scenes currentScene = new MenuScene(new Group());

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Bomberman");
        stage.sizeToScene();
        stage.getIcons().add(Sprite.balloom_left1.getFxImage());
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