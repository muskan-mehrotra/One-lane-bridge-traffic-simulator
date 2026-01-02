public class TrafficController {

    private int leftCount = 0; // cars currently on bridge going left → right
    private int rightCount = 0;
    private int leftWaiting = 0; //cars waiting on left side
    private int rightWaiting = 0;
    private boolean leftTurn = true; // whose turn it is when both sides wait

    public synchronized void enterLeft() {
        leftWaiting++;
        // while there are cars on the bridge going right → left and if its right turn
        while (rightCount > 0 || (rightWaiting > 0 && !leftTurn)) {
            //mutual exclusion on the bridge
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        leftWaiting--;
        leftCount++;
    }

    public synchronized void leaveLeft() {
        leftCount--;
        if (leftCount == 0) {
            //condition to check for collision
            leftTurn = false; // switch turn to right
            notifyAll();
        }
    }

    public synchronized void enterRight() {
        rightWaiting++;
        // while there are cars on the bridge going left → right and if its left turn
        while (leftCount > 0 || (leftWaiting > 0 && leftTurn)) {
            //first condition is to prevent collision, second is to prevent starvation
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        rightWaiting--;
        rightCount++;
    }

    public synchronized void leaveRight() {
        rightCount--;
        if (rightCount == 0) {
            leftTurn = true; // switch turn to left
            notifyAll();
        }
    }
}
