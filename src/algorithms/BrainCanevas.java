/* ******************************************************
 * Simovies - Eurobot 2015 Robomovies Simulator.
 * Copyright (C) 2014 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: algorithms/BrainCanevas.java 2014-10-19 buixuan.
 * ******************************************************/
package algorithms;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;
import characteristics.Parameters;
import characteristics.IRadarResult.Types;
import characteristics.Parameters.Direction;
import robotsimulator.Brain;

public class BrainCanevas extends Brain {
	static int ids = 0;
	int stat = 0;
	int id;
	int turnCount = 0;

	public BrainCanevas() {
		super();
	}

	int x = 1;


	public void activate() {
		// ---PARTIE A MODIFIER/ECRIRE---//

		// move();s
		//activate2();
	}
	public void step() {
		step4();
	}








	public void step1() {
		//		endTaskDirection=getHeading()+Parameters.LEFTTURNFULLANGLE;

		if (stat == 0 && Math.abs(getHeading() - Parameters.LEFTTURNFULLANGLE) < 0.1) {
			stat = 1;
			System.err.println(getHeading());
			return;
		}
		if (stat == 0) {
			stepTurn(Direction.LEFT);
			return;
		}
		if (stat == 1) {
			if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
				move();
			} else {
				stat = 2;
				return;
			}
		}

		if (stat == 2 && Math.abs(getHeading() - Parameters.EAST) < 0.01) {
			stat = 3;
			return;
		}

		if (stat == 2) {
			stepTurn(Direction.RIGHT);
			return;
		}
		if (stat == 3) {
			if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
				move();
			} else {
				stat = 4;
				return;
			}
		}

		if (stat == 4 && Math.abs(getHeading() - Parameters.SOUTH) < 0.01) {
			stat = 5;
			return;

		}
		if (stat == 4) {
			stepTurn(Direction.RIGHT);
			return;
		}
		if (stat == 5) {
			if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
				move();
			} else {
				stat = 6;
				return;
			}
		}

		if (stat == 6 && Math.abs(getHeading() - Parameters.WEST) < 0.01) {
			stat = 7;
			return;

		}
		if (stat == 6) {
			stepTurn(Direction.RIGHT);
			return;
		}
		if (stat == 7) {
			if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
				move();
			} else {
				turnCount = 1;
				stat = 0;
				return;
			}
		}

		if (stat == 9 && Math.abs(getHeading() - Parameters.SOUTH) < 0.01) {
			turnCount = 1;
			stat = 5;
			return;
		}
		if (stat == 9) {
			stepTurn(Direction.LEFT);
			return;
		}

		// if(id==0) {
		// System.out.println(turnCount);
		// }
		// // ---PARTIE A MODIFIER/ECRIRE---//'
		// if (turnCount > 0) {
		// turnCount--;
		// stepTurn(Direction.RIGHT);
		// } else {
		//
		// if (detectFront().getObjectType() != IFrontSensorResult.Types.NOTHING) {
		// turnCount = 5;
		// } else {
		// move();
		// }
		// }

		// if (detectFront().getObjectType() == IFrontSensorResult.Types.NOTHING) {
		// move();
		// }
	}

	double heading ;

	public void step2(){
		if (stat == 0 && isHeading( Parameters.LEFTTURNFULLANGLE) ) {
			stat = 2;
			return;
		}
		if (stat == 0) {
			stepTurn(Direction.LEFT);
			return;
		}
		if (stat == 1) {


			if(isHeading(Parameters.RIGHTTURNFULLANGLE+heading)  ){
				stat=2;
				return;
			}
			stepTurn(Direction.RIGHT);




		}
		if(stat==2){
			if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {

				move();
			}

			else{


				heading = getHeading();
				stat=1;
			}
			return;
		}
	}


	public void step3(){
		if (stat == 0 && isHeading( Parameters.LEFTTURNFULLANGLE) ) {
			stat = 2;
			return;
		}
		if (stat == 0) {
			stepTurn(Direction.LEFT);
			return;
		}
		if (stat == 1) {


			if(isHeading(Parameters.RIGHTTURNFULLANGLE+heading)  ){
				stat=2;
				return;
			}
			stepTurn(Direction.RIGHT);




		}
		if(stat==2){
			ArrayList<IRadarResult> temp = detectRadar();
			if(temp!=null)
			{
				for (IRadarResult e : temp) {
					if(e.getObjectType() == IRadarResult.Types.TeamMainBot || e.getObjectType() == IRadarResult.Types.TeamSecondaryBot ){
						if(e.getObjectDistance()<100){
							System.out.println("moovo");
							return;
						}					


					}
				}
			}
			if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {

				move();
			}

			else{


				heading = getHeading();
				stat=1;
			}
			return;
		}
	}

	private void step4() {
		if (stat == 0 && isHeading( Parameters.LEFTTURNFULLANGLE) ) {
			stat = 2;
			return;
		}
		if (stat == 0) {
			stepTurn(Direction.LEFT);
			return;
		}
		if (stat == 1) {


			if(isHeading(Parameters.RIGHTTURNFULLANGLE+heading)  ){
				stat=2;
				return;
			}
			stepTurn(Direction.RIGHT);




		}
		if(stat==2){
			ArrayList<IRadarResult> temp = detectRadar();
			if(temp!=null)
			{
				for (IRadarResult e : temp) {
					if(e.getObjectType() == IRadarResult.Types.TeamMainBot || e.getObjectType() == IRadarResult.Types.TeamSecondaryBot ){
						if(e.getObjectDistance()<100){
							stepTurn(Direction.RIGHT);
							return;
						}					
					}
				}
			}
			if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
				move();
			}
			else{
				//ThreadLocalRandom.current().
				heading = getHeading();
				stat=1;
			}
			return;
		}
	}

	private boolean isHeading(double dir){
		return Math.abs(Math.sin(getHeading()-dir))<0.001;
	}



}
