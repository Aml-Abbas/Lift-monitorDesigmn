import lift.Passenger;

import java.util.HashMap;
import java.util.Map;

public class LiftData {
    private int floor;
    private int direction;
    private int load;
    private boolean moving;
    private Map<Integer, Integer> waitEntry;
    private Map<Integer, Integer> waitExit;
    private boolean entering;

    public LiftData(){
        this.floor=0;
        this.direction=1;
        this.load=0;
        this.moving= false;
        this.entering= false;
        this.waitEntry= new HashMap<>();
        this.waitExit= new HashMap<>();
    }

    public synchronized int getNextFloor(){
        int nextFloor;
        if (direction>0){
           nextFloor= floor+1;
            if (nextFloor>6){
                nextFloor= floor-1;
                direction=-1;
            }

        }else {
            nextFloor=floor-1;
            if (nextFloor<0){
                nextFloor=floor+1;
                direction=1;
            }
        }

        return nextFloor;
    }
    public synchronized int getFloor(){
        return floor;
    }

    public synchronized void setFloor(int nextFloor) {
        this.floor= nextFloor;
        notifyAll();
    }

    public synchronized void setMoving(boolean moving) {
        this.moving= moving;
        notifyAll();
    }
    public synchronized void increaseLoad(){
        load++;
    }
    public synchronized void decreaseLoad(){
        load--;
        notifyAll();
    }


    public synchronized boolean canEnterFloor(Passenger passenger) throws InterruptedException {
        addToWaitEntry(passenger.getStartFloor());
        while ( !(passenger.getStartFloor()==floor && load<4 && moving ==false) ){
            wait();
        }
        setMoving(false);

        removeFromWaitEntry(passenger.getStartFloor());
        increaseLoad();
        entering=true;
        return true;
    }

    public synchronized boolean canExitFloor(Passenger passenger) throws InterruptedException {
        addToWaitExit(passenger.getDestinationFloor());
        while ( !(passenger.getDestinationFloor()==floor && moving ==false) ){
            wait();
        }
        removeFromWaitExit(passenger.getDestinationFloor());
        decreaseLoad();
        return true;
    }


    public synchronized void addToWaitEntry(int startFloor) {
        Integer numberOfWaiting= waitEntry.get(startFloor);
        if (numberOfWaiting== null){
            waitEntry.put(startFloor,1);
        }else {
            waitEntry.put(startFloor,numberOfWaiting+1);
        }
    }

    public synchronized void removeFromWaitEntry(int startFloor) {

        int numberOfWaiting= waitEntry.get(startFloor);
        numberOfWaiting--;
        if (numberOfWaiting==0){
            waitEntry.remove(startFloor);
        }else {
            waitEntry.put(startFloor,numberOfWaiting);
        }
    }

    public synchronized void addToWaitExit(int destinationFloor) {

        Integer numberOfExiting= waitExit.get(destinationFloor);
        if (numberOfExiting== null){
            waitExit.put(destinationFloor,1);
        }else {
            waitExit.put(destinationFloor,numberOfExiting+1);
        }
    }

    public synchronized void removeFromWaitExit(int destinationFloor) {
        int numberOfExiting= waitExit.get(destinationFloor);
        numberOfExiting--;
        if (numberOfExiting==0){
            waitExit.remove(destinationFloor);
        }else {
            waitExit.put(destinationFloor,numberOfExiting);
        }
    }

    public synchronized boolean shouldOpen(int floor){
        if((( waitEntry.get(floor)==null || load>3) && (waitExit.get(floor)==null))){
            return false;
        }
        return true;
    }

    public synchronized boolean shouldStop(){

        return waitEntry.size()==0 && waitExit.size()==0;
    }

    public synchronized boolean canCloseDoors() throws InterruptedException {
        while (entering==true){
            wait();
        }
        return true;
    }

    public synchronized void setEntering(boolean b) {
        this.entering= b;
        notifyAll();
    }
}