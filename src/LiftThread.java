import lift.LiftView;

public class LiftThread extends Thread{
    private LiftView liftView;
    private LiftData liftData;

    public LiftThread(LiftView liftView, LiftData liftData){
        this.liftView= liftView;
        this.liftData= liftData;
    }

    public void run(){

        while (true){


                int nextFloor=  liftData.getNextFloor();
                int floor= liftData.getFloor();
                System.out.println(floor+" -> " + nextFloor);
                liftData.setMoving(true);

            if (liftData.shouldOpen(floor)){
                    liftView.openDoors(floor);
                    liftData.setMoving(false);

                    try {
                        if (liftData.canCloseDoors(floor)){
                            liftView.closeDoors();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            try {
                liftData.shouldStop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                liftData.setMoving(true);
                liftView.moveLift(floor,nextFloor);
                liftData.setFloor(nextFloor);
                System.out.println(liftData.getFloor());
        }

    }

}
