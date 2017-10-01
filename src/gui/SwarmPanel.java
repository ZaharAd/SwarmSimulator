package gui;

import simulation.Drone;
import simulation.Main_Simulator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class SwarmPanel extends JPanel implements KeyListener {

	/**
	 *
	 */
	private static final long serialVersionUID = -8787570229900908897L;
	private droneComponent[] components;
	private ScreensPanel screens = ScreensPanel.getInstance();
	private boolean running = false;
	public static final int SCALE = 7;
	private JLabel messageLabel = new JLabel();
	private JLabel leaderDirection;;

	public void stop() {
		running = false;
	}

	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}

	public void init(Drone[] drones) {
		final JFrame frame = new JFrame();
		final JFrame sframe = new JFrame();

		final Dimension screensPanelSize = new Dimension( 600 , Main_Simulator.MAX_Y*SCALE );
		final Dimension swarmPanelSize = new Dimension(Main_Simulator.MAX_X*SCALE , Main_Simulator.MAX_Y*SCALE );
		setSize(swarmPanelSize);
		setLayout(null);

		messageLabel.setLocation(20, 20);
		messageLabel.setSize(500, 50);
		messageLabel.setText("Swarm simulation Panel");
		add(messageLabel);

		//screens frame
		sframe.add(screens);
		JLayer<Component> blurLayer = new JLayer<>(screens, new BlurLayerUI());
		sframe.setVisible(true);
		sframe.setContentPane(blurLayer);
		sframe.setSize(screensPanelSize);
		sframe.setLocation(0,0);

		//simulation frame
		frame.add(this);
		frame.addKeyListener(this);
		frame.setSize(swarmPanelSize);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = (int) rect.getMaxX() - frame.getWidth();
		int y = 0;
		frame.setLocation(x, y);


		this.setOpaque(true);
		Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		this.setBorder(border);

		//init drone components
		components = new droneComponent[drones.length];
		for (int i = 0; i < components.length; i++) {
			components[i] = new droneComponent(drones[i]);
			add(components[i]);
		}

		startThread();
	}

	private void startThread() {
		running = true;
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (running) {
					for (droneComponent comp : components) {
						comp.update();
					};
				} else {
					System.out.println("CANCELLING TIMER");
					cancel();
				}
			}
		}, 500, Main_Simulator.TIME_STEP);
	}

	public ScreensPanel getScreens() {
		return screens;
	}


	//key listeners
	@Override
	public void keyPressed(KeyEvent e) {
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
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {;
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

