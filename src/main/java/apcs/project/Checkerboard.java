package apcs.project;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Checkerboard {
    private int sqH;
    private int sqW;

    public Checkerboard(int squareDim) {
        sqH = squareDim;
        sqW = squareDim;
    }

    public void draw(Graphics2D g2) {


        for (int i = 0; i<8; i++) {
            for (int j=0;j<8;j++) {

                Rectangle rect = new Rectangle(i*sqH, j*sqH, sqW, sqH);
                if ((i+j)%2 == 0) {

                    g2.draw(rect);
                }else {

                    g2.fill(rect);
                }

            }
        }

    }
}
