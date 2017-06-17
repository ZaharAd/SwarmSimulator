package gui;

import simulation.Simulator;

import javax.swing.*;
import java.awt.*;

import static gui.SwarmPanel.SCALE;

/**
 * Created by zahar on 16/06/17.
 */
public class DirScreensPanel extends JPanel {


    private JSeparator separator;

    public DirScreensPanel() {
        init();
    }

    private void init() {

        final Dimension size = new Dimension(600, Simulator.MAX_Y*SCALE);

        setLayout(new GridLayout());
        setSize(size);
        setBounds(Simulator.MAX_X*SCALE +10, 0, Simulator.MAX_X*SCALE + 600  , Simulator.MAX_Y*SCALE);
        setBackground(Color.black);

        JPanel Leader = new JPanel();
        AgentPaintedPanel Second = new AgentPaintedPanel();
        AgentPaintedPanel BehindLeader = new AgentPaintedPanel();
        AgentPaintedPanel Last = new AgentPaintedPanel();

        Leader.setBackground(Color.yellow);
        Second.setBackground(Color.black);
        Second.paintPixel(150, 100 , 150, 200);
        BehindLeader.setBackground(Color.black);
        BehindLeader.paintPixel(150, 100 , 150, 200);
        Last.setBackground(Color.black);
        Last.paintPixel(150, 100 , 150, 200);

        JSplitPane firstRow = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT , Leader, Second);
        firstRow.setDividerLocation(300);
        JSplitPane secRow = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT , BehindLeader, Last);
        secRow.setDividerLocation(300);

        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,firstRow, secRow);
        pane.setDividerLocation(Simulator.MAX_Y*SCALE / 2);
        add(pane);

    }
}

