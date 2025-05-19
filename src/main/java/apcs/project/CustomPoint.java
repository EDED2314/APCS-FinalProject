package apcs.project;

import java.awt.*;

public class CustomPoint {
    public double x;
    public double y;

    public CustomPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point getPoint() {
        return new Point((int) x, (int) y);
    }
}
