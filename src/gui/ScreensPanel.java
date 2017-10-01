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

    private static droneScreen[] members;
    private Dimension leaderLocation;//, secondLocation, behindeLocation, lastLocation;
    private String keyPressed = "";

    private static final ScreensPanel screens = new ScreensPanel();

    public static ScreensPanel getInstance() {
        if(members == null) screens.init();
        return screens;
    }

    private void init() {
        leaderLocation = new Dimension(Main_Simulator.MAX_X*SCALE +10, 0);

        final Dimension size = new Dimension(600, Main_Simulator.MAX_Y*SCALE);
        setLayout(new GridLayout());
        setSize(size);
        setBounds(Main_Simulator.MAX_X*SCALE +10, 0, Main_Simulator.MAX_X*SCALE + 600  , Main_Simulator.MAX_Y*SCALE);
        setBackground(Color.black);

        LeaderPanel = new droneScreen();
        BehindLeaderPanel = new droneScreen();
        BehindLeaderPanel.setCameraSide("frontL");

        members = new droneScreen[] {LeaderPanel, BehindLeaderPanel};//{SecondPanel, BehindLeaderPanel, LastPanel};
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        members[0].drawLeader(g2);
        drawDirection(g2);

        members[1].draw(g2);
//        members[0].draw(g2,300,0);
//        members[1].draw(g2,0,365);
//        members[2].draw(g2,300,365);
    }

    private void drawDirection(Graphics2D g2) {
        BufferedImage image;

        switch (keyPressed) {
            case "rollRight":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/rollRight.png"));
                    g2.drawImage(image, 200, 100, 200, 200, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "rollLeft":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/rollLeft.png"));
                    g2.drawImage(image, 200, 100, 200, 200, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "pitchForward":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/pitchForward.png"));
                    g2.drawImage(image, 200, 100, 200, 200, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "throttleDown":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/throttleDown.png"));
                    g2.drawImage(image, 200, 100, 200, 200, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "throttleUp":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/throttleUp.png"));
                    g2.drawImage(image, 200, 100, 200, 200, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "yawLeft":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/yawLeft.png"));
                    g2.drawImage(image, 200, 100, 200, 200, this);
                } catch (IOException ex) {
                    // handle exception...
                }
                break;
            case "yawRight":
                try {
                    image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/yawRight1.png"));
                    g2.drawImage(image, 200, 100, 200, 200, this);
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

        String[] ans = members[1].secRowReading(img,0,365);

//        if(cameraSide.equals("left")){
//            ans = members[0].firstRowReading(img);
//        }else if(cameraSide.equals("frontL")){
//            // *    *
//            // +    *
//            ans = members[1].secRowReading(img,0,365);
//        }else if(cameraSide.equals("frontS")){
//            // *    *
//            // *    +
//            ans = members[2].secRowReading(img,300,365);
//        }

        g2.dispose();
        return ans;
    }

    public ScreensPanel() {

    }

    public void setKeyPressed(String keyPressed) {
        this.keyPressed = keyPressed;
    }

    public droneScreen[] getAgentPanelsArr (){
        return members;
    }


}




