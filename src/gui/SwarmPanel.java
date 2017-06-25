package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import simulation.Agent;
import simulation.Simulator;

public class SwarmPanel extends JPanel implements KeyListener {

	/**
	 *
	 */
	private static final long serialVersionUID = -8787570229900908897L;

	private AgentComponent[] components;
	private FollowScreensPanel screens;
	private boolean running = false;

	public static final int SCALE = 7;

	private JLabel messageLabel = new JLabel();
	private JPanel leaderPanel;
	private JLabel leaderDirection;;
	private int angle ;
	private double hight;
	private boolean isTurn = false;
	private int rabelError = 0;

	public void stop() {
		running = false;
	}

	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}

	public void init(Agent[] agents) {

		final Dimension sizeWithScreesns = new Dimension(Simulator.MAX_X*SCALE + 600 , Simulator.MAX_Y*SCALE );

		final Dimension swarmPanelSize = new Dimension(Simulator.MAX_X*SCALE , Simulator.MAX_Y*SCALE );
		setSize(swarmPanelSize);
		setLayout(null);

		messageLabel.setLocation(20, 20);
		messageLabel.setSize(500, 50);
		messageLabel.setText("SWARM simulation");
		add(messageLabel);

		screens = new FollowScreensPanel();

		final JFrame frame = new JFrame();
		frame.add(screens);
		frame.add(this);
		frame.addKeyListener(this);
		frame.setSize(sizeWithScreesns);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		this.setOpaque(true);
		Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		this.setBorder(border);

		components = new AgentComponent[agents.length];
		for (int i = 0; i < components.length; i++) {
			components[i] = new AgentComponent(agents[i]);
			add(components[i]);
		}


		leaderPanel = screens.getLeaderPanel();
		leaderDirection = new JLabel();;
		Dimension screenLocation = screens.getLeaderLocation();
		leaderDirection.setLocation((int)screenLocation.getWidth()+300,(int)screenLocation.getHeight()+450);
		leaderDirection.setSize(100, 50);


		angle = 0;
		hight = 0;


		startThread();

	}

	private void startThread() {
		running = true;
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (running) {
					for (AgentComponent comp : components) {
						comp.update();
					};
				} else {
					System.out.println("CANCELLING TIMER");
					cancel();
				}
			}
		}, 500, Simulator.TIME_STEP);
	}

	public void message(String message) {
		messageLabel.setText(message);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				messageLabel.setText("");
			}
		}, 2000);
	}

	public FollowScreensPanel getScreens() {
		return screens;
	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		Agent leaderAgent = components[0].getAgent();

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			leaderAgent.setCommand("goAhed");
			leaderDirection.setText("\nFront key pressed");
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			leaderAgent.setCommand("stop");
			leaderDirection.setText("\nStop key pressed");
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			leaderAgent.setCommand("goRight");
			leaderDirection.setText("\nRight key pressed");
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leaderAgent.setCommand("goLeft");
			leaderDirection.setText("\nLeft key pressed");
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {;
			isTurn = true;
			angle ++;
			leaderAgent.getDirection().turn(1);
			leaderDirection.setText("\nClockwise");
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			isTurn = true;
			angle++;
			leaderAgent.getDirection().turn(-1);
			leaderDirection.setText("\nCounterClockwise");
		}
		if (e.getKeyCode() == KeyEvent.VK_W) {
			hight+= 0.25;
			leaderDirection.setText("\nUp key pressed");
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			hight+= 0.25;
			leaderDirection.setText("\nDown key pressed");
		}

		leaderPanel.add(leaderDirection);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Agent leader = components[0].getAgent();
		if (e.getKeyCode() == KeyEvent.VK_D) {

		}
		if (e.getKeyCode() == KeyEvent.VK_A) {

		}
		if (e.getKeyCode() == KeyEvent.VK_W) {
			leaderDirection.setText("\n" + hight + " meters");
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			leaderDirection.setText("\n" + hight + " meters");
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				leaderDirection.setText("");
			}
		}, 5000);

		hight = 0;
	}

}

