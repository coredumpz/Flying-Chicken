import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cloud extends ScreenObject {
    private static BufferedImage cloud;

    static {
        try {
            cloud = ImageIO.read(new FileImageInputStream(new File("bulut.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Cloud() {
    }

    public Cloud(int XCoordinate, int YCoordinate, boolean isInLeft) {
        super(XCoordinate, YCoordinate, isInLeft);
    }

    public static BufferedImage getCloud() {
        return cloud;
    }

    public static void setCloud(BufferedImage cloud) {
        Cloud.cloud = cloud;
    }

}