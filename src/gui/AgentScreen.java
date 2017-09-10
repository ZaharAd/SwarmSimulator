package gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by zahar on 16/06/17.
 */

public class AgentScreen {
    private final int IRdim = 7;

    //    private final int X_1 = 150, Y_1 = 100;// first IR point
    //    private final int X_2 = 150, Y_2 = 200;// second IR point

    private int b1,b2;
    private final int X_1 = 295, Y_1 = 100;// first IR point
    private final int X_2 = 295, Y_2 = 200;// second IR point
    private final int Z = 0 ;//, Z_2 = 0;

    private final int diffDim = 2 , diffX = 15 , diffY = 15;
    private String cameraSide;


    private String side = "";
    private String dir = "";

    private int currX_1 = X_1 , currY_1 = Y_1, currX_2 = X_2, currY_2 = Y_2;
    private int currIRdim = IRdim;
    private int currIRslop_1 = currIRdim;
    private int currIRslop_2 = currIRdim;
    private int currZ = Z;//, currZ_2 = Z_2;
    private int turn = 0; // 1 - right, -1 - left

    public void drawDirection() {

    }

    public void draw(Graphics2D g2) {
        int x,y;
        int[] xs,ys;


        g2.drawRect(0,365,600,365);
//        g2.setColor(Color.WHITE);
//        g2.fillRect(0,365,600,365);


        int i = 0, i1=365;

        b1 = i ;
        b2 = i1;


        g2.setColor(Color.RED);

        //first point
        y = i1 + currY_1;
        x = i + currX_1;
        xs = new int[]{x - currIRdim, x - currIRdim, x + currIRdim, x + currIRdim};
        ys = new int[]{y + currIRslop_1, y - currIRslop_1, y - currIRslop_2, y + currIRslop_2};

        g2.drawPolygon(xs,ys,xs.length);
        g2.fillPolygon(xs,ys,xs.length);

        //second point
        y = i1 + currY_2;
        x = i + currX_2;
        xs = new int[]{x - currIRdim,x - currIRdim,x + currIRdim,x + currIRdim};
        ys = new int[]{y + currIRslop_1, y - currIRslop_1, y - currIRslop_2, y + currIRslop_2};

        g2.drawPolygon(xs,ys,xs.length);
        g2.fillPolygon(xs,ys,xs.length);


        //TODO add gausyan noise


    }

    public void drawLeader(Graphics2D g2) {
        b1 = 600;
        b2 = 365;

        g2.drawRect(0,0,b1,b2);
        g2.setColor(Color.ORANGE);
//        g2.drawString("lalalla",50,50);
        g2.fillRect(0,0,b1,b2);



    }


    public String firstRowReading(BufferedImage img) {
        int i = 300 , i1 = 0;

        if ((new Color(img.getRGB(i + X_1 - IRdim ,i1 + Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_2 - IRdim ,i1 + Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_1 - IRdim + diffX,i1 +  Y_1 - IRdim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim + diffX,i1 +  Y_1 - IRdim)).equals(Color.RED))) {
            side = "front";
        }else if ((new Color(img.getRGB(i + X_1 - IRdim - diffDim ,i1 + Y_1 - IRdim - diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim - diffDim ,i1 + Y_2 - IRdim - diffDim)).equals(Color.RED))){
            side ="right";
        }else if ((new Color(img.getRGB(i + X_1 - IRdim ,i1 + Y_1 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_2 - IRdim,i1 + Y_2 - IRdim)).equals(Color.BLACK))
                && (new Color(img.getRGB(i + X_1 - IRdim + diffDim ,i1 + Y_1 - IRdim + diffDim)).equals(Color.RED))
                && (new Color(img.getRGB(i + X_2 - IRdim + diffDim ,i1 + Y_2 - IRdim + diffDim)).equals(Color.RED))){
            side ="left";
        }else {
            side ="stop";
        }

        return side;
    }

    public String[] secRowReading(BufferedImage img, int i, int i1) {
        int point_x1 = i + X_1 - IRdim , point_x2 = i + X_2 - IRdim;
        int point_y1 = i1 + Y_1 - IRdim , point_y2 = i1 + Y_2 - IRdim;

        if ((currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 + diffX,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 + diffX,point_y2)).equals(Color.RED)){
            side = "right";
        }else if ((currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 - diffX,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 - diffX,point_y2)).equals(Color.RED)){
            side =  "left";
        }else if ((currPointColor(img, point_x1 + diffDim + IRdim*2, point_y1 + diffDim + IRdim*2).equals(Color.BLACK))
                &&(currPointColor(img, point_x2 + diffDim +IRdim*2, point_y2 + diffDim + IRdim*2).equals(Color.BLACK))
                &&(currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + IRdim*2 ,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 + IRdim*2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 + diffDim,point_y1 + diffDim)).equals(Color.RED)
                && (currPointColor(img,point_x2 + diffDim,point_y2 + diffDim)).equals(Color.RED)){
            side = "front";
        }else {
            side =  "stop";
        }


        if((currPointColor(img,point_x1 ,point_y1)).equals(Color.BLACK)
                &&(currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                &&(currPointColor(img,point_x1 ,point_y1 - diffY).equals(Color.RED))
                &&(currPointColor(img,point_x2 ,point_y2 - diffY).equals(Color.RED))){
            dir = "up";
        }else if((currPointColor(img,point_x1 ,point_y1)).equals(Color.BLACK)
                &&(currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                &&(currPointColor(img,point_x1 ,point_y1 + diffY).equals(Color.RED))
                &&(currPointColor(img,point_x2 ,point_y2 + diffY).equals(Color.RED))){
            dir = "down";
        }else if ((currPointColor(img,point_x1 + diffDim,point_y1)).equals(Color.BLACK)
                && (currPointColor(img,point_x2 + diffDim,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + diffDim ,point_y1 + diffDim - diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + diffDim ,point_y2 + diffDim - diffY)).equals(Color.RED)){
            side = "front";
            dir = "up";
        }else if ((currPointColor(img,point_x1 + diffDim,point_y1)).equals(Color.BLACK)
                && (currPointColor(img,point_x2 + diffDim,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + diffDim ,point_y1 + diffDim + diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + diffDim ,point_y2 + diffDim + diffY)).equals(Color.RED)){
            side = "front";
            dir = "down";
        }else if ((currPointColor(img ,point_x1 - diffX ,point_y1 - diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 - diffX ,point_y2 - diffY)).equals(Color.RED)){
            side = "left";
            dir = "up";
        }else if ((currPointColor(img ,point_x1 - diffX ,point_y1 + diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 - diffX ,point_y2 + diffY)).equals(Color.RED)){
            side = "left";
            dir = "down";
        }else if ((currPointColor(img ,point_x1 + diffX ,point_y1 - diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + diffX ,point_y2 - diffY)).equals(Color.RED)){
            side = "right";
            dir = "up";
        }else if ((currPointColor(img ,point_x1 + diffX ,point_y1 + diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + diffX ,point_y2 + diffY)).equals(Color.RED)) {
            side = "right";
            dir = "down";


            //yaw operations
        }else if((currPointColor(img ,point_x1 ,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + IRdim *2 ,point_y1)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + IRdim *2 ,point_y2)).equals(Color.RED)){
            dir = "right";
            }else if((currPointColor(img ,point_x1 + IRdim *2 ,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 + IRdim *2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 ,point_y1)).equals(Color.RED)
                && (currPointColor(img ,point_x2 ,point_y2)).equals(Color.RED)){
            dir = "left";
        }else if((currPointColor(img ,point_x1 ,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + IRdim *2 ,point_y1 - diffDim)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + IRdim *2 ,point_y2 - diffDim)).equals(Color.RED)){
            dir = "right";
            side = "front";
        }/*else if((currPointColor(img ,point_x1 ,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 ,point_y2)).equals(Color.BLACK)
                &&(currPointColor(img ,point_x1  + diffDim,point_y1 + diffDim)).equals(Color.RED)
                && (currPointColor(img ,point_x2  + diffDim,point_y2 + diffDim)).equals(Color.RED)
                && (currPointColor(img ,point_x1 + IRdim*2,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 + IRdim*2,point_y2)).equals(Color.BLACK)){
            dir = "left";
            side = "front";
        }*/
        /*else if((currPointColor(img ,point_x1 ,point_y1)).equals(Color.RED)
                && (currPointColor(img ,point_x2 ,point_y2)).equals(Color.RED)
                && (currPointColor(img ,point_x1 + IRdim*2 + diffX ,point_y1 + diffX )).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 + IRdim*2 + diffX ,point_y2 + diffX )).equals(Color.BLACK)){
            dir = "left";
            side = "right";
        }else if((currPointColor(img ,point_x1 ,point_y1)).equals(Color.RED)
                && (currPointColor(img ,point_x2 ,point_y2)).equals(Color.RED)
                && (currPointColor(img ,point_x1 + IRdim*2 - diffX ,point_y1 - diffX )).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 + IRdim*2 - diffX ,point_y2 - diffX )).equals(Color.BLACK)){
            dir = "left";
            side = "right";
        }*/else {
            dir =  "stop";
        }

        return new String[]{side,dir};
    }

    private Color currPointColor(BufferedImage img, int point_x1, int point_y1) {
        return new Color(img.getRGB(point_x1 ,point_y1));
    }

    private void printPoints(BufferedImage img) {
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                if(new Color(img.getRGB(i,j)).equals(Color.RED)) System.out.println(i + ", "+ j);
            }
        }
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

    public int getCurrIRslop_1() {
        return currIRslop_1;
    }

    public void setCurrIRslop_1(int currIRslop_1) {
        this.currIRslop_1 = currIRslop_1;
    }

    public int getCurrIRslop_2() {
        return currIRslop_2;
    }

    public void setCurrIRslop_2(int currIRslop_2) {
        this.currIRslop_2 = currIRslop_2;
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

    public int getCurrY_1() {
        return currY_1;
    }

    public void setCurrY_1(int currY_1) {
        if(currY_1 < b2){
            this.currY_1 = currY_1;
        }
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getY_1() {
        return Y_1;
    }

    public int getY_2() {
        return Y_2;
    }

    public int getCurrY_2() {
        return currY_2;
    }

    public void setCurrY_2(int currY_2) {
        this.currY_2 = currY_2;
    }

    public int getDiffY() {
        return diffY;
    }

    public int getZ() {
        return Z;
    }

    public int getCurrZ() {
        return currZ;
    }

    public void setCurrZ(int currZ) {
        this.currZ = currZ;
    }

    public String getCameraSide() {
        return cameraSide;
    }

    @Override
    public String toString() {
        return "AgentScreen{" +
                "IRdim=" + IRdim +
                ", currX_1=" + currX_1 +
                ", currY_1=" + currY_1 +
                ", currX_2=" + currX_2 +
                ", currY_2=" + currY_2 +
                ", currZ=" + currZ +
                ", currIRdim=" + currIRdim +
                ", side='" + side + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }
}