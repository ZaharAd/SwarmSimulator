package simulation;

import gui.SwarmPanel;

import java.util.*;

import javax.swing.SwingUtilities;

import simulation.util.Vector2D;

public class Main_Simulator implements Runnable {


	public static void main(String[] args) {

		final Main_Simulator simulator = Main_Simulator.getInstance();

		simulator.startGui();

		new Thread(simulator).start();

	}


	public static final int TIME_STEP = 50;
	public static final int MAX_X = 100;
	public static final int MAX_Y = 100;
	public static final int N_OF_AGENTS = 2;

	private SwarmPanel panel;

	private static Drone[][] grid;
	private Calendar today = new GregorianCalendar(2011, Calendar.MARCH, 13);
	private final Calendar LAST_DAY = new GregorianCalendar(2015, Calendar.MARCH, 24);


	private static final Main_Simulator sim = new Main_Simulator();

	private Drone[] drones = new Drone[N_OF_AGENTS];

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
		// *
		// -
		drones[i++] = grid[startPoint.getX()][startPoint.getY()] = new Drone(
				Drone.droneBehaviour.SWARM_LEADER,
				new Direction(0),
				new Height(15),
				startPoint.getX(),
				startPoint.getY()
		);
		//behind leader
		// -
		// *
		drones[i] = grid[startPoint.getX()][startPoint.getY() + 10] = new Drone(
				Drone.droneBehaviour.FOLLOW_FRONT_IR,
				new Direction(0),
				new Height(15),
				startPoint.getX(),
				startPoint.getY() + 10
		);
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
			}
		});
	}

	public void run() {

		printAgents();

//		printGrid();

		Vector2D tmpPos = new Vector2D();

		boolean[] respond = new boolean[3];
		for (int i = 0; i < respond.length; i++) {
			respond[i] = false;
		}

		while (today.before(LAST_DAY)) {//while(true){
			System.out.println();

			//print grid
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					if(grid[i][j] != null) System.out.println("i:" + i +", j:" +j + ": (" + grid[i][j]+")");
				}
			}

			for (Drone drone : drones) {
				tmpPos.x = drone.x;
				tmpPos.y = drone.y;

				if(panel != null && panel.getScreens() != null && panel.getScreens().getAgentPanelsArr() != null) {

					//the main function of the drones movemant
					drone.dailyCycle();
				}


				if (!drone.getPos().equals(tmpPos)) ensureAgentInValidPos(tmpPos, drone);
			}


			checkLocalityPrinciple();
			today.add(Calendar.DAY_OF_MONTH, 1);
			try {
				Thread.sleep(TIME_STEP);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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

	public void message(String message) {
		System.out.println(message);
		panel.message(message);
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
		if (count != N_OF_AGENTS) System.err.println("ERROR: Count is " + count);
	}

	private void printAgents() {
		for (Drone drone : drones) {
			System.out.println(drone);
		}
	}

}
