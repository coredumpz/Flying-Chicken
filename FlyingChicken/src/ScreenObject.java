public class ScreenObject {
    private int XCoordinate, YCoordinate;
    private boolean isInLeft;

    public ScreenObject() {}

    public ScreenObject(int XCoordinate, int YCoordinate, boolean isInLeft) {
        this.XCoordinate = XCoordinate;
        this.YCoordinate = YCoordinate;
        this.isInLeft = isInLeft;
    }

    public int getXCoordinate() {
        return XCoordinate;
    }

    public void setXCoordinate(int XCoordinate) {
        this.XCoordinate = XCoordinate;
    }

    public int getYCoordinate() {
        return YCoordinate;
    }

    public void setYCoordinate(int YCoordinate) {
        this.YCoordinate = YCoordinate;
    }

    public boolean isInLeft() {
        return isInLeft;
    }

    public void setInLeft(boolean inLeft) {
        isInLeft = inLeft;
    }
}
