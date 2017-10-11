package simulation.util;

import gui.ScreensPanel;
import gui.SwarmPanel;
import gui.droneComponent;
import simulation.Drone;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by zahar on 11/10/17.
 */
public class keyPress implements KeyListener {
    SwarmPanel panel ;
    ScreensPanel screens;

    public keyPress(SwarmPanel panel, ScreensPanel screens) {
        this.panel = panel;
        this.screens = screens;
    }

    //key listeners
    @Override
    public void keyPressed(KeyEvent e) {
        droneComponent[] components = panel.getComponents();
        Drone leaderDrone = components[0].getDrone();

        if(e.getKeyCode() == KeyEvent.VK_T){
            leaderDrone.setSideCommand(Drone.droneControl.TakeOf);
        }else if (e.getKeyCode() == KeyEvent.VK_L) {
            leaderDrone.setSideCommand(Drone.droneControl.Land);
        }else if (e.getKeyCode() == KeyEvent.VK_UP) {
            leaderDrone.setSideCommand(Drone.droneControl.PitchForward);
            screens.setKeyPressed("pitchForward");
        }else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            leaderDrone.setSideCommand(Drone.droneControl.RollRight);
            screens.setKeyPressed("rollRight");
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leaderDrone.setSideCommand(Drone.droneControl.RollLeft);
            screens.setKeyPressed("rollLeft");
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            leaderDrone.setSideCommand(Drone.droneControl.PitchBackward);
            screens.setKeyPressed("pitchBackward");
        }else if (e.getKeyCode() == KeyEvent.VK_SPACE) {;
            leaderDrone.setDirCommand(Drone.droneControl.Hover);
            leaderDrone.setSideCommand(Drone.droneControl.Hover);
            screens.setKeyPressed("stop");
        }else if (e.getKeyCode() == KeyEvent.VK_D) {;
            leaderDrone.setDirCommand(Drone.droneControl.YawRight);
            screens.setKeyPressed("yawRight");
        }else if (e.getKeyCode() == KeyEvent.VK_A) {
            leaderDrone.setDirCommand(Drone.droneControl.YawLeft);
            screens.setKeyPressed("yawLeft");
        }else if (e.getKeyCode() == KeyEvent.VK_W) {
            leaderDrone.setDirCommand(Drone.droneControl.ThrottleHigh);
            screens.setKeyPressed("throttleUp");
        }else if (e.getKeyCode() == KeyEvent.VK_S) {
            leaderDrone.setDirCommand(Drone.droneControl.ThrottleLow);
            screens.setKeyPressed("throttleDown");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //nothing to do
    }


    @Override
    public void keyTyped(KeyEvent e) {
        //nothing to do
    }
}
