package gui;

import simulation.Simulator;

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

    private AgentScreen LeaderPanel;
    private AgentScreen SecondPanel;
    private AgentScreen BehindLeaderPanel;
    private AgentScreen LastPanel;

    private static AgentScreen[] members;
    private Dimension leaderLocation;//, secondLocation, behindeLocation, lastLocation;
    private String keyPressed = "";

    private static final ScreensPanel screens = new ScreensPanel();

    public static ScreensPanel getInstance() {
        if(members == null) screens.init();
        return screens;
    }

    private void init() {
        leaderLocation = new Dimension(Simulator.MAX_X*SCALE +10, 0);

        final Dimension size = new Dimension(600, Simulator.MAX_Y*SCALE);

        setLayout(new GridLayout());
        setSize(size);

        setBounds(Simulator.MAX_X*SCALE +10, 0, Simulator.MAX_X*SCALE + 600  , Simulator.MAX_Y*SCALE);
        setBackground(Color.black);

//        LeaderPanel = new AgentScreen();//new JPanel();

//        SecondPanel = new AgentScreen();
//        SecondPanel.setCameraSide("left");

        LeaderPanel = new AgentScreen();


        BehindLeaderPanel = new AgentScreen();
        BehindLeaderPanel.setCameraSide("frontL");

//        LastPanel = new AgentScreen();
//        LastPanel.setCameraSide("frontS");

        members = new AgentScreen[] {LeaderPanel, BehindLeaderPanel};//{SecondPanel, BehindLeaderPanel, LastPanel};
//
////        paintComponent();
//
//        LeaderPanel.setBackground(Color.yellow);
//        LeaderPanel.setLocation(0,0);
//        LeaderPanel.setSize(20,20);
//        setBounds(0,0,20,20);
//        add(LeaderPanel);

//        SecondPanel.setBackground(Color.black);
//        SecondPanel.setCameraSide("left");
//        BehindLeaderPanel.setBackground(Color.black);
//        BehindLeaderPanel.setCameraSide("front");
//        LastPanel.setBackground(Color.black);
//        LastPanel.setCameraSide("front");
////
        JLabel direction = new JLabel();
        direction.setLocation((int)leaderLocation.getWidth() + 20,(int)leaderLocation.getHeight() + 20);
        direction.setSize(500, 50);
        direction.setText("SWARM simulation");

        //        LastPanel.add(direction);
//
//
//
//        JSplitPane firstRow = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT , LeaderPanel, SecondPanel);
//        firstRow.setDividerLocation(300);
//        JSplitPane secRow = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT , BehindLeaderPanel, LastPanel);
//        secRow.setDividerLocation(300);
//
//        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,firstRow, secRow);
//        pane.setDividerLocation(Simulator.MAX_Y*SCALE / 2);
//        add(pane);
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
        BufferedImage image = null;


        if(keyPressed.equals("VK_RIGHT")) {
            try {
                image = ImageIO.read(new File("/home/zahar/IdeaProjects/SwarmSimulatorG/src/gui/leaderDirection/rollRight.png"));
                g2.drawImage(image, 250, 150, 100, 100, this);
//                g2.drawString("roll right", 250,30);
            } catch (IOException ex) {
                // handle exception...
            }
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

    public String getKeyPressed() {
        return keyPressed;
    }

    public void setKeyPressed(String keyPressed) {
        this.keyPressed = keyPressed;
    }

    public AgentScreen[] getAgentPanelsArr (){
        return members;
    }

    public Dimension getLeaderLocation() {
        return leaderLocation;
    }

    public void setLeaderLocation(Dimension leaderLocation) {
        this.leaderLocation = leaderLocation;
    }

    public AgentScreen getLeaderPanel() {
        return LeaderPanel;
    }

    public void setLeaderPanel(AgentScreen leaderPanel) {
        LeaderPanel = leaderPanel;
    }

    public AgentScreen getSecondPanel() {
        return SecondPanel;
    }

    public void setSecondPanel(AgentScreen secondPanel) {
        SecondPanel = secondPanel;
    }

    public AgentScreen getBehindLeaderPanel() {
        return BehindLeaderPanel;
    }

    public void setBehindLeaderPanel(AgentScreen behindLeaderPanel) {
        BehindLeaderPanel = behindLeaderPanel;
    }

    public AgentScreen getLastPanel() {
        return LastPanel;
    }

    public void setLastPanel(AgentScreen lastPanel) {
        LastPanel = lastPanel;
    }
}




