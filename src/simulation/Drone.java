package simulation;

import gui.ScreensPanel;
import gui.droneScreen;
import simulation.util.Vector2D;

import java.util.LinkedList;
import java.util.Queue;

public class Drone {


	public enum droneBehaviour { SWARM_LEADER , FOLLOW_LEFT, FOLLOW_FRONT_IR, LAST }
	public enum droneControl { PitchForward , PitchBackward , RollLeft , RollRight ,
		ThrottleHigh , ThrottleLow , YawLeft , YawRight , Land, TakeOf, Hover}
	public enum droneState {Flying, Landed}

	double x;
	double y;
	private Direction direction;
	private Height height;
	private droneBehaviour behaviour;
	private droneState state = droneState.Landed;

	private Queue<droneControl[]> commands;
	private Queue<String[]> queue;


	private droneControl sideCommand;//Pith, Roll
	private droneControl dirCommand;//Yaw, Throttle
	private double xDiff;
	private double yDiff;

	private droneCycle leaderCycle = new ByPressCycle();
	private droneCycle backMemberCycle = new IrFrontCycle();


	private static ScreensPanel screensPanel = ScreensPanel.getInstance();

	public Drone(droneBehaviour behaviour, Direction direction, Height height, int x, int y) {
		this.behaviour = behaviour;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.height = height;
		this.sideCommand = droneControl.Hover;
		this.dirCommand = droneControl.Hover;
		this.commands = new LinkedList<>();
		this.commands.add(new droneControl[]{sideCommand,dirCommand});
		this.queue = new LinkedList<>();
	}

	public droneBehaviour getAgentBehaviour() {
		return behaviour;
	}



	private void takeOf() {
		height.setHeight(20);
	}

	private void land() {
		height.setHeight(15);
	}

	private void goAhead() {
		System.out.println("direction :" + direction);
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
		}else if(side.equals("right")){
			angle = Math.toRadians(direction.getAngle() + 90);
		}else{
			angle = Math.toRadians(direction.getAngle());
		}

		xDiff = Math.sin(angle);
		yDiff = Math.cos(angle);

		x += xDiff;
		y -= yDiff;
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

	public Height getHeight() {
		return height;
	}

	public String toString() {
		return "X=" + x + " Y=" + y + " D=" + direction.getAngle() +
				" , behaviour= " + behaviour + ", sideCommand=" + sideCommand + ", dirCommand=" + dirCommand + ", agentState=" +state;
	}

	public void setSideCommand(droneControl sideCommand) {
		this.sideCommand = sideCommand;
	}

	public void setDirCommand(droneControl dirCommand) {
		this.dirCommand = dirCommand;
	}





	// drones daily cycle
	public void dailyCycle() {
		switch(behaviour) {
			case SWARM_LEADER:leaderCycle.droneCycle(); break;
			case FOLLOW_FRONT_IR: backMemberCycle.droneCycle(); break;
		}
	}

	private interface droneCycle {
		void droneCycle();
	}


	/**
	 * leader cycle
	 * side and direction commands come from the key pressed
	 */
	public class ByPressCycle implements droneCycle {

		@Override
		public void droneCycle() {
			droneScreen[] screensToUpdate = new droneScreen[]{screensPanel.getAgentPanelsArr()[1]};

//			if(state.equals(droneState.Flying)) {
			if (sideCommand.equals(droneControl.PitchForward)) {
				goAhead();
			} else if (sideCommand.equals(droneControl.PitchBackward)) {
				goBack();
			} else if (sideCommand.equals(droneControl.RollRight)) {
				rollTo("right");
			} else if (sideCommand.equals(droneControl.RollLeft)) {
				rollTo("left");
			} else if (sideCommand.equals(droneControl.Land)) {
				land();
			} else {
				sideCommand = droneControl.Hover;
			}
//			}else {
//				if (sideCommand.equals(droneControl.TakeOf)) {
//					takeOf();
//					state = droneState.Flying;
//				}else{
//					sideCommand = droneControl.Hover;
//				}
//			}


			if (dirCommand.equals(droneControl.YawRight)) {
				direction.turn(5);
			} else if (dirCommand.equals(droneControl.YawLeft)) {
				direction.turn(-5);
			} else if (dirCommand.equals(droneControl.ThrottleHigh)) {
				if(!height.incHeight())
					dirCommand = droneControl.Hover;
			} else if (dirCommand.equals(droneControl.ThrottleLow)) {
				if(!height.decHeight())
					dirCommand = droneControl.Hover;
			}

			commands.add(new droneControl[]{sideCommand,dirCommand});
			updateFollowersScreen(screensToUpdate, commands);
			dirCommand = droneControl.Hover;
		}

	}

	public class IrFrontCycle implements droneCycle {

		@Override
		public void droneCycle() {
			droneScreen screen = screensPanel.getAgentPanelsArr()[1];
			System.out.println(screen);

			String[] findIR = screensPanel.findIRpos(screen.getCameraSide());

//			queue.add(findIR);
//			if(queue.size() > 1){
				followIRpoints(findIR);
				updateSelfScreen(screen);
//			}

		}
	}

	/**
	 * update the IR points in the follower screen by the direction and the side commands
	 * @param screensToUpdate
	 * @param commands
	 */
	private void updateFollowersScreen(droneScreen[] screensToUpdate, Queue<droneControl[]> commands) {
		int xdiff = screensToUpdate[0].getDiffX();
		int dimdiff = screensToUpdate[0].getDiffDim();
		int ydiff = screensToUpdate[0].getDiffY();

		droneControl side = droneControl.Hover;
		droneControl direction = droneControl.Hover;

		System.out.println(commands.size());

		if(commands.size() > 2) {
			droneControl[] currCommand = commands.poll();
			side = currCommand[0];
			direction = currCommand[1];
		}

		switch (side) {
			case PitchForward:
				screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() - dimdiff);
				break;
			case RollRight:
				screensToUpdate[0].setCurrX_1(screensToUpdate[0].getCurrX_1() + xdiff);
				screensToUpdate[0].setCurrX_2(screensToUpdate[0].getCurrX_2() + xdiff);
				break;
			case RollLeft:
				screensToUpdate[0].setCurrX_1(screensToUpdate[0].getCurrX_1() - xdiff);
				screensToUpdate[0].setCurrX_2(screensToUpdate[0].getCurrX_2() - xdiff);
				break;
			case PitchBackward:
				screensToUpdate[0].setCurrIRdim(screensToUpdate[0].getCurrIRdim() + dimdiff);
				break;
		}

		switch (direction) {
			case YawRight:
				if(screensToUpdate[0].getCurrIRslop_1() > 0 && screensToUpdate[0].getCurrIRslop_2() > 0) {
					screensToUpdate[0].setCurrIRslop_1((int) (screensToUpdate[0].getCurrIRslop_1() / 1.5));
					screensToUpdate[0].setCurrIRslop_2((int) (screensToUpdate[0].getCurrIRslop_2() / 0.8));
				}else{
					screensToUpdate[0].setCurrIRdim(0);
				}

				System.out.println("+++++++++++++++++++++++++++++++++++");
				System.out.println("getCurrIRslop_1: " + screensToUpdate[0].getCurrIRslop_1());
				System.out.println("getCurrIRslop_2: " + screensToUpdate[0].getCurrIRslop_2());
				System.out.println("getCurrIRdim: " + screensToUpdate[0].getCurrIRdim());

//				screensToUpdate[0].setTurn(1);
//				screensToUpdate[0].setCurrY_1(screensToUpdate[0].getCurrY_1() - ydiff);
//				screensToUpdate[0].setCurrY_2(screensToUpdate[0].getCurrY_2() - ydiff);
				break;

			case YawLeft:
				if(screensToUpdate[0].getCurrIRslop_1() > 0 && screensToUpdate[0].getCurrIRslop_2() > 0) {
					screensToUpdate[0].setCurrIRslop_1((int) (screensToUpdate[0].getCurrIRslop_1() / 0.8));
					screensToUpdate[0].setCurrIRslop_2((int) (screensToUpdate[0].getCurrIRslop_2() / 1.5));
				}else{
					screensToUpdate[0].setCurrIRdim(0);
				}

				System.out.println("----------------------------------");
				System.out.println("getCurrIRslop_1: " + screensToUpdate[0].getCurrIRslop_1());
				System.out.println("getCurrIRslop_2: " + screensToUpdate[0].getCurrIRslop_2());
				System.out.println("getCurrIRdim: " + screensToUpdate[0].getCurrIRdim());

//				screensToUpdate[0].setTurn(-1);
//				screensToUpdate[0].setCurrY_1(screensToUpdate[0].getCurrY_1() - ydiff);
//				screensToUpdate[0].setCurrY_2(screensToUpdate[0].getCurrY_2() - ydiff);
				break;
			case ThrottleHigh:
				screensToUpdate[0].setCurrY_1(screensToUpdate[0].getCurrY_1() - ydiff);
				screensToUpdate[0].setCurrY_2(screensToUpdate[0].getCurrY_2() - ydiff);
				break;
			case ThrottleLow:
				screensToUpdate[0].setCurrY_1(screensToUpdate[0].getCurrY_1() + ydiff);
				screensToUpdate[0].setCurrY_2(screensToUpdate[0].getCurrY_2() + ydiff);
				break;
		}

//		screensPanel.setVisible(true);
		screensPanel.repaintPoints();



	}

	private void followIRpoints(String[] findIR) {
//		if(state.equals(droneState.Flying)) {
		if (findIR[0].equals("front")) {
			goAhead();
			setSideCommand(droneControl.PitchForward);
		} else if (findIR[0].equals("right")) {
			rollTo("right");
			setSideCommand(droneControl.RollRight);
		} else if (findIR[0].equals("left")) {
			rollTo("left");
			setSideCommand(droneControl.RollLeft);
		} else if (findIR[0].equals("back")) {
			goBack();
			setSideCommand(droneControl.PitchBackward);
		} else {
			setSideCommand(droneControl.Hover);
		}

		if(findIR[1].equals("up")){
			height.incHeight();
			setDirCommand(droneControl.ThrottleHigh);
		} else if(findIR[1].equals("down")){
			height.decHeight();
			setDirCommand(droneControl.ThrottleLow);
		} else if(findIR[1].equals("right")){
			direction.turn(5);
			rollTo("left");
			setDirCommand(droneControl.YawRight);
		}  else if(findIR[1].equals("left")){
			direction.turn(-5);
			rollTo("right");
			setDirCommand(droneControl.YawLeft);
		}  else {
			setDirCommand(droneControl.Hover);
		}
//		}else {
//			if (findIR.equals("up")) {
//				takeOf();
//				setSideCommand(droneControl.TakeOf);
//				state = droneState.Flying;
//			}
//		}
	}

	private void updateSelfScreen(droneScreen memberPanel) {
//		if(sideCommand.equals(droneControl.TakeOf)){
//			memberPanel.setCurrY_1(memberPanel.getY_1());
//			memberPanel.setCurrY_2(memberPanel.getY_2());
//		}else

		if(sideCommand.equals(droneControl.PitchForward)){
			memberPanel.setCurrIRdim(memberPanel.getIRdim());
		}else if(sideCommand.equals(droneControl.RollLeft) || sideCommand.equals(droneControl.RollRight)){
			memberPanel.setCurrX_1(memberPanel.getX_1());
			memberPanel.setCurrX_2(memberPanel.getX_2());
		}else if(sideCommand.equals(droneControl.PitchBackward)){
			memberPanel.setCurrIRdim(memberPanel.getIRdim());
		}


		if(dirCommand.equals(droneControl.ThrottleHigh)){
			memberPanel.setCurrY_1(memberPanel.getY_1());
			memberPanel.setCurrY_2(memberPanel.getY_2());
		}else if(dirCommand.equals(droneControl.ThrottleLow)){
			memberPanel.setCurrY_1(memberPanel.getY_1());
			memberPanel.setCurrY_2(memberPanel.getY_2());
		}else if(dirCommand.equals(droneControl.YawRight)){
			memberPanel.setCurrIRslop_1(memberPanel.getCurrIRdim());
			memberPanel.setCurrIRslop_2(memberPanel.getCurrIRdim());
		}else if(dirCommand.equals(droneControl.YawLeft)){
			memberPanel.setCurrIRslop_1(memberPanel.getCurrIRdim());
			memberPanel.setCurrIRslop_2(memberPanel.getCurrIRdim());
		}


		screensPanel.repaintPoints();
	}
}
