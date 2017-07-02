package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by zahar on 16/06/17.
 */

public class AgentScreen {
    private final int IRdim = 7;
    private final int X_1 = 150, Y_1 = 100;// first IR point
    private final int X_2 = 150, Y_2 = 200;// second IR point
    private final int diffDim = 2 , diffX = 15;
    private String cameraSide;
    private String ans = "";

    private int currX_1 = X_1 , currY_1 = Y_1, currX_2 = X_2, currY_2 = Y_2;
    private int currIRdim = IRdim;


    public void draw(Graphics2D g2, int i, int i1) {

        g2.setColor(Color.WHITE);
        g2.drawRect(i,i1,300,365);

        for (int x = i + currX_1 - currIRdim; x < i + currX_1 + currIRdim; x++) {
            for (int y = i1 + currY_1 - currIRdim; y < i1 + currY_1 + currIRdim; y++) {
                g2.setColor(Color.RED);
                g2.drawLine(x, y, x, y);
            }
        }

        for (int x = i + currX_2 - currIRdim; x < i + currX_2 + currIRdim; x++) {
            for (int y = i1 + currY_2 - currIRdim; y < i1 + currY_2 + currIRdim; y++) {
                g2.setColor(Color.RED);
                g2.drawLine(x, y, x, y);
            }
        }
    }

    public String firstRowReading(BufferedImage img) {
        int i = 300 , i1 = 0;

        if ((new Color(img.getRGB(i + X_1 - IRdim ,i1 + Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_2 - IRdim ,i1 + Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_1 - IRdim + diffX,i1 +  Y_1 - IRdim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim + diffX,i1 +  Y_1 - IRdim)).equals(Color.RED))) {
            ans = "front";
        }else if ((new Color(img.getRGB(i + X_1 - IRdim - diffDim ,i1 + Y_1 - IRdim - diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim - diffDim ,i1 + Y_2 - IRdim - diffDim)).equals(Color.RED))){
            ans ="right";
        }else if ((new Color(img.getRGB(i + X_1 - IRdim ,i1 + Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_2 - IRdim,i1 + Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_1 - IRdim + diffDim ,i1 + Y_1 - IRdim + diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim + diffDim ,i1 + Y_2 - IRdim + diffDim)).equals(Color.RED))){
            ans ="left";
        }else {
            ans ="stop";
        }

        return ans;
    }

    public String secRowReading(BufferedImage img, int i, int i1) {

        if ((new Color(img.getRGB(i + X_1 - IRdim ,i1 + Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_2 - IRdim ,i1 + Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_1 - IRdim + diffX,i1 +  Y_1 - IRdim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim + diffX,i1 +  Y_1 - IRdim)).equals(Color.RED))){
            ans = "right";
        }else if ((new Color(img.getRGB(i + X_1 - IRdim ,i1 + Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_2 - IRdim ,i1 + Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_1 - IRdim - diffX,i1 +  Y_1 - IRdim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim - diffX,i1 +  Y_1 - IRdim)).equals(Color.RED))){
            ans =  "left";
        }else if ((new Color(img.getRGB(i + X_1 - IRdim ,i1 + Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_2 - IRdim,i1 + Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_1 - IRdim + diffDim ,i1 + Y_1 - IRdim + diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim + diffDim ,i1 + Y_2 - IRdim + diffDim)).equals(Color.RED))) {
            ans = "front";
        }else if ((new Color(img.getRGB(i + X_1 - IRdim - diffDim,i1 + Y_1 - IRdim -diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim - diffDim,i1 + Y_2 - IRdim -diffDim)).equals(Color.RED))){
            ans =  "back";
        }else {
            ans =  "stop";
        }

        return ans;
    }

    public void setCameraSide(String cameraSide) {
        this.cameraSide = cameraSide;
    }

    public int getX_1() {
        return X_1;
    }

    public int getX_2() {
        return X_2;
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

    public void setCurrX_1(int currX_1) {
        this.currX_1 = currX_1;
    }

    public void setCurrX_2(int currX_2) {
        this.currX_2 = currX_2;
    }

    public int getCurrX_1() {
        return currX_1;
    }

    public int getCurrX_2() {
        return currX_2;
    }

    public int getDiffDim() {
        return diffDim;
    }

    public int getDiffX() {
        return diffX;
    }

    public String getCameraSide() {
        return cameraSide;
    }

}