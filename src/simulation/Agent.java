package simulation;

import gui.AgentPaintedPanel;
import gui.SwarmPanel;

import java.util.Arrays;
import java.util.List;

import simulation.util.Vector2D;

public class Agent {


	public enum AgentBehaviour { NORMAL, REBEL, MIXED, SWARM_LEADER , FOLLOW_LEFT, FOLLOW_FRONT , LAST }

	private static final int INERTIA = 3;
	private static final int REACH = 20;
	private static final int AGENT_SIZE = SwarmPanel.SCALE;
	double x;
	double y;
	private Direction direction;
	private AgentBehaviour behaviour;
	private String command ="";

	private AgentDailyCycle leaderCycle = new ByPressCycle();
	private AgentDailyCycle rightMemberCycle = new followFromLeftCycle();
	private AgentDailyCycle backMemberCycle = new followFromFrontCycle();


	private static Simulator sim = Simulator.getInstance();

	public Agent(AgentBehaviour behaviour, Direction direction, int x, int y) {
		this.behaviour = behaviour;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public AgentBehaviour getAgentBehaviour() {
		return behaviour;
	}

	public void dailyCycle() {
		switch(behaviour) {
			case SWARM_LEADER:leaderCycle.dailyCycle(); break;
			case FOLLOW_LEFT:rightMemberCycle.dailyCycle(); break;
			case FOLLOW_FRONT: backMemberCycle.dailyCycle(); break;
			case LAST: backMemberCycle.dailyCycle(); break;
//			case MIXED:
//				if (mixedAgentSettings.timeAsRebel > 0) {
//					rebelCycle.dailyCycle();
//					mixedAgentSettings.timeAsRebel++;
//					if (mixedAgentSettings.timeAsRebel > MixedAgentSettings.T_AS_REBEL) {
//						mixedAgentSettings.timeAsRebel = 0;
//					}
//				} else if (Math.random() < MixedAgentSettings.P_OF_GETTING_REBEL) {
//					sim.message("Agent turning rebel");
//					rebelCycle.dailyCycle();
//					mixedAgentSettings.timeAsRebel = 1;
//				} else {
//					normalCycle.dailyCycle();
//				}
//				break;
//			case NORMAL: normalCycle.dailyCycle(); break;
//			case REBEL: rebelCycle.dailyCycle(); break;
//			case SWARM_LEADER: leaderCycle.dailyCycle(); break; //move by key press
//			case FOLLOW_LEFT: swarmCycle.dailyCycle(); break;
//			case FOLLOW_FRONT: swarmCycle.dailyCycle(); break;

		}
	}


	private void goAhead() {
		double angle = Math.toRadians(direction.getAngle());
		double xDiff = Math.sin(angle);
		double yDiff = Math.cos(angle);
		x += xDiff;
		y -= yDiff;
	}

	private void goToSide(String side) {
		double angle;
		if(side.equals("left")){
			angle = Math.toRadians(direction.getAngle() - 90);
		}else {
			angle = Math.toRadians(direction.getAngle() + 90);
		}
		double xDiff = Math.sin(angle);
		double yDiff = Math.cos(angle);
		x += xDiff;
		y -= yDiff;
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

	private Agent getAgent() {
		return this;
	}

	public String getCommand() {
		return command;
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
			if(command.equals("goAhed")){
				goAhead();
			}else if(command.equals("goRight")){
				goToSide("right");
			}else if(command.equals("goLeft")){
				goToSide("left");
			}
		}

	}

	public class followFromLeftCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {

			AgentPaintedPanel fromLeftTrack = sim.getPanel().getAgentPanelsArr()[0];

			if (fromLeftTrack.getX_1() == fromLeftTrack.getCurrX_1() - 15
					&& fromLeftTrack.getX_2() == fromLeftTrack.getCurrX_2() -  15) {
				setCommand("goAhed");
				goAhead();
			}else if(fromLeftTrack.getIRdim() < fromLeftTrack.getCurrIRdim()){
				setCommand("goRight");
				goToSide("right");
			}else if(fromLeftTrack.getIRdim() > fromLeftTrack.getCurrIRdim()){
				setCommand("goLeft");
				goToSide("left");
			}else{
				setCommand("stop");
			}
		}
	}


	public class followFromFrontCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
			AgentPaintedPanel fromFrontTrack;

			if(behaviour.equals(AgentBehaviour.FOLLOW_FRONT)) {
				fromFrontTrack = sim.getPanel().getAgentPanelsArr()[1];
			}else {
				fromFrontTrack = sim.getPanel().getAgentPanelsArr()[2];
			}
//
//			System.out.println("irDim: " + fromFrontTrack.getIRdim()  + ", curr: " + fromFrontTrack.getCurrIRdim());
			System.out.println("getX_1(): " + fromFrontTrack.getX_1()  + ", getX_2(): " + fromFrontTrack.getX_2());
			System.out.println("getX_1(): " + fromFrontTrack.getCurrX_1()  + ", getX_2(): " + fromFrontTrack.getCurrX_2());

			if(fromFrontTrack.getIRdim() > fromFrontTrack.getCurrIRdim()){
				setCommand("goAhed");
				goAhead();
			}else if (fromFrontTrack.getX_1() == fromFrontTrack.getCurrX_1() -  15
					&& fromFrontTrack.getX_2() == fromFrontTrack.getCurrX_2() -  15) {
				setCommand("goRight");
				goToSide("right");
			}else if(fromFrontTrack.getX_1() == fromFrontTrack.getCurrX_1() + 15
					&& fromFrontTrack.getX_2() == fromFrontTrack.getCurrX_2() + 15) {
				setCommand("goLeft");
				goToSide("left");
			}else{
				setCommand("stop");
			}
		}
	}

	private class NormalAgentDailyCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
			List<Agent> neighbours = sim.getNeighbours(getAgent(), REACH);
			int numberOfFriends = neighbours.size();

			if (numberOfFriends > 0) {
				Vector2D avgPos = new Vector2D();
				int avgDirection = 0;
				for (Agent friend : neighbours) {
					avgPos.add(friend.getX(), friend.getY());
					avgDirection += friend.direction.getAngle();
				}
				avgPos.divide(numberOfFriends);
				avgDirection /= numberOfFriends;

				double willingness = ((double) numberOfFriends)/ (double)INERTIA;
				if (willingness > 1.0) willingness = 1.0;
				steerTowards(avgPos, willingness);
				headInTheSameDirection(avgDirection, willingness);
				goAhead();
			} else {
				headSomewhere();
				goAhead();
			}
		}
	}

	private class RebelAgentDailyCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
			System.out.println("Rebel agent in action");
			changeDirectionRandomly();
			goAhead();
		}

	}
}
