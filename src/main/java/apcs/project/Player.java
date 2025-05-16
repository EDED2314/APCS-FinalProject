package apcs.project;

import java.awt.*;
import java.awt.geom.*;


public class Player {
    private double playerX;


    public void render(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g;
//
//        Rectangle2D rect = new Rectangle2D(10.5, 20.3, 30.2, 40.7);
//
//        g2.setColor(Color.WHITE);
//        g2.draw(rect.toRectangle());

    }
}

class Point2D{
    public double x;
    public double y;

    public Point2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point getPoint(){
        return new Point((int) x, (int)y);
    }
}

class Rectangle2D {
    public double x;
    public double y;
    public double width;
    public double height;

    private Point2D p1;
    private Point2D p2;
    private Point2D p3;
    private Point2D p4;

    public Rectangle2D(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public boolean intersects(Rectangle2D other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    // Convert to integer Rectangle when needed for rendering
    public void render(Graphics g){

    }

//    public Rectangle toRectangle() {
//        return new Rectangle((int)x, (int)y, (int)width, (int)height);
//    }
}