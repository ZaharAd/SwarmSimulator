package simulation;

import gui.BlurLayerUI;
import gui.ScreensPanel;
import gui.SwarmPanel;
import simulation.util.Vector2D;
import simulation.util.keyPress;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class Main_Simulator implements Runnable  {


	public static void main(String[] args) {

		final Main_Simulator simulator = Main_Simulator.getInstance();

		simulator.startGui();

		new Thread(simulator).start();

	}


	public static final int TIME_STEP = 50;
	public static final int MAX_X = 100;
	public static final int MAX_Y = 100;
	public static final int N_OF_MEMBERS = 4;
	public static final int SCALE = 7;


	private SwarmPanel panel;
	private ScreensPanel screens ;


	private static Drone[][] grid;
	private Calendar today = new GregorianCalendar(2011, Calendar.MARCH, 13);
	private final Calendar LAST_DAY = new GregorianCalendar(2015, Calendar.MARCH, 24);

	private static final Main_Simulator sim = new Main_Simulator();
	private Drone[] drones = new Drone[N_OF_MEMBERS];

	private Main_Simulator() {}

	/**
	 * singelton instance creation
	 * @return
	 */
	public static Main_Simulator getInstance() {
		if (grid == null) sim.init();
		return sim;
	}

	private void init() {
		System.out.println("Initialising the simulation");
		grid = new Drone[MAX_X][MAX_Y];
		int i = 0;
		Vector2D startPoint = new Vector2D(10, MAX_Y -20);

		//leader
		// *  -
		// -  -
		drones[i++] = grid[startPoint.getX()][startPoint.getY()] = new Drone(
				Drone.droneBehaviour.SWARM_LEADER,new Direction(0),new Height(15),startPoint.getX(),startPoint.getY());

		//behind leader
		// -  -
		// *  -
		drones[i++] = grid[startPoint.getX()][startPoint.getY() + 10] = new Drone(
				Drone.droneBehaviour.FOLLOW_FRONT_IR,new Direction(0),new Height(15),startPoint.getX(),startPoint.getY() + 10);

		//beside leader
		// -  *
		// -  -
		drones[i++] = grid[startPoint.getX() + 10][startPoint.getY()] = new Drone(
				Drone.droneBehaviour.FOLLOW_LEFT_IR,new Direction(0),new Height(15),startPoint.getX() + 10,startPoint.getY());

		//behind beside leader
		// -  -
		// -  *
		drones[i] = grid[startPoint.getX() + 10][startPoint.getY() + 10] = new Drone(
				Drone.droneBehaviour.FOLLOW_FRONT_IR,new Direction(0),new Height(15),startPoint.getX() + 10,startPoint.getY() + 10);
	}


	/**
	 * Start the SwarmPanel
	 */
	private void startGui() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panel = new SwarmPanel();
				panel.init(drones);
				screens = ScreensPanel.getInstance();


				keyPress keyPress = new keyPress(panel,screens);

				final JFrame frame = new JFrame();
				final JFrame screensFrame = new JFrame();

				final Dimension screensPanelSize = new Dimension( 600 , Main_Simulator.MAX_Y*SCALE );
				final Dimension swarmPanelSize = new Dimension(Main_Simulator.MAX_X*SCALE , Main_Simulator.MAX_Y*SCALE );

				//screens frame
				screensFrame.add(screens);
				JLayer<Component> blurLayer = new JLayer<>(screens, new BlurLayerUI());
				screensFrame.setContentPane(blurLayer);
				screensFrame.setVisible(true);
				screensFrame.setSize(screensPanelSize);
				screensFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				screensFrame.setLocation(0,0);



				//simulation frame
//				frame.setLayout(new GridLayout(1,2));
				frame.add(panel);
//				frame.add(screens);
				frame.addKeyListener(keyPress);
				frame.setVisible(true);
				frame.setSize(Main_Simulator.MAX_X*SCALE, Main_Simulator.MAX_Y*SCALE);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setLocation(0,0);


			}
		});
	}

	public void run() {

		printAgents();
//		printGrid();
		Vector2D tmpPos = new Vector2D();

		while (today.before(LAST_DAY)) {//while(true){
			System.out.println();
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					if(grid[i][j] != null) System.out.println("i:" + i +", j:" +j + ": (" + grid[i][j]+")");
				}
			}

			for (Drone drone : drones) {
				tmpPos.x = drone.x;
				tmpPos.y = drone.y;

				if(panel != null && screens != null && screens.getMemberScreens() != null) {
					drone.dailyCycle();//the main function of the drones movemant
				}

				if (!drone.getPos().equals(tmpPos)) ensureAgentInValidPos(tmpPos, drone);
			}


			checkLocalityPrinciple();
			today.add(Calendar.DAY_OF_MONTH, 1);
			try {
				Thread.sleep(TIME_STEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		panel.stop();
		System.out.println("END");
		printGrid();
	}

	private void ensureAgentInValidPos(Vector2D oldPos, Drone drone) {
		if (drone.getX() < 0) drone.x = MAX_X + drone.getX();
		if (drone.getX() >= MAX_X) drone.x = drone.getX() - MAX_X;
		if (drone.getY() < 0) drone.y = MAX_Y + drone.getY();
		if (drone.getY() >= MAX_Y) drone.y = drone.getY() - MAX_Y;
		Drone wantedGrid = grid[drone.getX()][drone.getY()];
		if (wantedGrid == null) {
			grid[drone.getX()][drone.getY()] = drone;
			grid[oldPos.getX()][oldPos.getY()] = null;
		} else if (wantedGrid != drone) {
			drone.setPos(oldPos);
		}
	}

	private void checkLocalityPrinciple() {
		Set<Vector2D> takenPos = new HashSet<Vector2D>();
		for (Drone drone : drones) {
			if (!takenPos.add(drone.getPos())) throw new RuntimeException("Position already taken: " + drone.getPos());
		}
	}


	private void printGrid() {
		int count = 0;
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				System.out.print(grid[x][y]!=null ? "X" : "-");
				if (grid[x][y]!=null) count++;
			}
			System.out.println();
		}
		if (count != N_OF_MEMBERS) System.err.println("ERROR: Count is " + count);
	}

	private void printAgents() {
		for (Drone drone : drones) {
			System.out.println(drone);
		}
	}



}
