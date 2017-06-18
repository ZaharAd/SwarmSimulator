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
	private FollowScreensPanel screens = new FollowScreensPanel();

	private boolean running = false;

	public static final int SCALE = 7;

	private JLabel messageLabel = new JLabel();

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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
	    JPanel leader = screens.getLeaderPanel();
        JLabel direction = new JLabel();
        Dimension screenLocation = screens.getLeaderLocation();

        direction.setLocation((int)screenLocation.getWidth() + 70,(int)screenLocation.getHeight() + 70);
        direction.setSize(500, 50);
        direction.setText("SWARM simulation");
        leader.add(direction);
//        screens.setLeaderPanel(leader);

        if (e.getKeyCode() == KeyEvent.VK_D) {
            System.out.println("Clockwise key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            System.out.println("CounterClockwise key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            System.out.println("Up key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("Down key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("Right key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("Left key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("Front key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            System.out.println("Stop key pressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            System.out.println("Clockwise key Released");
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            System.out.println("CounterClockwise key Released");
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            System.out.println("Up key Released");
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("Down key Released");
        }
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

