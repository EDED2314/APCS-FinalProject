package project;

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

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
    }

    public String toString(){
        return "Point: (" + x + "," + y + ")";
    }

}
