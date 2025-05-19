package apcs.project;

public class CustomPoint {
    public double x;
    public double y;

    public CustomPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(CustomPoint other) {
        return Math.sqrt((other.x - x) * (other.x - x) + (other.y - y) * (other.y - y));
    }

}
