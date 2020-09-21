import lift.LiftView;
import lift.Passenger;

import java.util.Random;

public class PassengerTread extends Thread {
    private LiftData liftData;
    private LiftView liftView;
    private Passenger passenger;
    private Random random;

    public PassengerTread(LiftData liftData, LiftView liftView){
    this.liftData= liftData;
    this.liftView= liftView;
    this.random= new Random();
    }

    public void run(){

        while (true){
             this.passenger= liftView.createPassenger();
            try {
                sleep(random.nextInt(46)*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            passenger.begin();
            try {
                if (liftData.canEnterFloor(passenger)){
                    passenger.enterLift();  // får fel när hissen precis börjar flytta
                    liftData.setEntering(false);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                if (liftData.canExitFloor(passenger)){
                    passenger.exitLift();
                }

                passenger.end();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            }
    }
}
