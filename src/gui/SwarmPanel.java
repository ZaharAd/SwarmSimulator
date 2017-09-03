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
	private ScreensPanel screens = ScreensPanel.getInstance();
	private boolean running = false;

	public static final int SCALE = 7;

	private JLabel messageLabel = new JLabel();
	//	private JPanel leaderPanel;
	private JLabel leaderDirection;;
	private int angle ;
	private double hight;
	private boolean isTurn = false;

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

//		screens = new ScreensPanel();

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

//		leaderPanel = screens.getLeaderPanel();
		leaderDirection = new JLabel();;
		Dimension screenLocation = screens.getLeaderLocation();
		leaderDirection.setLocation((int)screenLocation.getWidth()+300,(int)screenLocation.getHeight()+450);
//		leaderDirection.set
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

	public ScreensPanel getScreens() {
		return screens;
	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		Agent leaderAgent = components[0].getAgent();

		System.out.println("lalala");

		if(e.getKeyCode() == KeyEvent.VK_T){
			leaderAgent.setSideCommand(Agent.AgentControl.TakeOf);
//			screens.
//			components[0].
		}else if (e.getKeyCode() == KeyEvent.VK_L) {
			leaderAgent.setSideCommand(Agent.AgentControl.Land);
		}else if (e.getKeyCode() == KeyEvent.VK_UP) {
			leaderAgent.setSideCommand(Agent.AgentControl.PitchForward);
			leaderDirection.setText("\nFront key pressed");
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			leaderAgent.setSideCommand(Agent.AgentControl.RollRight);
			screens.setKeyPressed("VK_RIGHT");
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leaderAgent.setSideCommand(Agent.AgentControl.RollLeft);
			leaderDirection.setText("\nLeft key pressed");
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {;
			leaderAgent.setDirCommand(Agent.AgentControl.Hover);
			leaderAgent.setSideCommand(Agent.AgentControl.Hover);
		}else if (e.getKeyCode() == KeyEvent.VK_D) {;
			leaderAgent.setDirCommand(Agent.AgentControl.YawRight);
			leaderDirection.setText("\nYaw right");
		}else if (e.getKeyCode() == KeyEvent.VK_A) {
			leaderAgent.setDirCommand(Agent.AgentControl.YawLeft);
			leaderDirection.setText("\nYaw left");
		}else if (e.getKeyCode() == KeyEvent.VK_W) {
			leaderAgent.setDirCommand(Agent.AgentControl.ThrottleHigh);
			leaderDirection.setText("\nUp key pressed");
		}else if (e.getKeyCode() == KeyEvent.VK_S) {
			leaderAgent.setDirCommand(Agent.AgentControl.ThrottleLow);
			leaderDirection.setText("\nDown key pressed");
		}


//		screens.getAgentPanelsArr()[0].setLeaderDir(leaderDirection);
//		leaderPanel.add(leaderDirection);// TODO arrows
	}

	@Override
	public void keyReleased(KeyEvent e) {

		Agent leaderAgent = components[0].getAgent();
		leaderAgent.setDirCommand(Agent.AgentControl.Hover);

		if (e.getKeyCode() == KeyEvent.VK_D) {;

		}else if (e.getKeyCode() == KeyEvent.VK_A) {
//			components[1].update();
		}
		/*Agent leaderAgent = */
//		if (e.getKeyCode() == KeyEvent.VK_D) {
//			leaderAgent.setDirCommand(Agent.AgentControl.Hover);
//		}
//		if (e.getKeyCode() == KeyEvent.VK_A) {
//			leaderAgent.setDirCommand(Agent.AgentControl.Hover);
//		}
//		if (e.getKeyCode() == KeyEvent.VK_W) {
//			leaderAgent.setDirCommand(Agent.AgentControl.Hover);
//
//			leaderDirection.setText("\n" + hight + " meters");
//		}
//		if (e.getKeyCode() == KeyEvent.VK_S) {
//			leaderDirection.setText("\n" + hight + " meters");
//		}
//
//		leaderAgent.setDirCommand(Agent.AgentControl.Hover);
//
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				leaderDirection.setText("");
//			}
//		}, 5000);
//
//		hight = 0;
	}

}

