package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zahar on 16/06/17.
 */

public class AgentPaintedPanel extends JPanel {
    private final int MARGIN = 3;
    private int X_1, Y_1;// first IR
    private int X_2, Y_2;// second IR
    private String cameraSide;

    public void paintPixel(int x1 , int y1 , int x2, int y2){
        X_1 = x1;
        Y_1 = y1;

        X_2 = x2;
        Y_2 = y2;

        repaint();
    }

    public String getCameraSide() {
        return cameraSide;
    }

    public void setCameraSide(String cameraSide) {
        this.cameraSide = cameraSide;
    }

    public int getX_1() {
        return X_1;
    }

    public void setX_1(int x_1) {
        X_1 = x_1;
    }

    public int getY_1() {
        return Y_1;
    }

    public void setY_1(int y_1) {
        Y_1 = y_1;
    }

    public int getX_2() {
        return X_2;
    }

    public void setX_2(int x_2) {
        X_2 = x_2;
    }

    public int getY_2() {
        return Y_2;
    }

    public void setY_2(int y_2) {
        Y_2 = y_2;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int x = X_1 - MARGIN; x < X_1 + MARGIN; x++) {
            for (int y = Y_1 - MARGIN; y < Y_1 + MARGIN; y++) {
                g.setColor(Color.RED);
                g.drawLine(x, y, x, y);
            }
        }

        for (int x = X_2 - MARGIN; x < X_2 + MARGIN; x++) {
            for (int y = Y_2 - MARGIN; y < Y_2 + MARGIN; y++) {
                g.setColor(Color.RED);
                g.drawLine(x, y, x, y);
            }
        }
    }
}
