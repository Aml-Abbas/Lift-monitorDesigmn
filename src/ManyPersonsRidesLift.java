import lift.LiftView;

public class ManyPersonsRidesLift {

    public static void main(String[] args){
        LiftView liftView= new LiftView();
        LiftData liftData= new LiftData();

        LiftThread liftThread= new LiftThread(liftView, liftData);
        liftThread.start();

        for (int i=0; i<20;i++){
            PassengerTread passengerThread= new PassengerTread(liftData, liftView);
            passengerThread.start();
        }


    }
}
