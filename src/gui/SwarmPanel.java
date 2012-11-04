package gui;

import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import simulation.Agent;
import simulation.Simulator;

public class SwarmPanel extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8787570229900908897L;
	
	private AgentComponent[] components;
	private boolean running = false;
	
	public static final int SCALE = 8;
	
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
		
		final Dimension size = new Dimension(Simulator.MAX_X*SCALE, Simulator.MAX_Y*SCALE);
		setSize(size);
		setLayout(null);
		messageLabel.setLocation(20, 20);
		messageLabel.setSize(500, 50);
		messageLabel.setText("Running simulation");
		add(messageLabel);

		final JFrame frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(size);
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