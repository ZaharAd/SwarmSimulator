package gui;

import simulation.Agent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by zahar on 16/06/17.
 */

public class AgentPaintedPanel extends JPanel {
    private final int IRdim = 7;
    private final int X_1 = 150, Y_1 = 100;// first IR point
    private final int X_2 = 150, Y_2 = 200;// second IR point
    private final int diffDim = 2 , diffX = 15;
    private String cameraSide;

    private int currX_1 = X_1 , currY_1 = Y_1, currX_2 = X_2, currY_2 = Y_2;
    private int currIRdim = IRdim;


    public void repaintPixel(){
        repaint();
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

    public String findIRpoint(Agent.AgentSwarmBehaviour behaviour) {
        String ans = "";

        switch (behaviour){
            case FOLLOW_LEFT:
                ans = firstRowReading();
                return ans;
            case FOLLOW_FRONT:
                ans = secRowReading();
                return ans;
            case LAST:
                ans = secRowReading();
                return ans;
        }

        return "error";
    }

    private String firstRowReading() {
        BufferedImage img= new BufferedImage(300, 300, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2 =img.createGraphics();
        this.paint(g2);

        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                if(new Color(img.getRGB(i,j)).equals(Color.RED)){
                    System.out.println(i + ", " + j);
                }
            }
        }

        if ((new Color(img.getRGB(X_1 - IRdim ,Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_2 - IRdim ,Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_1 - IRdim + diffX, Y_1 - IRdim)).equals(Color.RED))
                && (new Color(img.getRGB(X_2 - IRdim + diffX, Y_1 - IRdim)).equals(Color.RED))) {
            g2.dispose();
            return "front";
        }else if ((new Color(img.getRGB(X_1 - IRdim - diffDim ,Y_1 - IRdim - diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(X_2 - IRdim - diffDim ,Y_2 - IRdim - diffDim)).equals(Color.RED))){
            g2.dispose();
            return "right";
        }else if ((new Color(img.getRGB(X_1 - IRdim ,Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_2 - IRdim,Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_1 - IRdim + diffDim ,Y_1 - IRdim + diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(X_2 - IRdim + diffDim ,Y_2 - IRdim + diffDim)).equals(Color.RED))){
            g2.dispose();
            return "left";
        }else {
            g2.dispose();
            return "stop";
        }
    }

    private String secRowReading() {
        BufferedImage img= new BufferedImage(300, 300, BufferedImage.SCALE_DEFAULT);
        Graphics2D g2 =img.createGraphics();
        this.paint(g2);



        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                if(new Color(img.getRGB(i,j)).equals(Color.RED)){
                    System.out.println(i + ", " + j);
                }
            }
        }

        if ((new Color(img.getRGB(X_1 - IRdim ,Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_2 - IRdim ,Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_1 - IRdim + diffX, Y_1 - IRdim)).equals(Color.RED))
                && (new Color(img.getRGB(X_2 - IRdim + diffX, Y_1 - IRdim)).equals(Color.RED))){
            g2.dispose();
            return "right";
        }else if ((new Color(img.getRGB(X_1 - IRdim ,Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_2 - IRdim ,Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_1 - IRdim - diffX, Y_1 - IRdim)).equals(Color.RED))
                && (new Color(img.getRGB(X_2 - IRdim - diffX, Y_1 - IRdim)).equals(Color.RED))){
            g2.dispose();
            return "left";
        }else if ((new Color(img.getRGB(X_1 - IRdim ,Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_2 - IRdim,Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(X_1 - IRdim + diffDim ,Y_1 - IRdim + diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(X_2 - IRdim + diffDim ,Y_2 - IRdim + diffDim)).equals(Color.RED))) {
            g2.dispose();
            return "front";
        }else if ((new Color(img.getRGB(X_1 - IRdim - diffDim,Y_1 - IRdim -diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(X_2 - IRdim - diffDim,Y_2 - IRdim -diffDim)).equals(Color.RED))){
            g2.dispose();
            return "back";
        }else {
            g2.dispose();
            return "stop";
        }
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
