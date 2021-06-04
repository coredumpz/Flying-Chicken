import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Chicken extends ScreenObject {
    private boolean isReloading, chickenBlock;
    private static BufferedImage chickenLeft, chickenRight, parachute, chickenIcon, health, healthEmpty;
    private int interval, startingTime;

    static {
        try {
            chickenLeft = ImageIO.read(new FileImageInputStream(new File("chickenleft.png")));
            chickenRight = ImageIO.read(new FileImageInputStream(new File("chickenright.png")));
            parachute = ImageIO.read(new FileImageInputStream(new File("parasut.png")));
            chickenIcon = ImageIO.read(new FileImageInputStream(new File("chickenicon.png")));
            chickenIcon = ImageIO.read(new FileImageInputStream(new File("chickenicon.png")));
            chickenIcon = ImageIO.read(new FileImageInputStream(new File("chickenicon.png")));
            health = ImageIO.read(new FileImageInputStream(new File("health.png")));
            healthEmpty = ImageIO.read(new FileImageInputStream(new File("healthempty.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chicken(int XCoordinate, int YCoordinate, boolean isInLeft) {
        super(XCoordinate, YCoordinate, isInLeft);
        this.isReloading = false;
        this.chickenBlock = false;
    }

    public void startReloading(int startingTime, int interval){
        this.isReloading = true;
        this.startingTime = startingTime;
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public int getStartingTime() {
        return startingTime;
    }

    public boolean isChickenBlock() {
        return chickenBlock;
    }

    public void setChickenBlock(boolean chickenBlock) {
        this.chickenBlock = chickenBlock;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public void setReloading(boolean reloading) {
        isReloading = reloading;
    }

    public static BufferedImage getHealth() {
        return health;
    }

    public static void setHealth(BufferedImage health) {
        Chicken.health = health;
    }

    public static BufferedImage getHealthEmpty() {
        return healthEmpty;
    }

    public static void setHealthEmpty(BufferedImage healthEmpty) {
        Chicken.healthEmpty = healthEmpty;
    }

    public static BufferedImage getChickenIcon() {
        return chickenIcon;
    }

    public static void setChickenIcon(BufferedImage chickenIcon) {
        Chicken.chickenIcon = chickenIcon;
    }

    public static BufferedImage getParachute() {
        return parachute;
    }

    public static void setParachute(BufferedImage parachute) {
        Chicken.parachute = parachute;
    }

    public static BufferedImage getChickenLeft() {
        return chickenLeft;
    }

    public static void setChickenLeft(BufferedImage chickenLeft) {
        Chicken.chickenLeft = chickenLeft;
    }

    public static BufferedImage getChickenRight() {
        return chickenRight;
    }

    public static void setChickenRight(BufferedImage chickenRight) {
        Chicken.chickenRight = chickenRight;
    }
}
