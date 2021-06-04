import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cat extends ScreenObject {
    private static BufferedImage catLeft, catRight;

    static {
        try {
            catLeft = ImageIO.read(new FileImageInputStream(new File("catleft.png")));
            catRight = ImageIO.read(new FileImageInputStream(new File("catright.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cat(int XCoordinate, int YCoordinate, boolean isInLeft) {
        super(XCoordinate, YCoordinate, isInLeft);
    }

    public static BufferedImage getCatLeft() {
        return catLeft;
    }

    public static void setCatLeft(BufferedImage catLeft) {
        Cat.catLeft = catLeft;
    }

    public static BufferedImage getCatRight() {
        return catRight;
    }

    public static void setCatRight(BufferedImage catRight) {
        Cat.catRight = catRight;
    }
}
