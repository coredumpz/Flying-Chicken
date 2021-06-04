import java.io.*;
import java.util.ArrayList;

public class GameInfo implements Serializable {
    private int level, time, score, heightScore, eggNumber, difficultyLevel, health;
    private int fullTime, fullEggNumber, fullHealth;
    private String gamer;

    public GameInfo() {
    }

    public GameInfo(int level, int time, int score, int heightScore, String gamer, int shutNumber, int difficultyLevel, int fullTime, int fullEggNumber, int health, int fullHealth) {
        this.level = level;
        this.time = time;
        this.score = score;
        this.heightScore = heightScore;
        this.gamer = gamer;
        this.eggNumber = shutNumber;
        this.difficultyLevel = difficultyLevel;
        this.fullTime = fullTime;
        this.fullEggNumber = fullEggNumber;
        this.health = health;
        this.fullHealth = fullHealth;
    }

    public int getFullHealth() {
        return fullHealth;
    }

    public void setFullHealth(int fullHealth) {
        this.fullHealth = fullHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getFullTime() {
        return fullTime;
    }

    public void setFullTime(int fullTime) {
        this.fullTime = fullTime;
    }

    public int getFullEggNumber() {
        return fullEggNumber;
    }

    public void setFullEggNumber(int fullEggNumber) {
        this.fullEggNumber = fullEggNumber;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getEggNumber() {
        return eggNumber;
    }

    public void setEggNumber(int eggNumber) {
        this.eggNumber = eggNumber;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHeightScore() {
        return heightScore;
    }

    public void setHeightScore(int heightScore) {
        this.heightScore = heightScore;
    }

    public String getGamer() {
        return gamer;
    }

    public void setGamer(String gamer) {
        this.gamer = gamer;
    }

    public void saveGameInfo() {
        ArrayList<GameInfo> gameInfos = new ArrayList<>();

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File("gameinfos"))));
            gameInfos = (ArrayList<GameInfo>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        GameInfo existingGameInfo = null;
        for (GameInfo temp : gameInfos) {
            if (this.gamer.equals(temp.getGamer())) {
                existingGameInfo = temp;
            }
        }

        if (existingGameInfo != null) {
            existingGameInfo.setScore(this.score);
            existingGameInfo.setTime(this.time);
            existingGameInfo.setLevel(this.level);
            existingGameInfo.setEggNumber(this.eggNumber);
            existingGameInfo.setHeightScore(this.heightScore);
            existingGameInfo.setHealth(this.health);
            existingGameInfo.setHeightScore(this.heightScore);
        } else {
            gameInfos.add(this);
        }

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("gameinfos"))));

            objectOutputStream.writeObject(gameInfos);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
