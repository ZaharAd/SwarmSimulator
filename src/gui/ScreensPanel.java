package gui;

import simulation.Main_Simulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static gui.SwarmPanel.SCALE;

/**
 * Created by zahar on 16/06/17.
 */
public class ScreensPanel extends JPanel {

    private droneScreen LeaderPanel;
    private droneScreen BehindLeaderPanel;
    private droneScreen BesideLeaderPanel;
    private droneScreen BehindBesideLeaderPanel;

    private static droneScreen[] memberScreens;
    private Dimension leaderLocation;//, secondLocation, behindeLocation, lastLocation;
    private String keyPressed = "";

    private static final ScreensPanel screens = new ScreensPanel();

    public static ScreensPanel getInstance() {
        if(memberScreens == null) screens.init();
        return screens;
    }


    private void init() {
//        leaderLocation = new Dimension(Main_Simulator.MAX_X*SCALE +10, 0);

        final Dimension size = new Dimension(600, Main_Simulator.MAX_Y*SCALE);
        setLayout(new GridLayout());
        setSize(size);
        setBounds(0, 0, Main_Simulator.MAX_X*SCALE + 600  , Main_Simulator.MAX_Y*SCALE);


        setBackground(Color.black);

        LeaderPanel = new droneScreen();

        BehindLeaderPanel = new droneScreen();
        BehindLeaderPanel.setCameraSide("frontL");

        BesideLeaderPanel = new droneScreen();
        BesideLeaderPanel.setCameraSide("left");

        BehindBesideLeaderPanel = new droneScreen();
        BehindBesideLeaderPanel.setCameraSide("front");

        memberScreens = new droneScreen[] {LeaderPanel, BehindLeaderPanel,BesideLeaderPanel, BehindBesideLeaderPanel};
    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        memberScreens[0].drawLeader(g2);
        drawDirection(g2);

        memberScreens[1].draw(g2,0,365);
        memberScreens[2].draw(g2,300,0);
        memberScreens[3].draw(g2,300,365);
//        g2.dispose();
    }

    private void drawDirection(Graphics2D g2) {
        BufferedImage image;

        switch (keyPressed) {
            case "rollRight":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/rollRight.png"));
                    g2.drawImage(image, 50, 100, 200, 150, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "rollLeft":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/rollLeft.png"));
                    g2.drawImage(image, 50, 100, 200, 150, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "pitchForward":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/pitchForward.png"));
                    g2.drawImage(image, 50, 100, 200, 150, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "throttleDown":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/throttleDown.png"));
                    g2.drawImage(image, 50, 100, 200, 150, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "throttleUp":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/throttleUp.png"));
                    g2.drawImage(image, 50, 100, 200, 150, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "yawLeft":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/yawLeft.png"));
                    g2.drawImage(image, 50, 100, 200, 150, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "yawRight":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/yawRight1.png"));
                    g2.drawImage(image, 50, 100, 200, 150, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
        }
    }

    public void repaintPoints(){
        repaint();
    }

    public String[] findIRpos(String cameraSide) {
        BufferedImage img = new BufferedImage(600, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        this.paint(g2);

        String[] ans = memberScreens[1].findIRfront(img,0,365);
//        System.out.println("=============ir pos: " + Arrays.toString(ans));

//        if(cameraSide.equals("left")){
//            ans = memberScreens[0].findIRleft(img);
//        }else if(cameraSide.equals("frontL")){
//            // *    *
//            // +    *
//            ans = memberScreens[1].findIRfront(img,0,365);
//        }else if(cameraSide.equals("frontR")){
//            // *    *
//            // *    +
//            ans = memberScreens[2].findIRfront(img,300,365);
//        }

//        g2.dispose();
        return ans;
    }

    public ScreensPanel() {

    }

    public void setKeyPressed(String keyPressed) {
        this.keyPressed = keyPressed;
    }

    public droneScreen[] getMemberScreens(){
        return memberScreens;
    }


}




