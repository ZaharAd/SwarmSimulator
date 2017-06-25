package gui;

import simulation.Simulator;

import javax.swing.*;
import java.awt.*;

import static gui.SwarmPanel.SCALE;

/**
 * Created by zahar on 16/06/17.
 */
public class FollowScreensPanel extends JPanel {

    private JPanel LeaderPanel;
    private AgentPaintedPanel SecondPanel;
    private AgentPaintedPanel BehindLeaderPanel;
    private AgentPaintedPanel LastPanel;

    private AgentPaintedPanel[] members;
    private Dimension leaderLocation;//, secondLocation, behindeLocation, lastLocation;

    public FollowScreensPanel() {
        leaderLocation = new Dimension(Simulator.MAX_X*SCALE +10, 0);


        final Dimension size = new Dimension(600, Simulator.MAX_Y*SCALE);

        setLayout(new GridLayout());
        setSize(size);

        setBounds(Simulator.MAX_X*SCALE +10, 0, Simulator.MAX_X*SCALE + 600  , Simulator.MAX_Y*SCALE);
        setBackground(Color.black);

        LeaderPanel = new JPanel();
        SecondPanel = new AgentPaintedPanel();
        BehindLeaderPanel = new AgentPaintedPanel();
        LastPanel = new AgentPaintedPanel();

        members =new AgentPaintedPanel[] {SecondPanel, BehindLeaderPanel, LastPanel};

        LeaderPanel.setBackground(Color.yellow);
        SecondPanel.setBackground(Color.black);
        SecondPanel.setCameraSide("left");
        BehindLeaderPanel.setBackground(Color.black);
        BehindLeaderPanel.setCameraSide("front");
        LastPanel.setBackground(Color.black);
        LastPanel.setCameraSide("front");
//
//        JLabel direction = new JLabel();
//        direction.setLocation((int)leaderLocation.getWidth() + 20,(int)leaderLocation.getHeight() + 20);
//        direction.setSize(500, 50);
//        direction.setText("SWARM simulation");
//        LastPanel.add(direction);



        JSplitPane firstRow = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT , LeaderPanel, SecondPanel);
        firstRow.setDividerLocation(300);
        JSplitPane secRow = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT , BehindLeaderPanel, LastPanel);
        secRow.setDividerLocation(300);

        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,firstRow, secRow);
        pane.setDividerLocation(Simulator.MAX_Y*SCALE / 2);
        add(pane);
    }

    public AgentPaintedPanel[] getAgentPanelsArr (){
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

    public AgentPaintedPanel getSecondPanel() {
        return SecondPanel;
    }

    public void setSecondPanel(AgentPaintedPanel secondPanel) {
        SecondPanel = secondPanel;
    }

    public AgentPaintedPanel getBehindLeaderPanel() {
        return BehindLeaderPanel;
    }

    public void setBehindLeaderPanel(AgentPaintedPanel behindLeaderPanel) {
        BehindLeaderPanel = behindLeaderPanel;
    }

    public AgentPaintedPanel getLastPanel() {
        return LastPanel;
    }

    public void setLastPanel(AgentPaintedPanel lastPanel) {
        LastPanel = lastPanel;
    }
}

