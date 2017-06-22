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
	private int[] lastLoaction;


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
			components[i].setLastLocation(new int[] {});
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

	public void updateScreens(String command, Agent.AgentBehaviour agentBehaviour) {
		System.out.println("command " + command + ", " + agentBehaviour);

		switch (command) {
			case "goAhed":

				switch (agentBehaviour){
					case SWARM_LEADER:
						screens.getSecondPanel().paintPixel(
								screens.getSecondPanel().getX_1() + 15, screens.getSecondPanel().getY_1(),
								screens.getSecondPanel().getX_2() + 15, screens.getSecondPanel().getY_2());

						screens.getBehindLeaderPanel().setCurrIRdim(screens.getSecondPanel().getIRdim() - 2);
						screens.getBehindLeaderPanel().repaintPixel();

						screens.getLastPanel().setCurrIRdim(screens.getSecondPanel().getIRdim() - 2);
						screens.getLastPanel().repaintPixel();
						break;
					case FOLLOW_LEFT:
						screens.getSecondPanel().paintPixel(
								screens.getSecondPanel().getX_1() - 15, screens.getSecondPanel().getY_1(),
								screens.getSecondPanel().getX_2() - 15, screens.getSecondPanel().getY_2());
						break;
					case FOLLOW_FRONT:
						break;
				}
				break;
			case "goRight":
				switch (agentBehaviour){
					case SWARM_LEADER:
						screens.getSecondPanel().setCurrIRdim(screens.getSecondPanel().getIRdim() + 2);
						screens.getSecondPanel().repaintPixel();

						screens.getBehindLeaderPanel().paintPixel(
								screens.getBehindLeaderPanel().getX_1() + 15, screens.getBehindLeaderPanel().getY_1(),
								screens.getBehindLeaderPanel().getX_2() + 15, screens.getBehindLeaderPanel().getY_2());

						screens.getLastPanel().paintPixel(
								screens.getLastPanel().getX_1() + 15, screens.getLastPanel().getY_1(),
								screens.getLastPanel().getX_2() + 15, screens.getLastPanel().getY_2());

						break;
					case FOLLOW_LEFT:
						screens.getSecondPanel().setCurrIRdim(screens.getSecondPanel().getIRdim());
						screens.getSecondPanel().repaintPixel();
						break;
					case FOLLOW_FRONT:
						break;
				}
				break;
			case "goLeft":
				switch (agentBehaviour){
					case SWARM_LEADER:
						screens.getSecondPanel().setCurrIRdim(screens.getSecondPanel().getIRdim() - 2);
						screens.getSecondPanel().repaintPixel();


						screens.getBehindLeaderPanel().paintPixel(
								screens.getBehindLeaderPanel().getX_1() - 15, screens.getBehindLeaderPanel().getY_1(),
								screens.getBehindLeaderPanel().getX_2() - 15, screens.getBehindLeaderPanel().getY_2());

						screens.getLastPanel().paintPixel(
								screens.getLastPanel().getX_1() - 15, screens.getLastPanel().getY_1(),
								screens.getLastPanel().getX_2() - 15, screens.getLastPanel().getY_2());

						break;
					case FOLLOW_LEFT:
						screens.getSecondPanel().setCurrIRdim(screens.getSecondPanel().getIRdim());
						screens.getSecondPanel().repaintPixel();
						break;
					case FOLLOW_FRONT:
						break;
				}
				break;
			case "stop":

				break;
		}
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
//			leader.getDirection().turn(angle);
//			leaderDirection.setText("\n"+ angle + " degrees");
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
//			leader.getDirection().turn(-angle);
//			leaderDirection.setText("\n"+ angle + " degrees");
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

//		angle = 0;
		hight = 0;
	}



//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		//final Graphics2D g2 = (Graphics2D) g;
//		//g2.setBackground(Color.WHITE);
//		//g2.setColor(Color.BLUE);
//		//g2.drawRect(0, 0, Simulator.MAX_X*SCALE, Simulator.MAX_Y*SCALE);
//
//
//	}





}

