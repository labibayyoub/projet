package algorithms;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.stream.events.StartDocument;

import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;
import characteristics.IFrontSensorResult.Types;
import characteristics.Parameters;
import robotsimulator.Brain;

public class PrimarySoldier extends Brain {

	private boolean temp, startTurn, turnTask, startBack;
	private double endTaskDirection, backCount, turnCount, turnDie;
	private Types WALL = IFrontSensorResult.Types.WALL;
	private Types MAIN_OPPONENT = IFrontSensorResult.Types.OpponentMainBot;
	private Types SECONDARY_OPPONEONT = IFrontSensorResult.Types.OpponentSecondaryBot;
	private Types MAIN_TEAM = IFrontSensorResult.Types.TeamMainBot;
	private Types SECONDARY_TEAM = IFrontSensorResult.Types.TeamSecondaryBot;
	private Types BULLET = IFrontSensorResult.Types.BULLET;
	private Types WRECK = IFrontSensorResult.Types.Wreck;
	static int id = 0;
	private int myId;

	@Override
	public void activate() {
		startTurn = false;
		myId = ++id;
		turnCount = 300;

	}

	double angle = 0.25;

	@Override
	public void step() {
		ArrayList<IRadarResult> radarResults = detectRadar();

		if (turnCount > 0) {
			// moveBack();
			turnCount--;
			if (turnCount == 0) {
				startTurn = true;
				angle = 0.05;
				turnCount = 100;
			}
			// return;
		}

		if (startBack) {
			backCount = 20;
			startBack = false;
		}
		if (backCount > 0) {
			backCount--;
			moveBack();
			if (backCount == 0) {
				startTurn = true;
				angle = 0.15;
			}
			return;
		}
		if (startTurn) {
			double tmp = 0;
			tmp = ThreadLocalRandom.current().nextDouble(-angle * Math.PI, angle * Math.PI);
			endTaskDirection = getHeading() + tmp;
			turnTask = true;
			startTurn = false;
		}
		if (turnTask) {
			turnDie++;
			if (turnDie > 100) {
				turnDie = 0;
				startBack = true;
				turnTask = false;
				angle = 0.25;
				step();
				return;
			}
			if (isHeading(endTaskDirection)) {
				turnTask = false;
				angle = 0.25;
				temp = true;
				turnDie = 0;
			} else if (Math.sin(getHeading() - endTaskDirection) > 0) {
				stepTurn(Parameters.Direction.RIGHT);
				return;
			} else {
				stepTurn(Parameters.Direction.LEFT);
				return;
			}
		}

		if (detectFront().getObjectType() == WALL || detectFront().getObjectType() == MAIN_TEAM
				|| detectFront().getObjectType() == SECONDARY_TEAM || detectFront().getObjectType() == WRECK) {
			sendLogMessage("detect obstacle" + detectFront().getObjectType());
			startTurn = true;
			step();
			return;
		}

		for (IRadarResult r : radarResults) {
			if (r.getObjectType() == IRadarResult.Types.OpponentSecondaryBot
					|| r.getObjectType() == IRadarResult.Types.OpponentMainBot) {

				fire(r.getObjectDirection());
				turnCount += 5;
				return;
			} else {
				// System.out.println(r.getObjectDirection());
				// if (r.getObjectType() != IRadarResult.Types.BULLET)
				// if (r.getObjectDistance() < 130 && Math.abs(Math.sin(getHeading() -
				// r.getObjectDirection())) < 0.3
				// || r.getObjectDistance() < 100) {
				// sendLogMessage(r.getObjectDistance() + ": un non ennemi detecte : " +
				// r.getObjectType());
				// backCount = 100;
				// startTurn = true;
				// step();
				// return;
				// }
			}
		}
		sendLogMessage("mooooove + " + myId);
		move();
	}

	@Override
	public double getHeading() {
		return Math.asin(Math.sin(super.getHeading()));
	}

	private boolean isHeading(double dir) {
		return Math.abs(Math.sin(getHeading() - dir)) < 0.1;
	}
}
