package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zahar on 16/06/17.
 */

public class AgentPaintedPanel extends JPanel {
    private final int IRdim = 7;
    private int X_1 = 150, Y_1 = 100;// first IR point
    private int X_2 = 150, Y_2 = 200;// second IR point
    private String cameraSide;

//    private int[] lastLocation;

    private int currX_1 = X_1 , currY_1 = Y_1, currX_2 = X_2, currY_2 = Y_2;
    private int currIRdim = IRdim;

    public void repaintPixel(){
//        lastLocation = new int[]{X_1,Y_1,X_2,Y_2};
        repaint();
    }

    public void paintPixel(int x1 , int y1 , int x2, int y2){
//        lastLocation = new int[]{X_1,Y_1,X_2,Y_2};

        X_1 = x1;
        Y_1 = y1;

        X_2 = x2;
        Y_2 = y2;

        repaint();
    }

//    public int[] getLastLocation() {
//        return lastLocation;
//    }


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

    public void setCurrIRdim(int IRdim) {
        currIRdim = IRdim;
    }

    public int getCurrIRdim() {
        return currIRdim;
    }

    public int getCurrX_1() {
        return currX_1;
    }

    public void setCurrX_1(int currX_1) {
        this.currX_1 = currX_1;
    }

    public int getCurrY_1() {
        return currY_1;
    }

    public void setCurrY_1(int currY_1) {
        this.currY_1 = currY_1;
    }

    public int getCurrX_2() {
        return currX_2;
    }

    public void setCurrX_2(int currX_2) {
        this.currX_2 = currX_2;
    }

    public int getCurrY_2() {
        return currY_2;
    }

    public void setCurrY_2(int currY_2) {
        this.currY_2 = currY_2;
    }

    public String getCameraSide() {
        return cameraSide;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int x = currX_1 - currIRdim; x < currX_1 + currIRdim; x++) {
            for (int y = currY_1 - currIRdim; y < currY_1 + currIRdim; y++) {
                g.setColor(Color.RED);
                g.drawLine(x, y, x, y);
            }
        }

        for (int x = currX_2 - currIRdim; x < currX_2 + currIRdim; x++) {
            for (int y = currY_2 - currIRdim; y < currY_2 + currIRdim; y++) {
                g.setColor(Color.RED);
                g.drawLine(x, y, x, y);
            }
        }
    }
}
