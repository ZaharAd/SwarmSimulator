package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zahar on 16/06/17.
 */

public class AgentPaintedPanel extends JPanel {
    private int IRdim = 7;
    private int X_1, Y_1;// first IR
    private int X_2, Y_2;// second IR
    private String cameraSide;

    private int[] lastLocation;
    private int lastIRdim = IRdim;

    public void repaintPixel(){
        repaint();
    }

    public void paintPixel(int x1 , int y1 , int x2, int y2){
        lastLocation = new int[]{X_1,Y_1,X_2,Y_2};

        X_1 = x1;
        Y_1 = y1;

        X_2 = x2;
        Y_2 = y2;

        repaint();
    }

    public int[] getLastLocation() {
        return lastLocation;
    }

    public int getLastIRdim() {
        return lastIRdim;
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

    public int getY_1() {
        return Y_1;
    }

    public int getX_2() {
        return X_2;
    }

    public int getY_2() {
        return Y_2;
    }

    public int getIRdim() {
        return IRdim;
    }

    public void setIRdim(int IRdim) {
        lastIRdim = this.IRdim;
        this.IRdim = IRdim;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int x = X_1 - IRdim; x < X_1 + IRdim; x++) {
            for (int y = Y_1 - IRdim; y < Y_1 + IRdim; y++) {
                g.setColor(Color.RED);
                g.drawLine(x, y, x, y);
            }
        }

        for (int x = X_2 - IRdim; x < X_2 + IRdim; x++) {
            for (int y = Y_2 - IRdim; y < Y_2 + IRdim; y++) {
                g.setColor(Color.RED);
                g.drawLine(x, y, x, y);
            }
        }
    }
}
