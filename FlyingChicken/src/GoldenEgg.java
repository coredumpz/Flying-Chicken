import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GoldenEgg extends ScreenObject {
    private static BufferedImage goldenEgg;

    static {
        try {
            goldenEgg = ImageIO.read(new FileImageInputStream(new File("goldenegg.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GoldenEgg() {
    }

    public GoldenEgg(int XCoordinate, int YCoordinate, boolean isInLeft) {
        super(XCoordinate, YCoordinate, isInLeft);
    }

    public static BufferedImage getGoldenEgg() {
        return goldenEgg;
    }

    public static void setGoldenEgg(BufferedImage goldenEgg) {
        GoldenEgg.goldenEgg = goldenEgg;
    }
}
