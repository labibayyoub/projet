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

	private boolean temp, startTurn, turnTask, moveTask;
	private double endTaskDirection, currentHeading, countTemp;
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
		startTurn = true;
		myId = ++id;

	}

	@Override
	public void step() {
		ArrayList<IRadarResult> radarResults = detectRadar();

		if (startTurn) {
			double tmp = 0;
			tmp = ThreadLocalRandom.current().nextDouble(-0.35 * Math.PI, 0.35 * Math.PI);
			endTaskDirection = getHeading() + tmp;
			turnTask = true;
			startTurn = false;
			countTemp = 100;
		}
		if (turnTask) {
			if (isHeading(endTaskDirection)) {
				turnTask = false;
				// TODO sortie du turn sans action
				temp = true;
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
				return;
			} else {
				System.out.println(r.getObjectDirection());
				if (r.getObjectType() != IRadarResult.Types.BULLET)
					if (r.getObjectDistance() < 130
							&& Math.abs(Math.sin(getHeading() - r.getObjectDirection())) < 0.3 || r.getObjectDistance() < 80) {
						sendLogMessage(r.getObjectDistance() + ":  un non ennemi detecte : " + r.getObjectType());
						startTurn = true;
						step();
						return;
					}
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
