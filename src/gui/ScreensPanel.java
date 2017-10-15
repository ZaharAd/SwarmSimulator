package gui;

import simulation.Main_Simulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static gui.SwarmPanel.SCALE;

/**
 * Created by zahar on 16/06/17.
 */
public class ScreensPanel extends JPanel {

    private static final ScreensPanel screens = new ScreensPanel();
    private static droneScreen[] memberScreens;
    private String keyPressed = "";

    public static ScreensPanel getInstance() {
        if(memberScreens == null) screens.init();
        return screens;
    }

    private void init() {
        final Dimension size = new Dimension(600, Main_Simulator.MAX_Y*SCALE);
        setLayout(new GridLayout());
        setSize(size);
        setBounds(0, 0, Main_Simulator.MAX_X*SCALE + 600  , Main_Simulator.MAX_Y*SCALE);
        setBackground(Color.black);

        droneScreen leaderPanel = new droneScreen();
        droneScreen behindLeaderPanel = new droneScreen();
        behindLeaderPanel.setCameraSide("frontL");
        droneScreen besideLeaderPanel = new droneScreen();
        besideLeaderPanel.setCameraSide("left");
        droneScreen lastPanel = new droneScreen();
        lastPanel.setCameraSide("frontR");

        memberScreens = new droneScreen[] {leaderPanel, behindLeaderPanel, besideLeaderPanel, lastPanel};
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
    }

    private void drawDirection(Graphics2D g2) {
        BufferedImage image = null;
        Path currentRelativePath = Paths.get("");
        String projPath = currentRelativePath.toAbsolutePath().toString();

        try {
            image = ImageIO.read(new File(projPath + "/src/gui/leaderDirection/" + keyPressed +".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2.drawImage(image, 50, 100, 200, 150, this);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 40));
        g2.drawString(keyPressed,20, 50);
    }

    public void repaintPoints(){
        repaint();
    }

    public String[] findIRpos(String cameraSide) {
        BufferedImage img = new BufferedImage(600, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        this.paint(g2);

        String[] ans = new String[2];

        switch (cameraSide) {
            case "frontL":  //left member with front camera
                ans = memberScreens[1].findIRfront(img, 0, 365);
                break;
            case "left":
                ans = memberScreens[2].findIRleft(img, 300, 0);
                break;
            case "frontR": //right member with front camera
                ans = memberScreens[3].findIRfront(img, 300, 365);
                break;
        }

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




