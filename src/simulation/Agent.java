package simulation;

import gui.AgentScreen;
import gui.ScreensPanel;
import gui.SwarmPanel;

import simulation.util.Vector2D;

public class Agent {


	public enum AgentSwarmBehaviour { SWARM_LEADER , FOLLOW_LEFT, FOLLOW_FRONT , LAST }
	public enum AgentControl { PitchForward , PitchBackward , RollLeft , RollRight ,
		ThrottleHigh , ThrottleLow , YawLeft , TawRight , Hover}

	private static final int INERTIA = 3;
	private static final int REACH = 20;
	private static final int AGENT_SIZE = SwarmPanel.SCALE;
	double x;
	double y;
	private Direction direction;
	private AgentSwarmBehaviour behaviour;
//	private String command ="";
	private AgentControl command;
	private double xDiff;
	private double yDiff;

	private AgentDailyCycle leaderCycle = new ByPressCycle();
	private AgentDailyCycle rightMemberCycle = new followFromLeftCycle();
	private AgentDailyCycle backMemberCycle = new followFromFrontCycle();
	private AgentDailyCycle lastMemberCycle = new followLastCycle();


//	private static Simulator sim = Simulator.getInstance();
	private static ScreensPanel screensPanel = ScreensPanel.getInstance();

	public Agent(AgentSwarmBehaviour behaviour, Direction direction, int x, int y) {
		this.behaviour = behaviour;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.command = AgentControl.Hover;

	}

	public AgentSwarmBehaviour getAgentBehaviour() {
		return behaviour;
	}

	public void dailyCycle() {
		switch(behaviour) {
			case SWARM_LEADER:leaderCycle.dailyCycle(); break;
			case FOLLOW_LEFT:rightMemberCycle.dailyCycle(); break;
			case FOLLOW_FRONT: backMemberCycle.dailyCycle(); break;
			case LAST: lastMemberCycle.dailyCycle(); break;
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

	private void rollTo(String side) {
		double angle;
		if(side.equals("left")){
			angle = Math.toRadians(direction.getAngle() - 90);
		}else {
			angle = Math.toRadians(direction.getAngle() + 90);
		}

		xDiff = Math.sin(angle);
		yDiff = Math.cos(angle);

		x += xDiff;
		y -= yDiff;
	}


//	private void steerTowards(Vector2D position, double willingness) {
//		if (position.x > x) {
//			x += willingness;
//		} else {
//			x -= willingness;
//		}
//		if (position.y > y) {
//			y += willingness;
//		} else {
//			y -= willingness;
//		}
//		if (position.distance(x, y) < AGENT_SIZE) {
//			moveAwayFrom(position);// TODO use in the rabel
//		}
//	}
//
//	private void moveAwayFrom(Vector2D position) {
//		double step = AGENT_SIZE/INERTIA;
//		x = position.x > x ? x - step : x + step;
//		y = position.y > y ? y - step : y + step;
//	}

//	private void changeDirectionRandomly() {
//		double random = Math.random();
//		if (random < 0.01) {
//			direction.turn(Simulator.randomGenerator.nextInt(180) - 90);
//		}
//	}
//
//	private void headInTheSameDirection(int avgDirection, double willingness) {
//		System.out.println("Was heading " + direction.getAngle() + " but avgDirection is " + avgDirection);
//		System.out.println("Will: " + willingness);
//		int diffAngle = avgDirection - direction.getAngle();
//		direction.turn((int)(diffAngle*willingness));
//		System.out.println("Heading in angle " + direction.getAngle());
//	}
//
//	private void headSomewhere() {
//		double random = Math.random();
//		if (random > 0.95) {
//			direction.turn(1);
//		} else if (random > 0.9) {
//			direction.turn(-1);
//		}
//	}

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
		return "X=" + x + " Y=" + y + " D=" + direction.getAngle() +
				" , behaviour= " + behaviour + ", command=" + command;
	}

	public void setCommand(AgentControl command) {
		this.command = command;
	}

	private interface AgentDailyCycle {
		void dailyCycle();
	}

	public class ByPressCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
			AgentScreen[] screensToUpdate = new AgentScreen[]{screensPanel.getAgentPanelsArr()[0] ,
					/*screensPanel.getAgentPanelsArr()[1]*/};

//			System.out.println(command);
			if(command.equals(AgentControl.PitchForward)){
				goAhead();
				System.out.println("still go ahed");
			}else if(command.equals(AgentControl.RollRight)){
				rollTo("right");
			}else if(command.equals(AgentControl.RollLeft)){
				rollTo("left");
			}else if(command.equals(AgentControl.Hover)){
			}
//			System.out.println(behaviour + ", command:" + command);
			updateFollowers(screensToUpdate, command);

		}

	}

	public class followFromLeftCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
//
//			AgentScreen secMemberPanel = screensPanel.getAgentPanelsArr()[0];
//			AgentScreen[] screensToUpdate = new AgentScreen[]{screensPanel.getAgentPanelsArr()[2]};
//
//			String IRpos = screensPanel.findIRpos(secMemberPanel.getCameraSide());
////			System.out.println(behaviour + ", find:" + IRpos+ "\n");
//			followIRpoints(IRpos);
//			updateSelfScreen(secMemberPanel);
//			updateFollowers(screensToUpdate,command);
//			System.out.println(behaviour + ", command:" + command + "\n");

		}
	}



	public class followFromFrontCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
//			AgentScreen secRowsPanel = screensPanel.getAgentPanelsArr()[1];
			AgentScreen secRowsPanel = screensPanel.getAgentPanelsArr()[0];

			String findIR = screensPanel.findIRpos(secRowsPanel.getCameraSide());
//			System.out.println(behaviour + ", find:" + findIR+ "\n");

			followIRpoints(findIR);
			updateSelfScreen(secRowsPanel);
//			System.out.println(behaviour + ", command:" + command);

		}
	}

	public class followLastCycle implements AgentDailyCycle {

		@Override
		public void dailyCycle() {
//			AgentScreen secRowsPanel = screensPanel.getAgentPanelsArr()[2];
//
//			String findIR = screensPanel.findIRpos(secRowsPanel.getCameraSide());
////			System.out.println(behaviour + ", find:" + findIR+ "\n");
//			System.out.println(behaviour + ", command:" + command + "\n");
//
//			followIRpoints(findIR);
//			updateSelfScreen(secRowsPanel);
		}
	}

	private void updateFollowers(AgentScreen[] screensToUpdate, AgentControl command) {
		int xdiff = screensToUpdate[0].getDiffX();
		int dimdiff = screensToUpdate[0].getDiffDim();
		switch (command) {
			case PitchForward:
				switch (behaviour){
					case SWARM_LEADER:
//						screensToUpdate[0].setCurrX_1(screensToUpdate[0].getCurrX_1() + xdiff);
//						screensToUpdate[0].setCurrX_2(screensToUpdate[0].getCurrX_2() + xdiff);
//
//						screensToUpdate[1].setCurrIRdim(screensToUpdate[1].getCurrIRdim() - dimdiff);
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() - dimdiff);

//						System.out.println("getCurrIRdim():" + screensToUpdate[1].getCurrIRdim());
						break;
					case FOLLOW_LEFT:
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() - dimdiff);

						break;
				}
				break;
			case RollRight:
				switch (behaviour){
					case SWARM_LEADER:
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() + dimdiff);

						screensToUpdate[1].setCurrX_1(screensToUpdate[1].getCurrX_1() + xdiff);
						screensToUpdate[1].setCurrX_2(screensToUpdate[1].getCurrX_2() + xdiff);

						break;
					case FOLLOW_LEFT:

						screensToUpdate[0].setCurrX_1(screensToUpdate[0].getCurrX_1() + xdiff);
						screensToUpdate[0].setCurrX_2(screensToUpdate[0].getCurrX_2() + xdiff);

						break;
				}
				break;
			case RollLeft:
				switch (behaviour){
					case SWARM_LEADER:
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() - dimdiff);

						screensToUpdate[1].setCurrX_1(screensToUpdate[1].getCurrX_1() - xdiff);
						screensToUpdate[1].setCurrX_2(screensToUpdate[1].getCurrX_2() - xdiff);

						break;
					case FOLLOW_LEFT:
						screensToUpdate[0].setCurrX_1(screensToUpdate[0].getCurrX_1() - xdiff);
						screensToUpdate[0].setCurrX_2(screensToUpdate[0].getCurrX_2() - xdiff);

						break;
				}
				break;
			case PitchBackward:
				switch (behaviour) {
					case SWARM_LEADER:
						screensToUpdate[1].setCurrIRdim(screensToUpdate[1].getCurrIRdim() + dimdiff);
						break;

					case FOLLOW_FRONT:
						screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() + dimdiff);
						break;
				}

				break;
			case Hover:
				break;
		}

		screensPanel.repaintPoints();

	}

	private void followIRpoints(String findIR) {
		if(findIR.equals("front")){
			goAhead();
			setCommand(AgentControl.PitchForward);
		}else if(findIR.equals("right")){
			rollTo("right");
			setCommand(AgentControl.RollRight);
		}else if(findIR.equals("left")) {
			rollTo("left");
			setCommand(AgentControl.RollLeft);
		}else if(findIR.equals("back")){
			goBack();
			setCommand(AgentControl.PitchBackward);
		}else{
			setCommand(AgentControl.Hover);
		}

	}

	private void updateSelfScreen(AgentScreen memberPanel) {
		if(command.equals(AgentControl.PitchForward)){
			switch (behaviour) {
				case FOLLOW_LEFT:
					memberPanel.setCurrX_1(memberPanel.getX_1());
					memberPanel.setCurrX_2(memberPanel.getX_2());

					break;
				case FOLLOW_FRONT:
					memberPanel.setCurrIRdim(memberPanel.getIRdim());

					break;
				case LAST:
					memberPanel.setCurrIRdim(memberPanel.getIRdim());

					break;
			}
		}else if(command.equals(AgentControl.RollLeft) || command.equals(AgentControl.RollRight)){
			switch (behaviour) {
				case FOLLOW_LEFT:
					memberPanel.setCurrIRdim(memberPanel.getIRdim());

					break;
				case FOLLOW_FRONT:
					memberPanel.setCurrX_1(memberPanel.getX_1());
					memberPanel.setCurrX_2(memberPanel.getX_2());

					break;
				case LAST:
					memberPanel.setCurrX_1(memberPanel.getX_1());
					memberPanel.setCurrX_2(memberPanel.getX_2());

					break;
			}
		}
		screensPanel.repaintPoints();
	}
}
