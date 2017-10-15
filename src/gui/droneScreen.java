package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by zahar on 16/06/17.
 */

public class droneScreen{
    private final int IRdim = 7;
    private final int X_1 = 150, Y_1 = 100;// first IR point
    private final int X_2 = 150, Y_2 = 200;// second IR point
    private int currX_1 = X_1 , currY_1 = Y_1, currX_2 = X_2, currY_2 = Y_2;
    private int currIRdim = IRdim;
    private int currIRslop_1 = currIRdim;
    private int currIRslop_2 = currIRdim;

    private final int diffDim = 2 , diffX = 15 , diffY = 15;
    private String cameraSide;
    private String side = "";
    private String dir = "";


    void draw(Graphics2D g2, int i, int i1) {
        int x,y;
        g2.setColor(Color.WHITE);
        g2.drawRect(i,i1,i+300,i1+365);

        g2.setColor(Color.RED);

        //first point
        x = i + currX_1;
        y = i1 + currY_1;
        drawIRpoint(x,y,g2);

        //second point
        x = i + currX_2;
        y = i1 + currY_2;
        drawIRpoint(x,y,g2);
    }

    private void drawIRpoint(int x, int y, Graphics2D g2) {
        int[] xs,ys;

//        System.out.println("x - currIRdim:" + (x - currIRdim) + ",x - currIRdim:" + (x - currIRdim) +",x + currIRdim:"
//                + (x + currIRdim)+",x + currIRdim:" + (x + currIRdim));
//
//        System.out.println("y + currIRslop_1:" + (y + currIRslop_1)+ ", y - currIRslop_1:"+ (y - currIRslop_1)
//                +",y - currIRslop_2:"+(y - currIRslop_2)+",y + currIRslop_2:"+ (y + currIRslop_2));



//        if ((x != x1 && x != x2 && x != x3 && x != x4) && (y != y1 && y != y2 && y != y3 && y != y4)) {
        if(currIRdim >= 0 && currIRslop_1 >= 0 && currIRslop_2 >= 0){
            int x1 = x - currIRdim, y1 = y + currIRslop_1;
            int x2 = x - currIRdim, y2 = y - currIRslop_1;
            int x3 = x + currIRdim, y3 = y - currIRslop_2;
            int x4 = x + currIRdim, y4 = y + currIRslop_2;

            xs = new int[]{x1, x2, x3, x4};
            ys = new int[]{y1, y2, y3, y4};

            g2.drawPolygon(xs,ys,xs.length);
            g2.fillPolygon(xs,ys,xs.length);
        }
    }

    void drawLeader(Graphics2D g2) {
        int b1 = 300;
        int b2 = 365;

//        g2.drawRect(0,0,b1,b2);
        g2.setColor(Color.ORANGE);
        g2.fillRect(0,0,b1,b2);

    }

    public String[] findIRleft(BufferedImage img, int i, int i1) {
        int point_x1 = i + X_1 - IRdim , point_x2 = i + X_2 - IRdim;
        int point_y1 = i1 + Y_1 - IRdim , point_y2 = i1 + Y_2 - IRdim;

        //find the side
        if ((currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 + diffX,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 + diffX,point_y2)).equals(Color.RED)) {
            side = "front";
        }else if ((currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 + diffX*2,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 + diffX*2,point_y2)).equals(Color.RED)) {
            side = "frontx2";
        }else if ((currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 - diffX,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 - diffX,point_y2)).equals(Color.RED)){
            side = "back";
        }else if ((currPointColor(img,point_x1 ,point_y1).equals(Color.RED))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.RED)
                && (currPointColor(img,point_x1 - diffDim,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 - diffDim,point_y2)).equals(Color.RED)){
            side ="right";
        }else if ((currPointColor(img, point_x1 + diffDim + IRdim*2, point_y1 + diffDim + IRdim*2).equals(Color.BLACK))
                &&(currPointColor(img, point_x2 + diffDim +IRdim*2, point_y2 + diffDim + IRdim*2).equals(Color.BLACK))
                &&(currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + IRdim*2 ,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 + IRdim*2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 + diffDim,point_y1 + diffDim)).equals(Color.RED)
                && (currPointColor(img,point_x2 + diffDim,point_y2 + diffDim)).equals(Color.RED)){
            side ="left";
        }else {
            side ="stop";
        }

        //find the direction
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
            side = "left";
            dir = "up";
        }else if ((currPointColor(img,point_x1 + diffDim,point_y1)).equals(Color.BLACK)
                && (currPointColor(img,point_x2 + diffDim,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + diffDim ,point_y1 + diffDim + diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + diffDim ,point_y2 + diffDim + diffY)).equals(Color.RED)){
            side = "left";
            dir = "down";
        }else if ((currPointColor(img ,point_x1 - diffX ,point_y1 - diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 - diffX ,point_y2 - diffY)).equals(Color.RED)){
            side = "front";
            dir = "up";
        }else if ((currPointColor(img ,point_x1 - diffX ,point_y1 + diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 - diffX ,point_y2 + diffY)).equals(Color.RED)){
            side = "front";
            dir = "down";
        }else if ((currPointColor(img ,point_x1 + diffX ,point_y1 - diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + diffX ,point_y2 - diffY)).equals(Color.RED)){
            side = "back";
            dir = "up";
        }else if ((currPointColor(img ,point_x1 + diffX ,point_y1 + diffY)).equals(Color.RED)
                && (currPointColor(img ,point_x2 + diffX ,point_y2 + diffY)).equals(Color.RED)) {
            side = "back";
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
        }else{
            dir = "stop";
        }

        return new String[] {side,dir};
    }

    public String[] findIRfront(BufferedImage img, int i, int i1) {

        int point_x1 = i + X_1 - IRdim , point_x2 = i + X_2 - IRdim;
        int point_y1 = i1 + Y_1 - IRdim , point_y2 = i1 + Y_2 - IRdim;

        //find the side

        if ((currPointColor(img, point_x1 + diffDim + IRdim*2, point_y1 + diffDim + IRdim*2).equals(Color.BLACK))
                &&(currPointColor(img, point_x2 + diffDim +IRdim*2, point_y2 + diffDim + IRdim*2).equals(Color.BLACK))
                &&(currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + IRdim*2 ,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 + IRdim*2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 + diffDim,point_y1 + diffDim)).equals(Color.RED)
                && (currPointColor(img,point_x2 + diffDim,point_y2 + diffDim)).equals(Color.RED)) {
            side = "front";
        }else if ((currPointColor(img, point_x1 + diffDim + IRdim*2, point_y1 + diffDim + IRdim*2).equals(Color.BLACK))
                &&(currPointColor(img, point_x2 + diffDim +IRdim*2, point_y2 + diffDim + IRdim*2).equals(Color.BLACK))
                &&(currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img ,point_x1 + IRdim*2 ,point_y1)).equals(Color.BLACK)
                && (currPointColor(img ,point_x2 + IRdim*2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 + diffDim*2,point_y1 + diffDim*2)).equals(Color.RED)
                && (currPointColor(img,point_x2 + diffDim*2,point_y2 + diffDim*2)).equals(Color.RED)){
            side = "frontx2";
        }else if ((currPointColor(img,point_x1 ,point_y1).equals(Color.RED))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.RED)
                && (currPointColor(img,point_x1 - diffDim,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 - diffDim,point_y2)).equals(Color.RED)){
            side =  "back";
        }else if ((currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 + diffX,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 + diffX,point_y2)).equals(Color.RED)){
            side = "right";
        }else if ((currPointColor(img,point_x1 ,point_y1).equals(Color.BLACK))
                && (currPointColor(img,point_x2 ,point_y2)).equals(Color.BLACK)
                && (currPointColor(img,point_x1 - diffX,point_y1)).equals(Color.RED)
                && (currPointColor(img,point_x2 - diffX,point_y2)).equals(Color.RED)){
            side =  "left";
        }else  {
            side =  "stop";
        }


        //find the direction
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
        this.currY_1 = currY_1;
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

    public String getCameraSide() {
        return cameraSide;
    }

    @Override
    public String toString() {
        return "droneScreen{" +
                "IRdim=" + IRdim +
                ", currX_1=" + currX_1 +
                ", currY_1=" + currY_1 +
                ", currX_2=" + currX_2 +
                ", currY_2=" + currY_2 +
                ", currIRdim=" + currIRdim +
                ", side='" + side + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }
}