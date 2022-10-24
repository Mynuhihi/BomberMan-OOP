package uet.oop.bomberman;

import uet.oop.bomberman.entities.Bomber;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class IO {
    public static <T>void writeToFile(T data, String path) throws IOException {
        FileWriter out = new FileWriter(path);
        out.write(data.toString());
        out.close();
    }

    public static String readFromFile(String path) throws IOException {
        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);
        String data = scanner.next();
        reader.close();
        return data;
    }

    public static void writeToBomberFile(int score, int life, int speedLevel, int bombLength, int maxBomb) {
        try {
            FileWriter out = new FileWriter("res/data/bomber.txt");
            out.write(score + " ");
            out.write(life + " ");
            out.write(speedLevel + " ");
            out.write(bombLength + " ");
            out.write(maxBomb + " ");
            out.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public static void writeBomberData(Bomber b) {
        writeToBomberFile(b.getScore(), b.getLife(), b.getSpeedLevel(), b.getBombLength(), b.getMaxBomb());
    }

    public static void readBomberData(Bomber b) {
        try {
            FileReader reader = new FileReader("res/data/bomber.txt");
            Scanner scanner = new Scanner(reader);
            b.setScore(scanner.nextInt());
            b.setLife(scanner.nextInt());
            b.setSpeedLevel(scanner.nextInt());
            b.setBombLength(scanner.nextInt());
            b.setMaxBomb(scanner.nextInt());
            reader.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public static void newGame() {
        try {
            IO.writeToFile(1, "res/data/curlevel.txt");
            BombermanGame.CURRENT_LEVEL = 1;
            IO.writeToBomberFile(0, 2, 0, 1, 1);
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
