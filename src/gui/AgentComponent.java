package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;

import javax.swing.JComponent;

import simulation.Drone;

@SuppressWarnings("serial")
class AgentComponent extends JComponent {

	private static final Random random = new Random();
	private static final int SIZE = 2;
	private Drone drone;
	private Polygon shape;
	private int shapeWidth = 10;
	private Color agentColor;
	private Drone.droneBehaviour currentBehaviour;
	private int[] lastLocation;

	AgentComponent(Drone drone) {
		this.drone = drone;
//		this.setSize(new Dimension(SIZE*SwarmPanel.SCALE,SIZE*SwarmPanel.SCALE));
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

//	private static Color giveMeAColor() {
//		return new Color(getRandomRgb(),getRandomRgb(),getRandomRgb());
//	}

	public Drone getDrone() {
		return drone;
	}

//	private static int getRandomRgb() {
//		return random.nextInt(256);
//	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		//g2.clearRect(0, 0, getWidth(), getHeight());
		g2.setColor(agentColor);

//		System.out.println("*********** drone.getAngle() : " + drone.getAngle());
		g2.rotate(Math.toRadians(drone.getAngle()),
				getWidth() / 2, getHeight() / 2);

		g2.fill(shape);
//		this.setSize(new Dimension(drone.getHeight().getHeight(),drone.getHeight().getHeight()));
	}
}