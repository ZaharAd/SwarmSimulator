package gui;

import simulation.Drone;
import simulation.Main_Simulator;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class SwarmPanel extends JPanel {

	private static final long serialVersionUID = -8787570229900908897L;
	private droneComponent[] components;
	private boolean running = false;
	private JLabel messageLabel = new JLabel();
	static final int SCALE = 7;

	public void stop() {
		running = false;
	}

	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}

	public void init(Drone[] drones) {
		final Dimension swarmPanelSize = new Dimension(Main_Simulator.MAX_X*SCALE , Main_Simulator.MAX_Y*SCALE );
		setSize(swarmPanelSize);

		messageLabel.setLocation(20, 20);
		messageLabel.setSize(500, 50);
		messageLabel.setText("Swarm simulation Panel");
		add(messageLabel);

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

	@Override
	public droneComponent[] getComponents() {
		return components;
	}
}

