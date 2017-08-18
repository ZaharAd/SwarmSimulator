package simulation;

import gui.ScreensPanel;
import gui.SwarmPanel;

import java.util.*;

import javax.swing.SwingUtilities;

import simulation.Agent.AgentSwarmBehaviour;
import simulation.util.Vector2D;

public class Simulator implements Runnable {


	public static void main(String[] args) {

		final Simulator simulator = Simulator.getInstance();

		simulator.startGui();

		new Thread(simulator).start();

	}


	public static final int TIME_STEP = 50;
	public static final int MAX_X = 100;
	public static final int MAX_Y = 100;
	public static final int N_OF_AGENTS = 2;

	private SwarmPanel panel;

	private static Agent[][] grid;
	private Calendar today = new GregorianCalendar(2011, Calendar.MARCH, 13);
	private final Calendar LAST_DAY = new GregorianCalendar(2015, Calendar.MARCH, 24);

	static final Random randomGenerator = new Random();

	private static final Simulator sim = new Simulator();
	private boolean firstInSecRow = true;

	private Agent[] agents = new Agent[N_OF_AGENTS];

	private Simulator() {}

	public static Simulator getInstance() {
		if (grid == null) sim.init();
		return sim;
	}

	private void init() {
		System.out.println("Initialising the simulation");
		grid = new Agent[MAX_X][MAX_Y];
		int i = 0;
		Vector2D startPoint = new Vector2D(10, MAX_Y -20);
		Direction startDir = new Direction(0);

		//leader
		// * -
		// - -
		agents[i++] = grid[startPoint.getX()][startPoint.getY()] = new Agent(
				Agent.AgentSwarmBehaviour.SWARM_LEADER,
				startDir,
				startPoint.getX(),
				startPoint.getY()
		);
		//leaders row
		// - *
		// - -
//		agents[i++] = grid[startPoint.getX() + 10][startPoint.getY()] = new Agent(
//				AgentSwarmBehaviour.FOLLOW_LEFT,
////				AgentSwarmBehaviour.NORMAL,
//				startDir,
//				startPoint.getX() + 10,
//				startPoint.getY()
//		);

		//behind leader
		// - -
		// * -
		agents[i] = grid[startPoint.getX()][startPoint.getY() + 10] = new Agent(
				Agent.AgentSwarmBehaviour.FOLLOW_FRONT,
				startDir,
				startPoint.getX(),
				startPoint.getY() + 10
		);
		// - -
		// - *
//		agents[i++] = grid[startPoint.getX() + 10][startPoint.getY() - 10] = new Agent(
//				Agent.AgentSwarmBehaviour.LAST,
//				startDir,
//				startPoint.getX() + 10,
//				startPoint.getY()  + 10
//		);
	}
//
//	private Direction getRandomDirection() {
//		return new Direction(Math.random());
//	}

	public List<Agent> getNeighbours(Agent requester, int distance) {
		List<Agent> neighbours = new ArrayList<Agent>();
		int maxHor = requester.getX() + distance;
		maxHor = Math.min(maxHor, MAX_X-1);
		int maxVer = requester.getY() + distance;
		maxVer = Math.min(maxVer, MAX_Y-1);
		int minHor = requester.getX() - distance;
		minHor = Math.max(minHor, 0);
		int minVer = requester.getY() - distance;
		minVer = Math.max(minVer, 0);
		for (int hor = minHor; hor <= maxHor; hor++) {
			for (int ver = minVer; ver <= maxVer; ver++) {
				if (grid[hor][ver] != null && grid[hor][ver] != requester) neighbours.add(grid[hor][ver]);
			}
		}
		return neighbours;
	}

	private void startGui() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panel = new SwarmPanel();
				panel.init(agents);
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

		while (today.before(LAST_DAY)) {
			System.out.println();
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					if(grid[i][j] != null) System.out.println("i:" + i +", j:" +j + ": (" + grid[i][j]+")");
				}
			}
			for (Agent agent : agents) {
				tmpPos.x = agent.x;
				tmpPos.y = agent.y;

				if(panel != null && panel.getScreens() != null && panel.getScreens().getAgentPanelsArr() != null) {
					agent.dailyCycle();// read from screens / byPress for the leader
				}


				if (!agent.getPos().equals(tmpPos)) ensureAgentInValidPos(tmpPos, agent);
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

	public ScreensPanel getPanel() {
		return panel.getScreens();
	}

	private void ensureAgentInValidPos(Vector2D oldPos, Agent agent) {
		if (agent.getX() < 0) agent.x = MAX_X + agent.getX();
		if (agent.getX() >= MAX_X) agent.x = agent.getX() - MAX_X;
		if (agent.getY() < 0) agent.y = MAX_Y + agent.getY();
		if (agent.getY() >= MAX_Y) agent.y = agent.getY() - MAX_Y;
		Agent wantedGrid = grid[agent.getX()][agent.getY()];
		if (wantedGrid == null) {
			grid[agent.getX()][agent.getY()] = agent;
			grid[oldPos.getX()][oldPos.getY()] = null;
		} else if (wantedGrid != agent) {
			agent.setPos(oldPos);
		}
	}

	private void checkLocalityPrinciple() {
		Set<Vector2D> takenPos = new HashSet<Vector2D>();
		for (Agent agent : agents) {
			if (!takenPos.add(agent.getPos())) throw new RuntimeException("Position already taken: " + agent.getPos());
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
		for (Agent agent : agents) {
			System.out.println(agent);
		}
	}

	public boolean isFirstInSecRow() {
		return firstInSecRow;
	}

	public void setFirstInSecRow(boolean firstInSecRow) {
		this.firstInSecRow = firstInSecRow;
	}
}
