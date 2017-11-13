package algorithms;

import java.util.Random;

import characteristics.IFrontSensorResult;
import characteristics.Parameters;
import robotsimulator.Brain;

public class SecondarySoldier extends Brain {

	private boolean firstTask,turnLeft,turnRight;
	private double endTaskDirection ;
	private int myId;
	public static int id=0;
	private Random gen= new Random();


	public SecondarySoldier() {
		super();
	}

	@Override
	public void activate() {
	//	endturn=false;
		firstTask=true;
		myId=++id;
		System.out.println("myyid= "+myId);		
		turnLeft=false;
		turnRight=false;



	}

	@Override
	public void step() {

		if(turnRight){
		//	System.err.println("turnright = "+turnRight);
			if(isHeading(endTaskDirection)){
				//fire(Math.PI*(0.98+0.04*gen.nextDouble()));
				turnRight=false;
				firstTask=false;
			}
			else
				stepTurn(Parameters.Direction.RIGHT);
			return;
		}
		if(turnLeft){

			if(isHeading(endTaskDirection)){
				turnLeft=false;
				firstTask=false;
			}
			else{

				stepTurn(Parameters.Direction.LEFT);
			}
			return;
		}
		if (detectFront().getObjectType() == IFrontSensorResult.Types.WALL ) {
			//sendLogMessage("camping ready to fire!!!");
			return;
		}
		else{
			if(firstTask){
				if(myId==1) {
					endTaskDirection=getHeading()+Parameters.LEFTTURNFULLANGLE;
					turnLeft=true;
					//System.out.println("id1 , turnLeft="+turnLeft);

				}
				if(myId==2){
					endTaskDirection=getHeading()+Parameters.RIGHTTURNFULLANGLE;
					turnRight=true;
					//System.out.println("id2 , turnright="+turnRight);

				}
				return;
			}
			else {
				//System.out.println("aa");
				if(detectFront().getObjectType()!=IFrontSensorResult.Types.WALL) {
					move();
					return;
				}
				
				fire(Math.PI*(0.98+0.04*gen.nextDouble()));
				return;
			}

		}


	}


	private boolean isHeading(double dir){
		return Math.abs(Math.sin(getHeading()-dir))<0.1;
	}
}
