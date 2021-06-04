import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Polygon extends ScreenObject {
    private int scaleRate;
    private static BufferedImage polygon;

    static {
        try {
            polygon = ImageIO.read(new FileImageInputStream(new File("polygon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Polygon() {
    }

    public Polygon(int XCoordinate, int YCoordinate, boolean isInLeft, int scaleRate) {
        super(XCoordinate, YCoordinate, isInLeft);
        this.scaleRate = scaleRate;
    }

    public int getScaleRate() {
        return scaleRate;
    }

    public void setScaleRate(int scaleRate) {
        this.scaleRate = scaleRate;
    }

    public static BufferedImage getPolygon() {
        return polygon;
    }

    public static void setPolygon(BufferedImage polygon) {
        Polygon.polygon = polygon;
    }
}
