package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;

import javax.swing.JComponent;

import simulation.Agent;
import simulation.Agent.AgentBehaviour;

@SuppressWarnings("serial")
class AgentComponent extends JComponent {

	private static final Random random = new Random();
	private static final int SIZE = 2;
	private Agent agent;
	private Polygon shape;
	private int shapeWidth = 10;
	private Color agentColor;
	private AgentBehaviour currentBehaviour;
	private int[] lastLocation;

	AgentComponent(Agent agent) {
		this.agent = agent;
		this.setSize(new Dimension(SIZE*SwarmPanel.SCALE,SIZE*SwarmPanel.SCALE));
		this.setVisible(true);
		decideAgentColor();
		shape = new Polygon();
		shape.addPoint(0, 0);
		shape.addPoint(0, 0);
		shape.addPoint(0, 0);
	}

	private void decideAgentColor() {
		switch (agent.getAgentBehaviour()) {
			case MIXED: this.agentColor = Color.GREEN; break;
			case NORMAL: this.agentColor =  Color.BLACK; break;//giveMeAColor(); break;
			case REBEL: this.agentColor = Color.RED; break;
			case SWARM_LEADER: this.agentColor = Color.RED; break;
		}
	}

	void update() {
		if (agent.getAgentBehaviour() != currentBehaviour) {
			decideAgentColor();
		}
		currentBehaviour = agent.getAgentBehaviour();
		setLocation(agent.getX()*SwarmPanel.SCALE, agent.getY()*SwarmPanel.SCALE);
		int[] xs = shape.xpoints;
		int[] ys = shape.ypoints;
		xs[0] = getWidth()/2;
		ys[0] = 0;
		xs[1] = xs[0] - shapeWidth;
		ys[1] = getHeight();
		xs[2] = xs[0] + shapeWidth;
		ys[2] = getHeight();
	}

	private static Color giveMeAColor() {
		return new Color(getRandomRgb(),getRandomRgb(),getRandomRgb());
	}

	public Agent getAgent() {
		return agent;
	}

	private static int getRandomRgb() {
		return random.nextInt(256);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		//g2.clearRect(0, 0, getWidth(), getHeight());
		g2.setColor(agentColor);

		g2.rotate(Math.toRadians(agent.getAngle()),
				getWidth()/2, getHeight()/2);
		g2.fill(shape);
	}

	public void setLastLocation(int[] lastLocation) {
		this.lastLocation = lastLocation;
	}
}