package gui;

import simulation.Drone;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

@SuppressWarnings("serial")
class droneComponent extends JComponent {

	private static final Random random = new Random();
	private static final int SIZE = 2;
	private Drone drone;
	private Polygon shape;
	private int shapeWidth = 10;
	private Color agentColor;
	private Drone.droneBehaviour currentBehaviour;

	droneComponent(Drone drone) {
		this.drone = drone;
		this.setSize(new Dimension(10, 10));
		this.setVisible(true);
		decideAgentColor();
		shape = new Polygon();
		shape.addPoint(0, 0);
		shape.addPoint(0, 0);
		shape.addPoint(0, 0);
	}

	private void decideAgentColor() {
		switch (drone.getAgentBehaviour()) {
			case SWARM_LEADER:
				this.agentColor = Color.RED;
				break;
			case FOLLOW_LEFT:
				this.agentColor = Color.BLACK;
				break;
			case FOLLOW_FRONT_IR:
				this.agentColor = Color.BLACK;
				break;
			case LAST:
				this.agentColor = Color.BLACK;
				break;
		}
	}

	void update() {
		if (drone.getAgentBehaviour() != currentBehaviour) {
			decideAgentColor();
		}
		currentBehaviour = drone.getAgentBehaviour();
		setLocation(drone.getX() * SwarmPanel.SCALE, drone.getY() * SwarmPanel.SCALE);
		setSize(new Dimension(drone.getHeight().getHeight(), drone.getHeight().getHeight()));
		this.repaint();

		int[] xs = shape.xpoints;
		int[] ys = shape.ypoints;
		xs[0] = getWidth() / 2;
		ys[0] = 0;
		xs[1] = xs[0] - shapeWidth;
		ys[1] = getHeight();
		xs[2] = xs[0] + shapeWidth;
		ys[2] = getHeight();
	}

	public Drone getDrone() {
		return drone;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(agentColor);
		g2.rotate(Math.toRadians(drone.getAngle()),
				getWidth() / 2, getHeight() / 2);
		g2.fill(shape);
	}
}