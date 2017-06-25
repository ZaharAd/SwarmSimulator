package simulation;

import gui.AgentPaintedPanel;
import gui.SwarmPanel;

import java.util.Timer;
import java.util.TimerTask;

import simulation.util.Vector2D;

import static simulation.Agent.AgentSwarmBehaviour.*;

public class Agent {


	public enum AgentSwarmBehaviour { SWARM_LEADER , FOLLOW_LEFT, FOLLOW_FRONT , LAST }

	private static final int INERTIA = 3;
	private static final int REACH = 20;
	private static final int AGENT_SIZE = SwarmPanel.SCALE;
	double x;
	double y;
	private Direction direction;
	private AgentSwarmBehaviour behaviour;
	private String command ="";
	private boolean isRabel = false;
	private int rabelCount;
	private double xDiff;
	private double yDiff;
	private double rabelErrorX;
	private double rabelErrorY;

	private AgentDailyCycle leaderCycle = new ByPressCycle();
	private AgentDailyCycle rightMemberCycle = new followFromLeftCycle();
	private AgentDailyCycle backMemberCycle = new followFromFrontCycle();


	private static Simulator sim = Simulator.getInstance();

	public Agent(AgentSwarmBehaviour behaviour, Direction direction, int x, int y) {
		this.behaviour = behaviour;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public Agent(AgentSwarmBehaviour behaviour, Direction direction, int x, int y, boolean isRabel) {
		this.behaviour = behaviour;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.isRabel = isRabel;
		rabelCount = 0;
		rabelErrorX = 0;
		rabelErrorY = 0;
	}

	public AgentSwarmBehaviour getAgentBehaviour() {
		return behaviour;
	}

	public void dailyCycle() {
		switch(behaviour) {
			case SWARM_LEADER:leaderCycle.dailyCycle(); break;
			case FOLLOW_LEFT:rightMemberCycle.dailyCycle(); break;
			case FOLLOW_FRONT: backMemberCycle.dailyCycle(); break;
			case LAST: backMemberCycle.dailyCycle(); break;
		}
	}


	private void goAhead() {
		double angle = Math.toRadians(direction.getAngle());
		xDiff = Math.sin(angle);
		yDiff = Math.cos(angle);
		x += xDiff;
		y -= yDiff;
	}

	private void goBack() {
		double angle = Math.toRadians(direction.getAngle());
		xDiff = Math.sin(angle);
		yDiff = Math.cos(angle);
		x -= xDiff;
		y += yDiff;
	}

	private void goToSide(String side) {
		double angle;
		if(side.equals("left")){
			angle = Math.toRadians(direction.getAngle() - 90);
		}else {
			angle = Math.toRadians(direction.getAngle() + 90);
		}

//		if(isRabel && rabelCount< 6) {
//			xDiff = Math.sin(angle) * 0.4;
//			yDiff = Math.cos(angle) * 0.4;
//
//			x += xDiff;
//			y -= yDiff;
//		}else {

		xDiff = Math.sin(angle);
		yDiff = Math.cos(angle);

		x += xDiff;
		y -= yDiff;
	}

	public static void setTimeout(Runnable runnable, int delay){

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {

			}
		}, 2000);
	}


	private void steerTowards(Vector2D position, double willingness) {
		if (position.x > x) {
			x += willingness;
		} else {
			x -= willingness;
		}
		if (position.y > y) {
			y += willingness;
		} else {
			y -= willingness;
		}
		if (position.distance(x, y) < AGENT_SIZE) {
			moveAwayFrom(position);// TODO use in the rabel
		}
	}

	private void moveAwayFrom(Vector2D position) {
		double step = AGENT_SIZE/INERTIA;
		x = position.x > x ? x - step : x + step;
		y = position.y > y ? y - step : y + step;
	}

	private void changeDirectionRandomly() {
		double random = Math.random();
		if (random < 0.01) {
			direction.turn(Simulator.randomGenerator.nextInt(180) - 90);
		}
	}

	private void headInTheSameDirection(int avgDirection, double willingness) {
		System.out.println("Was heading " + direction.getAngle() + " but avgDirection is " + avgDirection);
		System.out.println("Will: " + willingness);
		int diffAngle = avgDirection - direction.getAngle();
		direction.turn((int)(diffAngle*willingness));
		System.out.println("Heading in angle " + direction.getAngle());
	}

	private void headSomewhere() {
		double random = Math.random();
		if (random > 0.95) {
			direction.turn(1);
		} else if (random > 0.9) {
			direction.turn(-1);
		}
	}

	public Vector2D getPos() {
		return new Vector2D(x, y);
	}

	public void setPos(Vector2D pos) {
		this.x = pos.x;
		this.y = pos.y;
	}

	public int getX() {
		return (int) Math.round(x);
	}

	public int getY() {
		return (int) Math.round(y);
	}

	public int getAngle() {
		return direction.getAngle();
	}

	public Direction getDirection() {
		return direction;
	}

	public String toString() {
		return "X=" + x + " Y=" + y + " D=" + direction;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	private interface AgentDailyCycle {
		void dailyCycle();
	}

	public class ByPressCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
			AgentPaintedPanel[] screensToUpdate = new AgentPaintedPanel[]{sim.getPanel().getAgentPanelsArr()[0] ,
					sim.getPanel().getAgentPanelsArr()[1]};

			if(command.equals("goAhed")){
				goAhead();
			}else if(command.equals("goRight")){
				goToSide("right");
			}else if(command.equals("goLeft")){
				goToSide("left");
			}
			System.out.println(behaviour + ", command:" + command + "\n");

			updateFollowers(screensToUpdate, command);
		}
	}

	public class followFromLeftCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {

			AgentPaintedPanel secMemberPanel = sim.getPanel().getAgentPanelsArr()[0];
			AgentPaintedPanel[] screensToUpdate = new AgentPaintedPanel[]{sim.getPanel().getAgentPanelsArr()[2]};

			String findIR = secMemberPanel.findIRpoint(behaviour);
			System.out.println(behaviour + ", find:" + findIR+ "\n");
			followIR(findIR);
			updateFollowers(screensToUpdate,command);
			updateSelfScreen(secMemberPanel);
		}
	}



	public class followFromFrontCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
			AgentPaintedPanel secRowsPanel;

			if(behaviour.equals(FOLLOW_FRONT)) {
				secRowsPanel = sim.getPanel().getAgentPanelsArr()[1];
			}else {
				secRowsPanel = sim.getPanel().getAgentPanelsArr()[2];
			}

			String findIR = secRowsPanel.findIRpoint(behaviour);
			System.out.println(behaviour + ", find:" + findIR+ "\n");

			followIR(findIR);
			updateSelfScreen(secRowsPanel);
		}
	}

	private void updateFollowers(AgentPaintedPanel[] screensToUpdate, String command) {
		int xdiff = screensToUpdate[0].getDiffX();
		int dimdiff = screensToUpdate[0].getDiffDim();
		switch (command) {
			case "goAhed":
				switch (behaviour){
					case SWARM_LEADER:
						screensToUpdate[0].setCurrX_1(screensToUpdate[0].getCurrX_1() + xdiff);
						screensToUpdate[0].setCurrX_2(screensToUpdate[0].getCurrX_2() + xdiff);
						screensToUpdate[0].repaintPixel();

						screensToUpdate[1].setCurrIRdim(screensToUpdate[1].getCurrIRdim() - dimdiff);
						screensToUpdate[1].repaintPixel();

						break;
					case FOLLOW_LEFT:
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() - dimdiff);
						screensToUpdate[0].repaintPixel();

						break;
				}
				break;
			case "goRight":
				switch (behaviour){
					case SWARM_LEADER:
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() + dimdiff);
						screensToUpdate[0].repaintPixel();

						screensToUpdate[1].setCurrX_1(screensToUpdate[1].getCurrX_1() + xdiff);
						screensToUpdate[1].setCurrX_2(screensToUpdate[1].getCurrX_2() + xdiff);
						screensToUpdate[1].repaintPixel();

						break;
					case FOLLOW_LEFT:

						screensToUpdate[0].setCurrX_1(screensToUpdate[0].getCurrX_1() + xdiff);
						screensToUpdate[0].setCurrX_2(screensToUpdate[0].getCurrX_2() + xdiff);
						screensToUpdate[0].repaintPixel();

						break;
				}
				break;
			case "goLeft":
				switch (behaviour){
					case SWARM_LEADER:
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() - dimdiff);
						screensToUpdate[0].repaintPixel();

						screensToUpdate[1].setCurrX_1(screensToUpdate[1].getCurrX_1() - xdiff);
						screensToUpdate[1].setCurrX_2(screensToUpdate[1].getCurrX_2() - xdiff);
						screensToUpdate[1].repaintPixel();

						break;
					case FOLLOW_LEFT:
						screensToUpdate[0].setCurrX_1(screensToUpdate[0].getCurrX_1() - xdiff);
						screensToUpdate[0].setCurrX_2(screensToUpdate[0].getCurrX_2() - xdiff);
						screensToUpdate[0].repaintPixel();

						break;
				}
				break;
			case "goBack":
				switch (behaviour) {
					case SWARM_LEADER:
						screensToUpdate[1].setCurrIRdim(screensToUpdate[1].getCurrIRdim() + dimdiff);
						screensToUpdate[1].repaintPixel();
						break;

					case FOLLOW_FRONT:
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() + dimdiff);
						screensToUpdate[0].repaintPixel();
						break;
				}

				break;
			case "stop":
				break;
		}
	}

	private void followIR(String findIR) {
		if(findIR.equals("front")){
			goAhead();
			setCommand("goAhed");
		}else if(findIR.equals("right")){
			goToSide("right");
			setCommand("goRight");
		}else if(findIR.equals("left")) {
			goToSide("left");
			setCommand("goLeft");
		}else if(findIR.equals("back")){
			goBack();
			setCommand("goBack");
		}else{
			setCommand("stop");
		}

	}


	private void updateSelfScreen(AgentPaintedPanel memberPanel) {
		if(command.equals("goAhed")){
			switch (behaviour) {
				case FOLLOW_LEFT:
					memberPanel.setCurrX_1(memberPanel.getCurrX_1() - memberPanel.getDiffX());
					memberPanel.setCurrX_2(memberPanel.getCurrX_2() - memberPanel.getDiffX());
					memberPanel.repaintPixel();

					break;
				case FOLLOW_FRONT:
					memberPanel.setCurrIRdim(memberPanel.getCurrIRdim() + memberPanel.getDiffDim());
					memberPanel.repaintPixel();

					break;
				case LAST:
					memberPanel.setCurrIRdim(memberPanel.getCurrIRdim() + memberPanel.getDiffDim());
					memberPanel.repaintPixel();

					break;
			}
		}else if(command.equals("goLeft")){
			switch (behaviour) {
				case FOLLOW_LEFT:
					memberPanel.setCurrIRdim(memberPanel.getCurrIRdim() + memberPanel.getDiffDim());
					memberPanel.repaintPixel();

					break;
				case FOLLOW_FRONT:
					memberPanel.setCurrX_1(memberPanel.getCurrX_1() + memberPanel.getDiffX());
					memberPanel.setCurrX_2(memberPanel.getCurrX_2() + memberPanel.getDiffX());
					memberPanel.repaintPixel();

					break;
				case LAST:
					memberPanel.setCurrX_1(memberPanel.getCurrX_1() + memberPanel.getDiffX());
					memberPanel.setCurrX_2(memberPanel.getCurrX_2() + memberPanel.getDiffX());
					memberPanel.repaintPixel();

					break;
			}
		}else if(command.equals("goRight")){
			switch (behaviour) {
				case FOLLOW_LEFT:
					memberPanel.setCurrIRdim(memberPanel.getCurrIRdim() - memberPanel.getDiffDim());
					memberPanel.repaintPixel();

					break;
				case FOLLOW_FRONT:
					memberPanel.setCurrX_1(memberPanel.getCurrX_1() - memberPanel.getDiffX());
					memberPanel.setCurrX_2(memberPanel.getCurrX_2() - memberPanel.getDiffX());
					memberPanel.repaintPixel();

					break;
				case LAST:
					memberPanel.setCurrX_1(memberPanel.getCurrX_1() - memberPanel.getDiffX());
					memberPanel.setCurrX_2(memberPanel.getCurrX_2() - memberPanel.getDiffX());
					memberPanel.repaintPixel();

					break;
			}
		}
	}
}
