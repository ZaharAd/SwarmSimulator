package gui;

import simulation.Simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static gui.SwarmPanel.SCALE;

/**
 * Created by zahar on 16/06/17.
 */
public class FollowScreensPanel extends JPanel {

    private JPanel LeaderPanel;
    private AgentScreen SecondPanel;
    private AgentScreen BehindLeaderPanel;
    private AgentScreen LastPanel;

    private static AgentScreen[] members;
    private Dimension leaderLocation;//, secondLocation, behindeLocation, lastLocation;

    private static final FollowScreensPanel screens = new FollowScreensPanel();

    public static FollowScreensPanel getInstance() {
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

        LeaderPanel = new JPanel();

        SecondPanel = new AgentScreen();
        SecondPanel.setCameraSide("left");

        BehindLeaderPanel = new AgentScreen();
        BehindLeaderPanel.setCameraSide("frontL");

        LastPanel = new AgentScreen();
        LastPanel.setCameraSide("frontS");

        members = new AgentScreen[] {SecondPanel, BehindLeaderPanel, LastPanel};
//
////        paintComponent();
//
//        LeaderPanel.setBackground(Color.yellow);
//        LeaderPanel.setLocation(0,0);
//        LeaderPanel.setSize(20,20);
////        setBounds(0,0,20,20);
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

        members[0].draw(g2,300,0);
        members[1].draw(g2,0,365);
        members[2].draw(g2,300,365);

    }

    public void repaintPoints(){
        repaint();
    }

    public String findIRpoint(String side) {
        BufferedImage img = new BufferedImage(600, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        this.paint(g2);

        String ans = "";

        if(side.equals("left")){
            ans = members[0].firstRowReading(img);
        }else if(side.equals("frontL")){
            ans = members[1].secRowReading(img,0,365);
        }else if(side.equals("frontS")){
            ans = members[2].secRowReading(img,300,365);
        }

        g2.dispose();
        return ans;
    }

    public FollowScreensPanel() {

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

    public JPanel getLeaderPanel() {
        return LeaderPanel;
    }

    public void setLeaderPanel(JPanel leaderPanel) {
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

